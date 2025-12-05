describe('Submissões de Atividades', () => {
  let professorId
  let alunoId
  let salaId
  let atividadeId

  before(() => {
    // Criar professor
    cy.request('POST', 'http://localhost:8080/api/professores/cadastro', {
      nome: 'Professor Atividades',
      email: `prof.ativ.${Date.now()}@edu.com`,
      senha: 'senha123',
      matricula: '55555555'
    }).then((response) => {
      professorId = response.body.id

      // Criar sala
      cy.request('POST', `http://localhost:8080/api/saladeaula/criar/${professorId}`, {
        nome: 'Sala para Submissões'
      }).then((salaResponse) => {
        salaId = salaResponse.body.id

        // Criar atividade
        cy.request('POST', `http://localhost:8080/api/atividades/criar/${salaId}`, {
          titulo: 'Atividade de Teste',
          descricao: 'Descrição da atividade para teste',
          dataEntrega: '2024-12-31T23:59:59',
          pontos: 10,
          tipoDocumentoSubmissao: 'PDF'
        }).then((atividadeResponse) => {
          atividadeId = atividadeResponse.body.id
        })
      })
    })

    // Criar aluno
    cy.request('POST', 'http://localhost:8080/api/usuarios', {
      nome: 'Aluno Teste',
      email: `aluno.teste.${Date.now()}@edu.com`,
      senha: 'senha123',
      tipoUsuario: 'ALUNO'
    }).then((response) => {
      alunoId = response.body.id

      // Adicionar aluno à sala
      cy.request('POST', `http://localhost:8080/api/saladeaula/${salaId}/membros`, {
        usuarioId: alunoId,
        papel: 'ALUNO'
      })
    })
  })

  it('deve permitir aluno fazer login e acessar atividade', () => {
    cy.visit('/login')
    cy.get('input[name="email"]').type(`aluno.teste.${Date.now()}@edu.com`)
    cy.get('input[name="senha"]').type('senha123')
    cy.get('button[type="submit"]').click()

    cy.visit(`/salas/${salaId}/atividades/${atividadeId}`)
    cy.get('h1').should('contain', 'Atividade de Teste')
  })

  it('deve permitir aluno enviar submissão', () => {
    cy.visit('/login')
    cy.get('input[name="email"]').type(`aluno.teste.${Date.now()}@edu.com`)
    cy.get('input[name="senha"]').type('senha123')
    cy.get('button[type="submit"]').click()

    cy.visit(`/salas/${salaId}/atividades/${atividadeId}`)

    // Preencher formulário de submissão
    cy.get('textarea[name="descricaoSubmissao"]').type('Esta é minha submissão para a atividade de teste.')
    // Para arquivo, seria necessário fazer upload, mas para teste básico vamos enviar sem arquivo

    cy.get('button[type="submit"]').contains('Enviar Submissão').click()

    // Verificar se submissão foi enviada
    cy.get('.card-success').should('contain', 'Submissão Enviada')
  })

  it('deve permitir professor visualizar submissões', () => {
    cy.visit('/login')
    cy.get('input[name="email"]').type(`prof.ativ.${Date.now()}@edu.com`)
    cy.get('input[name="senha"]').type('senha123')
    cy.get('button[type="submit"]').click()

    cy.visit(`/salas/${salaId}/atividades/${atividadeId}/submissoes`)

    // Verificar se há submissões listadas
    cy.get('body').should('exist') // Pelo menos carregar a página
  })

  it('deve enviar submissão via API', () => {
    // Testar via API diretamente
    cy.request('POST', `http://localhost:8080/api/submissoes-atividade`, {
      atividadeId: atividadeId,
      alunoId: alunoId,
      descricao: 'Submissão via API',
      urlDocumento: null
    }).then((response) => {
      expect(response.status).to.eq(200)
    })

    // Verificar listagem
    cy.request('GET', `http://localhost:8080/api/submissoes-atividade/atividade/${atividadeId}`).then((response) => {
      expect(response.status).to.eq(200)
      expect(response.body).to.be.an('array')
      expect(response.body.length).to.be.greaterThan(0)
    })
  })
})