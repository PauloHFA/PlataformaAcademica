# ADR-001: Padrão de Reconstituição de Agregados sem Reflexão

## Status
Aceito

## Contexto
Durante a auditoria da camada de domínio do contexto `identity`, identificamos que os agregados `Usuario` e `ConexaoAmizade` utilizavam campos `final` para garantir imutabilidade, mas o método de reconstituição (`reconstruir`/`reconstituir`) dependia de reflexão (via `UsuarioBuilder` interno) para definir valores em campos `final` após a construção do objeto.

Isso violava os princípios de:
1. **Pureza do Domínio**: O domínio não deve conhecer detalhes de infraestrutura (reflexão).
2. **Encapsulamento**: Quebra do encapsulamento via `setAccessible(true)`.
3. **Performance**: Reflexão tem overhead significativo.
4. **Manutenibilidade**: Código frágil e difícil de testar.

## Decisão
Adotar o padrão **Construtor Privado de Reconstituição** (Private Reconstruction Constructor):

1. Manter todos os campos como `final` (imutabilidade garantida pelo compilador).
2. Adicionar um construtor privado `package-private` (ou `private` com factory method estático) que recebe **todos** os campos do agregado.
3. O método estático `reconstruir`/`reconstituir` delega diretamente para este construtor.
4. A camada de infraestrutura (Mappers) chama o método estático público, sem necessidade de reflexão.

## Consequências

### Positivas
- **Domínio Puro**: Zero dependências de `java.lang.reflect` ou frameworks.
- **Imutabilidade Real**: Campos `final` garantidos pelo compilador em todos os cenários.
- **Performance**: Eliminação do overhead de reflexão.
- **Testabilidade**: Construtores diretos facilitam testes unitários de reconstituição.
- **Legibilidade**: Código explícito e declarativo.

### Negativas
- **Verbosity**: Construtores longos para agregados complexos (mitigado pelo padrão Factory Method).
- **Manutenção do Construtor**: Ao adicionar campos, deve-se atualizar o construtor de reconstituição (compilador força isso, o que é bom).

## Implementação Aplicada

### `ConexaoAmizade.java`
```java
// Construtor principal (criação nova)
private ConexaoAmizade(ConexaoId id, UsuarioId solicitanteId, UsuarioId destinatarioId) { ... }

// Construtor de reconstituição (package-private)
ConexaoAmizade(ConexaoId id, UsuarioId solicitanteId, UsuarioId destinatarioId,
               StatusAmizade status, LocalDateTime dataSolicitacao, LocalDateTime dataResposta) { ... }

public static ConexaoAmizade reconstituir(...) {
    return new ConexaoAmizade(id, solicitanteId, destinatarioId, status, dataSolicitacao, dataResposta);
}
```

### `Usuario.java`
```java
// Construtor principal (criação nova)
private Usuario(UsuarioId id, Email email, SenhaHash senhaHash, String nome, Papel papelInicial) { ... }

// Construtor de reconstituição (package-private)
Usuario(UsuarioId id, Email email, SenhaHash senhaHash, String nome,
        PerfilUsuario perfil, Set<Papel> papeis, SaldoGamificacao saldo,
        LocalDateTime dataCadastro, LocalDateTime ultimoLogin) { ... }

public static Usuario reconstruir(...) {
    return new Usuario(id, email, senhaHash, nome, perfil, papeis, saldo, dataCadastro, ultimoLogin);
}
```

## Referências
- Guia de Implementação DDD (Fase 1.2 - Identity Domain Model)
- Arquitetura Hexagonal: Isolamento do Domínio
- Effective Java, Item 1: Consider static factory methods instead of constructors

package com.plataforma_academica.plataforma.academic.domain.model;

import com.plataforma_academica.plataforma.sharedkernel.domain.model.identifier.SalaId;
import com.plataforma_academica.plataforma.sharedkernel.domain.model.identifier.UsuarioId;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Agregado Raiz que representa uma Sala de Aula Virtual.
 * 
 * Este agregado encapsula a lógica de negócio relacionada à gestão de salas de aula,
 * incluindo a criação, gestão de membros e controle de invariantes de negócio.
 * 
 * Segue os princípios de Domain-Driven Design (DDD), mantendo-se puro e sem
 * dependências de frameworks de persistência ou infraestrutura.
 */
public final class SalaDeAula {
    private final SalaId id;
    private String nome;
    private final String codigoSala;
    private final UsuarioId criadorId;
    private final List<MembroSala> membros;
    private final LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    // ... (Construtores, Factory Methods e Comportamentos de Negócio)
}