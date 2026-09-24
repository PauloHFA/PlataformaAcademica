describe('Login Funcional', () => {
    it('deve realizar o login com sucesso usando o bypass', () => {
        // 1. Realiza o bypass de login
        cy.login();

        // 2. Visita a página de login ou a página inicial
        cy.visit('/login');

        // 3. Verifica se o usuário foi redirecionado ou se o estado de autenticação está presente
        // Ajuste o seletor conforme a estrutura real da sua página após o login
        cy.window().then((win) => {
            expect(win.localStorage.getItem('auth_token')).to.eq('mock-token-para-testes');
        });

        // Exemplo: verificar se um elemento de "usuário logado" aparece
        // cy.get('.user-profile').should('be.visible');
    });
});
