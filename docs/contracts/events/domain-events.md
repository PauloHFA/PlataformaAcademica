# 📋 Contratos de Eventos de Domínio (Domain Event Contracts)

Este documento define os **contratos formais** (schemas) dos eventos de domínio publicados entre contextos. Seguem o padrão **CloudEvents 1.0** com payload **Avro** (serialização binária) e **JSON Schema** (validação/documentação).

---

## 1. Governança de Contratos

| Regra | Especificação |
|-------|---------------|
| **Formato** | CloudEvents 1.0 (Structured Mode) + Avro Payload |
| **Versionamento** | Semântico no `type`: `dominio.entidade.acao.v{major}` |
| **Schema Registry** | Confluent Schema Registry / Apicurio (compatibilidade `BACKWARD`) |
| **Tópicos Kafka** | `{contexto}.{entidade}.{acao}.v{major}` (ex: `academic.sala.criada.v1`) |
| **Compatibilidade** | **BACKWARD**: Consumidores novos leem eventos antigos. Campos novos = `optional` com `default`. |
| **Validação** | Produtor valida contra schema antes de publicar. Consumidor valida ao desserializar. |

---

## 2. Metadados CloudEvents (Obrigatórios em Todos)

```json
{
  "specversion": "1.0",
  "id": "uuid-v4",                    // ID único do evento
  "source": "/contexts/{context-name}", // Origem do evento. Ex: "/contexts/academic"
  "type": "br.com.plataforma.academic.sala.criada.v1",
  "datacontenttype": "application/avro+binary",
  "time": "2026-08-22T10:30:00.000Z",  // Formato RFC3339
  "subject": "sala/{salaId}",           // Recurso afetado
  "correlationid": "uuid-v4",           // ID da Saga/Requisição original
  "causationid": "uuid-v4",             // ID do evento que causou este
  "data": { ... }                       // Payload Avro (binário) ou JSON (desenvolvimento)
}
```

---

## 3. Contexto Acadêmico (Academic Context) — Eventos

### 3.1. `SalaCriadaEvent` (v1)
**Tópico**: `academic.sala.criada.v1`  
**Gatilho**: `CriarSalaCommand` concluído com sucesso.  
**Consumidores**: `Notification`, `Social`, `Identity` (sugestões), `Analytics`.

#### Avro Schema (`sala-criada-v1.avsc`)
```json
{
  "type": "record",
  "name": "SalaCriada",
  "namespace": "br.com.plataforma.academic.events.v1",
  "doc": "Emitido quando uma nova sala de aula é criada.",
  "fields": [
    {"name": "salaId", "type": "string", "doc": "UUID da sala"},
    {"name": "codigoSala", "type": "string", "doc": "Código de acesso de 8 chars"},
    {"name": "nome", "type": "string"},
    {"name": "professorId", "type": "string", "doc": "UsuarioId do criador"},
    {"name": "criadoEm", "type": {"type": "long", "logicalType": "timestamp-millis"}}
  ],
  "connect.name": "academic.sala.criada.v1"
}
```

#### JSON Schema (Validação/Documentação)
```json
{
  "$schema": "http://json-schema.org/draft-07/schema#",
  "$id": "https://plataforma.academic/events/sala-criada-v1.json",
  "title": "SalaCriadaEvent",
  "type": "object",
  "required": ["salaId", "codigoSala", "nome", "professorId", "criadoEm"],
  "properties": {
    "salaId": {"type": "string", "format": "uuid"},
    "codigoSala": {"type": "string", "pattern": "^[A-Z0-9]{8}$"},
    "nome": {"type": "string", "minLength": 3, "maxLength": 100},
    "professorId": {"type": "string", "format": "uuid"},
    "criadoEm": {"type": "string", "format": "date-time"}
  }
}
```

---

### 3.2. `AtividadePublicadaEvent` (v1)
**Tópico**: `academic.atividade.publicada.v1`  
**Gatilho**: `PublicarAtividadeCommand` (status `RASCUNHO` → `PUBLICADA`).  
**Consumidores**: `Notification` (alunos da sala), `Social` (card no feed), `Analytics`.

#### Avro Schema
```json
{
  "type": "record",
  "name": "AtividadePublicada",
  "namespace": "br.com.plataforma.academic.events.v1",
  "fields": [
    {"name": "atividadeId", "type": "string"},
    {"name": "salaId", "type": "string"},
    {"name": "titulo", "type": "string"},
    {"name": "descricao", "type": ["null", "string"], "default": null},
    {"name": "prazoEntrega", "type": {"type": "long", "logicalType": "timestamp-millis"}},
    {"name": "pontuacaoMaxima", "type": "double"},
    {"name": "criadorId", "type": "string"},
    {"name": "publicadoEm", "type": {"type": "long", "logicalType": "timestamp-millis"}}
  ]
}
```

---

### 3.3. `SubmissaoRealizadaEvent` (v1)
**Tópico**: `academic.submissao.realizada.v1`  
**Gatilho**: Aluno executa `entregar()` na `SubmissaoAtividade`.  
**Consumidores**: `Notification` (notificar docente), `Identity` (gamificação: +pontos).

#### Avro Schema
```json
{
  "type": "record",
  "name": "SubmissaoRealizada",
  "namespace": "br.com.plataforma.academic.events.v1",
  "fields": [
    {"name": "submissaoId", "type": "string"},
    {"name": "atividadeId", "type": "string"},
    {"name": "salaId", "type": "string"},
    {"name": "alunoId", "type": "string"},
    {"name": "arquivoUrl", "type": "string"},
    {"name": "entregueEm", "type": {"type": "long", "logicalType": "timestamp-millis"}},
    {"name": "emAtraso", "type": "boolean"}
  ]
}
```

---

### 3.4. `SubmissaoAvaliadaEvent` (v1)
**Tópico**: `academic.submissao.avaliada.v1`  
**Gatilho**: Docente executa `avaliar()` na `SubmissaoAtividade`.  
**Consumidores**: `Notification` (notificar aluno), `Identity` (gamificação: +pontos/nota), `Analytics`.

#### Avro Schema
```json
{
  "type": "record",
  "name": "SubmissaoAvaliada",
  "namespace": "br.com.plataforma.academic.events.v1",
  "fields": [
    {"name": "submissaoId", "type": "string"},
    {"name": "atividadeId", "type": "string"},
    {"name": "salaId", "type": "string"},
    {"name": "alunoId", "type": "string"},
    {"name": "docenteId", "type": "string"},
    {"name": "nota", "type": "double"},
    {"name": "feedback", "type": ["null", "string"], "default": null},
    {"name": "avaliadoEm", "type": {"type": "long", "logicalType": "timestamp-millis"}}
  ]
}
```

---

## 4. Contexto de Identidade (Identity Context) — Eventos

### 4.1. `UsuarioCadastradoEvent` (v1)
**Tópico**: `identity.usuario.cadastrado.v1`  
**Gatilho**: Registro concluído (local ou OAuth).  
**Consumidores**: `Notification` (boas-vindas), `Analytics`.

#### Avro Schema
```json
{
  "type": "record",
  "name": "UsuarioCadastrado",
  "namespace": "br.com.plataforma.identity.events.v1",
  "fields": [
    {"name": "usuarioId", "type": "string"},
    {"name": "email", "type": "string"},
    {"name": "nome", "type": "string"},
    {"name": "papel", "type": {"type": "enum", "name": "Papel", "symbols": ["ALUNO", "PROFESSOR", "ADMIN"]}},
    {"name": "origem", "type": {"type": "enum", "name": "OrigemCadastro", "symbols": ["LOCAL", "GOOGLE", "FACEBOOK"]}},
    {"name": "cadastradoEm", "type": {"type": "long", "logicalType": "timestamp-millis"}}
  ]
}
```

---

### 4.2. `SolicitacaoAmizadeEnviadaEvent` (v1)
**Tópico**: `identity.amizade.solicitada.v1`  
**Gatilho**: `ConexaoAmizade` criada com status `PENDENTE`.  
**Consumidores**: `Notification` (push para destinatário).

#### Avro Schema
```json
{
  "type": "record",
  "name": "SolicitacaoAmizadeEnviada",
  "namespace": "br.com.plataforma.identity.events.v1",
  "fields": [
    {"name": "conexaoId", "type": "string"},
    {"name": "solicitanteId", "type": "string"},
    {"name": "destinatarioId", "type": "string"},
    {"name": "enviadoEm", "type": {"type": "long", "logicalType": "timestamp-millis"}}
  ]
}
```

---

### 4.3. `AmizadeAceitaEvent` (v1)
**Tópico**: `identity.amizade.aceita.v1`  
**Gatilho**: Destinatário executa `aceitar()` na `ConexaoAmizade`.  
**Consumidores**: `Social` (liberar visibilidade `AMIGOS` no feed), `Notification`.

#### Avro Schema
```json
{
  "type": "record",
  "name": "AmizadeAceita",
  "namespace": "br.com.plataforma.identity.events.v1",
  "fields": [
    {"name": "conexaoId", "type": "string"},
    {"name": "solicitanteId", "type": "string"},
    {"name": "destinatarioId", "type": "string"},
    {"name": "aceitoEm", "type": {"type": "long", "logicalType": "timestamp-millis"}}
  ]
}
```

---

## 5. Contexto Social (Social Context) — Eventos

### 5.1. `PostagemCriadaEvent` (v1)
**Tópico**: `social.postagem.criada.v1`  
**Gatilho**: Nova postagem no feed.  
**Consumidores**: `Notification`, `Identity` (gamificação), `Analytics`.

#### Avro Schema
```json
{
  "type": "record",
  "name": "PostagemCriada",
  "namespace": "br.com.plataforma.social.events.v1",
  "fields": [
    {"name": "postagemId", "type": "string"},
    {"name": "autorId", "type": "string"},
    {"name": "conteudoResumo", "type": "string"},
    {"name": "visibilidade", "type": {"type": "enum", "name": "Visibilidade", "symbols": ["PUBLICO", "AMIGOS"]}},
    {"name": "criadoEm", "type": {"type": "long", "logicalType": "timestamp-millis"}}
  ]
}
```

---

### 5.2. `ComentarioAdicionadoEvent` (v1)
**Tópico**: `social.comentario.adicionado.v1`  
**Gatilho**: Usuário comenta em `Postagem` ou `Atividade`.  
**Consumidores**: `Notification` (notificar autor do alvo).

#### Avro Schema
```json
{
  "type": "record",
  "name": "ComentarioAdicionado",
  "namespace": "br.com.plataforma.social.events.v1",
  "fields": [
    {"name": "comentarioId", "type": "string"},
    {"name": "autorId", "type": "string"},
    {"name": "alvoTipo", "type": {"type": "enum", "name": "AlvoTipo", "symbols": ["POSTAGEM", "ATIVIDADE"]}},
    {"name": "alvoId", "type": "string"},
    {"name": "texto", "type": "string"},
    {"name": "criadoEm", "type": {"type": "long", "logicalType": "timestamp-millis"}}
  ]
}
```

---

### 5.3. `InteracaoRegistradaEvent` (v1)
**Tópico**: `social.interacao.registrada.v1`  
**Gatilho**: `InteracaoUsuario` criada (Curtida/Compartilhamento).  
**Consumidores**: `Identity` (gamificação: +pontos engajamento), `Analytics`.

#### Avro Schema
```json
{
  "type": "record",
  "name": "InteracaoRegistrada",
  "namespace": "br.com.plataforma.social.events.v1",
  "fields": [
    {"name": "interacaoId", "type": "string"},
    {"name": "usuarioId", "type": "string"},
    {"name": "alvoTipo", "type": {"type": "enum", "name": "AlvoTipo", "symbols": ["POSTAGEM", "COMENTARIO"]}},
    {"name": "alvoId", "type": "string"},
    {"name": "tipo", "type": {"type": "enum", "name": "TipoInteracao", "symbols": ["CURTIDA", "COMPARTILHAMENTO"]}},
    {"name": "criadoEm", "type": {"type": "long", "logicalType": "timestamp-millis"}}
  ]
}
```

---

## 6. Estrutura de Arquivos no Repositório

```
docs/
└── contracts/
    └── events/
        ├── academic/
        │   ├── sala-criada-v1.avsc
        │   ├── sala-criada-v1.json
        │   ├── atividade-publicada-v1.avsc
        │   ├── submissao-realizada-v1.avsc
        │   └── submissao-avaliada-v1.avsc
        ├── identity/
        │   ├── usuario-cadastrado-v1.avsc
        │   ├── amizade-solicitada-v1.avsc
        │   └── amizade-aceita-v1.avsc
        └── social/
            ├── postagem-criada-v1.avsc
            ├── comentario-adicionado-v1.avsc
            └── interacao-registrada-v1.avsc
```

---

## 7. Checklist de Evolução de Schema (Para PRs)

- [ ] Novo campo adicionado? → `optional` + `default` (ex: `"default": null` ou `"default": 0`).
- [ ] Campo removido? → **NUNCA**. Marcar como `deprecated` no doc e remover apenas na v{major+1}.
- [ ] Tipo alterado? → **NUNCA**. Criar novo campo com tipo correto.
- [ ] Enum expandido? → Adicionar novo símbolo ao final (compatível BACKWARD).
- [ ] Testes de contrato atualizados? (Pact / Schema Registry compatibility check).
- [ ] Documentação (`docs/contracts/events/`) atualizada?

---

## 5. Social Context — Eventos

### 5.1. `PostagemCriadaEvent` (v1)
**Tópico**: `social.postagem.criada.v1`  
**Gatilho**: Usuário cria `Postagem`.  
**Consumidores**: `Notification` (notificar amigos/seguidores), `Analytics`.

#### Avro Schema
```json
{
  "type": "record",
  "name": "PostagemCriada",
  "namespace": "br.com.plataforma.social.events.v1",
  "fields": [
    {"name": "postagemId", "type": "string"},
    {"name": "autorId", "type": "string"},
    {"name": "conteudoTexto", "type": ["null", "string"], "default": null},
    {"name": "midiaUrls", "type": {"type": "array", "items": "string"}, "default": []},
    {"name": "visibilidade", "type": {"type": "enum", "name": "Visibilidade", "symbols": ["PUBLICO", "AMIGOS"]}},
    {"name": "criadoEm", "type": {"type": "long", "logicalType": "timestamp-millis"}}
  ]
}
```

---

### 5.2. `ComentarioAdicionadoEvent` (v1)
**Tópico**: `social.comentario.adicionado.v1`  
**Gatilho**: Usuário comenta em `Postagem` ou `Atividade`.  
**Consumidores**: `Notification` (notificar autor do alvo).

#### Avro Schema
```json
{
  "type": "record",
  "name": "ComentarioAdicionado",
  "namespace": "br.com.plataforma.social.events.v1",
  "fields": [
    {"name": "comentarioId", "type": "string"},
    {"name": "autorId", "type": "string"},
    {"name": "alvoTipo", "type": {"type": "enum", "name": "AlvoTipo", "symbols": ["POSTAGEM", "ATIVIDADE"]}},
    {"name": "alvoId", "type": "string"},
    {"name": "texto", "type": "string"},
    {"name": "criadoEm", "type": {"type": "long", "logicalType": "timestamp-millis"}}
  ]
}
```

---

### 5.3. `InteracaoRegistradaEvent` (v1)
**Tópico**: `social.interacao.registrada.v1`  
**Gatilho**: `InteracaoUsuario` criada (Like/Share).  
**Consumidores**: `Identity` (gamificação: +pontos engajamento), `Analytics`.

#### Avro Schema
```json
{
  "type": "record",
  "name": "InteracaoRegistrada",
  "namespace": "br.com.plataforma.social.events.v1",
  "fields": [
    {"name": "interacaoId", "type": "string"},
    {"name": "usuarioId", "type": "string"},
    {"name": "alvoTipo", "type": {"type": "enum", "name": "AlvoTipo", "symbols": ["POSTAGEM", "COMENTARIO"]}},
    {"name": "alvoId", "type": "string"},
    {"name": "tipo", "type": {"type": "enum", "name": "TipoInteracao", "symbols": ["CURTIDA", "COMPARTILHAMENTO"]}},
    {"name": "criadoEm", "type": {"type": "long", "logicalType": "timestamp-millis"}}
  ]
}
```

---

## 6. Estrutura de Arquivos no Repositório

```
docs/
└── contracts/
    └── events/
        ├── academic/
        │   ├── sala-criada-v1.avsc
        │   ├── sala-criada-v1.json
        │   ├── atividade-publicada-v1.avsc
        │   ├── submissao-realizada-v1.avsc
        │   └── submissao-avaliada-v1.avsc
        ├── identity/
        │   ├── usuario-cadastrado-v1.avsc
        │   ├── amizade-solicitada-v1.avsc
        │   └── amizade-aceita-v1.avsc
        └── social/
            ├── postagem-criada-v1.avsc
            ├── comentario-adicionado-v1.avsc
            └── interacao-registrada-v1.avsc
```

---

## 7. Checklist de Evolução de Schema (Para PRs)

- [ ] Novo campo adicionado? → `optional` + `default` (ex: `"default": null` ou `"default": 0`).
- [ ] Campo removido? → **NUNCA**. Marcar como `deprecated` no doc e remover apenas na v{major+1}.
- [ ] Tipo alterado? → **NUNCA**. Criar novo campo com tipo correto.
- [ ] Enum expandido? → Adicionar novo símbolo ao final (compatível BACKWARD).
- [ ] Testes de contrato atualizados? (Pact / Schema Registry compatibility check).
- [ ] Documentação (`docs/contracts/events/`) atualizada?