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
| `model/` | 25 entidades e enums JPA | ✅ 25/25 |
| `repository/` | Repositórios Spring Data JPA | ✅ 21/21 |

### ⏳ Em Andamento / Pendentes
| Pacote | Status | Observação |
|---|---|---|
| `service/` | 🟡 5/32 | Serviços principais documentados |
| `controller/` | 🟡 18/20 | Todos os controllers principais documentados (Comentario, Atividade, Artigo, Comunidade, Usuario, Dashboard, Amizade, Perfil, Postagem, SaladeAula, SubmissaoAtividade, SolicitacaoEntrada, Recomendacao, Professor, Mensagem, Frequencia, File, Plataforma, MembroComunidade, Health) |
| `mapper/` | ✅ 10/10 | `ComentarioMapper`, `ComunidadeMapper`, `AtividadeMapper`, `ArtigoMapper`, `AmizadeMapper`, `SalaDeAulaMapper`, `PostagemMapper`, `PerfilMapper`, `SubmissaoAtividadeMapper`, `UsuarioMapper` |
| `dto/` | 🟡 9/26 | `ComentarioDTO`, `AtividadeDTO`, `ArtigoDTO`, `ComunidadeDTO`, `AmizadeDTO`, `PostagemDTO`, `UsuarioResponseDTO`, `PerfilDTO`, `SubmissaoAtividadeDTO` |
| `config/`, `security/`, `interceptor/` | ⚪ 0% | Próximas etapas |

---

## ✅ Aplicação
O padrão atualizado já está refletido nos arquivos documentados (`RecomendacaoService.java`, `PerfilServiceImpl.java`, etc.).
