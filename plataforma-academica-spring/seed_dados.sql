-- Script para popular dados ilustrativos (feed, pessoas, professores, salas, etc.)
-- Executar no banco PostgreSQL: psql -U postgres -d plataforma_academica -f seed_dados.sql

-- 1. Usuários base (pessoas)
INSERT INTO usuario (nome, email, senha, tipo_usuario, sobrenome, descricao, instituicao_ensino, cidade, pais)
VALUES
('Ana Clara', 'ana@academica.edu', 'senha123', 'Usuario', 'Silva', 'Estudante de Engenharia', 'UFRJ', 'Rio de Janeiro', 'Brasil'),
('Bruno Lima', 'bruno@academica.edu', 'senha123', 'Usuario', 'Lima', 'Pesquisador', 'USP', 'São Paulo', 'Brasil'),
('Carlos Mendes', 'carlos@academica.edu', 'senha123', 'Usuario', 'Mendes', 'Professor de Matemática', 'UNICAMP', 'Campinas', 'Brasil'),
('Diana Rocha', 'diana@academica.edu', 'senha123', 'Usuario', 'Rocha', 'Estudante de Física', 'UFRJ', 'Rio de Janeiro', 'Brasil'),
('Eduardo Souza', 'eduardo@academica.edu', 'senha123', 'Usuario', 'Souza', 'Professor de Física', 'USP', 'São Paulo', 'Brasil'),
('Fernanda Alves', 'fernanda@academica.edu', 'senha123', 'Usuario', 'Alves', 'Estudante de Química', 'UNICAMP', 'Campinas', 'Brasil')
ON CONFLICT (email) DO NOTHING;

-- 2. Professores (tabela filha professor)
INSERT INTO professor (id, matricula)
SELECT id, 'PROF-' || LPAD(id::text, 4, '0')
FROM usuario
WHERE email IN ('carlos@academica.edu', 'eduardo@academica.edu')
ON CONFLICT (id) DO NOTHING;

-- 3. Salas de aula
INSERT INTO saladeaula (nome, descricao, codigo, criado_por, criado_em)
VALUES
('Sala de Matemática Avançada', 'Discussões sobre cálculo e álgebra linear', 'MAT-101', 3, NOW()),
('Sala de Física Moderna', 'Estudos de mecânica quântica e óptica', 'FIS-202', 5, NOW()),
('Sala de Química Orgânica', 'Reações e sínteses orgânicas', 'QUI-303', 6, NOW())
ON CONFLICT (codigo) DO NOTHING;

-- 4. Membros das salas (exemplo)
INSERT INTO saladeaula_membro (sala_id, usuario_id, papel, entrou_em)
SELECT s.id, u.id, 'MEMBRO', NOW()
FROM saladeaula s, usuario u
WHERE s.codigo = 'MAT-101' AND u.email = 'ana@academica.edu';

-- 5. Postagens / feed ilustrativo
INSERT INTO postagem (titulo, conteudo, autor_id, criado_em, tipo)
VALUES
('Bem-vindo à plataforma!', 'Este é um exemplo de postagem para ilustrar o feed.', 1, NOW(), 'GERAL'),
('Dica de estudo: Cálculo', 'Revisem os capítulos 3 e 4 antes da prova.', 3, NOW(), 'DICA'),
('Evento: Seminário de Física', 'Acontece na Sala de Física Moderna na sexta-feira.', 5, NOW(), 'EVENTO')
ON CONFLICT DO NOTHING;

-- 6. Comunidades
INSERT INTO comunidade (nome, descricao, criado_por, criado_em)
VALUES
('Comunidade de Matemática', 'Espaço para discutir problemas e soluções.', 3, NOW()),
('Comunidade de Física', 'Troca de ideias sobre física teórica e aplicada.', 5, NOW())
ON CONFLICT DO NOTHING;

-- 7. Membros das comunidades
INSERT INTO membro_comunidade (comunidade_id, usuario_id, papel, entrou_em)
SELECT c.id, u.id, 'MEMBRO', NOW()
FROM comunidade c, usuario u
WHERE c.nome = 'Comunidade de Matemática' AND u.email = 'ana@academica.edu';

-- 8. Artigos / conteúdo
INSERT INTO artigo (titulo, conteudo, autor_id, publicado_em)
VALUES
('Introdução ao Cálculo Vetorial', 'Texto ilustrativo sobre vetores e campos.', 3, NOW()),
('Onda e Partícula', 'Discussão sobre dualidade onda-partícula.', 5, NOW())
ON CONFLICT DO NOTHING;

-- 9. Curtidas (exemplo)
INSERT INTO curtida (postagem_id, usuario_id, criado_em)
SELECT p.id, 2, NOW()
FROM postagem p WHERE p.titulo = 'Bem-vindo à plataforma!'
ON CONFLICT DO NOTHING;

-- 10. Notificações
INSERT INTO notificacao (usuario_id, mensagem, lida, criado_em)
VALUES
(1, 'Você recebeu uma nova mensagem na sala MAT-101.', false, NOW()),
(4, 'Novo artigo publicado pelo professor Carlos.', false, NOW())
ON CONFLICT DO NOTHING;

SELECT 'Dados ilustrativos inseridos com sucesso.' AS status;
