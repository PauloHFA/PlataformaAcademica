-- Script de seed expandido para testes - Dados fictícios em massa
-- Executar: psql -U postgres -d plataforma_academica -f seed_dados_expandido.sql

-- 1. USUÁRIOS (professores, alunos, admins) - 20 registros
INSERT INTO usuario (id, nome, email, senha_hash, tipo_usuario, sobrenome, descricao, instituicao_ensino, cidade, pais, senha)
SELECT gen_random_uuid(), nome, email, 'senha123', tipo, sobrenome, descricao, instituicao, cidade, pais, 'senha123'
FROM (VALUES
('Prof. Ana Silva', 'ana.silva@academica.edu', 'Professor', 'Silva', 'Professora de Matemática', 'UFRJ', 'Rio de Janeiro', 'Brasil'),
('Prof. Bruno Costa', 'bruno.costa@academica.edu', 'Professor', 'Costa', 'Professor de Física', 'USP', 'São Paulo', 'Brasil'),
('Prof. Carla Mendes', 'carla.mendes@academica.edu', 'Professor', 'Mendes', 'Professora de Química', 'UNICAMP', 'Campinas', 'Brasil'),
('Prof. Diego Rocha', 'diego.rocha@academica.edu', 'Professor', 'Rocha', 'Professor de Engenharia', 'UFRJ', 'Rio de Janeiro', 'Brasil'),
('Prof. Fernanda Lima', 'fernanda.lima@academica.edu', 'Professor', 'Lima', 'Professora de Biologia', 'USP', 'São Paulo', 'Brasil'),
('Aluno João Pereira', 'joao.pereira@academica.edu', 'Aluno', 'Pereira', 'Estudante de Engenharia', 'UFRJ', 'Rio de Janeiro', 'Brasil'),
('Aluno Maria Souza', 'maria.souza@academica.edu', 'Aluno', 'Souza', 'Estudante de Física', 'USP', 'São Paulo', 'Brasil'),
('Aluno Pedro Almeida', 'pedro.almeida@academica.edu', 'Aluno', 'Almeida', 'Estudante de Química', 'UNICAMP', 'Campinas', 'Brasil'),
('Aluno Lucas Oliveira', 'lucas.oliveira@academica.edu', 'Aluno', 'Oliveira', 'Estudante de Matemática', 'UFRJ', 'Rio de Janeiro', 'Brasil'),
('Aluno Julia Santos', 'julia.santos@academica.edu', 'Aluno', 'Santos', 'Estudante de Biologia', 'USP', 'São Paulo', 'Brasil'),
('Aluno Rafael Costa', 'rafael.costa@academica.edu', 'Aluno', 'Costa', 'Estudante de Engenharia', 'UNICAMP', 'Campinas', 'Brasil'),
('Aluno Beatriz Lima', 'beatriz.lima@academica.edu', 'Aluno', 'Lima', 'Estudante de Física', 'UFRJ', 'Rio de Janeiro', 'Brasil'),
('Admin Carlos Nunes', 'carlos.nunes@academica.edu', 'Admin', 'Nunes', 'Administrador da Plataforma', 'UFRJ', 'Rio de Janeiro', 'Brasil'),
('Aluno Mariana Dias', 'mariana.dias@academica.edu', 'Aluno', 'Dias', 'Estudante de Química', 'USP', 'São Paulo', 'Brasil'),
('Aluno Felipe Rocha', 'felipe.rocha@academica.edu', 'Aluno', 'Rocha', 'Estudante de Matemática', 'UNICAMP', 'Campinas', 'Brasil'),
('Prof. Helena Torres', 'helena.torres@academica.edu', 'Professor', 'Torres', 'Professora de Física', 'UFRJ', 'Rio de Janeiro', 'Brasil'),
('Aluno Gabriela Martins', 'gabriela.martins@academica.edu', 'Aluno', 'Martins', 'Estudante de Engenharia', 'USP', 'São Paulo', 'Brasil'),
('Aluno Thiago Pereira', 'thiago.pereira@academica.edu', 'Aluno', 'Pereira', 'Estudante de Biologia', 'UNICAMP', 'Campinas', 'Brasil'),
('Prof. Ricardo Almeida', 'ricardo.almeida@academica.edu', 'Professor', 'Almeida', 'Professor de Matemática', 'UFRJ', 'Rio de Janeiro', 'Brasil'),
('Aluno Camila Souza', 'camila.souza@academica.edu', 'Aluno', 'Souza', 'Estudante de Física', 'USP', 'São Paulo', 'Brasil')
) AS dados(nome, email, tipo, sobrenome, descricao, instituicao, cidade, pais)
ON CONFLICT (email) DO NOTHING;

-- 2. PROFESSORES (tabela professor - id UUID, matricula)
INSERT INTO professor (id, matricula)
SELECT id, 'PROF-' || LPAD(gen_random_uuid()::text, 4, '0')
FROM usuario WHERE tipo_usuario = 'Professor'
ON CONFLICT (id) DO NOTHING;

-- 3. SALAS DE AULA (10 salas)
INSERT INTO sala_de_aula (id, nome, descricao, codigo, codigo_sala, criado_por, criado_em)
SELECT gen_random_uuid(), nome, descricao, codigo, codigo, (SELECT id FROM usuario WHERE email='ana.silva@academica.edu'), NOW()
FROM (VALUES
('Matemática Avançada', 'Cálculo e álgebra linear', 'MAT-101', 'MAT-101'),
('Física Moderna', 'Mecânica quântica', 'FIS-202', 'FIS-202'),
('Química Orgânica', 'Reações orgânicas', 'QUI-303', 'QUI-303'),
('Biologia Celular', 'Estudo das células', 'BIO-404', 'BIO-404'),
('Engenharia de Software', 'Desenvolvimento de sistemas', 'ENG-505', 'ENG-505'),
('Estatística Aplicada', 'Análise de dados', 'EST-606', 'EST-606'),
('Programação Python', 'Python avançado', 'PY-707', 'PY-707'),
('História da Ciência', 'Evolução científica', 'HIS-808', 'HIS-808'),
('Física Experimental', 'Laboratório de física', 'FIS-909', 'FIS-909'),
('Matemática Discreta', 'Lógica e conjuntos', 'MAT-1010', 'MAT-1010')
) AS dados(nome, descricao, codigo, codigo_sala)
ON CONFLICT (codigo_sala) DO NOTHING;

-- 4. MEMBROS DAS SALAS (múltiplos alunos por sala)
INSERT INTO sala_membro (sala_id, usuario_id, papel, data_entrada)
SELECT s.id, u.id, 'ALUNO', NOW()
FROM sala_de_aula s
CROSS JOIN usuario u
WHERE s.codigo_sala IN ('MAT-101', 'FIS-202', 'QUI-303', 'BIO-404', 'ENG-505')
AND u.tipo_usuario = 'Aluno'
AND u.email IN ('joao.pereira@academica.edu', 'maria.souza@academica.edu', 'pedro.almeida@academica.edu', 'lucas.oliveira@academica.edu', 'julia.santos@academica.edu', 'rafael.costa@academica.edu', 'beatriz.lima@academica.edu', 'mariana.dias@academica.edu', 'felipe.rocha@academica.edu', 'gabriela.martins@academica.edu', 'thiago.pereira@academica.edu', 'camila.souza@academica.edu')
ON CONFLICT DO NOTHING;

-- 5. POSTAGENS / FEED (15 postagens variadas)
INSERT INTO postagem (titulo, conteudo, usuario_id, criado_em, tipo)
SELECT titulo, conteudo, (SELECT id FROM usuario WHERE email=email_ref), NOW(), tipo
FROM (VALUES
('Bem-vindo à plataforma!', 'Este é um exemplo de postagem para ilustrar o feed.', 'ana.silva@academica.edu', 'GERAL'),
('Dica de estudo: Cálculo', 'Revisem os capítulos 3 e 4 antes da prova.', 'ana.silva@academica.edu', 'DICA'),
('Evento: Seminário de Física', 'Acontece na Sala de Física Moderna na sexta-feira.', 'bruno.costa@academica.edu', 'EVENTO'),
('Novo artigo publicado', 'Confira o artigo sobre cálculo vetorial.', 'carla.mendes@academica.edu', 'ARTIGO'),
('Dúvida sobre Química', 'Como balancear esta reação?', 'pedro.almeida@academica.edu', 'PERGUNTA'),
('Projeto de Engenharia', 'Apresentação do projeto de software.', 'diego.rocha@academica.edu', 'PROJETO'),
('Recomendação de leitura', 'Leiam o capítulo 5 do livro de física.', 'fernanda.lima@academica.edu', 'DICA'),
('Resultado da prova', 'Média da turma foi 7.8.', 'ricardo.almeida@academica.edu', 'GERAL'),
('Convite para sala', 'Entre na sala de Matemática Avançada.', 'helena.torres@academica.edu', 'CONVITE'),
('Discussão sobre Biologia', 'Qual a relação entre DNA e RNA?', 'joao.pereira@academica.edu', 'PERGUNTA'),
('Material de apoio', 'Link para slides da aula de estatística.', 'maria.souza@academica.edu', 'MATERIAL'),
('Feedback do professor', 'Excelente trabalho na atividade 3.', 'ana.silva@academica.edu', 'FEEDBACK'),
('Anúncio de evento', 'Palestra com pesquisador externo.', 'bruno.costa@academica.edu', 'EVENTO'),
('Dica de programação', 'Use listas de compreensão em Python.', 'lucas.oliveira@academica.edu', 'DICA'),
('Pergunta sobre Engenharia', 'Como modelar este sistema?', 'rafael.costa@academica.edu', 'PERGUNTA')
) AS dados(titulo, conteudo, email_ref, tipo)
ON CONFLICT DO NOTHING;

-- 6. COMUNIDADES (8 comunidades)
INSERT INTO comunidades (nome, descricao, dono_id, criado_em)
SELECT nome, descricao, (SELECT id FROM usuario WHERE email=email_ref), NOW()
FROM (VALUES
('Comunidade de Matemática', 'Espaço para discutir problemas e soluções.', 'ana.silva@academica.edu'),
('Comunidade de Física', 'Troca de ideias sobre física teórica e aplicada.', 'bruno.costa@academica.edu'),
('Comunidade de Química', 'Reações, sínteses e análises.', 'carla.mendes@academica.edu'),
('Comunidade de Engenharia', 'Projetos e desenvolvimento.', 'diego.rocha@academica.edu'),
('Comunidade de Biologia', 'Estudos celulares e genéticos.', 'fernanda.lima@academica.edu'),
('Comunidade de Estatística', 'Análise de dados e modelos.', 'ricardo.almeida@academica.edu'),
('Comunidade de Programação', 'Python, Java e desenvolvimento.', 'lucas.oliveira@academica.edu'),
('Comunidade Geral', 'Assuntos diversos da plataforma.', 'carlos.nunes@academica.edu')
) AS dados(nome, descricao, email_ref)
ON CONFLICT DO NOTHING;

-- 7. MEMBROS DAS COMUNIDADES (múltiplos membros)
INSERT INTO membros_comunidade (comunidade_id, usuario_id, papel, entrou_em)
SELECT c.id, u.id, 'MEMBRO', NOW()
FROM comunidades c
CROSS JOIN usuario u
WHERE c.nome IN ('Comunidade de Matemática', 'Comunidade de Física', 'Comunidade de Química', 'Comunidade de Engenharia')
AND u.tipo_usuario = 'Aluno'
AND u.email IN ('joao.pereira@academica.edu', 'maria.souza@academica.edu', 'pedro.almeida@academica.edu', 'lucas.oliveira@academica.edu', 'julia.santos@academica.edu', 'rafael.costa@academica.edu', 'beatriz.lima@academica.edu', 'mariana.dias@academica.edu', 'felipe.rocha@academica.edu', 'gabriela.martins@academica.edu', 'thiago.pereira@academica.edu', 'camila.souza@academica.edu')
ON CONFLICT DO NOTHING;

-- 8. ATIVIDADES (10 atividades)
INSERT INTO atividade (id, titulo, descricao, sala_id, criado_por, criado_em, data_entrega)
SELECT gen_random_uuid(), titulo, descricao, (SELECT id FROM sala_de_aula WHERE codigo_sala=codigo), (SELECT id FROM usuario WHERE email=email_ref), NOW(), NOW() + INTERVAL '7 days'
FROM (VALUES
('Lista 1 - Cálculo', 'Resolver exercícios do capítulo 3.', 'MAT-101', 'ana.silva@academica.edu'),
('Experimento de Física', 'Montar circuito elétrico.', 'FIS-202', 'bruno.costa@academica.edu'),
('Síntese Orgânica', 'Preparar composto simples.', 'QUI-303', 'carla.mendes@academica.edu'),
('Relatório de Biologia', 'Analisar amostra celular.', 'BIO-404', 'fernanda.lima@academica.edu'),
('Projeto de Software', 'Criar aplicação web simples.', 'ENG-505', 'diego.rocha@academica.edu'),
('Análise Estatística', 'Calcular média e desvio.', 'EST-606', 'ricardo.almeida@academica.edu'),
('Script Python', 'Automatizar tarefa simples.', 'PY-707', 'lucas.oliveira@academica.edu'),
('Pesquisa Histórica', 'Escrever texto curto.', 'HIS-808', 'helena.torres@academica.edu'),
('Laboratório de Física', 'Medir aceleração da gravidade.', 'FIS-909', 'bruno.costa@academica.edu'),
('Problemas de Matemática', 'Resolver 10 problemas.', 'MAT-1010', 'ricardo.almeida@academica.edu')
) AS dados(titulo, descricao, codigo, email_ref)
ON CONFLICT DO NOTHING;

-- 9. SUBMISSÕES DE ATIVIDADE (múltiplas submissões)
INSERT INTO submissao_atividade (id, atividade_id, aluno_id, arquivo_url, nota, status, criado_em)
SELECT gen_random_uuid(), a.id, u.id, 'uploads/submissao_' || gen_random_uuid() || '.pdf', ROUND((random()*10)::numeric, 1), CASE WHEN random()>0.3 THEN 'ENTREGUE' ELSE 'PENDENTE' END, NOW()
FROM atividade a
CROSS JOIN usuario u
WHERE a.titulo IN ('Lista 1 - Cálculo', 'Experimento de Física', 'Síntese Orgânica', 'Relatório de Biologia', 'Projeto de Software')
AND u.tipo_usuario = 'Aluno'
AND u.email IN ('joao.pereira@academica.edu', 'maria.souza@academica.edu', 'pedro.almeida@academica.edu', 'lucas.oliveira@academica.edu', 'julia.santos@academica.edu', 'rafael.costa@academica.edu', 'beatriz.lima@academica.edu', 'mariana.dias@academica.edu', 'felipe.rocha@academica.edu', 'gabriela.martins@academica.edu', 'thiago.pereira@academica.edu', 'camila.souza@academica.edu')
ON CONFLICT DO NOTHING;

-- 10. ARTIGOS (8 artigos)
INSERT INTO artigo (titulo, conteudo, autor_id, publicado_em)
SELECT titulo, conteudo, (SELECT id FROM usuario WHERE email=email_ref), NOW()
FROM (VALUES
('Introdução ao Cálculo Vetorial', 'Texto ilustrativo sobre vetores e campos.', 'ana.silva@academica.edu'),
('Onda e Partícula', 'Discussão sobre dualidade onda-partícula.', 'bruno.costa@academica.edu'),
('Química Orgânica Básica', 'Reações de substituição e adição.', 'carla.mendes@academica.edu'),
('Biologia Celular Moderna', 'Estrutura e função das células.', 'fernanda.lima@academica.edu'),
('Engenharia de Software Ágil', 'Métodos ágeis e Scrum.', 'diego.rocha@academica.edu'),
('Estatística Descritiva', 'Medidas de posição e dispersão.', 'ricardo.almeida@academica.edu'),
('Programação Python para Dados', 'Pandas e NumPy.', 'lucas.oliveira@academica.edu'),
('História da Ciência Moderna', 'Evolução do pensamento científico.', 'helena.torres@academica.edu')
) AS dados(titulo, conteudo, email_ref)
ON CONFLICT DO NOTHING;

-- 11. CURTIDAS (múltiplas curtidas)
INSERT INTO curtida (postagem_id, usuario_id, criado_em)
SELECT p.id, u.id, NOW()
FROM postagem p
CROSS JOIN usuario u
WHERE p.titulo IN ('Bem-vindo à plataforma!', 'Dica de estudo: Cálculo', 'Evento: Seminário de Física', 'Novo artigo publicado', 'Dúvida sobre Química')
AND u.tipo_usuario = 'Aluno'
AND u.email IN ('joao.pereira@academica.edu', 'maria.souza@academica.edu', 'pedro.almeida@academica.edu', 'lucas.oliveira@academica.edu', 'julia.santos@academica.edu', 'rafael.costa@academica.edu', 'beatriz.lima@academica.edu', 'mariana.dias@academica.edu', 'felipe.rocha@academica.edu', 'gabriela.martins@academica.edu', 'thiago.pereira@academica.edu', 'camila.souza@academica.edu')
ON CONFLICT DO NOTHING;

-- 12. NOTIFICAÇÕES (múltiplas notificações)
INSERT INTO notificacao (usuario_id, mensagem, lida, criado_em)
SELECT u.id, msg, false, NOW()
FROM usuario u
CROSS JOIN (VALUES
('Você recebeu uma nova mensagem na sala MAT-101.', 'ana.silva@academica.edu'),
('Novo artigo publicado pelo professor Carlos.', 'diana.rocha@academica.edu'),
('Sua atividade foi corrigida.', 'joao.pereira@academica.edu'),
('Convite para a sala de Física Moderna.', 'maria.souza@academica.edu'),
('Novo evento na plataforma.', 'pedro.almeida@academica.edu'),
('Você recebeu uma curtida.', 'lucas.oliveira@academica.edu'),
('Nova postagem no feed.', 'julia.santos@academica.edu'),
('Sua submissão foi entregue.', 'rafael.costa@academica.edu'),
('Mensagem da comunidade de Matemática.', 'beatriz.lima@academica.edu'),
('Atualização de perfil necessária.', 'mariana.dias@academica.edu')
) AS dados(msg, email_ref)
WHERE u.email = email_ref
ON CONFLICT DO NOTHING;

SELECT 'Dados fictícios inseridos com sucesso (seed expandido).' AS status;
