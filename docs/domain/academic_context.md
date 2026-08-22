# 🎓 Contexto Acadêmico (Academic Context)

Este documento detalha as regras de negócio, entidades e objetos de valor que comporão o **Contexto Acadêmico**, o coração da Plataforma Acadêmica.

## 🎯 Objetivo do Contexto
Gerenciar a estrutura de ensino-aprendizagem, permitindo que professores criem ambientes de estudo (Salas), lancem tarefas (Atividades) e acompanhem o progresso dos alunos através de entregas (Submissões).

---

## 🏗️ Modelagem de Domínio

### 1. Entidades (Entities)
*Objetos com identidade única que persiste ao longo do tempo.*

#### **SaladeAula**
Representa o ambiente virtual de uma disciplina ou grupo de estudo.
- **Atributos Principamente:** `id`, `nome`, `codigoSala` (identificador único para convites).
- **Responsabilidades:** 
    - Agrupar alunos e professores.
    - Servir como container para atividades acadêmicas.
    - Gerenciar a lista de membros participantes.

#### **Atividade**
Representa uma tarefa ou desafio proposto dentro de uma sala.
- **Atributos Principamente:** `id`, `titulo`, `descricao`, `dataEntrega`, `pontos`.
- **Responsabilidades:**
    - Definir o escopo do que deve ser entregue.
    - Estabelecer o prazo limite para submissão.
    - Determinar o peso (pontuação) da tarefa.

#### **SubmissaoAtividade**
Representa a entrega realizada pelo aluno para uma atividade específica.
- **Atributos Principamente:** `id`, `urlDocumento`, `dataSubmissao`, `nota`, `feedback`.
- **Responsabilidades:**
    - Registrar o ato da entrega do aluno.
    - Armazenar o conteúdo (link/arquivo) enviado.
    - Permitir que o professor registre a avaliação e o feedback.

---

### 2. Objetos de Valor (Value Objects)
*Objetos sem identidade própria, definidos apenas por seus atributos.*

#### **CodigoSala**
- **Regra:** Deve seguir um padrão alfanumérico específico (ex: 8 caracteres) para garantir a segurança e facilamente de compartilhamento.
- **Validação:** Impede a criação de salas com códigos inválidos ou curtos demais.

#### **TipoDocumentoSubmissao**
- **Regra:** Restringe os formatos aceitos para entrega (ex: `PDF`, `DOCX`, `ZIP`, `LINK`).
- **Validação:** Garante que o professor e o aluno estejam alinhados sobre o formato esperado.

#### **Nota**
- **Regra:** Um valor numérico decimal.
- **Validação:** Deve estar sempre dentro do intervalo de `0.0` a `valor_maximo_da_atividade`.

---

## ⚙️ Regras de Negócio (Business Rules)

### Gestão de Salas e Acesso
1.  **Criação de Sala:** Somente usuários autenticados podem criar salas.
2.  **Acesso à Sala:** Um usuário só pode visualizar atividades de uma sala se for membro dela (via `codigoSala` ou convite).
3.  **Privacidade:** O criador da sala possui poderes administrativos sobre ela (ex: exclusão).

### Ciclo de Vida da Atividade e Submissão
1.  **Prazo de Entrega:** Uma `SubmissaoAtividade` não pode ter uma `dataSubmissao` posterior à `dataEntrega` da `Atividade`.
2.  **Integridade do Arquivo:** O formato do arquivo enviado deve respeitar o `TipoDocumentoSubmissao` definido na atividade.
3.  **Fluxo de Avaliação:** 
    - Uma submissão nasce com o status `recebida = false`.
    - Ao receber o arquivo, o sistema marca `recebida = true`.
    - O professor pode adicionar um `feedback` e uma `nota` apenas após a submissão ser registrada.
4.  **Unicidade de Entrega:** (Regra a implementar) Um aluno só pode realizar uma única submissão válida por atividade.

---

## 🔗 Relacionamentos Principantes
- **SaladeAula 1 ↔ N Atividade:** Uma sala contém múltiplas atividades.
- **Atividade 1 ↔ N SubmissaoAtividade:** Uma atividade recebe várias entregas de alunos diferentes.
- **Usuario 1 ↔ N SaladeAula (Membros):** Um usuário pode participar de diversas salas.
- **Usuario 1 ↔ N SubmissaoAtividade (Aluno):** Um aluno pode realizar múltiplas submissões em diferentes atividades.