# Padrão de Comentários e Documentação de Código (Nível Sênior/Especialista)

## 🎯 Objetivo
Estabelecer um padrão rigoroso de documentação em código (JavaDoc e comentários *inline*) para todo o backend da **Plataforma Acadêmica**, garantindo legibilidade, rastreabilidade de regras de negócio e clareza arquitetural.

---

## 📐 Diretrizes do Padrão

### 1. Comentários em Classes (JavaDoc)
Toda classe (Service, Controller, Repository, Agregado, Value Object, DTO) deve possuir um cabeçalho JavaDoc explicando:
*   **Papel arquitetural**: Qual camada ela pertence e qual sua responsabilidade primária (ex: Orquestração de casos de uso, Tradução de dados, Regra de domínio pura).
*   **Contexto de Negócio**: Qual domínio do sistema ela atende (Identity, Academic, Social, etc.).
*   **Padrões aplicados**: Se utiliza Service Layer, Repository Pattern, Rich Domain Model, etc.

### 2. Comentários em Métodos (JavaDoc)
Todo método público ou de negócio deve conter:
*   **O que o método faz**: Descrição clara da ação executada.
*   **Parâmetros (`@param`)**: Significado de cada argumento.
*   **Retorno (`@return`)**: O que é retornado pela operação.
*   **Exceções (`@throws`)**: Quais regras de negócio podem falhar e lançar exceções.

### 3. Comentários *Inline* (`//`) no Corpo do Método
Para blocos de lógica complexa, algoritmos de cálculo (como similaridade, pontuação, criptografia) ou consultas customizadas, devem ser inseridos comentários *inline* explicando o **porquê** e o **como** o fluxo se comporta, dividindo o código em etapas lógicas (ex: `// Passo 1: ...`, `// Validação de invariantes`, `// Tratamento de vetores`).

---

## 📝 Exemplo Prático de Referência

```java
/**
 * Serviço responsável por calcular e gerenciar recomendações de usuários.
 * 
 * Camada: Application / Business Service
 * Utiliza algoritmos de similaridade baseados em interações (similaridade cosseno).
 */
@Service
@RequiredArgsConstructor
public class ExemploService {

    /**
     * Gera recomendações personalizadas para um usuário com base em seu histórico recente.
     * 
     * @param usuarioId ID identificador do usuário alvo.
     * @return Lista de DTOs contendo os usuários recomendados e scores.
     * @throws ResourceNotFoundException se o usuário não for encontrado no sistema.
     */
    @Transactional
    public List<RecomendacaoDTO> gerar(Long usuarioId) {
        // Passo 1: Validar e recuperar a entidade raiz do usuário alvo
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        // Passo 2: Filtrar interações dos últimos 30 dias para extrair entidades correlatas
        List<Interacao> interacoes = repositorio.buscarRecentes(usuario);

        // Passo 3: Aplicar cálculo de similaridade e montar score ponderado
        ...
    }
}
```
