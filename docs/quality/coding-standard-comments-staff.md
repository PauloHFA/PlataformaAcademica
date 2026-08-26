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

## ✅ Aplicação
O padrão atualizado já está refletido nos arquivos documentados (`RecomendacaoService.java`, `PerfilServiceImpl.java`, etc.).
