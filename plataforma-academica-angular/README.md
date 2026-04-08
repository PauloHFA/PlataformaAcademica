# 🖥️ Plataforma Acadêmica - Frontend SPA

[![Angular](https://img.shields.io/badge/Angular-19-red)](https://angular.io/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.6-blue)](https://www.typescriptlang.org/)
[![Node.js](https://img.shields.io/badge/Node.js-22-green)](https://nodejs.org/)
[![Angular Material](https://img.shields.io/badge/Angular%20Material-19-pink)](https://material.angular.io/)

Interface de usuário moderna e responsiva para a Plataforma Acadêmica Integrada da UNINASSAU. Desenvolvida como Single Page Application (SPA) com Angular, oferecendo experiência fluida e intuitiva para docentes e discentes.

## 📋 Visão Geral

Este módulo frontend implementa uma aplicação web progressiva, utilizando as melhores práticas do ecossistema Angular. Conta com design system consistente, comunicação em tempo real via WebSocket, e arquitetura modular para escalabilidade.

### 🎯 Principais Características
- ✅ **SPA Moderna**: Navegação fluida sem recarregamento de página
- ✅ **Design Responsivo**: Interface adaptável para desktop, tablet e mobile
- ✅ **Acessibilidade**: Conformidade com WCAG 2.1 guidelines
- ✅ **Performance**: Lazy loading, tree-shaking e otimização de bundles
- ✅ **Real-time**: Notificações instantâneas via WebSocket
- ✅ **UX/UI**: Interface intuitiva com Angular Material

## 🏗️ Arquitetura Técnica

### Tecnologias Core
- **Angular 19** - Framework SPA com Ivy renderer
- **TypeScript 5.6** - Superset JavaScript com tipagem estática
- **RxJS 7** - Programação reativa para operações assíncronas
- **Angular Material 19** - Componentes UI/UX consistentes
- **ngx-socket-io** - Cliente WebSocket para Angular
- **Angular CLI** - Ferramentas de desenvolvimento e build

### Padrões e Boas Práticas
- **Component-Based Architecture** - Componentes reutilizáveis e modulares
- **Smart/Dumb Components** - Separação clara de responsabilidades
- **Reactive Forms** - Validação robusta e reatividade
- **Dependency Injection** - Injeção de dependências para testabilidade
- **Lazy Loading** - Carregamento sob demanda de módulos
- **Change Detection Strategy** - Otimização de performance

### Estrutura de Diretórios
```
src/
├── app/
│   ├── components/              # Componentes reutilizáveis
│   │   ├── dashboard/          # Dashboard acadêmico
│   │   ├── usuario-list/       # Lista de usuários
│   │   └── sala-detalhes/      # Detalhes da sala
│   ├── models/                 # Interfaces TypeScript
│   │   ├── dashboard-aluno.model.ts
│   │   ├── frequencia.model.ts
│   │   └── usuario.model.ts
│   ├── services/               # Serviços de integração
│   │   ├── dashboard.service.ts
│   │   ├── notification.service.ts
│   │   └── usuario.service.ts
│   ├── guards/                 # Guards de rota
│   ├── interceptors/           # Interceptors HTTP
│   ├── app.config.ts           # Configurações da aplicação
│   ├── app.routes.ts           # Definição de rotas
│   └── app.component.ts        # Componente raiz
├── assets/                     # Recursos estáticos
├── environments/               # Configurações por ambiente
└── styles.css                  # Estilos globais
```

## 🚀 Instalação e Configuração

### Pré-requisitos
- **Node.js 22+** ([Download Node.js](https://nodejs.org/))
- **NPM 10+** (incluído com Node.js)
- **Angular CLI 19** (`npm install -g @angular/cli`)

### 1. Clonagem e Setup
```bash
# Clonar repositório
git clone https://github.com/rhianpb/ProjetoFinal.git
cd ProjetoFinal-nassau/plataforma-academica-angular

# Instalar dependências
npm install
```

### 2. Configuração do Ambiente
O arquivo `src/environments/environment.ts` contém as configurações:

```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api',
  websocketUrl: 'http://localhost:8080/ws'
};
```

### 3. Execução em Desenvolvimento
```bash
# Servidor de desenvolvimento
npm start
# ou
ng serve --port 4200

# Com hot reload e proxy para API
ng serve --proxy-config proxy.conf.json
```

**Aplicação disponível em:** `http://localhost:4200`

### 4. Build para Produção
```bash
# Build otimizado
npm run build
# ou
ng build --configuration production

# Build com análise de bundles
ng build --stats-json
```

## 🎨 Design System

### Paleta de Cores Acadêmica
- **Primary**: `#1976d2` (Azul UNINASSAU)
- **Accent**: `#42a5f5` (Azul claro)
- **Warn**: `#f44336` (Vermelho para alertas)
- **Background**: `#fafafa` (Cinza muito claro)

### Componentes Principais
- **Dashboard**: Visualização de métricas e progresso acadêmico
- **Cards**: Exibição de informações com ícones Material Design
- **Forms**: Formulários reativos com validação
- **Navigation**: Menu lateral responsivo
- **Notifications**: Toast e snackbars para feedback

### Responsividade
- **Mobile-first**: Design otimizado para dispositivos móveis
- **Breakpoints**: xs (0-599px), sm (600-959px), md (960-1279px), lg (1280px+)
- **Flexbox/Grid**: Layouts flexíveis e adaptáveis

## 🧪 Testes e Qualidade

### Executar Testes
```bash
# Testes unitários
npm test
# ou
ng test

# Testes end-to-end (se configurado)
npm run e2e
# ou
ng e2e

# Cobertura de testes
npm run test:coverage
```

### Configuração de Testes
- **Jasmine/Karma** para testes unitários
- **Cypress** para testes E2E (planejado)
- **TestBed** para configuração de testes Angular
- **Mocks** para serviços externos

### Qualidade de Código
- **ESLint** para linting
- **Prettier** para formatação
- **Husky** para git hooks (planejado)
- **Compodoc** para documentação de componentes

## 🔧 Scripts Disponíveis

```json
{
  "start": "ng serve",
  "build": "ng build",
  "test": "ng test",
  "lint": "ng lint",
  "e2e": "ng e2e",
  "compodoc": "compodoc -p tsconfig.doc.json"
}
```

## 🌐 Funcionalidades Implementadas

### 📊 Dashboard Acadêmico
- **Métricas Visuais**: Gráficos de desempenho e frequência
- **Cards Informativos**: Atividades, submissões, notas médias
- **Filtros Temporais**: Análise por período
- **Notificações**: Alertas em tempo real

### 👥 Gestão de Usuários
- **Perfis Personalizados**: Alunos, professores, administradores
- **Busca e Filtros**: Localização rápida de usuários
- **Interações Sociais**: Sistema de conexões e mensagens

### 📚 Ambiente Acadêmico
- **Salas Virtuais**: Organização por turmas/disciplinas
- **Atividades Interativas**: Criação e acompanhamento
- **Submissões Online**: Upload e avaliação de trabalhos
- **Calendário Integrado**: Prazos e eventos acadêmicos

## 🚀 Deploy e Otimização

### Build de Produção
```bash
ng build --configuration production --aot --build-optimizer
```

### Otimizações Implementadas
- **Tree Shaking**: Remoção de código não utilizado
- **Lazy Loading**: Carregamento sob demanda
- **Service Worker**: PWA com cache offline (planejado)
- **Bundle Analyzer**: Análise de tamanho de bundles

### Deploy no Vercel/Netlify
```bash
# Build otimizado
ng build --prod

# Deploy estático
# Configurar CI/CD no provedor de hospedagem
```

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch (`git checkout -b feature/nova-funcionalidade`)
3. Commit suas mudanças (`git commit -m 'feat: adiciona nova funcionalidade'`)
4. Push para a branch (`git push origin feature/nova-funcionalidade`)
5. Abra um Pull Request

### Convenções de Commit
- `feat:` - Nova funcionalidade
- `fix:` - Correção de bug
- `docs:` - Documentação
- `style:` - Formatação/código
- `refactor:` - Refatoração
- `test:` - Testes

### Padrões de Código
- Seguir [Angular Style Guide](https://angular.io/guide/styleguide)
- Usar TypeScript strict mode
- Componentes com prefixo `app-`
- Interfaces com sufixo `Model` ou `Interface`

## 📚 Documentação Adicional

- [Guia de Tema Global](GUIA-TEMA-GLOBAL.md)
- [Paleta Acadêmica](PALETA-ACADEMICA.md)
- [Tema Implementado](TEMA-IMPLEMENTADO.md)
- [Remoção Tailwind](REMOCAO-TAILWIND.md)

## 📄 Licença

Este projeto é distribuído sob a licença MIT. Ver arquivo LICENSE para detalhes.

---

**Desenvolvido como parte do projeto de Arquitetura de Software - UNINASSAU**

### Build para Produção
```bash
# Gerar build otimizado
ng build --configuration production

# Os artefatos estarão em dist/plataforma-academica-angular/
```

## 🧪 Testes

### Testes Unitários
```bash
# Executar testes com Karma
ng test
```

### Testes End-to-End
```bash
# Executar testes E2E com Cypress
ng e2e
```

## 📁 Estrutura do Projeto

```
src/
├── app/
│   ├── components/          # Componentes reutilizáveis
│   │   ├── navbar/         # Barra de navegação
│   │   ├── sidebar/        # Menu lateral
│   │   ├── feed/           # Feed de postagens
│   │   └── ...
│   ├── services/           # Serviços de API
│   ├── models/             # Interfaces TypeScript
│   ├── guards/             # Guards de rota
│   ├── interceptors/       # Interceptors HTTP
│   └── app.routes.ts       # Configuração de rotas
├── assets/                 # Recursos estáticos
├── environments/           # Configurações de ambiente
└── styles.css              # Estilos globais
```

## 🔧 Scripts Disponíveis

- `ng serve` - Inicia servidor de desenvolvimento
- `ng build` - Compila a aplicação
- `ng test` - Executa testes unitários
- `ng lint` - Verifica qualidade do código
- `ng e2e` - Executa testes end-to-end

## 🌐 Integração com Backend

A aplicação se comunica com o backend Spring Boot através de APIs REST:

- **Base URL:** http://localhost:8080/api
- **Autenticação:** JWT tokens
- **Formato:** JSON

## 📱 Responsividade

A interface é totalmente responsiva, adaptando-se a:
- 📱 Dispositivos móveis
- 💻 Tablets
- 🖥️ Desktops

## 🎨 Tema e UI

- **Tema adaptativo:** Claro/escuro automático
- **Paleta profissional:** Tons roxos institucionais
- **Animações suaves:** Transições fluidas
- **Acessibilidade:** Conformidade com WCAG

## 👨‍💻 Equipe de Desenvolvimento

- **Rhian Pablo** - Desenvolvedor Frontend Principal
- **Paulo Henrique Ferreira de Albuquerque** - Desenvolvedor Frontend e UI/UX

## 📄 Licença

Este projeto é desenvolvido para fins acadêmicos como parte do curso de Arquitetura de Software da UNINASSAU.

```bash
ng e2e
```

Angular CLI does not come with an end-to-end testing framework by default. You can choose one that suits your needs.

## Additional Resources

For more information on using the Angular CLI, including detailed command references, visit the [Angular CLI Overview and Command Reference](https://angular.dev/tools/cli) page.
