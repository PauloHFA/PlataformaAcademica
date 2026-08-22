# 🚀 Por que utilizar DDD neste projeto?

A adoção de **Domain-Driven Design (DDD)** em uma plataforma acadêmica pode parecer um exagero inicial, mas ela é a resposta estratégica para evitar que o software se torne impossível de manter à medida que novas funcionalidades (como chats, certificados ou fóruns) são adicionadas.

---

## ❌ Os Problemas que estamos resolvendo

### 1. O "Big Ball of Mud" (A Grande Bola de Lama)
Sem DDD, as responsabilidades de uma funcionalidade (ex: uma nota de atividade) ficam espalhadas entre o `Controller`, o `Service` e o `Repository`. Com o tempo, o código torna-se um emaranhado onde alterar uma regra de "nota" pode quebrar acidentalmente o sistema de "amizades".
* **Solução DDD:** O uso de **Bounded Contexts** isola as funcionalidades. O que acontece no contexto `social` não afeta o contexto `academic`.

### 2. O Modelo Anêmico (Lógica Espalhada)
Em arquiteturas tradicionais, as entidades são apenas "sacos de dados" (apenas getters e setters). A inteligência do sistema fica escondida em Services gigantescos. Isso torna o código difícil de entender e propenso a erros.
* **Solução DDD:** O uso de **Rich Domain Models** (Entidades e Value Objects) traz a lógica para dentro do objeto. Se uma regra diz que "uma nota não pode ser negativa", essa regra vive dentro da classe `Nota`, e não em um Service remoto.

### 3. A Barreira da Linguagem (Gap de Comunicação)
Muitas vezes, o desenvolvedor chama algo de `UserUpdateService`, enquanto o professor chama de `AtualizarPerfilDocente`. Essa diferença gera confusão e bugs de interpretação.
* **Solução DDD:** A criação da **Ubiquitous Language** (Linguagem Ubíqua) garante que o nome da classe no Java seja exatamente o mesmo termo usado na documentação e nas conversas com os stakeholders.

---

## ✅ Os Benefícios para a Plataforma Acadêmica

### 1. Testabilidade Elevada
Como as regras de negócio estão encapsuladas em **Value Objects** (como `Email` ou `CodigoSala`), podemos escrever testes unitários extremamente rápidos e simples que garantem que o sistema nunca aceite dados inválidos, sem precisar subir um banco de dados ou um servidor Spring.

### 2. Manutenibilidade e Evolução
Se amanhã decidirmos mudar a regra de "como uma atividade é aprovada", alteramos apenas o objeto `Atividade`. O resto do sistema (Frontend, Controllers, Repositories) permanece intacto, pois a interface de comunicação não mudou, apenas a inteligência interna.

### 3. Proteção contra Erros de Negócio (Invariantes)
O uso de **Value Objects** garante que o sistema seja "impossível de usar incorretamente". É impossível instanciar um `CodigoSala` inválido. O erro é detectado no momento da criação do objeto, e não muito tempo depois.

---

_Documento gerado para fins de documentação arquitetural do projeto._
