# Plataforma Acadêmica — Frontend

Frontend Angular 19 da Plataforma Acadêmica: uma rede de estudo com salas por matéria, atividades, feed social, conexões e perfis acadêmicos.

## Estado atual

O frontend deixou de usar o template visual genérico anterior. A landing pública foi refeita como uma SPA editorial de scroll longo, com demonstrações visuais das funcionalidades do produto.

### Mudanças em relação ao template

- Navegação pública fixa com apenas `Fazer Login` e `Criar Conta`.
- Hero em tela cheia com CTA único e prévia realista da plataforma.
- Seções alternadas para Salas, Atividades, Feed, Amizades e Perfil.
- Cada seção usa uma demonstração específica da funcionalidade, em vez de cards repetidos com ícones.
- Identidade visual baseada em noite de estudo, papel quente, cobre, sálvia e azul poeira.
- Tipografia editorial `Fraunces` para títulos e `DM Sans` para interface.
- Remoção do visual rainbow/glassmorphism legado.
- Entradas por viewport com variações de movimento e suporte a `prefers-reduced-motion`.
- Interação local demonstrável no fluxo de pedido de amizade.

A especificação visual completa está em [docs/landing-page-design-guide.md](../docs/landing-page-design-guide.md).

## Stack

- Angular 19, TypeScript 5.7 e RxJS 7.
- Angular Material/CDK em componentes internos que precisam deles.
- CSS nativo com tokens em `src/styles-tokens.css`.
- SSR pelo Angular e comunicação WebSocket com `ngx-socket-io`.
- Cypress 13 para testes E2E.

## Desenvolvimento

Pré-requisitos: Node.js 22+ e npm 10+.

```bash
npm install
npm start
```

A aplicação fica disponível em `http://localhost:4200`.

## Build e testes

```bash
npm run build
npm test
npm run cypress:run
npm run cypress:open
```

Os scripts disponíveis são os definidos em `package.json`: `start`, `build`, `watch`, `test`, `serve:ssr:ProjetoFinal`, `cypress:open` e `cypress:run`.

## Estrutura relevante

```text
src/
├── app/
│   ├── components/       # telas, navegação e demos da landing
│   ├── services/         # integração com a API e estado
│   ├── models/           # contratos TypeScript
│   ├── guards/           # proteção de rotas
│   └── app.routes.ts      # rotas da aplicação
├── environments/         # configurações de ambiente
├── styles.css            # estilos globais e primitives da landing
└── styles-tokens.css     # tokens de cor, tipografia e espaçamento
```

## Documentação adicional

- [Guia de direção visual da landing](../docs/landing-page-design-guide.md)
- [Documentação dos testes Cypress](cypress/README.md)
- [Registro da remoção do Tailwind](REMOCAO-TAILWIND.md)

## Integração

A aplicação usa a API Spring Boot em `http://localhost:8080/api`, autenticação por token e WebSocket conforme os serviços utilizados por cada fluxo.
