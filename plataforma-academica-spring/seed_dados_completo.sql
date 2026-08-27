-- Script de seed ilustrativo completo para o banco PostgreSQL (plataforma_academica)
-- Adaptado ao schema real gerado pelo Spring Boot / JPA (UUID, bigint, etc.)
-- Executar: psql -U postgres -d plataforma_academica -f seed_dados_completo.sql

-- 1. USUÁRIOS (tabela usuario - id UUID, senha_hash, tipo_usuario NOT NULL)
INSERT INTO usuario (id, nome, email, senha_hash, tipo_usuario, sobrenome, descricao, instituicao_ensino, cidade, pais, senha)
VALUES
(gen_random_uuid(), 'Ana Clara', 'ana@academica.edu', 'senha123', 'Usuario', 'Silva', 'Estudante de Engenharia', 'UFRJ', 'Rio de Janeiro', 'Brasil', 'senha123'),
(gen_random_uuid(), 'Bruno Lima', 'bruno@academica.edu', 'senha123', 'Usuario', 'Lima', 'Pesquisador', 'USP', 'São Paulo', 'Brasil', 'senha123'),
(gen_random_uuid(), 'Carlos Mendes', 'carlos@academica.edu', 'senha123', 'Usuario', 'Mendes', 'Professor de Matemática', 'UNICAMP', 'Campinas', 'Brasil', 'senha123'),
(gen_random_uuid(), 'Diana Rocha', 'diana@academica.edu', 'senha123', 'Usuario', 'Rocha', 'Estudante de Física', 'UFRJ', 'Rio de Janeiro', 'Brasil', 'senha123'),
(gen_random_uuid(), 'Eduardo Souza', 'eduardo@academica.edu', 'senha123', 'Usuario', 'Souza', 'Professor de Física', 'USP', 'São Paulo', 'Brasil', 'senha123'),
(gen_random_uuid(), 'Fernanda Alves', 'fernanda@academica.edu', 'senha123', 'Usuario', 'Alves', 'Estudante de Química', 'UNICAMP', 'Campinas', 'Brasil', 'senha123')
ON CONFLICT (email) DO NOTHING;

-- 2. PROFESSORES (tabela professor - id bigint, matricula)
INSERT INTO professor (id, matricula)
SELECT id, 'PROF-' || LPAD(id::text, 4, '0')
FROM usuario
WHERE email IN ('carlos@academica.edu', 'eduardo@academica.edu')
ON CONFLICT (id) DO NOTHING;

-- 3. SALAS DE AULA (tabela sala_de_aula - id UUID, codigo, codigo_sala, criador_id UUID)
INSERT INTO sala_de_aula (id, nome, descricao, codigo, codigo_sala, criado_por, criado_em)
VALUES
(gen_random_uuid(), 'Sala de Matemática Avançada', 'Discussões sobre cálculo e álgebra linear', 'MAT-101', 'MAT-101', (SELECT id FROM usuario WHERE email='carlos@academica.edu'), NOW()),
(gen_random_uuid(), 'Sala de Física Moderna', 'Estudos de mecânica quântica e óptica', 'FIS-202', 'FIS-202', (SELECT id FROM usuario WHERE email='eduardo@academica.edu'), NOW()),
(gen_random_uuid(), 'Sala de Química Orgânica', 'Reações e sínteses orgânicas', 'QUI-303', 'QUI-303', (SELECT id FROM usuario WHERE email='fernanda@academica.edu'), NOW())
ON CONFLICT (codigo_sala) DO NOTHING;

-- 4. MEMBROS DAS SALAS (tabela sala_membro - sala_id UUID, usuario_id UUID, papel)
INSERT INTO sala_membro (sala_id, usuario_id, papel, data_entrada)
SELECT s.id, u.id, 'ALUNO', NOW()
FROM sala_de_aula s, usuario u
WHERE s.codigo_sala = 'MAT-101' AND u.email = 'ana@academica.edu';

-- 5. POSTAGENS / FEED (tabela postagem - id bigint, titulo, conteudo, usuario_id bigint)
INSERT INTO postagem (titulo, conteudo, usuario_id, criado_em, tipo)
VALUES
('Bem-vindo à plataforma!', 'Este é um exemplo de postagem para ilustrar o feed.', (SELECT id FROM usuario WHERE email='ana@academica.edu'), NOW(), 'GERAL'),
('Dica de estudo: Cálculo', 'Revisem os capítulos 3 e 4 antes da prova.', (SELECT id FROM usuario WHERE email='carlos@academica.edu'), NOW(), 'DICA'),
('Evento: Seminário de Física', 'Acontece na Sala de Física Moderna na sexta-feira.', (SELECT id FROM usuario WHERE email='eduardo@academica.edu'), NOW(), 'EVENTO')
ON CONFLICT DO NOTHING;

-- 6. COMUNIDADES (tabela comunidades - id bigint, nome, descricao, dono_id bigint)
INSERT INTO comunidades (nome, descricao, dono_id, criado_em)
VALUES
('Comunidade de Matemática', 'Espaço para discutir problemas e soluções.', (SELECT id FROM usuario WHERE email='carlos@academica.edu'), NOW()),
('Comunidade de Física', 'Troca de ideias sobre física teórica e aplicada.', (SELECT id FROM usuario WHERE email='eduardo@academica.edu'), NOW())
ON CONFLICT DO NOTHING;

-- 7. MEMBROS DAS COMUNIDADES (tabela membros_comunidade - comunidade_id bigint, usuario_id bigint, papel)
INSERT INTO membros_comunidade (comunidade_id, usuario_id, papel, entrou_em)
SELECT c.id, u.id, 'MEMBRO', NOW()
FROM comunidades c, usuario u
WHERE c.nome = 'Comunidade de Matemática' AND u.email = 'ana@academica.edu';

-- 8. ARTIGOS (tabela artigo - id bigint, titulo, conteudo, autor_id bigint, publicado_em)
INSERT INTO artigo (titulo, conteudo, autor_id, publicado_em)
VALUES
('Introdução ao Cálculo Vetorial', 'Texto ilustrativo sobre vetores e campos.', (SELECT id FROM usuario WHERE email='carlos@academica.edu'), NOW()),
('Onda e Partícula', 'Discussão sobre dualidade onda-partícula.', (SELECT id FROM usuario WHERE email='eduardo@academica.edu'), NOW())
ON CONFLICT DO NOTHING;

-- 9. CURTIDAS (tabela curtida - postagem_id bigint, usuario_id bigint, criado_em)
INSERT INTO curtida (postagem_id, usuario_id, criado_em)
SELECT p.id, (SELECT id FROM usuario WHERE email='bruno@academica.edu'), NOW()
FROM postagem p WHERE p.titulo = 'Bem-vindo à plataforma!'
ON CONFLICT DO NOTHING;

-- 10. NOTIFICAÇÕES (tabela notificacao - usuario_id bigint, mensagem, lida, criado_em)
INSERT INTO notificacao (usuario_id, mensagem, lida, criado_em)
VALUES
((SELECT id FROM usuario WHERE email='ana@academica.edu'), 'Você recebeu uma nova mensagem na sala MAT-101.', false, NOW()),
((SELECT id FROM usuario WHERE email='diana@academica.edu'), 'Novo artigo publicado pelo professor Carlos.', false, NOW())
ON CONFLICT DO NOTHING;

SELECT 'Dados ilustrativos inseridos com sucesso (seed completo).' AS status;
