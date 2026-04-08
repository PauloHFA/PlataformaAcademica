# 🖥️ Plataforma Acadêmica - Frontend Angular

## 📋 Descrição

Este é o módulo frontend da Plataforma Acadêmica Integrada, desenvolvido com **Angular 19**. A aplicação fornece uma interface de usuário moderna e responsiva para professores e alunos interagirem em um ambiente acadêmico digital.

## 🚀 Tecnologias Utilizadas

- **Angular** 19.2.17
- **TypeScript** 5.6
- **Node.js** 22.x
- **NPM** 10.x
- **RxJS** para programação reativa
- **Angular Material** e **TailwindCSS** para estilização
- **Axios/HttpClient** para comunicação com API

## 🏗️ Arquitetura

A aplicação segue a arquitetura de componentes do Angular, com:

- **Components:** Elementos reutilizáveis da UI
- **Services:** Lógica de integração com API backend
- **Models:** Interfaces TypeScript para tipagem
- **Guards:** Controle de rotas e autenticação
- **Interceptors:** Tratamento global de requisições HTTP

## 📦 Instalação e Execução

### Pré-requisitos
- Node.js 22.x ou superior
- NPM 10.x ou superior

### Instalação
```bash
# Clonar o repositório
git clone <repository-url>
cd plataforma-academica-angular

# Instalar dependências
npm install
```

### Execução em Desenvolvimento
```bash
# Iniciar servidor de desenvolvimento
ng serve

# A aplicação estará disponível em http://localhost:4200
```

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
