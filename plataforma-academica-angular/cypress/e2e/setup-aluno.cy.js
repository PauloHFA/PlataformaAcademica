describe('Setup de Aluno', () => {
    it('deve criar um aluno randômico', () => {
        const randomId = Date.now();
        cy.request({
            method: 'POST',
            url: 'http://localhost:8090/api/usuarios/cadastro',
            body: {
                nome: 'Aluno ' + randomId,
                sobrenome: 'Teste',
                email: `aluno.${randomId}@teste.com`,
                senha: 'SenhaTeste123!'
            },
            failOnStatusCode: false
        }).then((response) => {
            cy.log('Aluno criado com ID: ' + response.body.id);
        });
    });
});