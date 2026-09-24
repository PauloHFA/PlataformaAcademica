describe('Login de Professor e Aluno', () => {
  const randomId = Date.now();
  const professorEmail = `professor.${randomId}@teste.com`;
  const alunoEmail = `aluno.${randomId}@teste.com`;
  const senha = 'SenhaTeste123!';

  before(() => {
    // Criar professor randômico
    cy.request({
      method: 'POST',
      url: 'http://localhost:8090/api/professores/cadastro',
      body: {
        nome: 'Professor ' + randomId,
        email: professorEmail,
        senha: senha,
        matricula: 'MAT' + randomId
      },
      failOnStatusCode: false // Continua mesmo se o backend falhar (ex: erro de e-mail)
    });

    // Criar aluno randômico
    cy.request({
      method: 'POST',
      url: 'http://localhost:8090/api/usuarios/cadastro',
      body: {
        nome: 'Aluno ' + randomId,
        sobrenome: 'Teste',
        email: alunoEmail,
        senha: senha
      },
      failOnStatusCode: false
    });
  });

  beforeEach(() => {
    cy.visit('/login');
  });

  it('deve exibir a página de login corretamente', () => {
    cy.get('h2').should('contain', 'Login');
    cy.get('#email').should('be.visible');
    cy.get('#senha').should('be.visible');
    cy.get('button[type="submit"]').should('be.visible');
  });

  it('deve fazer login com credenciais de professor', () => {
    cy.get('#email').type(professorEmail);
    cy.get('#senha').type(senha);
    cy.get('button[type="submit"]').click();
    // Verifica se saiu da página de login
    cy.url().should('not.include', '/login');
  });

  it('deve fazer login com credenciais de aluno', () => {
    cy.get('#email').type(alunoEmail);
    cy.get('#senha').type(senha);
    cy.get('button[type="submit"]').click();
    cy.url().should('not.include', '/login');
  });
});