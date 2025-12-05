// ***********************************************
// This example commands.js shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************
//
//
// -- This is a parent command --
// Cypress.Commands.add('login', (email, password) => { ... })
//
//
// -- This is a child command --
// Cypress.Commands.add('drag', { prevSubject: 'element'}, (subject, options) => { ... })
//
//
// -- This is a dual command --
// Cypress.Commands.add('dismiss', { prevSubject: 'optional'}, (subject, options) => { ... })
//
//
// -- This will overwrite an existing command --
// Cypress.Commands.overwrite('visit', (originalFn, url, options) => { ... })

// Comando personalizado para login de professor
Cypress.Commands.add('loginProfessor', (email, password) => {
  cy.visit('/usuario/login')
  cy.get('input[name="email"]').type(email)
  cy.get('input[name="senha"]').type(password)
  cy.get('button[type="submit"]').click()
})

// Comando personalizado para cadastro de professor
Cypress.Commands.add('cadastrarProfessor', (professor) => {
  cy.visit('/cadastro')
  cy.get('input[name="nome"]').type(professor.nome)
  cy.get('input[name="sobrenome"]').type(professor.sobrenome || '')
  cy.get('input[name="email"]').type(professor.email)
  cy.get('input[name="senha"]').type(professor.senha)
  cy.get('input[name="matricula"]').type(professor.matricula)
  cy.get('button[type="submit"]').click()
})

// Comando para limpar dados de teste
Cypress.Commands.add('limparDadosTeste', () => {
  // Implementar limpeza via API se necessário
  cy.request('DELETE', '/api/test/cleanup').then((response) => {
    expect(response.status).to.eq(200)
  })
})