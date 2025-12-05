describe('Login de Professor', () => {
  beforeEach(() => {
    cy.visit('/login')
  })

  it('deve exibir a página de login corretamente', () => {
    cy.get('h2').should('contain', 'Login')
    cy.get('input[name="email"]').should('be.visible')
    cy.get('input[name="senha"]').should('be.visible')
    cy.get('button[type="submit"]').should('be.visible')
  })

  it('deve fazer login com credenciais válidas', () => {
    // Primeiro, cadastrar um professor via API
    cy.request('POST', 'http://localhost:8080/api/professores/cadastro', {
      nome: 'Professor Teste',
      email: 'professor.teste@edu.com',
      senha: 'senha123',
      matricula: '12345678'
    })

    cy.get('input[name="email"]').type('professor.teste@edu.com')
    cy.get('input[name="senha"]').type('senha123')
    cy.get('button[type="submit"]').click()

    // Verificar se redirecionou para o feed ou home
    cy.url().should('not.include', '/login')
  })

  it('deve mostrar erro com credenciais inválidas', () => {
    cy.get('input[name="email"]').type('invalido@edu.com')
    cy.get('input[name="senha"]').type('senhaerrada')
    cy.get('button[type="submit"]').click()

    // Verificar se permanece na página ou mostra erro
    cy.url().should('include', '/login')
  })

  it('deve navegar para página de cadastro', () => {
    cy.get('a').contains('Cadastrar').click()
    cy.url().should('include', '/cadastro')
  })
})