# Testes E2E com Cypress

A suíte E2E valida os fluxos públicos e autenticados da Plataforma Acadêmica.

## Pré-requisitos

- Frontend Angular em `http://localhost:4200`.
- Backend Spring Boot em `http://localhost:8090` para specs que usam API.
- Banco de teste com dados descartáveis para specs de integração.

## Comandos

```bash
# abrir o Cypress
npm run cypress:open

# executar toda a suíte headless
npm run cypress:run

# executar apenas o smoke determinístico
npm run test:e2e:smoke
```

## Organização

```text
cypress/
├── e2e/
│   ├── landing-auth-smoke.cy.js  # landing, validação e ações OAuth
│   ├── login.cy.js               # login local
│   ├── cadastro.cy.js            # cadastro
│   ├── salas.cy.js               # salas e atividades
│   ├── perfil-feed.cy.js         # perfil, feed e usuários
│   └── ...                       # módulos adicionais
├── support/
│   ├── commands.js
│   └── e2e.js
└── README.md
```

## Padrão dos testes

- Preferir seletores `data-cy` para elementos de negócio.
- Não usar `cy.wait` com milissegundos; aguardar URL, request ou estado visível.
- Limpar localStorage e dados de teste por spec.
- Não depender da ordem dos arquivos.
- Usar `cy.intercept` para smoke tests determinísticos.
- OAuth real deve ser exercitado em staging com credenciais de teste; o smoke local verifica que os botões estão ativos.
- Dados, tokens e credenciais nunca entram em fixtures ou screenshots.

## Suites existentes

Os arquivos em `cypress/e2e` cobrem login, cadastro, salas, atividades, submissões, feed, postagens, comunidades, artigos, notificações e mensagens. Os cenários que usam API precisam de backend e banco compatíveis com o seed de teste.
