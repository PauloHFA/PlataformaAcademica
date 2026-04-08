# 📚 Plataforma Acadêmica UNINASSAU

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-21-orange)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.0-brightgreen)](https://spring.io/projects/spring-boot)
[![Angular](https://img.shields.io/badge/Angular-19-red)](https://angular.io/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)](https://www.postgresql.org/)

Uma plataforma digital integrada para potencializar a interação acadêmica entre professores e alunos da UNINASSAU, oferecendo ferramentas modernas de comunicação, acompanhamento de desempenho e gestão educacional.

## 📋 Sobre o Projeto

Esta plataforma fullstack foi desenvolvida como projeto final da disciplina de **Arquitetura de Software** da UNINASSAU. O sistema visa modernizar o ambiente acadêmico através de uma interface intuitiva e funcionalidades colaborativas, facilitando o processo de ensino-aprendizagem.

### 🎯 Principais Objetivos
- ✅ Centralizar informações acadêmicas em um ambiente digital unificado
- ✅ Facilitar a comunicação síncrona e assíncrona entre docentes e discentes
- ✅ Proporcionar ferramentas de acompanhamento de desempenho individual
- ✅ Oferecer recursos colaborativos como fóruns e notificações em tempo real
- ✅ Garantir segurança e privacidade dos dados acadêmicos
- ✅ Promover uma experiência moderna alinhada às melhores práticas de UX/UI

## 🏗️ Arquitetura e Tecnologias

### Backend (Spring Boot)
- **Java 21** - Linguagem de programação principal
- **Spring Boot 3.4.0** - Framework para desenvolvimento de aplicações Java
- **Spring Data JPA** - Persistência de dados com Hibernate
- **Spring Security** - Autenticação e autorização
- **Spring WebSocket** - Comunicação em tempo real
- **PostgreSQL** - Banco de dados relacional
- **Maven** - Gerenciamento de dependências e build

### Frontend (Angular)
- **Angular 19** - Framework SPA (Single Page Application)
- **TypeScript** - Superset do JavaScript com tipagem estática
- **Angular Material** - Componentes UI/UX modernos
- **RxJS** - Programação reativa para operações assíncronas
- **ngx-socket-io** - Cliente WebSocket para Angular
- **Node.js 22 + NPM** - Ambiente de execução e gerenciamento de pacotes

### Infraestrutura
- **PostgreSQL 15** - Sistema de gerenciamento de banco de dados
- **WebSocket/STOMP** - Protocolo para comunicação bidirecional
- **RESTful API** - Arquitetura de comunicação entre frontend e backend
- **JWT** - Autenticação stateless (planejado)

## 📁 Estrutura do Projeto

```
plataforma-academica/
├── plataforma-academica-spring/          # Backend - API REST
│   ├── src/main/java/com/plataforma_academica/plataforma/
│   │   ├── config/                      # Configurações (WebSocket, Security)
│   │   ├── controller/                  # Endpoints REST
│   │   ├── dto/                         # Data Transfer Objects
│   │   ├── model/                       # Entidades JPA
│   │   ├── repository/                  # Interfaces de persistência
│   │   ├── service/                     # Regras de negócio
│   │   └── PlataformaAcademicaApplication.java
│   ├── src/main/resources/
│   │   ├── application.properties       # Configurações da aplicação
│   │   └── data.sql                     # Scripts de inicialização (opcional)
│   ├── src/test/                        # Testes unitários e integração
│   ├── pom.xml                          # Dependências Maven
│   └── README.md                        # Documentação específica do backend
│
├── plataforma-academica-angular/         # Frontend - SPA
│   ├── src/
│   │   ├── app/
│   │   │   ├── components/              # Componentes reutilizáveis
│   │   │   ├── models/                  # Interfaces TypeScript
│   │   │   ├── services/                # Serviços de integração
│   │   │   ├── guards/                  # Guards de rota
│   │   │   └── app.config.ts            # Configurações da aplicação
│   │   ├── assets/                      # Recursos estáticos
│   │   ├── environments/                # Configurações por ambiente
│   │   └── index.html                   # Template principal
│   ├── angular.json                     # Configurações do Angular CLI
│   ├── package.json                     # Dependências NPM
│   ├── tsconfig.json                    # Configurações TypeScript
│   └── README.md                        # Documentação específica do frontend
│
├── docs/                                # Documentação adicional
│   ├── CONFIGURACAO_LOGIN_SOCIAL.md
│   ├── ERRO_BACKEND.md
│   ├── INSTRUCOES_OAUTH_RAPIDO.md
│   ├── RESUMO_PROJETO.md
│   ├── SOLUCAO_ERROS.md
│   └── SOLUCAO_FAILED_FETCH.md
│
├── .gitignore                           # Arquivos ignorados pelo Git
├── README.md                            # Este arquivo
└── RESUMO_PROJETO.md                    # Resumo executivo do projeto
```

## 🚀 Instalação e Execução

### Pré-requisitos
- **Java 21** ou superior
- **Node.js 22** ou superior
- **PostgreSQL 15** ou superior
- **Maven 3.9+** (opcional, wrapper incluído)
- **Git** para controle de versão

### 1. Clonagem do Repositório
```bash
git clone https://github.com/rhianpb/ProjetoFinal.git
cd ProjetoFinal-nassau
```

### 2. Configuração do Banco de Dados
```bash
# Criar banco de dados PostgreSQL
sudo -u postgres createdb plataforma_academica

# Executar migrações (se aplicável)
# Scripts SQL estão localizados em plataforma-academica-spring/
```

### 3. Backend - Spring Boot
```bash
cd plataforma-academica-spring

# Compilar e executar
./mvnw clean install
./mvnw spring-boot:run

# Ou usando Maven instalado
mvn clean install
mvn spring-boot:run
```

**API disponível em:** `http://localhost:8080`

### 4. Frontend - Angular
```bash
cd plataforma-academica-angular

# Instalar dependências
npm install

# Executar em modo desenvolvimento
npm start
# ou
ng serve --port 4200
```

**Aplicação disponível em:** `http://localhost:4200`

## ✨ Funcionalidades Implementadas

### 🎓 Gestão Acadêmica
- **Dashboard Interativo**: Visualização de métricas de desempenho e progresso
- **Sistema de Atividades**: Criação, distribuição e acompanhamento de tarefas
- **Avaliações e Notas**: Registro e consulta de desempenhos acadêmicos
- **Calendário Acadêmico**: Controle de prazos e eventos importantes

### 💬 Comunicação e Colaboração
- **Notificações em Tempo Real**: Sistema WebSocket para alertas instantâneos
- **Fóruns de Discussão**: Espaços para troca de conhecimento
- **Mensagens Diretas**: Comunicação privada entre usuários
- **Grupos de Estudo**: Organização de salas virtuais

### 👥 Gestão de Usuários
- **Perfis Diferenciados**: Professores, alunos e administradores
- **Autenticação Segura**: Controle de acesso baseado em roles
- **Perfis Personalizáveis**: Informações acadêmicas e pessoais
- **Histórico de Interações**: Rastreamento de participação

### 📊 Analytics e Relatórios
- **Métricas de Desempenho**: Estatísticas individuais e coletivas
- **Relatórios de Frequência**: Controle de presença em atividades
- **Análises de Engajamento**: Indicadores de participação ativa
- **Exportação de Dados**: Relatórios em formatos diversos

## 🧪 Testes

### Backend (JUnit)
```bash
cd plataforma-academica-spring
./mvnw test
```

### Frontend (Jasmine/Karma)
```bash
cd plataforma-academica-angular
npm test
```

## 📚 Documentação Adicional

- [Resumo Executivo do Projeto](RESUMO_PROJETO.md)
- [Configuração de Login Social](CONFIGURACAO_LOGIN_SOCIAL.md)
- [Instruções OAuth Rápido](INSTRUCOES_OAUTH_RAPIDO.md)
- [Soluções de Problemas](SOLUCAO_ERROS.md)
- [Documentação do Backend](plataforma-academica-spring/README.md)
- [Documentação do Frontend](plataforma-academica-angular/README.md)

## 👨‍💻 Equipe de Desenvolvimento

| Nome | Matrícula | Papel |
|------|-----------|-------|
| **Rafael Victor** | 03351641 | Desenvolvedor |
| **Rhian Pablo** | 03347356 | Desenvolvedor |
| **Paulo Henrique Ferreira de Albuquerque** | - | Desenvolvedor e Documentador |

**Instituição:** Centro Universitário Maurício de Nassau (UNINASSAU)  
**Disciplina:** Arquitetura de Software  
**Período:** 2026.1

## 🤝 Como Contribuir

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

### Padrões de Código
- **Backend**: Seguir convenções Java e Spring Boot
- **Frontend**: Seguir Angular Style Guide
- **Commits**: Usar mensagens descritivas em português
- **Testes**: Manter cobertura mínima de 80%

## 📄 Licença

Este projeto é distribuído sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

---

**Status do Projeto:** 🚧 Em Desenvolvimento (MVP Funcional)

Para dúvidas ou sugestões, entre em contato com a equipe de desenvolvimento.
