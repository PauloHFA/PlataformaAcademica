# Testes E2E com Cypress

Este diretório contém os testes end-to-end (E2E) para a aplicação Angular usando Cypress.

## Pré-requisitos

1. Node.js e npm instalados
2. Aplicação Angular rodando em `http://localhost:4200`
3. Backend Spring Boot rodando em `http://localhost:8080`

## Instalação

```bash
cd plataforma-academica-angular
npm install
```

## Executando os Testes

### Modo Interativo (GUI)
```bash
npm run cypress:open
```

### Modo Headless (CI/CD)
```bash
npm run cypress:run
```

## Estrutura dos Testes

```
cypress/
├── e2e/
│   ├── login.cy.js          # Testes de login
│   ├── cadastro.cy.js       # Testes de cadastro
│   ├── salas.cy.js          # Testes de salas de aula
│   └── perfil-feed.cy.js    # Testes de perfil e feed
├── support/
│   ├── commands.js          # Comandos customizados
│   └── e2e.js              # Configuração global
└── config.js                # Configuração do Cypress
```

## Comandos Customizados

- `cy.loginProfessor(email, password)` - Faz login de professor
- `cy.cadastrarProfessor(professor)` - Cadastra novo professor
- `cy.limparDadosTeste()` - Limpa dados de teste

## Cenários de Teste

### Login
- ✅ Exibição correta da página
- ✅ Login com credenciais válidas
- ✅ Tratamento de credenciais inválidas
- ✅ Navegação para cadastro

### Cadastro
- ✅ Exibição do formulário
- ✅ Cadastro com dados válidos
- ✅ Validação de email duplicado
- ✅ Navegação para login

### Salas de Aula
- ✅ Criação de sala
- ✅ Listagem de salas
- ✅ Adição de membros
- ✅ Criação de atividades
- ✅ Visualização de detalhes

### Perfil e Feed
- ✅ Acesso ao perfil
- ✅ Navegação no feed
- ✅ Edição de perfil
- ✅ Listagem de usuários
- ✅ Página de amigos

## Configuração

A configuração está no arquivo `cypress.config.js`:
- Base URL: `http://localhost:4200`
- Viewport: 1280x720
- Timeouts configurados para API calls

## Executando em CI/CD

Para integração contínua, use:

```yaml
# GitHub Actions example
- name: Run E2E Tests
  run: |
    npm run cypress:run
```

## Troubleshooting

1. **Aplicação não está rodando**: Certifique-se que o Angular e Spring estão executando
2. **Timeouts**: Ajuste os timeouts no `cypress.config.js`
3. **Elementos não encontrados**: Verifique os seletores nos testes
4. **API calls falhando**: Verifique se o backend está respondendo corretamente

## Boas Práticas

- Use seletores específicos (data-cy attributes quando possível)
- Limpe dados de teste entre execuções
- Use fixtures para dados de teste grandes
- Mantenha testes independentes
- Use page objects para componentes complexos