describe('Cadastro de Professor', () => {
  beforeEach(() => {
    cy.visit('/cadastro')
  })

  it('deve exibir o formulário de cadastro corretamente', () => {
    cy.get('h2').should('contain', 'Cadastro')
    cy.get('input[name="nome"]').should('be.visible')
    cy.get('input[name="email"]').should('be.visible')
    cy.get('input[name="senha"]').should('be.visible')
    cy.get('input[name="matricula"]').should('be.visible')
    cy.get('button[type="submit"]').should('be.visible')
  })

  it('deve cadastrar professor com dados válidos', () => {
    const professor = {
      nome: 'João Silva',
      sobrenome: 'Teste',
      email: `joao.${Date.now()}@edu.com`,
      senha: 'senha123',
      matricula: '87654321'
    }

    cy.get('input[name="nome"]').type(professor.nome)
    cy.get('input[name="sobrenome"]').type(professor.sobrenome)
    cy.get('input[name="email"]').type(professor.email)
    cy.get('input[name="senha"]').type(professor.senha)
    cy.get('input[name="matricula"]').type(professor.matricula)
    cy.get('button[type="submit"]').click()

    // Verificar se redirecionou para login ou perfil
    cy.url().should('not.include', '/cadastro')
  })

  it('deve mostrar erro ao tentar cadastrar email duplicado', () => {
    // Primeiro cadastro
    const professor = {
      nome: 'Maria Santos',
      email: `maria.${Date.now()}@edu.com`,
      senha: 'senha123',
      matricula: '11223344'
    }

    cy.request('POST', 'http://localhost:8080/api/professores/cadastro', professor)

    // Segundo cadastro com mesmo email
    cy.get('input[name="nome"]').type('Outro Nome')
    cy.get('input[name="email"]').type(professor.email)
    cy.get('input[name="senha"]').type('outrasenha')
    cy.get('input[name="matricula"]').type('55667788')
    cy.get('button[type="submit"]').click()

    // Deve permanecer na página ou mostrar erro
    cy.url().should('include', '/cadastro')
  })

  it('deve navegar para página de login', () => {
    cy.get('a').contains('Login').click()
    cy.url().should('include', '/login')
  })
})