-- Adicionar novos campos na tabela usuario
ALTER TABLE usuario ADD COLUMN IF NOT EXISTS sobrenome VARCHAR(255);
ALTER TABLE usuario ADD COLUMN IF NOT EXISTS data_nascimento DATE;
ALTER TABLE usuario ADD COLUMN IF NOT EXISTS telefone VARCHAR(20);
ALTER TABLE usuario ADD COLUMN IF NOT EXISTS descricao TEXT;
ALTER TABLE usuario ADD COLUMN IF NOT EXISTS instituicao_ensino VARCHAR(255);
ALTER TABLE usuario ADD COLUMN IF NOT EXISTS cep VARCHAR(10);
ALTER TABLE usuario ADD COLUMN IF NOT EXISTS pais VARCHAR(100);
ALTER TABLE usuario ADD COLUMN IF NOT EXISTS cidade VARCHAR(100);
ALTER TABLE usuario ADD COLUMN IF NOT EXISTS site VARCHAR(255);

-- Tornar campos obrigatórios (se necessário, ajuste conforme seus dados existentes)
-- ALTER TABLE usuario ALTER COLUMN nome SET NOT NULL;
-- ALTER TABLE usuario ALTER COLUMN email SET NOT NULL;
-- ALTER TABLE usuario ALTER COLUMN senha SET NOT NULL;
