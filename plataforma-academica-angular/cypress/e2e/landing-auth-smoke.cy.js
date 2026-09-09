describe('Smoke da landing e autenticacao', () => {
    beforeEach(() => {
        cy.clearLocalStorage()
    })

    it('renderiza a landing com as cinco funcionalidades', () => {
        cy.visit('/')
        cy.contains('Estudar junto').should('be.visible')
        cy.get('#salas').should('exist')
        cy.get('#atividades').should('exist')
        cy.get('#feed').should('exist')
        cy.get('#amigos').should('exist')
        cy.get('#perfil').should('exist')
    })

    it('altera o estado do pedido de amizade', () => {
        cy.visit('/')
        cy.get('#amigos').scrollIntoView()
        cy.get('#amigos .friend-request button').first().click()
        cy.get('#amigos .friend-request button').first().should('contain', 'Pedido enviado')
        cy.get('#amigos').should('contain', 'agora vocês podem estudar juntos')
    })

    it('valida login vazio e mantém a rota de autenticação', () => {
        cy.visit('/login')
        cy.get('button[type="submit"]').click()
        cy.url().should('include', '/login')
        cy.contains('preencha todos os campos').should('be.visible')
    })

    it('expõe os provedores sociais como ações ativas', () => {
        cy.visit('/login')
        cy.get('.btn-google').should('be.visible').and('not.be.disabled')
        cy.get('.btn-facebook').should('be.visible').and('not.be.disabled')
    })
})
