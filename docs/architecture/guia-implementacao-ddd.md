# 🗺️ Guia de Implementação DDD & Arquitetura Hexagonal (Nível Especialista)

Este documento serve como o **guia de referência técnica definitivo** para a migração da **Plataforma Acadêmica Integrada** de um modelo monolítico MVC anêmico para uma arquitetura baseada em **Domain-Driven Design (DDD) Estratégico e Tático** com **Arquitetura Hexagonal (Ports & Adapters)**.

---

## 🎯 Princípios Arquiteturais Inegociáveis

Como Engenheiro de Software Sênior/Especialista, você deve garantir que as seguintes regras de design sejam estritamente seguidas em cada commit:

1. **Isolamento Total do Domínio**: A camada `domain` deve ser composta por POJOs puros (ou Java `records`). Ela **nunca** deve depender de frameworks (Spring, Hibernate/JPA, Jackson, etc.). Nenhuma anotação como `@Entity`, `@Table`, `@Autowired` ou `@JsonProperty` pode entrar no domínio.
2. **Sem Setters Públicos (Modelo Rico)**: Os agregados e entidades não devem expor setters públicos. Toda alteração de estado deve ocorrer por meio de métodos de negócio expressivos que garantam as invariantes (ex: `usuario.creditarGamificacao(...)` em vez de `usuario.setPontos(...)`).
3. **Validação na Entrada (Value Objects)**: Tipos primitivos (`String`, `Long`, `Double`) que carregam regras de negócio devem ser encapsulados em **Value Objects** imutáveis (Java `record`) que se autovalidam no construtor.
4. **Ports & Adapters**: A comunicação com o mundo externo (banco de dados, brokers de mensageria, APIs externas) deve ser feita estritamente através de interfaces declaradas no domínio (`ports`). A implementação concreta dessas interfaces vive na camada de `infrastructure` (`adapters`).
5. **Consistência Eventual**: Transações que cruzam múltiplos agregados ou contextos devem ser resolvidas de forma assíncrona usando **Domain Events** e **Sagas**, garantindo que não haja acoplamento temporal ou transações distribuídas síncronas (2PC).

---

## 🏗️ Estrutura de Pacotes de Referência (Hexagonal)

Cada Bounded Context deve seguir rigorosamente a estrutura abaixo:

```
br.com.plataformaacademica.<contexto>/
├── domain/                         # 🧠 CAMADA DE DOMÍNIO (Lógica de Negócio Pura - Zero Dependências)
│   ├── model/                      # Entidades Ricas, Agregados e Value Objects
│   │   ├── aggregate/              # Raízes de Agregado (Aggregate Roots)
│   │   ├── entity/                 # Entidades internas do agregado
│   │   └── valueobject/            # Objetos de Valor Imutáveis (records)
│   ├── event/                      # Eventos de Domínio Imutáveis
│   ├── exception/                  # Exceções de Regra de Negócio de Domínio
│   ├── service/                    # Domain Services (Lógica cross-agregado)
│   └── port/                       # Interfaces de Comunicação (Ports)
│       ├── inbound/                # Casos de uso / Comandos (Ports de Entrada)
│       └── outbound/               # Repositórios, Gateways, Publishers (Ports de Saída)
├── application/                    # ⚙️ CAMADA DE APLICAÇÃO (Orquestração de Casos de Uso)
│   ├── usecase/                    # Implementações dos Casos de Uso (Application Services)
│   ├── dto/                        # DTOs de Entrada (Commands) e Saída (Queries/Responses)
│   └── handler/                    # Event Handlers e Listeners de Eventos de Domínio
└── infrastructure/                 # 🔌 CAMADA DE INFRAESTRUTURA (Detalhes Técnicos & Adapters)
    ├── adapter/
    │   ├── inbound/
    │   │   └── web/                # REST Controllers (Adapters de Entrada)
    │   └── outbound/
    │       ├── persistence/        # JPA Entities, JPA Repositories, Mappers e Adapters de Persistência
    │       ├── messaging/          # Produtores/Consumidores Kafka ou RabbitMQ
    │       └── security/           # Implementações de criptografia, JWT, etc.
    └── configuration/              # Beans Spring, Security Config, Database Config
```

---

## 📅 Cronograma de Implementação em Fases

---

### 🧱 FASE 1: Fundação - Shared Kernel & Identity Context

O objetivo desta fase é estabelecer a base compartilhada de tipos e migrar o subdomínio de suporte de Identidade, que serve de base para todos os outros contextos.

#### 1.1. Shared Kernel (`shared-kernel` module)
Crie um módulo Maven separado ou um pacote isolado contendo apenas Value Objects de identidade e tipos compartilhados fisicamente:
* **Value Objects de Identidade**:
  * `UsuarioId` (UUID wrapper)
  * `SalaId` (UUID wrapper)
  * `AtividadeId` (UUID wrapper)
  * `PostagemId` (UUID wrapper)
  * `ComentarioId` (UUID wrapper)
* **Value Objects de Negócio Globais**:
  * `Email` (Validação regex RFC 5322 no construtor)
  * `Papel` (Enum: `ROLE_ALUNO`, `ROLE_PROFESSOR`, `ROLE_ADMIN`)
  * `CodigoSala` (Validação de formato alfanumérico de 8 caracteres)
* **Abstrações de Eventos**:
  * `DomainEvent` (Interface base com `eventId`, `occurredOn`, `correlationId`)

#### 1.2. Identity Domain Model (Modelo Rico)
* **Agregado `Usuario`**:
  * Campos: `UsuarioId`, `Email`, `SenhaHash`, `String nome`, `PerfilUsuario`, `Set<Papel>`, `SaldoGamificacao`, `LocalDateTime dataCadastro`.
  * Invariantes:
    * Impedir saldo de gamificação negativo.
    * Garantir que o usuário tenha pelo menos um papel ativo.
    * Métodos de negócio: `creditarGamificacao(Pontos, Moedas)`, `alterarSenha(SenhaHash)`, `atualizarPerfil(PerfilUsuario)`.
* **Agregado `ConexaoAmizade`**:
  * Campos: `ConexaoId`, `UsuarioId solicitanteId`, `UsuarioId destinatarioId`, `StatusAmizade` (Enum: `PENDENTE`, `ACEITO`, `RECUSADO`, `BLOQUEADO`), `LocalDateTime dataSolicitacao`, `LocalDateTime dataResposta`.
  * Invariantes:
    * Impedir auto-conexão (`solicitanteId` igual a `destinatarioId`).
    * Máquina de estados estrita: apenas o destinatário pode aceitar/recusar uma solicitação pendente.
* **Domain Events**:
  * `UsuarioCadastradoEvent`
  * `SolicitacaoAmizadeEnviadaEvent`
  * `AmizadeAceitaEvent`

#### 1.3. Ports & Adapters (Identity)
* **Outbound Ports**:
  * `UsuarioRepository` (Interface de domínio)
  * `ConexaoAmizadeRepository` (Interface de domínio)
  * `PasswordHasherPort` (Interface para hash de senhas)
  * `EventPublisherPort` (Interface para publicação de eventos)
* **Adapters**:
  * `UsuarioRepositoryAdapter` (Implementa `UsuarioRepository` usando `UsuarioJpaRepository` e `UsuarioEntityMapper`)
  * `BCryptPasswordHasherAdapter` (Implementa `PasswordHasherPort` usando Spring Security BCrypt)
  * `KafkaEventPublisherAdapter` (Implementa `EventPublisherPort` usando Spring Kafka com Transactional Outbox)

#### 1.4. Testes de Domínio (Identity)
* Implementar testes unitários puros (sem Spring Boot Context) para `Usuario` e `ConexaoAmizade` usando JUnit 5 e AssertJ. Cobertura mínima de 90% das ramificações de regras de negócio.

---

### 🎓 FASE 2: Core Domain - Academic Context

Esta fase foca no coração do negócio: o ciclo de vida do ensino-aprendizagem, salas de aula virtuais, atividades e submissões.

#### 2.1. Academic Domain Model
* **Agregado `SalaDeAula`**:
  * Campos: `SalaId`, `CodigoSala`, `String nome`, `UsuarioId criadorId`, `List<MembroSala> membros`.
  * Invariantes:
    * Geração automática de `CodigoSala` único de 8 caracteres.
    * O criador é automaticamente adicionado como membro com papel `CRIADOR`/`DOCENTE`.
    * Impedir membros duplicados.
* **Agregado `Atividade`**:
  * Campos: `AtividadeId`, `SalaId`, `String titulo`, `String descricao`, `PrazoEntrega`, `Pontuacao`, `StatusAtividade` (Enum: `RASCUNHO`, `PUBLICADA`, `ENCERRADA`, `CANCELADA`).
  * Invariantes:
    * `PrazoEntrega` deve ser obrigatoriamente no futuro em relação à publicação.
    * `Pontuacao` deve ser positiva (0.0 a 100.0).
* **Agregado `SubmissaoAtividade`**:
  * Campos: `SubmissaoId`, `AtividadeId`, `UsuarioId alunoId`, `ArquivoSubmissao`, `LocalDateTime dataEntrega`, `Nota`, `Feedback`, `StatusSubmissao`.
  * Invariantes:
    * Apenas uma submissão ativa por aluno por atividade.
    * A `Nota` atribuída não pode exceder a `Pontuacao` máxima da atividade.
* **Domain Services**:
  * `CalculoDesempenhoAcademicoDomainService`: Consolida médias e taxas de entrega.
  * `ValidadorAcessoDocenteDomainService`: Valida permissões de escrita na sala.

#### 2.2. Saga de Criação de Sala (`CriarSalaSaga`)
Implementar o orquestrador de Saga para coordenar a criação de salas de aula virtuais de forma resiliente:
1. **Passo 1 (Local)**: Persistir `SalaDeAula` no banco de dados (Academic Context).
2. **Passo 2 (Async)**: Publicar `SalaCriadaEvent` via Outbox Pattern.
3. **Passo 3 (Social)**: O Social Context consome o evento e cria uma postagem automática de anúncio no feed.
4. **Passo 4 (Notification)**: O Notification Context consome o evento e envia e-mail de confirmação ao professor.
* **Compensação**: Se houver falha crítica nos passos subsequentes, disparar compensação para marcar a sala como `CANCELADA_POR_ERRO_SISTEMA` (Soft Delete) e remover postagens criadas.

#### 2.3. Transactional Outbox Pattern
Para garantir a entrega de eventos de domínio de forma atômica com a transação do banco de dados:
* Salvar o evento na tabela `outbox_event` na mesma transação de banco de dados do agregado.
* Um worker em background (Spring `@Scheduled` ou Debezium CDC) lê a tabela e publica no Kafka.
* Após a confirmação de publicação (Ack), o evento é marcado como processado ou removido.

---

### 📱 FASE 3: Supporting Domain - Social Context

Esta fase implementa o feed de postagens, comentários e interações de engajamento, utilizando camadas de anticorrupção para se integrar com os outros contextos.

#### 3.1. Social Domain Model
* **Agregado `Postagem`**:
  * Campos: `PostagemId`, `UsuarioId autorId`, `ConteudoPostagem` (texto + mídias), `LocalDateTime dataCriacao`, `Visibilidade` (Enum: `PUBLICO`, `AMIGOS`).
  * Invariantes:
    * Moderação automática básica de palavras proibidas no `ConteudoPostagem`.
    * Apenas o autor pode editar ou alterar a visibilidade da postagem.
* **Agregado `Comentario`**:
  * Campos: `ComentarioId`, `UsuarioId autorId`, `EntidadeAlvo` (Polimórfico: `POSTAGEM`, `ATIVIDADE`), `String texto`, `LocalDateTime dataCriacao`.
* **Agregado `InteracaoUsuario`**:
  * Campos: `InteracaoId`, `UsuarioId`, `EntidadeAlvo`, `TipoInteracao` (Enum: `CURTIDA`, `COMPARTILHAMENTO`).
  * Invariantes:
    * Garantir unicidade de interação por usuário por alvo (impedir múltiplos likes na mesma postagem).

#### 3.2. Anti-Corruption Layers (ACLs)
Para evitar que o modelo do Social Context seja corrompido pelos modelos de Identity e Academic:
* **`SocialIdentityPort`**:
  * Interface no domínio do Social que define consultas de amizade e perfis.
  * O adaptador na infraestrutura traduz as chamadas para o `Identity Context` (via chamadas de serviço internas ou consultas otimizadas de banco de dados).
* **`AcademicContextACL`**:
  * Traduz eventos como `AtividadePublicadaEvent` do Academic Context para o formato de `Postagem` do Social Context, permitindo que atividades apareçam automaticamente no feed.

---

### 🔔 FASE 4: Integração, Subdomínios Genéricos & Quality Gates

Esta fase consolida a integração assíncrona entre todos os contextos e estabelece as barreiras de qualidade para o pipeline de CI/CD.

#### 4.1. Notification Context (Generic Subdomain)
* Consome eventos de domínio do Kafka:
  * `UsuarioCadastradoEvent` -> Envia e-mail de boas-vindas.
  * `SolicitacaoAmizadeEnviadaEvent` -> Envia notificação push/in-app para o destinatário.
  * `AtividadePublicadaEvent` -> Notifica todos os alunos membros da sala.
  * `SubmissaoAvaliadaEvent` -> Notifica o aluno sobre a nota e feedback.

#### 4.2. Analytics Context (Generic Subdomain)
* Consome o stream completo de eventos de domínio para alimentar tabelas otimizadas para leitura (CQRS / Read Models) ou dashboards de engajamento acadêmico.

#### 4.3. Testes de Contrato (Consumer-Driven Contracts)
* Implementar testes de contrato usando **Pact** ou **Spring Cloud Contract** para garantir que alterações nos schemas de eventos de um contexto (Upstream) não quebrem os consumidores (Downstream).

#### 4.4. CI/CD Quality Gates
Configurar o pipeline de integração contínua para falhar se:
* A cobertura de testes unitários da camada `domain` de qualquer contexto for inferior a **90%**.
* Qualquer teste de contrato falhar.
* Houver dependências cíclicas entre contextos (validado via **ArchUnit**).

---

## 🛠️ Padrões de Código para o Desenvolvedor Sênior

### 1. Implementação de Value Object Rico (Exemplo)
```java
package br.com.plataforma.sharedkernel.valueobject;

import br.com.plataforma.sharedkernel.exception.EmailInvalidoException;
import java.util.regex.Pattern;

public final record Email(String endereco) {
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public Email {
        if (endereco == null || endereco.isBlank()) {
            throw new EmailInvalidoException("O endereço de e-mail não pode ser vazio.");
        }
        if (!EMAIL_PATTERN.matcher(endereco).matches()) {
            throw new EmailInvalidoException("Endereço de e-mail malformado: " + endereco);
        }
    }
}
```

### 2. Proteção de Invariantes no Agregado (Exemplo)
```java
package br.com.plataforma.identity.domain.model.aggregate;

import br.com.plataforma.sharedkernel.valueobject.UsuarioId;
import br.com.plataforma.identity.domain.model.valueobject.SaldoGamificacao;
import br.com.plataforma.identity.domain.exception.SaldoInsuficienteException;

public class Usuario {
    private final UsuarioId id;
    private SaldoGamificacao saldo;
    // ...outros campos...

    public void debitarGamificacao(SaldoGamificacao debito) {
        if (this.saldo.pontos() < debito.pontos() || this.saldo.moedas() < debito.moedas()) {
            throw new SaldoInsuficienteException("Saldo de gamificação insuficiente para realizar o débito.");
        }
        this.saldo = new SaldoGamificacao(
            this.saldo.pontos() - debito.pontos(),
            this.saldo.moedas() - debito.moedas()
        );
    }
}
```

### 3. Tratamento de Exceções de Domínio
* Todas as exceções lançadas no domínio devem herdar de uma classe base `DomainException` (que herda de `RuntimeException`).
* Os controllers na camada de infraestrutura devem capturar essas exceções usando um `@ControllerAdvice` global e traduzi-las para códigos HTTP apropriados (ex: `DomainException` -> `400 Bad Request` ou `422 Unprocessable Entity`).

---

## 🚀 Próximos Passos Recomendados para os Próximos Commits

1. **Commit 1**: Inicialização do módulo `shared-kernel` e criação de todos os Value Objects de Identidade (`UsuarioId`, `SalaId`, etc.) e globais (`Email`, `CodigoSala`).
2. **Commit 2**: Criação do esqueleto do módulo `identity-context` com a camada `domain` completa (Agregados `Usuario` e `ConexaoAmizade` ricos, Domain Events e Ports).
3. **Commit 3**: Implementação dos testes unitários puros do domínio de Identidade para garantir 100% de cobertura nas invariantes.
4. **Commit 4**: Implementação da camada de `infrastructure` do `identity-context` (Adapters JPA, mappers e controllers REST finos).
