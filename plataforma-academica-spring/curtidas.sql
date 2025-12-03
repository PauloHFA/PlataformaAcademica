-- Criar tabela de curtidas com constraint de unicidade
CREATE TABLE IF NOT EXISTS curtida (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL REFERENCES usuario(id) ON DELETE CASCADE,
    postagem_id BIGINT NOT NULL REFERENCES postagem(id) ON DELETE CASCADE,
    CONSTRAINT uk_curtida_usuario_postagem UNIQUE (usuario_id, postagem_id)
);

-- Criar índices para melhor performance
CREATE INDEX IF NOT EXISTS idx_curtida_usuario ON curtida(usuario_id);
CREATE INDEX IF NOT EXISTS idx_curtida_postagem ON curtida(postagem_id);
