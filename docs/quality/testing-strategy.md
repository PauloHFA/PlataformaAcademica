# 🧪 Estratégia de Testes (Testing Strategy)

Esta estratégia define a abordagem de testes para a **Plataforma Acadêmica**, focada em garantir a integridade do domínio, a confiabilidade das integrações assíncronas e a experiência do usuário final.

---

## 🎯 Filosofia de Testes
Adotamos a **Pirâmide de Testes** com foco em **Shift-Left**:
1.  **Confiança**: Testes de domínio devem ser rápidos e determinísticos.
2.  **Isolamento**: Testes de infraestrutura utilizam *Testcontainers* para garantir ambiente real.
3.  **Contrato**: Integrações entre contextos são validadas por testes de contrato, não apenas testes E2E.

---

## 🏗️ Pirâmide de Testes

| Camada | Foco | Ferramentas | Velocidade |
| :--- | :--- | :--- | :--- |
| **E2E** | Fluxos críticos do usuário (Sagas) | Cypress | Lenta |
| **Contrato** | Integração entre Bounded Contexts | Pact / Spring Cloud Contract | Média |
| **Integração** | Repositórios, Kafka, APIs externas | JUnit 5, Testcontainers | Média |
| **Unidade** | Regras de negócio (Agregados, VOs) | JUnit 5, AssertJ | Rápida |

---

## 💎 Foco em DDD (Domain-Driven Design)

### 1. Testes de Domínio (Unidade)
*   **Agregados**: Testar invariantes. Ex: `Usuario` não pode ter saldo negativo.
*   **Value Objects**: Testar validações de formato e imutabilidade. Ex: `Email` inválido deve lançar `EmailInvalidoException`.
*   **Domain Services**: Testar lógica que envolve múltiplos agregados.
*   *Regra*: **Nenhum mock** nesta camada. Apenas POJOs e lógica pura.

### 2. Testes de Integração (Infraestrutura)
*   **Repositórios**: Validar mapeamento JPA/Hibernate e queries customizadas usando **Testcontainers** (banco de dados real).
*   **Mensageria (Kafka)**: Validar produtores/consumidores de eventos usando *EmbeddedKafka* ou *Testcontainers*.
*   **Portas de Saída**: Validar implementações de adaptadores (ex: `PasswordHasherPort`).

### 3. Testes de Contrato (Inter-Contexto)
*   **Objetivo**: Garantir que o `Social Context` não quebre se o `Academic Context` alterar o schema do evento `AtividadePublicadaEvent`.
*   **Abordagem**: *Consumer-Driven Contracts*. O consumidor define o contrato, o produtor valida.

### 4. Testes de Saga (Orquestração)
*   **Orquestrador**: Testar a máquina de estados da Saga.
*   **Compensação**: Simular falha em um passo (ex: falha no Social) e validar se a compensação (ex: cancelar sala no Academic) foi disparada.

---

## 🛠️ Ferramentas e Frameworks

*   **Linguagem**: Java 21+
*   **Framework**: Spring Boot 3.x
*   **Testes**: JUnit 5, AssertJ, Mockito (apenas para infraestrutura)
*   **Containers**: Testcontainers (PostgreSQL, Kafka, Redis)
*   **Contrato**: Pact
*   **E2E**: Cypress (Angular)

---

## 🚀 Integração CI/CD (Quality Gates)

1.  **Commit**: Executa testes de unidade.
2.  **Build**: Executa testes de integração (Testcontainers).
3.  **Pipeline**:
    *   Valida contratos (Pact).
    *   Executa testes E2E (Cypress) em ambiente de staging.
    *   **Gate de Qualidade**: Falha se cobertura de domínio < 90% ou se contratos falharem.
