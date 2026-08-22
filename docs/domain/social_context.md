# 📱 Contexto Social (Social Context)

Este documento detalha as regras de negócio, entidades e objetos de valor que compõem o **Contexto Social**, responsável por promover o engajamento e a interação entre os membros da comunidade acadêmica.

## 🎯 Objetivo do Contexto
Gerenciar a camada de interatividade da plataforma, permitindo que usuários compartilhem conhecimentos, comentem em tópicos e interajam através de curtidas, criando um ambiente de rede social voltado ao aprendun.

---

## 🏗️ Modelagem de Domínio

### 1. Entidades (Entities)
*Objetos com identidade única que persiste ao longo do tempo.*

#### **Postagem**
O elemento central de comunicação no feed.
- **Atributos Principais:** `id`, `conteudo`, `autor`, `dataCriacao`.
- **Responsabilidades:**
    - Armazenar o conteúdo textual ou multimídia compartilhado.
    - Vincular uma publicação a um autor específico.
    - Permitir a exclusão pelo próprio autor.

#### **Comentario**
A resposta ou interação direta sobre uma postagem ou atividade.
- **Atributos Principais:** `id`, `texto`, `autor`, `postagem_ref`, `atividade_ref`.
- **Responsabilidades:**
    - Permitstring a discussão em torno de um tópico.
    - Vincular-se tanto a postagens quanto a atividades (contexto polimórfico).

#### **InteracaoUsuario**
O registro de ações rápidas de engajamento (como curtidas).
- **Atributos Principais:** `id`, `tipo` (LIKE, etc.), `usuario`, `entidade_alvo`.
- **Responsabilidades:**
    - Registrar o "like" em uma postagem ou comentário.
    - Contabilizar o engajamento para fins de algoritmos de feed.

---

### 2. Objetos de Valor (Value Objects)

#### **TipoInteracao**
- **Regra:** Define a natureza da interação realizada.
- **Valores:** `LIKE`, `REPLY`, `SHARE`.
- **Validação:** Garante que apenas interações suportadas pelo sistema sejam processadas.

#### **FiltroFeed**
- **Regra:** Critério de filtragem para a visualização do feed.
- **Valores:** `TODAS`, `AMIGOS`, `MAIS_CURTIDAS`.
- **Responsabilidades:** Determinar quais postagens devem ser carregadas na visão do usuário.

---

## ⚙️ Regras de Negócio (Business Rules)

### Gestão de Engajamento
1.  **Visibilidade de Postagem:** O conteúdo de uma postagem pode ser restrito ao círculo de amigos ou público para a comunidade.
2.  **Propriedade e Moderação:** Um usuário tem permissão total para excluir suas próprias postagens e comentários.
3.  **Integridade da Interação:** Uma curtida (`LIKE`) não pode ser duplicada pelo mesmo usuário na mesma postagem.

### Fluxo de Feed
1.  **Ordenação por Relevância:** O sistema deve ser capaz de reordenar o feed com base no número de interações (ex: "Mais Curtidas").
2.  **Contextualização:** Comentários devem sempre referenciar um alvo válido (uma `Postagem` ou uma `Atividade`).

---

## 🔗 Relacionamentos Principantes
- **Usuario 1 ↔ N Postagem:** Um usuário é o autor de múltiplas postagens.
- **Postagem 1 ↔ N Comentario:** Uma postagem pode receber diversos comentários.
- **Usuario 1 ↔ N InteracaoUsuario:** Um usuário gera diversas interações no sistema.
- **Atividade 1 ↔ N Comentario:** Atividades acadêmicas também podem receber discussões via comentários.
