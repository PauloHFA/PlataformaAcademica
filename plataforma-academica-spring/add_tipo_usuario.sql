-- Adicionar coluna tipo_usuario com valor padrão para herança JOINED
-- Isso previne o erro de coluna NOT NULL com valores nulos existentes

-- Adicionar a coluna tipo_usuario como nullable primeiro
ALTER TABLE usuario ADD COLUMN IF NOT EXISTS tipo_usuario VARCHAR(31);

-- Definir valor padrão para registros existentes (assumindo que são usuários simples, não perfis)
UPDATE usuario SET tipo_usuario = 'Usuario' WHERE tipo_usuario IS NULL;

-- Agora tornar a coluna NOT NULL
ALTER TABLE usuario ALTER COLUMN tipo_usuario SET NOT NULL;