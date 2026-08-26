# Padrão de Comentários — Nível Staff/Principal (Atualização)

## 🆕 Elementos Adicionais ao Padrão Sênior

### 1. Rastros de Requisitos (`REQ-XXX`)
Em métodos que implementam regras de negócio específicas, incluir referência ao requisito:
```java
/**
 * Gera recomendações... 
 * @see REQ-042 (Sistema de Recomendação por Similaridade)
 */
```

### 2. Trade-offs de Performance / Arquitetura
Documentar decisões de design que impactam performance ou escalabilidade:
```java
// Trade-off: limit(20) evita explosão de resultados em usuários com alta interação,
// mas pode omitir similares relevantes em casos de nicho.
```

### 3. Referências Arquiteturais
Linkar para documentação externa quando o método depende de contratos de serviço:
```java
/**
 * @see docs/architecture/recommendation-engine.md
 */
```

---

## 📊 Status de Documentação por Pacote (Atualizado 2026-08-26)

### ✅ Documentados (Padrão Staff Completo)
| Pacote | Entidades / Arquivos | Status |
|---|---|---|
| `model/` | `Comentario`, `Comunidade`, `Artigo`, `Atividade`, `Usuario`, `Amizade`, `Curtida`, `Postagem`, `Notificacao`, `Mensagem`, `SaladeAula`, `Perfil`, `SubmissaoAtividade`, `MembroComunidade`, `SolicitacaoEntrada`, `RecomendacaoUsuario`, `Professor`, `Admin`, `Plataforma`, `Frequencia`, `InteracaoUsuario`, `ConteudoMercado`, `CompraConteudo`, `ChatTempoRealUsuarios`, `TipoDestinoComentario` | ✅ 25/25 |

### ⏳ Pendentes (Não Documentados no Padrão Staff)
| Pacote | Arquivos / Componentes | Observação |
|---|---|---|
| `service/` | `ComentarioServiceImpl`, `UsuarioServiceImpl`, `AtividadeServiceImpl`, `ComunidadeServiceImpl`, `PerfilServiceImpl` | ✅ 5/32 (iniciado) |
| `controller/` | `ComentarioController` | ✅ 1/15 (iniciado) |
| `mapper/` | `ComentarioMapper` | ✅ 1/10 (iniciado) |
| `dto/` | `ComentarioDTO` | ✅ 1/26 (iniciado) |
| `mapper/` | `ComentarioMapper.java`, `AtividadeMapper.java`, `ArtigoMapper.java`, `AmizadeMapper.java`, `PerfilMapper.java`, `ComunidadeMapper.java`, `PostagemMapper.java`, `SalaDeAulaMapper.java`, `UsuarioMapper.java`, `SubmissaoAtividadeMapper.java` | Sem documentação de padrão |
| `repository/` | Todos os repositórios JPA | ✅ 21/21 (modelo+repository) |
| `config/` | Configurações de segurança, OAuth, etc. | Não verificados |
| `interceptor/` | Interceptores de requisição | Não verificados |
| `security/` | Configurações de autenticação/autorização | Não verificados |

---

## ✅ Aplicação
O padrão atualizado já está refletido nos arquivos documentados (`RecomendacaoService.java`, `PerfilServiceImpl.java`, etc.).
