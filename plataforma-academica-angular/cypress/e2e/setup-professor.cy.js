describe('Setup de Professor', () => {
    it('deve criar um professor randômico', () => {
        const randomId = Date.now();
        cy.request({
            method: 'POST',
            url: 'http://localhost:8090/api/professores/cadastro',
            body: {
                nome: 'Professor ' + randomId,
                email: `professor.${randomId}@teste.com`,
                senha: 'SenhaTeste123!',
                matricula: 'MAT' + randomId
            },
            failOnStatusCode: false
        }).then((response) => {
            cy.log('Professor criado com ID: ' + response.body.id);
        });
    });
});