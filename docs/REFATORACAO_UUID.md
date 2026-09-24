# Plano de Refatoração: Migração completa para UUID

## Objetivo
Alinhar todas as entidades, DTOs, services, controllers e o frontend para usar `UUID` como identificador, eliminando o conflito `bigint` × `uuid` no PostgreSQL.

## Estado Atual
- **Entidades JPA:** A maioria das entidades (`Usuario`, `Postagem`, `Comentario`, `Atividade`, etc.) já utiliza `UUID` como `@Id`. ✅
- **DTOs, Services, Controllers:** Ainda precisam de revisão para garantir consistência com `UUID`.
- **Frontend:** Ainda utiliza `id: number` ou `string | number` em alguns modelos.

## Etapas

### Etapa 1: Backend - Entidades JPA (Concluído)
- [x] `Usuario.java`
- [x] `Postagem.java`
- [x] `Comentario.java`
- [x] `Atividade.java`
- [x] `SubmissaoAtividade.java`
- [x] `Plataforma.java`
- [x] `RecomendacaoUsuario.java`
- [x] `SalaDeAulaEntity.java`
- [x] `Mensagem.java`
- [x] `Comunidade.java`
- [x] `Perfil.java`
- [x] `ConexaoAmizade.java`
- [x] `SalaMembros.java`
- [x] `SolicitacaoEntrada.java`
- [x] `Artigo.java`
- [x] `Curtida.java`
- [x] `Frequencia.java`

### Etapa 2: Backend - DTOs e Mappers (Pendente)
- [ ] Revisar `UsuarioResponseDTO` e outros DTOs para garantir que IDs sejam `UUID`.
- [ ] Atualizar `UsuarioMapper` e outros mappers.

### Etapa 3: Backend - Repositories (Revisar)
- [ ] Verificar se todos os métodos de busca utilizam `UUID`.

### Etapa 4: Backend - Services (Pendente)
- [ ] Atualizar assinaturas de métodos que recebem/retornam `Long id`.

### Etapa 5: Backend - Controllers (Pendente)
- [ ] Atualizar parâmetros de controllers (`@PathVariable`, `@RequestParam`) para `UUID`.

### Etapa 6: Frontend - Models (Pendente)
- [ ] `usuario.model.ts` - definir `id: string` (UUID).
- [ ] Atualizar outros modelos que referenciam IDs.

### Etapa 7: Frontend - Services & Components (Pendente)
- [ ] Atualizar chamadas de serviço e componentes que manipulam IDs.

### Etapa 8: Teste de Comunicação (Pendente)
- [ ] Validar fluxo completo de login/cadastro.

### Etapa 5: Backend - Controllers
- `UsuarioController.java` - parâmetros `Long` → `UUID`
- `ProfessorController.java`
- Demais controllers

### Etapa 6: Frontend - Models
- `usuario.model.ts` - `id: string` (UUID)
- `login-request.model.ts`
- `login-response.model.ts`
- Demais models que referenciam `usuarioId`

### Etapa 7: Frontend - Services
- `usuario.service.ts` - já usa string no URL
- Atualizar tipos de retorno

### Etapa 8: Frontend - Components
- Componentes que fazem `usuarioId: number` → `usuarioId: string`
- localStorage que armazena `id` do usuário

### Etapa 9: Banco de Dados
- Dropar todas as tabelas
- Recriar (Hibernate cria com `ddl-auto=update` ou `create`)

### Etapa 10: Teste de Comunicação
- Iniciar backend
- Iniciar frontend
- Testar login/cadastro
- Verificar console do navegador
- Verificar logs do backend

## Considerações Importantes

### Converter `Long` para `UUID` em:
1. **Campos `id`** das entidades
2. **Chaves estrangeiras** (`@JoinColumn`, `@ManyToOne`, etc.)
3. **DTOs** que expõem IDs
4. **Parâmetros de controllers** (`@PathVariable`, `@RequestParam`)
5. **Assinaturas de métodos** em services
6. **Tipos no frontend** (TypeScript)

### Pontos de Atenção
- Lombok `@Data` e `@EqualsAndHashCode` precisam ser revisados
- Herança JPA (`@Inheritance`) precisa de tipos consistentes
- Verificar todos os `@ElementCollection` (ex: `membrosIds` em `SalaDeAulaEntity`)
- Senha continua sendo `String` (sem mudança)

## Comando Útil - Dropar Banco
```sql
DROP DATABASE plataforma_academica;
CREATE DATABASE plataforma_academica;
```

## Configuração Recomendada (após refatoração)
```properties
# application.properties
spring.jpa.hibernate.ddl-auto=create-drop  # Apenas para reset inicial
# Ou usar: spring.jpa.hibernate.ddl-auto=update
```

## Progresso
- [x] Etapa 1: Usuario.java
- [x] Etapa 1: SaladeAulaJpaRepository.java (@Repository)
- [ ] Etapa 1: Demais entidades
- [ ] Etapa 2: DTOs
- [ ] Etapa 3: Repositories
- [ ] Etapa 4: Services
- [ ] Etapa 5: Controllers
- [ ] Etapa 6: Frontend Models
- [ ] Etapa 7: Frontend Services
- [ ] Etapa 8: Frontend Components
- [ ] Etapa 9: Banco
- [ ] Etapa 10: Teste
