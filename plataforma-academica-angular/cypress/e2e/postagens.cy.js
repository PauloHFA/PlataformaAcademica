describe('Gerenciamento de Postagens', () => {
  before(() => {
    // Criar professor para os testes
    cy.request('POST', 'http://localhost:8080/api/professores/cadastro', {
      nome: 'Professor Postagens',
      email: `prof.postagens.${Date.now()}@edu.com`,
      senha: 'senha123',
      matricula: '11111111'
    })
  })

  it('deve criar uma nova postagem', () => {
    cy.visit('/login')
    cy.get('input[name="email"]').type(`prof.postagens.${Date.now()}@edu.com`)
    cy.get('input[name="senha"]').type('senha123')
    cy.get('button[type="submit"]').click()

    cy.visit('/feed')
    cy.get('button').contains('Nova Postagem').click()
    cy.get('input[name="titulo"]').type('Postagem de Teste Cypress')
    cy.get('textarea[name="conteudo"]').type('Esta é uma postagem criada automaticamente por Cypress para testar as funcionalidades.')
    cy.get('button').contains('Publicar').click()

    // Verificar se a postagem aparece no feed
    cy.get('.postagem-titulo').should('contain', 'Postagem de Teste Cypress')
    cy.get('.postagem-conteudo').should('contain', 'Esta é uma postagem criada automaticamente por Cypress')
  })

  it('deve curtir uma postagem', () => {
    cy.visit('/login')
    cy.get('input[name="email"]').type(`prof.postagens.${Date.now()}@edu.com`)
    cy.get('input[name="senha"]').type('senha123')
    cy.get('button[type="submit"]').click()

    cy.visit('/feed')
    // Aguardar postagens carregarem
    cy.wait(2000)
    // Verificar se há postagens e clicar no botão curtir da primeira
    cy.get('.btn-curtir').first().click()
    // Verificar se o contador aumentou
    cy.get('.curtidas-count').first().should('not.contain', '0')
  })

  it('deve adicionar comentário a uma postagem', () => {
    cy.visit('/login')
    cy.get('input[name="email"]').type(`prof.postagens.${Date.now()}@edu.com`)
    cy.get('input[name="senha"]').type('senha123')
    cy.get('button[type="submit"]').click()

    cy.visit('/feed')
    cy.wait(2000)
    // Clicar no botão de comentários da primeira postagem
    cy.get('.btn-comentar').first().click()
    // Aguardar a seção de comentários aparecer
    cy.wait(1000)
    // Digitar comentário
    cy.get('textarea').last().type('Este é um comentário de teste do Cypress!')
    cy.get('button').contains('Comentar').click()
    // Verificar se o comentário aparece
    cy.get('.comentario-content').should('contain', 'Este é um comentário de teste do Cypress!')
  })
})