-- Script para limpar dados inválidos na tabela perfis
-- Execute este script no banco de dados para remover dados corrompidos

-- Primeiro, backup (opcional)
CREATE TABLE perfis_backup_limpeza AS SELECT * FROM perfis;

-- Verificar dados inválidos
-- Se id não for numérico ou bio tiver dados estranhos
SELECT id, bio, foto_perfil FROM perfis WHERE CAST(id AS TEXT) !~ '^[0-9]+$' OR bio = 'sdasdasdasda' OR foto_perfil = 'sdasdasdasda';

-- Remover linhas com dados inválidos
DELETE FROM perfis WHERE CAST(id AS TEXT) !~ '^[0-9]+$' OR bio = 'sdasdasdasda' OR foto_perfil = 'sdasdasdasda';

-- Ou, se preferir truncar completamente a tabela (cuidado, perde todos os perfis)
-- TRUNCATE TABLE perfis;

-- Verificar se ainda há problemas
SELECT * FROM perfis LIMIT 10;