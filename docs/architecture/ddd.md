# 🏗️ Documentação de Domínio (DDD) - Plataforma Acadêmica

Este documento descreve a visão de **Domain-Driven Design (DDD)** aplicada à plataforma, identificando os domínunios, subdomínios e as regras de negócio que compõem o sistema.

## 🎯 Visão Geral do Domínio
O domínio principal da aplicação é o **Ecossistema Acadêmico**. O objetivo central é facilitar a interação entre alunos e professores através de ferramentas colaborativas e gestão de conteúdo educacional.

---

## 🗺️ Mapeamento de Subdomínios

Para organizar a complexidade, dividimos o sistema em três subdomínios principais:

### 1. Core Domain (Domínio Principal)
*Foco no valor central do negócio.*

#### **A. Gestão Acadêmica**
Este é o coração da plataforma. Ele lida com a estrutura de ensino e as atividades práticas.
- **Regras de Negócio:**
    - Criação, edição e exclusão de salas virtuais.
    - Controle de acesso baseado em papéis (ex: apenas criadores podem excluir salas).
    - Gestão de atividades acadêmicas com prazos específicos e sistemas de pontuação.
    - Processamento de submissões de arquivos por parte dos alunos.
    - Sistema de correção e feedback pelos professores.

#### **B. Engajamento Social**
Focado na interação dinâmica entre os usuários fora do contexto estrito de "entrega de tarefas".
- **Regras de Negócio:**
    - Publicação de postagens no feed.
    - Sistema de curtidas para engajamento.
    - Filtros de visualização (Amint, Mais Curtidos).

### 2. Supporting Subdomains (Subdomínios de Suporte)
*Funcionalidades que apoiam o core domain, mas não são o objetivo principal.*

#### **C. Identidade e Perfil**
Gerencia quem é o usuário e como ele se apresenta no sistema.
- **Regras de Negócio:**
    - Autenticação via login padrão ou redes sociais (Google/Facebook).
    - Gestão de perfis (foto, biografia).
    - Sistema de amizades (solicitação, aceitação e remoção).
    - Busca de usuários para networking.

### 3. Generic Subdomains (Subdomínios Genéricos)
*Funcionalidades comuns que poderiam ser fornecidas por ferramentas externas.*

#### **D. Notificações**
Sistema de alertas sobre interações, novas mensagens ou atualamentos de status.

---

## 🏗️ Contextos Delimitados (Bounded Contexts)

Para a implementação técnica no Spring Boot, os módulos serão organizados nos seguintes contextos:

1.  **Contexto de Identidade (`identity`):** Abrange usuários, perfis e amizades.
2.  **Contexto Acadêmico (`academic`):** Abrange salas, atividades e submissões.
3.  **Contexto Social (`social`):** Abrange postagens e interações do feed.

---

## 📝 Glossário de Termos (Linguagem Ubíqua)
*Termos que devem ser usados consistentemente no código para refletir o negócio:*

- **Sala:** Espaço virtual onde as atividades são agrupadas.
- **Atividade:** Tarefa específica com prazo e valor em pontos.
- **Submissão:** O ato do aluno entregar um trabalho (arquivo ou texto) para uma atividade.
- **Feedback:** Comuntoário e nota atribuídos pelo professor a uma submissão.
- **Seguidor/Amigo:** Usuários que possuem conexão direta no sistema de rede social.

# 🔐 Contexto de Identidade (Identity Context)

Este documento detalha as regras de negócio, entidades e objetos de valor que compõem o **Contexto de Identidade**, responsável por gerenciar quem são os usuários e como eles se relacionam na plataforma.

## 🎯 Objetivo do Contexto
Gerenciar a identidade digital dos usuários, seus perfis sociais, autenticação e o estabelecimento de conexões (amizades).

---

## 🗺️ Entidades e Objetos de Valor

### 1. Usuário
*O principal objeto de valor no contexto de identidade.*
- **Atributos:**
    - `id`: Identificador único.
    - `nome`: Nome completo.
    - `email`: Endereço de e-mail.
    - `senha`: Senha (criptografada).
    - `dataNascimento`: Data de nascimento.
    - `perfil`: Perfil do usuário (foto, biografia).
    - `papéis`: Papéis no sistema (ex: aluno, professor, admin).
- **Regras de Negócio:**
    - Um usuário só pode ter um e-mail único.
    - A senha deve ser forte (mínimo 8 caracteres, com letras maiúsculas, minúsculas e números).
    - O perfil é opcional, mas recomendado.
    - Os papéis são definidos pelo sistema e podem ser modificados.

### 2. Perfil
*Representa a apresentação do usuário no sistema.*
- **Atributos:**
    - `foto`: Foto do usuário.
    - `biografia`: Biografia do usuário.
    - `localização`: Localização do usuário.
    - `links`: Links para redes sociais.
- **Regras de Negócio:**
    - A foto deve ser um arquivo válido (JPEG, PNG, GIF).
    - A biografia pode ser um texto longo.
    - A localização é opcional.
    - Os links são para redes sociais.

### 3. Amizade
*Conexão entre dois usuários.*
- **Atributos:**
    - `usuario1`: Primeiro usuário.
    - `usuario2`: Segundo usuário.
    - `status`: Status da amizade (ex: aceito, pendente).
- **Regras de Negócio:**
    - A amizade só pode existir entre dois usuários.
    - O status é definido pelo sistema.
    - A solicitação é feita por um usuário e aceita por outro.

---

## 📝 Regras de Negócio

### 1. Autenticação
- Um usuário pode se autenticar com login e senha.
- Um usuário pode se autenticar com redes sociais.

### 2. Criação de Perfil
- Um usuário pode criar um perfil.
- Um perfil pode ser editado.

### 3. Amizade
- Um usuário pode solicitar amizade a outro.
- A amizade só pode existir entre dois usuários.
- O status da amizade é definido pelo sistema.

---

## 🏗️ Implementação Técnica

O Contexto de Identidade será implementado com os seguintes componentes:

1.  **Autenticação:**
    - Login e senha.
    - Redes sociais.

2.  **Perfil:**
    - Foto, biografia, localização, links.

3.  **Amizade:**
    - Solicitação e aceitação.
    - Status da amizade.

---

## 📝 Glossário de Termos (Linguagem Ubíqua)
*Termos que devem ser usados consistentemente no código para refletir o negócio:*

- **Sala:** Espaço virtual onde as atividades são agrupadas.
- **Atividade:** Tarefa específica com prazo e valor em pontos.
- **Submissão:** O ato do aluno entregar um trabalho (arquivo ou texto) para uma atividade.
- **Feedback:** Comuntoário e nota atribuídos pelo professor a uma submissão.
- **Seguidor/Amigo:** Usuários que possuem conexão direta no sistema de rede social.
