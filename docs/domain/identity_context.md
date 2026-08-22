# 🔐 Contexto de Identidade (Identity Context)

Este documento detalha as regras de negócio, entidades e objetos de valor que compõem o **Contexto de Identidade**, responsável por gerenciar quem são os usuários e como eles se relacionam na plataforma.

## 🎯 Objetivo do Contexto
Gerenciar a identidade digital dos usuários, seus perfis sociais, autenticação e o estabelecimento de conexões (amizades) dentro da rede acadêmica.

---

## 🏗️ Modelagem de Domínio

### 1. Entidades (Entities)

#### **Usuario**
A base de identidade para qualquer participante do sistema.
- **Atributos Principamente:** `id`, `nome`, `email`, `senha`, `dataNascimento`, `instituicaoEnsino`, `pontos`, `moedas`.
- **Responsabilidades:**
    - Representar a identidade fundamental do usuário.
    - Armazenar informações de contato e localização.
    - Manter o saldo de gamificação (`pontos` e `moedas`).

#### **Perfil** (Especialização de Usuario)
Uma extensão do usuário que foca na sua presença social e visual.
- **Atributos Principamente:** `fotoPerfil`, `descricao`.
- **Responsabilidades:**
    - Gerenciar a aparência pública do usuário.
    - Fornecer informações biográficas para interação social.

#### **Amizade**
Representa o vínculo de conexão entre dois usuários.
- **Atributos Principamente:** `usuario_origem`, `usuario_destino`, `status` (Solicitado, Aceito, Recusado).
- **Responsabilidades:**
    - Gerenciar o fluxo de solicitações de amizade.
    - Controlar o estado da conexão entre usuários.

---

### 2. Objetos de Valor (Value Objects)

#### **Email**
- **Regra:** Deve ser um endereço de e-mail válido e único no sistema.
- **Validação:** Garante a integridade do campo `email` na entidade `Usuario`.

#### **Endereco** (Extraído de campos de Usuario)
- **Regra:** Agrupamento de `cep`, `cidade` e `pais`.
- **Responsabilidades:** Encapsular a lógica de localização do usuário.

---

## ⚙️ Regras de Negócio (Business Rules)

### Gestão de Identidade e Perfil
1.  **Unicidade:** O `email` deve ser único para cada usuário no sistema.
2.  **Segurança:** A senha deve ser tratada de forma segura (apenas escrita/hash).
3.  **Gamificação:** O aumento de `pontos` ou `moedas` deve seguir regras de eventos do sistema (ex: completar atividades).

### Gestão de Conexões (Amizades)
1.  **Fluxo de Solicitação:** Um usuário envia uma solicitação (`ENVIO_SOLICITACAO_AMIZADE`) para outro.
2.  **Aceite/Recusa:** A conexão só é efetivada após a `ACEITACAO_AMIZADE`.
3.  **Interação Social:** O status de amizade deve permitir ou restringir visibilidade de postagens e comentários (dependendo do contexto social).

---

## 🔗 Relacionamentos Principais
- **Usuario 1 ↔ N Amizade (Enviada):** Um usuário pode enviar várias solicitações.
- **Usuario 1 ↔ N Amizade (Recebida):** Um usuário pode receber várias solicitações.
- **Perfil 1 ↔ 1 Usuario:** Cada perfil é uma especialização de um usuário específico.
