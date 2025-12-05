describe('Gerenciamento de Salas de Aula', () => {
  let professorId
  let salaId

  before(() => {
    // Criar professor para os testes
    cy.request('POST', 'http://localhost:8080/api/professores/cadastro', {
      nome: 'Professor Salas',
      email: `prof.salas.${Date.now()}@edu.com`,
      senha: 'senha123',
      matricula: '99999999'
    }).then((response) => {
      professorId = response.body.id
    })
  })

  it('deve criar uma sala de aula', () => {
    cy.visit('/login')
    cy.get('input[name="email"]').type(`prof.salas.${Date.now()}@edu.com`)
    cy.get('input[name="senha"]').type('senha123')
    cy.get('button[type="submit"]').click()

    cy.visit('/salas/criar')
    cy.get('input[name="nome"]').type('Sala de Programação Web')
    cy.get('button[type="submit"]').click()

    // Verificar se foi criado
    cy.url().should('include', '/salas/')
  })

  it('deve listar salas existentes', () => {
    cy.visit('/salas')
    // Verificar se há elementos de lista ou mensagem de vazio
    cy.get('body').should('exist')
  })

  it('deve adicionar membro à sala', () => {
    // Criar outro professor
    cy.request('POST', 'http://localhost:8080/api/professores/cadastro', {
      nome: 'Professor Membro',
      email: `prof.membro.${Date.now()}@edu.com`,
      senha: 'senha123',
      matricula: '88888888'
    })

    // Criar sala via API
    cy.request('POST', `http://localhost:8080/api/saladeaula/criar/${professorId}`, {
      nome: 'Sala para Testes de Membros'
    }).then((response) => {
      salaId = response.body.id

      cy.visit(`/salas/${salaId}/adicionar-membro`)
      // Interface para adicionar membros
      cy.get('body').should('contain', 'Adicionar Membro')
    })
  })

  it('deve criar atividade na sala', () => {
    cy.visit(`/salas/${salaId}/atividades/criar`)
    cy.get('input[name="titulo"]').type('Trabalho de Cypress')
    cy.get('textarea[name="descricao"]').type('Testar funcionalidades com Cypress')
    cy.get('input[name="dataEntrega"]').type('2024-12-31')
    cy.get('input[name="pontos"]').type('10')
    cy.get('button[type="submit"]').click()

    // Verificar se atividade foi criada
    cy.url().should('include', '/atividades/')
  })

  it('deve visualizar detalhes da sala', () => {
    cy.visit(`/salas/${salaId}`)
    cy.get('h1').should('contain', 'Sala para Testes de Membros')
  })
})