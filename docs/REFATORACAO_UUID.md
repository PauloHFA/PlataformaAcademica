# Plano de Refatoração: Migração completa para UUID

## Objetivo
Alinhar todas as entidades, DTOs, services, controllers e o frontend para usar `UUID` como identificador do `Usuario`, eliminando o conflito `bigint` × `uuid` no PostgreSQL.

## Estado Atual
- `Usuario.java` (model package): alterado para `UUID id` ✅
- Demais entidades: ainda usam `Long id` para chaves estrangeiras
- DTOs e Services: usam `Long`
- Frontend: usa `id: number`

## Etapas

### Etapa 1: Backend - Entidades JPA
Alterar `id` e chaves estrangeiras de `Long` para `UUID` em:
- `Usuario.java` (já feito)
- `Postagem.java`
- `Comentario.java`
- `Atividade.java`
- `SubmissaoAtividade.java`
- `Plataforma.java`
- `RecomendacaoUsuario.java`
- `SalaDeAulaEntity.java` (já é UUID)
- `Mensagem.java`
- `Comunidade.java`
- `Perfil.java` / entidades de herança (Aluno, Professor, Admin)
- `ConexaoAmizade.java`
- `SalaMembros.java`
- `SolicitacaoEntrada.java`
- `Artigo.java`
- `Curtida.java`
- `Frequencia.java`
- `Dashboard.java` (se existir)

### Etapa 2: Backend - DTOs e Mappers
- `UsuarioResponseDTO`
- `LoginRequest` / `LoginResponse`
- `UsuarioMapper`
- DTOs de outras entidades que expõem `id` numérico

### Etapa 3: Backend - Repositories
- `UsuarioRepository.java` - métodos com `Long` → `UUID`
- `PostagemRepository.java`
- Demais repositories

### Etapa 4: Backend - Services
- `UsuarioService.java`
- `PostagemService.java`
- Demais services que recebem/retornam `Long id`

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
