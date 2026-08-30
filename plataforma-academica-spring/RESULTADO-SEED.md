# Resultado do Seed - Dados Ficticios

Data: 2026-08-30
Banco: plataforma_academica (PostgreSQL 18)

## Status do Banco de Dados

### Tabelas Populadas com Sucesso
- **usuario**: 20 usuarios (6 professores, 13 alunos, 1 admin)
- **sala_de_aula**: 10 salas de aula (MAT, FIS, QUI, BIO, ENG, EST, PY, HIS)

### Tabelas com Schema Parcialmente Migrado
- **postagem**, **comunidades**, **curtida**, **notificacao**: ainda com `id` bigint (migracao UUID nao aplicada)
- **professor**: id bigint (espera-se UUID conforme refatoracao backend)

## Schema Real do Banco (informacoes columns)

| Tabela | Colunas Principais | Tipo |
|--------|-------------------|------|
| usuario | id | uuid |
| sala_de_aula | id, criador_id | uuid |
| postagem | id, usuario_id | bigint |
| comunidades | id, dono_id | bigint |
| professor | id, matricula | bigint |
| sala_membro | sala_id, usuario_id | uuid |
| membros_comunidade | comunidade_id, usuario_id | bigint |
| curtida | postagem_id, usuario_id | bigint |
| notificacao | usuario_id | bigint |

## Script Executado
- `seed_final.sql` - Versao adaptada ao schema real
- `seed_dados_completo.sql` - Versao original (com erros de UUID/bigint)
- `seed_dados_expandido.sql` - Versao expandida (com erros de schema)

## Proximos Passos Recomendados
1. Finalizar migracao UUID nas tabelas: postagem, comunidades, curtida, notificacao, professor
2. Ajustar entidades JPA correspondentes no Spring Boot
3. Atualizar o backend para usar UUID em todos os IDs
4. Re-executar seed completo apos migracao

## Conquistas
- Backend Spring compila com sucesso (BUILD SUCCESS)
- Testes mockados com UUID passam
- Frontend refatorado com variaveis CSS
- Banco populado com dados basicos
