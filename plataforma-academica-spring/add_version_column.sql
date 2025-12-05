-- Adicionar coluna version para controle de versão otimista
ALTER TABLE usuario ADD COLUMN version BIGINT DEFAULT 0;