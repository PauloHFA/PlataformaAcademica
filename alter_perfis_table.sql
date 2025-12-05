-- Script para alterar as colunas bio e foto_perfil da tabela perfis para TEXT
-- Isso resolve o erro "value too long for type character varying(500)"

ALTER TABLE perfis ALTER COLUMN bio TYPE TEXT;
ALTER TABLE perfis ALTER COLUMN foto_perfil TYPE TEXT;