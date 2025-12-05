-- Script para corrigir valores inconsistentes de discriminator na tabela usuario
-- Atualizar registros com 'PADRAO' ou outros valores incorretos para os valores corretos

-- Definir valor padrão para usuários normais (se 'PADRAO' for usado)
UPDATE usuario SET tipo_usuario = 'PADRAO' WHERE tipo_usuario IS NULL OR tipo_usuario NOT IN ('PADRAO', 'PERFIL', 'Professor');

-- Ou, se preferir usar 'Usuario' como padrão:
-- UPDATE usuario SET tipo_usuario = 'Usuario' WHERE tipo_usuario NOT IN ('Usuario', 'PERFIL', 'Professor');