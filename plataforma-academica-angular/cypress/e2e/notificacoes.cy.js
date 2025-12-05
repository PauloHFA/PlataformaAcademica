describe('Sistema de Notificações', () => {
  let usuario1Id
  let usuario2Id

  before(() => {
    // Criar dois usuários para testar notificações
    cy.request('POST', 'http://localhost:8080/api/professores/cadastro', {
      nome: 'Professor Notif 1',
      email: `prof.notif1.${Date.now()}@edu.com`,
      senha: 'senha123',
      matricula: '77777777'
    }).then((response) => {
      usuario1Id = response.body.id
    })

    cy.request('POST', 'http://localhost:8080/api/professores/cadastro', {
      nome: 'Professor Notif 2',
      email: `prof.notif2.${Date.now()}@edu.com`,
      senha: 'senha123',
      matricula: '88888888'
    }).then((response) => {
      usuario2Id = response.body.id
    })
  })

  it('deve acessar página de notificações', () => {
    cy.visit('/login')
    cy.get('input[name="email"]').type(`prof.notif1.${Date.now()}@edu.com`)
    cy.get('input[name="senha"]').type('senha123')
    cy.get('button[type="submit"]').click()

    cy.visit('/notificacoes')
    cy.get('body').should('exist')
  })

  it('deve criar notificação via API', () => {
    cy.request('POST', 'http://localhost:8080/api/notificacoes', {
      usuarioId: usuario1Id,
      titulo: 'Nova Notificação',
      mensagem: 'Esta é uma notificação de teste criada pelo Cypress',
      tipo: 'INFORMATIVA',
      remetenteId: usuario2Id
    }).then((response) => {
      expect(response.status).to.eq(200)
      expect(response.body).to.have.property('id')
    })
  })

  it('deve listar notificações do usuário', () => {
    cy.request('GET', `http://localhost:8080/api/notificacoes/usuario/${usuario1Id}`).then((response) => {
      expect(response.status).to.eq(200)
      expect(response.body).to.be.an('array')
      expect(response.body.length).to.be.greaterThan(0)
    })
  })

  it('deve marcar notificação como lida', () => {
    // Primeiro obter uma notificação
    cy.request('GET', `http://localhost:8080/api/notificacoes/usuario/${usuario1Id}`).then((response) => {
      if (response.body.length > 0) {
        const notificacaoId = response.body[0].id
        cy.request('PUT', `http://localhost:8080/api/notificacoes/${notificacaoId}/lida`).then((updateResponse) => {
          expect(updateResponse.status).to.eq(200)
        })
      }
    })
  })
})