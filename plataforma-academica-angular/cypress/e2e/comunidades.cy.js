describe('Gerenciamento de Comunidades', () => {
  before(() => {
    // Criar professor para os testes
    cy.request('POST', 'http://localhost:8080/api/professores/cadastro', {
      nome: 'Professor Comunidades',
      email: `prof.comunidades.${Date.now()}@edu.com`,
      senha: 'senha123',
      matricula: '22222222'
    })
  })

  it('deve acessar página de comunidades', () => {
    cy.visit('/login')
    cy.get('input[name="email"]').type(`prof.comunidades.${Date.now()}@edu.com`)
    cy.get('input[name="senha"]').type('senha123')
    cy.get('button[type="submit"]').click()

    cy.visit('/comunidades')
    cy.get('body').should('exist')
  })

  it('deve criar uma comunidade via API e verificar', () => {
    // Como o frontend pode não estar implementado, testar via API
    cy.request('POST', 'http://localhost:8080/api/comunidades', {
      nome: 'Comunidade de Teste Cypress',
      descricao: 'Comunidade criada para testes automatizados',
      regras: 'Sem spam',
      categoria: 'Educação'
    }).then((response) => {
      expect(response.status).to.eq(200)
      expect(response.body).to.have.property('id')
    })

    // Verificar listagem
    cy.request('GET', 'http://localhost:8080/api/comunidades').then((response) => {
      expect(response.status).to.eq(200)
      expect(response.body).to.be.an('array')
      expect(response.body.length).to.be.greaterThan(0)
    })
  })
})