-- Script de seed completo - Adaptado ao schema real
-- usuario.id = uuid, sala_de_aula.id = uuid
-- postagem.id = bigint, comunidades.id = bigint, atividade.id = bigint
-- artigo.id = bigint, curtida.id = bigint, notificacao.id = bigint
-- professor.id = bigint, sala_membro.id = bigint, membros_comunidade.id = bigint

-- 1. USUARIOS (id UUID)
INSERT INTO usuario (id, nome, email, senha_hash, tipo_usuario, sobrenome, descricao, instituicao_ensino, cidade, pais, senha, data_cadastro)
VALUES
(gen_random_uuid(), 'Ana Clara', 'ana.clara@academica.edu', 'senha123', 'Professor', 'Silva', 'Professora de Matematica', 'UFRJ', 'Rio de Janeiro', 'Brasil', 'senha123', NOW()),
(gen_random_uuid(), 'Bruno Lima', 'bruno.lima@academica.edu', 'senha123', 'Professor', 'Lima', 'Professor de Fisica', 'USP', 'Sao Paulo', 'Brasil', 'senha123', NOW()),
(gen_random_uuid(), 'Carlos Mendes', 'carlos.mendes@academica.edu', 'senha123', 'Professor', 'Mendes', 'Professor de Quimica', 'UNICAMP', 'Campinas', 'Brasil', 'senha123', NOW()),
(gen_random_uuid(), 'Diana Rocha', 'diana.rocha@academica.edu', 'senha123', 'Aluno', 'Rocha', 'Estudante de Fisica', 'UFRJ', 'Rio de Janeiro', 'Brasil', 'senha123', NOW()),
(gen_random_uuid(), 'Eduardo Souza', 'eduardo.souza@academica.edu', 'senha123', 'Professor', 'Souza', 'Professor de Engenharia', 'USP', 'Sao Paulo', 'Brasil', 'senha123', NOW()),
(gen_random_uuid(), 'Fernanda Alves', 'fernanda.alves@academica.edu', 'senha123', 'Aluno', 'Alves', 'Estudante de Quimica', 'UNICAMP', 'Campinas', 'Brasil', 'senha123', NOW()),
(gen_random_uuid(), 'Gabriela Martins', 'gabriela.martins@academica.edu', 'senha123', 'Aluno', 'Martins', 'Estudante de Engenharia', 'USP', 'Sao Paulo', 'Brasil', 'senha123', NOW()),
(gen_random_uuid(), 'Helena Torres', 'helena.torres@academica.edu', 'senha123', 'Professor', 'Torres', 'Professora de Biologia', 'UFRJ', 'Rio de Janeiro', 'Brasil', 'senha123', NOW()),
(gen_random_uuid(), 'Igor Santos', 'igor.santos@academica.edu', 'senha123', 'Aluno', 'Santos', 'Estudante de Biologia', 'UNICAMP', 'Campinas', 'Brasil', 'senha123', NOW()),
(gen_random_uuid(), 'Julia Costa', 'julia.costa@academica.edu', 'senha123', 'Aluno', 'Costa', 'Estudante de Matematica', 'UFRJ', 'Rio de Janeiro', 'Brasil', 'senha123', NOW()),
(gen_random_uuid(), 'Karina Oliveira', 'karina.oliveira@academica.edu', 'senha123', 'Aluno', 'Oliveira', 'Estudante de Fisica', 'USP', 'Sao Paulo', 'Brasil', 'senha123', NOW()),
(gen_random_uuid(), 'Lucas Pereira', 'lucas.pereira@academica.edu', 'senha123', 'Aluno', 'Pereira', 'Estudante de Quimica', 'UNICAMP', 'Campinas', 'Brasil', 'senha123', NOW()),
(gen_random_uuid(), 'Mariana Dias', 'mariana.dias@academica.edu', 'senha123', 'Admin', 'Dias', 'Administradora', 'UFRJ', 'Rio de Janeiro', 'Brasil', 'senha123', NOW()),
(gen_random_uuid(), 'Nicolas Almeida', 'nicolas.almeida@academica.edu', 'senha123', 'Aluno', 'Almeida', 'Estudante de Engenharia', 'USP', 'Sao Paulo', 'Brasil', 'senha123', NOW()),
(gen_random_uuid(), 'Otavio Lima', 'otavio.lima@academica.edu', 'senha123', 'Aluno', 'Lima', 'Estudante de Matematica', 'UFRJ', 'Rio de Janeiro', 'Brasil', 'senha123', NOW()),
(gen_random_uuid(), 'Patricia Souza', 'patricia.souza@academica.edu', 'senha123', 'Professor', 'Souza', 'Professora de Estatistica', 'UNICAMP', 'Campinas', 'Brasil', 'senha123', NOW()),
(gen_random_uuid(), 'Quiteria Rocha', 'quiteria.rocha@academica.edu', 'senha123', 'Aluno', 'Rocha', 'Estudante de Biologia', 'UFRJ', 'Rio de Janeiro', 'Brasil', 'senha123', NOW()),
(gen_random_uuid(), 'Rafael Mendes', 'rafael.mendes@academica.edu', 'senha123', 'Aluno', 'Mendes', 'Estudante de Fisica', 'USP', 'Sao Paulo', 'Brasil', 'senha123', NOW()),
(gen_random_uuid(), 'Sofia Martins', 'sofia.martins@academica.edu', 'senha123', 'Aluno', 'Martins', 'Estudante de Quimica', 'UNICAMP', 'Campinas', 'Brasil', 'senha123', NOW()),
(gen_random_uuid(), 'Tiago Ferreira', 'tiago.ferreira@academica.edu', 'senha123', 'Aluno', 'Ferreira', 'Estudante de Engenharia', 'UFRJ', 'Rio de Janeiro', 'Brasil', 'senha123', NOW())
ON CONFLICT (email) DO NOTHING;

-- 2. SALAS DE AULA (id UUID, criador_id UUID)
INSERT INTO sala_de_aula (id, nome, codigo, codigo_sala, criador_id)
VALUES
(gen_random_uuid(), 'Sala de Matematica Avancada', 'MAT-101', 'MAT-101', (SELECT id FROM usuario WHERE email='ana.clara@academica.edu')),
(gen_random_uuid(), 'Sala de Fisica Moderna', 'FIS-202', 'FIS-202', (SELECT id FROM usuario WHERE email='bruno.lima@academica.edu')),
(gen_random_uuid(), 'Sala de Quimica Organica', 'QUI-303', 'QUI-303', (SELECT id FROM usuario WHERE email='carlos.mendes@academica.edu')),
(gen_random_uuid(), 'Sala de Biologia Celular', 'BIO-404', 'BIO-404', (SELECT id FROM usuario WHERE email='helena.torres@academica.edu')),
(gen_random_uuid(), 'Sala de Engenharia de Software', 'ENG-505', 'ENG-505', (SELECT id FROM usuario WHERE email='eduardo.souza@academica.edu')),
(gen_random_uuid(), 'Sala de Estatistica Aplicada', 'EST-606', 'EST-606', (SELECT id FROM usuario WHERE email='patricia.souza@academica.edu')),
(gen_random_uuid(), 'Sala de Programacao Python', 'PY-707', 'PY-707', (SELECT id FROM usuario WHERE email='lucas.pereira@academica.edu')),
(gen_random_uuid(), 'Sala de Historia da Ciencia', 'HIS-808', 'HIS-808', (SELECT id FROM usuario WHERE email='helena.torres@academica.edu')),
(gen_random_uuid(), 'Sala de Fisica Experimental', 'FIS-909', 'FIS-909', (SELECT id FROM usuario WHERE email='bruno.lima@academica.edu')),
(gen_random_uuid(), 'Sala de Matematica Discreta', 'MAT-1010', 'MAT-1010', (SELECT id FROM usuario WHERE email='ana.clara@academica.edu'))
ON CONFLICT (codigo_sala) DO NOTHING;

-- 3. MEMBROS DAS SALAS (sala_id UUID, usuario_id UUID)
INSERT INTO sala_membro (sala_id, usuario_id, papel, data_entrada)
SELECT s.id, u.id, 'ALUNO', NOW()
FROM sala_de_aula s
CROSS JOIN usuario u
WHERE s.codigo_sala IN ('MAT-101', 'FIS-202', 'QUI-303', 'BIO-404', 'ENG-505')
AND u.tipo_usuario = 'Aluno'
ON CONFLICT DO NOTHING;

-- 4. POSTAGENS (id bigint, usuario_id bigint)
INSERT INTO postagem (titulo, conteudo, usuario_id, tipo, curtidas)
VALUES
('Bem-vindo a plataforma!', 'Este e um exemplo de postagem para ilustrar o feed.', (SELECT id FROM usuario WHERE email='ana.clara@academica.edu'), 'GERAL', 0),
('Dica de estudo: Calculo', 'Revisem os capitulos 3 e 4 antes da prova.', (SELECT id FROM usuario WHERE email='ana.clara@academica.edu'), 'DICA', 0),
('Evento: Seminaro de Fisica', 'Acontece na Sala de Fisica Moderna na sexta-feira.', (SELECT id FROM usuario WHERE email='bruno.lima@academica.edu'), 'EVENTO', 0),
('Novo artigo publicado', 'Confira o artigo sobre calculo vetorial.', (SELECT id FROM usuario WHERE email='carlos.mendes@academica.edu'), 'ARTIGO', 0),
('Duvida sobre Quimica', 'Como balancear esta reacao?', (SELECT id FROM usuario WHERE email='lucas.pereira@academica.edu'), 'PERGUNTA', 0),
('Projeto de Engenharia', 'Apresentacao do projeto de software.', (SELECT id FROM usuario WHERE email='eduardo.souza@academica.edu'), 'PROJETO', 0),
('Recomendacao de leitura', 'Leiam o capitulo 5 do livro de fisica.', (SELECT id FROM usuario WHERE email='helena.torres@academica.edu'), 'DICA', 0),
('Resultado da prova', 'Media da turma foi 7.8.', (SELECT id FROM usuario WHERE email='patricia.souza@academica.edu'), 'GERAL', 0),
('Convite para sala', 'Entre na sala de Matematica Avancada.', (SELECT id FROM usuario WHERE email='helena.torres@academica.edu'), 'CONVITE', 0),
('Discussao sobre Biologia', 'Qual a relacao entre DNA e RNA?', (SELECT id FROM usuario WHERE email='igor.santos@academica.edu'), 'PERGUNTA', 0),
('Material de apoio', 'Link para slides da aula de estatistica.', (SELECT id FROM usuario WHERE email='karina.oliveira@academica.edu'), 'MATERIAL', 0),
('Feedback do professor', 'Excelente trabalho na atividade 3.', (SELECT id FROM usuario WHERE email='ana.clara@academica.edu'), 'FEEDBACK', 0),
('Anuncio de evento', 'Palestra com pesquisador externo.', (SELECT id FROM usuario WHERE email='bruno.lima@academica.edu'), 'EVENTO', 0),
('Dica de programacao', 'Use listas de compreensao em Python.', (SELECT id FROM usuario WHERE email='julia.costa@academica.edu'), 'DICA', 0),
('Pergunta sobre Engenharia', 'Como modelar este sistema?', (SELECT id FROM usuario WHERE email='tiago.ferreira@academica.edu'), 'PERGUNTA', 0)
ON CONFLICT DO NOTHING;

-- 5. COMUNIDADES (id bigint, dono_id bigint)
INSERT INTO comunidades (nome, descricao, dono_id)
VALUES
('Comunidade de Matematica', 'Espaco para discutir problemas e solucoes.', (SELECT id FROM usuario WHERE email='ana.clara@academica.edu')),
('Comunidade de Fisica', 'Troca de ideias sobre fisica teorica e aplicada.', (SELECT id FROM usuario WHERE email='bruno.lima@academica.edu')),
('Comunidade de Quimica', 'Reacoes, sinteses e analises.', (SELECT id FROM usuario WHERE email='carlos.mendes@academica.edu')),
('Comunidade de Engenharia', 'Projetos e desenvolvimento.', (SELECT id FROM usuario WHERE email='eduardo.souza@academica.edu')),
('Comunidade de Biologia', 'Estudos celulares e geneticos.', (SELECT id FROM usuario WHERE email='helena.torres@academica.edu')),
('Comunidade de Estatistica', 'Analise de dados e modelos.', (SELECT id FROM usuario WHERE email='patricia.souza@academica.edu')),
('Comunidade de Programacao', 'Python, Java e desenvolvimento.', (SELECT id FROM usuario WHERE email='lucas.pereira@academica.edu')),
('Comunidade Geral', 'Assuntos diversos da plataforma.', (SELECT id FROM usuario WHERE email='mariana.dias@academica.edu'))
ON CONFLICT DO NOTHING;

-- 6. MEMBROS DAS COMUNIDADES (comunidade_id bigint, usuario_id bigint)
INSERT INTO membros_comunidade (comunidade_id, usuario_id, papel, entrou_em)
SELECT c.id, u.id, 'MEMBRO', NOW()
FROM comunidades c
CROSS JOIN usuario u
WHERE c.nome IN ('Comunidade de Matematica', 'Comunidade de Fisica', 'Comunidade de Quimica', 'Comunidade de Engenharia')
AND u.tipo_usuario = 'Aluno'
ON CONFLICT DO NOTHING;

-- 7. CURTIDAS (postagem_id bigint, usuario_id bigint)
INSERT INTO curtida (postagem_id, usuario_id)
SELECT p.id, u.id
FROM postagem p
CROSS JOIN usuario u
WHERE p.titulo IN ('Bem-vindo a plataforma!', 'Dica de estudo: Calculo', 'Evento: Seminaro de Fisica', 'Novo artigo publicado', 'Duvida sobre Quimica')
AND u.tipo_usuario = 'Aluno'
ON CONFLICT DO NOTHING;

-- 8. NOTIFICACOES (usuario_id bigint)
INSERT INTO notificacao (usuario_id, mensagem, lida, tipo)
VALUES
((SELECT id FROM usuario WHERE email='diana.rocha@academica.edu'), 'Voce recebeu uma nova mensagem na sala MAT-101.', false, 'MENSAGEM'),
((SELECT id FROM usuario WHERE email='fernanda.alves@academica.edu'), 'Novo artigo publicado pelo professor Carlos.', false, 'ARTIGO'),
((SELECT id FROM usuario WHERE email='gabriela.martins@academica.edu'), 'Sua atividade foi corrigida.', false, 'ATIVIDADE'),
((SELECT id FROM usuario WHERE email='igor.santos@academica.edu'), 'Convite para a sala de Fisica Moderna.', false, 'CONVITE'),
((SELECT id FROM usuario WHERE email='julia.costa@academica.edu'), 'Novo evento na plataforma.', false, 'EVENTO'),
((SELECT id FROM usuario WHERE email='karina.oliveira@academica.edu'), 'Voce recebeu uma curtida.', false, 'CURTIDA'),
((SELECT id FROM usuario WHERE email='lucas.pereira@academica.edu'), 'Nova postagem no feed.', false, 'POSTAGEM'),
((SELECT id FROM usuario WHERE email='nicolas.almeida@academica.edu'), 'Sua submissao foi entregue.', false, 'SUBMISSAO'),
((SELECT id FROM usuario WHERE email='otavio.lima@academica.edu'), 'Mensagem da comunidade de Matematica.', false, 'MENSAGEM'),
((SELECT id FROM usuario WHERE email='quiteria.rocha@academica.edu'), 'Atualizacao de perfil necessaria.', false, 'SISTEMA')
ON CONFLICT DO NOTHING;

SELECT 'Seed concluido com sucesso.' AS status;
