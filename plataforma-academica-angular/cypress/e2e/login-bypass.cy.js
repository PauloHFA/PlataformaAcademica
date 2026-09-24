describe('Login Bypass Test', () => {
    it('should bypass login and set user data', () => {
        cy.login();
        // Verify if the data was set in localStorage
        cy.window().then((win) => {
            expect(win.localStorage.getItem('auth_token')).to.eq('mock-token-para-testes');
            const userData = JSON.parse(win.localStorage.getItem('user_data'));
            expect(userData.nome).to.eq('Usuário de Teste');
        });
    });
});
