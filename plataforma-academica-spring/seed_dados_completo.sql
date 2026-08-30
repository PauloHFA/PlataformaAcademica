-- Script de seed completo - Dados ficticios em massa
-- Adaptado ao schema real: usuario, sala_de_aula, professor = UUID
-- postagem, comunidades, artigo, curtida, notificacao, atividade = bigint
-- Executar: psql -U postgres -d plataforma_academica -f seed_dados_completo.sql

-- 1. USUARIOS (id UUID) - 20 registros variados
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

-- 2. PROFESSORES (id UUID, matricula)
INSERT INTO professor (id, matricula)
SELECT id, 'PROF-' || SUBSTRING(id::text, 1, 8)
FROM usuario WHERE tipo_usuario = 'Professor'
ON CONFLICT (id) DO NOTHING;

-- 3. SALAS DE AULA (id UUID) - 10 salas
INSERT INTO sala_de_aula (id, nome, codigo, codigo_sala, criador_id, criado_em)
VALUES
(gen_random_uuid(), 'Sala de Matematica Avancada', 'MAT-101', 'MAT-101', (SELECT id FROM usuario WHERE email='ana.clara@academica.edu'), NOW()),
(gen_random_uuid(), 'Sala de Fisica Moderna', 'FIS-202', 'FIS-202', (SELECT id FROM usuario WHERE email='bruno.lima@academica.edu'), NOW()),
(gen_random_uuid(), 'Sala de Quimica Organica', 'QUI-303', 'QUI-303', (SELECT id FROM usuario WHERE email='carlos.mendes@academica.edu'), NOW()),
(gen_random_uuid(), 'Sala de Biologia Celular', 'BIO-404', 'BIO-404', (SELECT id FROM usuario WHERE email='helena.torres@academica.edu'), NOW()),
(gen_random_uuid(), 'Sala de Engenharia de Software', 'ENG-505', 'ENG-505', (SELECT id FROM usuario WHERE email='eduardo.souza@academica.edu'), NOW()),
(gen_random_uuid(), 'Sala de Estatistica Aplicada', 'EST-606', 'EST-606', (SELECT id FROM usuario WHERE email='patricia.souza@academica.edu'), NOW()),
(gen_random_uuid(), 'Sala de Programacao Python', 'PY-707', 'PY-707', (SELECT id FROM usuario WHERE email='lucas.pereira@academica.edu'), NOW()),
(gen_random_uuid(), 'Sala de Historia da Ciencia', 'HIS-808', 'HIS-808', (SELECT id FROM usuario WHERE email='helena.torres@academica.edu'), NOW()),
(gen_random_uuid(), 'Sala de Fisica Experimental', 'FIS-909', 'FIS-909', (SELECT id FROM usuario WHERE email='bruno.lima@academica.edu'), NOW()),
(gen_random_uuid(), 'Sala de Matematica Discreta', 'MAT-1010', 'MAT-1010', (SELECT id FROM usuario WHERE email='ana.clara@academica.edu'), NOW())
ON CONFLICT (codigo_sala) DO NOTHING;

-- 4. MEMBROS DAS SALAS (sala_id UUID, usuario_id UUID)
INSERT INTO sala_membro (sala_id, usuario_id, papel, data_entrada)
SELECT s.id, u.id, 'ALUNO', NOW()
FROM sala_de_aula s
CROSS JOIN usuario u
WHERE s.codigo_sala IN ('MAT-101', 'FIS-202', 'QUI-303', 'BIO-404', 'ENG-505')
AND u.tipo_usuario = 'Aluno'
ON CONFLICT DO NOTHING;

-- 5. POSTAGENS (id bigint) - 15 postagens
INSERT INTO postagem (titulo, conteudo, usuario_id, criado_em, tipo)
VALUES
('Bem-vindo a plataforma!', 'Este e um exemplo de postagem para ilustrar o feed.', (SELECT id FROM usuario WHERE email='ana.clara@academica.edu'), NOW(), 'GERAL'),
('Dica de estudo: Calculo', 'Revisem os capitulos 3 e 4 antes da prova.', (SELECT id FROM usuario WHERE email='ana.clara@academica.edu'), NOW(), 'DICA'),
('Evento: Seminaro de Fisica', 'Acontece na Sala de Fisica Moderna na sexta-feira.', (SELECT id FROM usuario WHERE email='bruno.lima@academica.edu'), NOW(), 'EVENTO'),
('Novo artigo publicado', 'Confira o artigo sobre calculo vetorial.', (SELECT id FROM usuario WHERE email='carlos.mendes@academica.edu'), NOW(), 'ARTIGO'),
('Duvida sobre Quimica', 'Como balancear esta reacao?', (SELECT id FROM usuario WHERE email='lucas.pereira@academica.edu'), NOW(), 'PERGUNTA'),
('Projeto de Engenharia', 'Apresentacao do projeto de software.', (SELECT id FROM usuario WHERE email='eduardo.souza@academica.edu'), NOW(), 'PROJETO'),
('Recomendacao de leitura', 'Leiam o capitulo 5 do livro de fisica.', (SELECT id FROM usuario WHERE email='helena.torres@academica.edu'), NOW(), 'DICA'),
('Resultado da prova', 'Media da turma foi 7.8.', (SELECT id FROM usuario WHERE email='patricia.souza@academica.edu'), NOW(), 'GERAL'),
('Convite para sala', 'Entre na sala de Matematica Avancada.', (SELECT id FROM usuario WHERE email='helena.torres@academica.edu'), NOW(), 'CONVITE'),
('Discussao sobre Biologia', 'Qual a relacao entre DNA e RNA?', (SELECT id FROM usuario WHERE email='igor.santos@academica.edu'), NOW(), 'PERGUNTA'),
('Material de apoio', 'Link para slides da aula de estatistica.', (SELECT id FROM usuario WHERE email='karina.oliveira@academica.edu'), NOW(), 'MATERIAL'),
('Feedback do professor', 'Excelente trabalho na atividade 3.', (SELECT id FROM usuario WHERE email='ana.clara@academica.edu'), NOW(), 'FEEDBACK'),
('Anuncio de evento', 'Palestra com pesquisador externo.', (SELECT id FROM usuario WHERE email='bruno.lima@academica.edu'), NOW(), 'EVENTO'),
('Dica de programacao', 'Use listas de compreensao em Python.', (SELECT id FROM usuario WHERE email='julia.costa@academica.edu'), NOW(), 'DICA'),
('Pergunta sobre Engenharia', 'Como modelar este sistema?', (SELECT id FROM usuario WHERE email='tiago.ferreira@academica.edu'), NOW(), 'PERGUNTA')
ON CONFLICT DO NOTHING;

-- 6. COMUNIDADES (id bigint) - 8 comunidades
INSERT INTO comunidades (nome, descricao, dono_id, criado_em)
VALUES
('Comunidade de Matematica', 'Espaco para discutir problemas e solucoes.', (SELECT id FROM usuario WHERE email='ana.clara@academica.edu'), NOW()),
('Comunidade de Fisica', 'Troca de ideias sobre fisica teorica e aplicada.', (SELECT id FROM usuario WHERE email='bruno.lima@academica.edu'), NOW()),
('Comunidade de Quimica', 'Reacoes, sinteses e analises.', (SELECT id FROM usuario WHERE email='carlos.mendes@academica.edu'), NOW()),
('Comunidade de Engenharia', 'Projetos e desenvolvimento.', (SELECT id FROM usuario WHERE email='eduardo.souza@academica.edu'), NOW()),
('Comunidade de Biologia', 'Estudos celulares e geneticos.', (SELECT id FROM usuario WHERE email='helena.torres@academica.edu'), NOW()),
('Comunidade de Estatistica', 'Analise de dados e modelos.', (SELECT id FROM usuario WHERE email='patricia.souza@academica.edu'), NOW()),
('Comunidade de Programacao', 'Python, Java e desenvolvimento.', (SELECT id FROM usuario WHERE email='lucas.pereira@academica.edu'), NOW()),
('Comunidade Geral', 'Assuntos diversos da plataforma.', (SELECT id FROM usuario WHERE email='mariana.dias@academica.edu'), NOW())
ON CONFLICT DO NOTHING;

-- 7. MEMBROS DAS COMUNIDADES
INSERT INTO membros_comunidade (comunidade_id, usuario_id, papel, entrou_em)
SELECT c.id, u.id, 'MEMBRO', NOW()
FROM comunidades c
CROSS JOIN usuario u
WHERE c.nome IN ('Comunidade de Matematica', 'Comunidade de Fisica', 'Comunidade de Quimica', 'Comunidade de Engenharia')
AND u.tipo_usuario = 'Aluno'
ON CONFLICT DO NOTHING;

-- 8. ATIVIDADES (id bigint, sala_id bigint) - 10 atividades
INSERT INTO atividade (titulo, descricao, sala_id, autor_id, criado_em, data_entrega)
VALUES
('Lista 1 - Calculo', 'Resolver exercicios do capitulo 3.', (SELECT s.id FROM sala_de_aula s WHERE s.codigo_sala='MAT-101'), (SELECT id FROM usuario WHERE email='ana.clara@academica.edu'), NOW(), NOW() + INTERVAL '7 days'),
('Experimento de Fisica', 'Montar circuito eletrico.', (SELECT s.id FROM sala_de_aula s WHERE s.codigo_sala='FIS-202'), (SELECT id FROM usuario WHERE email='bruno.lima@academica.edu'), NOW(), NOW() + INTERVAL '5 days'),
('Sintese Organica', 'Preparar composto simples.', (SELECT s.id FROM sala_de_aula s WHERE s.codigo_sala='QUI-303'), (SELECT id FROM usuario WHERE email='carlos.mendes@academica.edu'), NOW(), NOW() + INTERVAL '10 days'),
('Relatorio de Biologia', 'Analisar amostra celular.', (SELECT s.id FROM sala_de_aula s WHERE s.codigo_sala='BIO-404'), (SELECT id FROM usuario WHERE email='helena.torres@academica.edu'), NOW(), NOW() + INTERVAL '14 days'),
('Projeto de Software', 'Criar aplicacao web simples.', (SELECT s.id FROM sala_de_aula s WHERE s.codigo_sala='ENG-505'), (SELECT id FROM usuario WHERE email='eduardo.souza@academica.edu'), NOW(), NOW() + INTERVAL '21 days'),
('Analise Estatistica', 'Calcular media e desvio.', (SELECT s.id FROM sala_de_aula s WHERE s.codigo_sala='EST-606'), (SELECT id FROM usuario WHERE email='patricia.souza@academica.edu'), NOW(), NOW() + INTERVAL '7 days'),
('Script Python', 'Automatizar tarefa simples.', (SELECT s.id FROM sala_de_aula s WHERE s.codigo_sala='PY-707'), (SELECT id FROM usuario WHERE email='lucas.pereira@academica.edu'), NOW(), NOW() + INTERVAL '3 days'),
('Pesquisa Historica', 'Escrever texto curto.', (SELECT s.id FROM sala_de_aula s WHERE s.codigo_sala='HIS-808'), (SELECT id FROM usuario WHERE email='helena.torres@academica.edu'), NOW(), NOW() + INTERVAL '10 days'),
('Laboratorio de Fisica', 'Medir aceleracao da gravidade.', (SELECT s.id FROM sala_de_aula s WHERE s.codigo_sala='FIS-909'), (SELECT id FROM usuario WHERE email='bruno.lima@academica.edu'), NOW(), NOW() + INTERVAL '7 days'),
('Problemas de Matematica', 'Resolver 10 problemas.', (SELECT s.id FROM sala_de_aula s WHERE s.codigo_sala='MAT-1010'), (SELECT id FROM usuario WHERE email='patricia.souza@academica.edu'), NOW(), NOW() + INTERVAL '5 days')
ON CONFLICT DO NOTHING;

-- 9. ARTIGOS (id bigint) - 8 artigos
INSERT INTO artigo (titulo, conteudo, autor_id, criado_em)
VALUES
('Introducao ao Calculo Vetorial', 'Texto ilustrativo sobre vetores e campos.', (SELECT id FROM usuario WHERE email='ana.clara@academica.edu'), NOW()),
('Onda e Particula', 'Discussao sobre dualidade onda-particula.', (SELECT id FROM usuario WHERE email='bruno.lima@academica.edu'), NOW()),
('Quimica Organica Basica', 'Reacoes de substituicao e adicao.', (SELECT id FROM usuario WHERE email='carlos.mendes@academica.edu'), NOW()),
('Biologia Celular Moderna', 'Estrutura e funcao das celulas.', (SELECT id FROM usuario WHERE email='helena.torres@academica.edu'), NOW()),
('Engenharia de Software Agil', 'Metodos ageis e Scrum.', (SELECT id FROM usuario WHERE email='eduardo.souza@academica.edu'), NOW()),
('Estatistica Descritiva', 'Medidas de posicao e dispersao.', (SELECT id FROM usuario WHERE email='patricia.souza@academica.edu'), NOW()),
('Programacao Python para Dados', 'Pandas e NumPy.', (SELECT id FROM usuario WHERE email='lucas.pereira@academica.edu'), NOW()),
('Historia da Ciencia Moderna', 'Evolucao do pensamento cientifico.', (SELECT id FROM usuario WHERE email='helena.torres@academica.edu'), NOW())
ON CONFLICT DO NOTHING;

-- 10. CURTIDAS
INSERT INTO curtida (postagem_id, usuario_id, criado_em)
SELECT p.id, u.id, NOW()
FROM postagem p
CROSS JOIN usuario u
WHERE p.titulo IN ('Bem-vindo a plataforma!', 'Dica de estudo: Calculo', 'Evento: Seminaro de Fisica', 'Novo artigo publicado', 'Duvida sobre Quimica')
AND u.tipo_usuario = 'Aluno'
ON CONFLICT DO NOTHING;

-- 11. NOTIFICACOES
INSERT INTO notificacao (usuario_id, mensagem, lida, data_criacao)
SELECT u.id, msg, false, NOW()
FROM usuario u
CROSS JOIN (VALUES
('Voce recebeu uma nova mensagem na sala MAT-101.', 'diana.rocha@academica.edu'),
('Novo artigo publicado pelo professor Carlos.', 'fernanda.alves@academica.edu'),
('Sua atividade foi corrigida.', 'gabriela.martins@academica.edu'),
('Convite para a sala de Fisica Moderna.', 'igor.santos@academica.edu'),
('Novo evento na plataforma.', 'julia.costa@academica.edu'),
('Voce recebeu uma curtida.', 'karina.oliveira@academica.edu'),
('Nova postagem no feed.', 'lucas.pereira@academica.edu'),
('Sua submissao foi entregue.', 'nicolas.almeida@academica.edu'),
('Mensagem da comunidade de Matematica.', 'otavio.lima@academica.edu'),
('Atualizacao de perfil necessaria.', 'quiteria.rocha@academica.edu')
) AS dados(msg, email_ref)
WHERE u.email = email_ref
ON CONFLICT DO NOTHING;

-- 12. AMIZADES
INSERT INTO amizade (solicitante_id, destinatario_id, status, data_solicitacao)
SELECT a.id, b.id, 'ACEITA', NOW()
FROM usuario a, usuario b
WHERE a.email = 'diana.rocha@academica.edu' AND b.email = 'fernanda.alves@academica.edu'
ON CONFLICT DO NOTHING;

INSERT INTO amizade (solicitante_id, destinatario_id, status, data_solicitacao)
SELECT a.id, b.id, 'ACEITA', NOW()
FROM usuario a, usuario b
WHERE a.email = 'gabriela.martins@academica.edu' AND b.email = 'igor.santos@academica.edu'
ON CONFLICT DO NOTHING;

INSERT INTO amizade (solicitante_id, destinatario_id, status, data_solicitacao)
SELECT a.id, b.id, 'PENDENTE', NOW()
FROM usuario a, usuario b
WHERE a.email = 'julia.costa@academica.edu' AND b.email = 'karina.oliveira@academica.edu'
ON CONFLICT DO NOTHING;

SELECT 'Dados ficticios inseridos com sucesso (seed completo corrigido).' AS status;
