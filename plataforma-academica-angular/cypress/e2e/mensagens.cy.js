describe('Sistema de Mensagens', () => {
  let usuario1 = {}
  let usuario2 = {}

  before(() => {
    // Criar dois professores para testar mensagens
    cy.request('POST', 'http://localhost:8080/api/professores/cadastro', {
      nome: 'Professor Mensagens 1',
      email: `prof.msg1.${Date.now()}@edu.com`,
      senha: 'senha123',
      matricula: '33333333'
    }).then((response) => {
      usuario1 = response.body
    })

    cy.request('POST', 'http://localhost:8080/api/professores/cadastro', {
      nome: 'Professor Mensagens 2',
      email: `prof.msg2.${Date.now()}@edu.com`,
      senha: 'senha123',
      matricula: '44444444'
    }).then((response) => {
      usuario2 = response.body
    })
  })

  it('deve abrir o chat flutuante', () => {
    cy.visit('/login')
    cy.get('input[name="email"]').type(`prof.msg1.${Date.now()}@edu.com`)
    cy.get('input[name="senha"]').type('senha123')
    cy.get('button[type="submit"]').click()

    // Verificar se o botão do chat existe
    cy.get('.btn-chat-flutuante').should('be.visible').click()
    // Verificar se a janela do chat abriu
    cy.get('.janela-chat').should('be.visible')
  })

  it('deve enviar mensagem para outro usuário', () => {
    cy.visit('/login')
    cy.get('input[name="email"]').type(`prof.msg1.${Date.now()}@edu.com`)
    cy.get('input[name="senha"]').type('senha123')
    cy.get('button[type="submit"]').click()

    cy.wait(2000) // Aguardar carregamento

    // Abrir chat
    cy.get('.btn-chat-flutuante').click()

    // Assumindo que há conversas ou permitir iniciar nova conversa
    // Como pode não haver amigos, testar a interface
    cy.get('.janela-chat').should('contain', 'Mensagens')

    // Se há conversas, clicar na primeira
    cy.get('.conversa-item').first().click({ force: true }).then(() => {
      // Digitar mensagem
      cy.get('input[placeholder*="Digite uma mensagem"]').type('Olá, esta é uma mensagem de teste!')
      cy.get('.btn-enviar').click()
      // Verificar se a mensagem aparece
      cy.get('.mensagens-container').should('contain', 'Olá, esta é uma mensagem de teste!')
    })
  })

  it('deve enviar mensagem via API', () => {
    // Testar envio via API para garantir backend funciona
    cy.request('POST', `http://localhost:8080/api/mensagens`, {
      remetenteId: usuario1.id,
      destinatarioId: usuario2.id,
      conteudo: 'Mensagem de teste via API',
      tipo: 'TEXTO'
    }).then((response) => {
      expect(response.status).to.eq(200)
    })

    // Verificar se foi salva
    cy.request('GET', `http://localhost:8080/api/mensagens/conversas/${usuario1.id}`).then((response) => {
      expect(response.status).to.eq(200)
      expect(response.body).to.be.an('array')
    })
  })
})