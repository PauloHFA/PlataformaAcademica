describe('Sistema de Artigos', () => {
  before(() => {
    // Criar professor para os testes
    cy.request('POST', 'http://localhost:8080/api/professores/cadastro', {
      nome: 'Professor Artigos',
      email: `prof.artigos.${Date.now()}@edu.com`,
      senha: 'senha123',
      matricula: '66666666'
    })
  })

  it('deve acessar página de artigos', () => {
    cy.visit('/login')
    cy.get('input[name="email"]').type(`prof.artigos.${Date.now()}@edu.com`)
    cy.get('input[name="senha"]').type('senha123')
    cy.get('button[type="submit"]').click()

    cy.visit('/artigos')
    cy.get('body').should('exist')
  })

  it('deve criar um artigo via API', () => {
    cy.request('POST', 'http://localhost:8080/api/artigos', {
      titulo: 'Artigo de Teste Cypress',
      conteudo: 'Conteúdo completo do artigo criado para testes automatizados.',
      resumo: 'Resumo do artigo de teste',
      categoria: 'Tecnologia',
      tags: ['teste', 'cypress', 'automação']
    }).then((response) => {
      expect(response.status).to.eq(200)
      expect(response.body).to.have.property('id')
    })
  })

  it('deve listar artigos via API', () => {
    cy.request('GET', 'http://localhost:8080/api/artigos').then((response) => {
      expect(response.status).to.eq(200)
      expect(response.body).to.be.an('array')
    })
  })

  it('deve buscar artigos por categoria', () => {
    cy.request('GET', 'http://localhost:8080/api/artigos/categoria/Tecnologia').then((response) => {
      expect(response.status).to.eq(200)
      expect(response.body).to.be.an('array')
    })
  })
})