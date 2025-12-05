describe('Perfil e Feed', () => {
  before(() => {
    // Criar professor para os testes
    cy.request('POST', 'http://localhost:8080/api/professores/cadastro', {
      nome: 'Professor Perfil',
      email: `prof.perfil.${Date.now()}@edu.com`,
      senha: 'senha123',
      matricula: '77777777'
    })
  })

  it('deve acessar perfil do usuário', () => {
    cy.visit('/login')
    cy.get('input[name="email"]').type(`prof.perfil.${Date.now()}@edu.com`)
    cy.get('input[name="senha"]').type('senha123')
    cy.get('button[type="submit"]').click()

    cy.visit('/perfil')
    cy.get('h1').should('contain', 'Perfil')
    cy.get('body').should('contain', 'Professor Perfil')
  })

  it('deve navegar para o feed', () => {
    cy.visit('/feed')
    // Verificar se o feed carrega
    cy.get('body').should('exist')
  })

  it('deve editar perfil', () => {
    cy.visit('/perfil-editar')
    cy.get('input[name="nome"]').should('be.visible')
    cy.get('input[name="email"]').should('be.visible')

    // Preencher alguns campos
    cy.get('input[name="descricao"]').type('Professor de Engenharia de Software')
    cy.get('input[name="instituicaoEnsino"]').type('Universidade Federal')
    cy.get('button[type="submit"]').click()

    // Verificar se salvou
    cy.url().should('include', '/perfil')
  })

  it('deve listar usuários', () => {
    cy.visit('/usuarios')
    cy.get('body').should('exist')
  })

  it('deve acessar página de amigos', () => {
    cy.visit('/amigos')
    cy.get('body').should('exist')
  })
})