# 📚 Resumo Executivo do Projeto - Plataforma Acadêmica Integrada

## 🎯 Visão Geral

A Plataforma Acadêmica Integrada é uma solução fullstack desenvolvida para otimizar a interação acadêmica entre professores e alunos da UNINASSAU. A plataforma oferece um ambiente digital colaborativo, acessível e intuitivo, facilitando a comunicação, o acompanhamento de atividades e a troca de conhecimento.

**Tecnologias Principais:**
- **Backend:** Spring Boot (Java 21)
- **Frontend:** Angular 19
- **Banco de Dados:** PostgreSQL

---

## ✅ Funcionalidades Implementadas

### 🔐 Autenticação e Gerenciamento de Usuários
- Sistema completo de login e cadastro de usuários
- Integração com login social (Google e Facebook) em modo demonstração
- Gerenciamento de perfis com upload de foto
- Sistema de amizades com solicitações, aceitação e recusa
- Listagem e busca de usuários

### 📚 Gestão de Salas de Aula
- Criação, edição e exclusão de salas virtuais
- Adição de membros via busca por nome ou e-mail
- Visualização detalhada das informações da sala
- Controle de permissões: apenas o criador pode excluir a sala
- Sistema de permissões baseado em papéis

### 📝 Sistema de Atividades Acadêmicas
- Criação de atividades com prazos e pontuação
- Listagem organizada de atividades por sala
- Visualização de detalhes das atividades
- Submissão de atividades com anexos (documentos)
- Sistema de correção com atribuição de notas e feedback

### 📰 Feed de Postagens e Interação
- Criação e publicação de postagens
- Sistema de curtidas para engajamento
- Exclusão de postagens próprias
- Filtros avançados: Todas, Amigos, Mais Curtidas
- Exibição de informações do autor e contadores

### 👥 Rede Social Acadêmica
- Envio e gerenciamento de solicitações de amizade
- Aceitação ou recusa de convites
- Remoção de conexões existentes
- Feed personalizado com postagens de amigos
- Busca inteligente de usuários para networking

### 🎨 Interface e Experiência do Usuário
- Tema adaptativo (claro/escuro) com alternância automática
- Paleta de cores profissional em tons roxos
- Design responsivo otimizado para dispositivos móveis e desktop
- Navegação intuitiva com sidebar lateral
- Avatares personalizados com iniciais dos usuários
- Animações fluidas e transições suaves

---

## 📁 Arquitetura do Sistema

```
ProjetoFinal-nassau/
├── plataforma-academica-spring/     # Backend - API REST
│   ├── src/main/java/
│   │   └── com.plataforma_academica.plataforma/
│   │       ├── controller/          # Controladores REST
│   │       ├── service/             # Lógica de negócio
│   │       ├── repository/          # Repositórios JPA
│   │       ├── model/               # Entidades do domínio
│   │       ├── dto/                 # Objetos de Transferência de Dados
│   │       └── mapper/              # Mapeadores DTO/Entity
│   └── pom.xml
│
├── plataforma-academica-angular/    # Frontend - SPA
│   ├── src/app/
│   │   ├── components/              # Componentes reutilizáveis
│   │   ├── services/                # Serviços de integração
│   │   ├── models/                  # Interfaces TypeScript
│   │   └── styles.css               # Tema global
│   └── package.json
│
└── Documentação/
    ├── README.md                    # Documentação principal
    ├── RESUMO_PROJETO.md            # Resumo executivo
    ├── CONFIGURACAO_LOGIN_SOCIAL.md # Configuração OAuth
    ├── INSTRUCOES_OAUTH_RAPIDO.md   # Guia rápido OAuth
    └── SOLUCAO_ERROS.md             # Troubleshooting
```

---

## 🔧 Tecnologias e Dependências

### Backend
- **Spring Boot** 3.5.6 - Framework principal
- **Spring Data JPA** - Persistência de dados
- **Spring Security** - Autenticação e autorização OAuth2
- **PostgreSQL Driver** - Conectividade com banco de dados

### Frontend
- **Angular** 19 - Framework SPA
- **TypeScript** - Linguagem de programação
- **RxJS** - Programação reativa
- **Angular Material** - Componentes UI
- **TailwindCSS** - Estilização utilitária

### Infraestrutura
- **PostgreSQL** - Sistema de gerenciamento de banco de dados
- **Maven** - Gerenciamento de dependências e build
- **NPM** - Gerenciamento de pacotes frontend

---

## 👨‍💻 Equipe de Desenvolvimento

- **Rafael Victor** (Matrícula: 03351641) - Desenvolvedor Backend
- **Rhian Pablo** (Matrícula: 03347356) - Desenvolvedor Frontend
- **Paulo Henrique Ferreira de Albuquerque** - Desenvolvedor Fullstack e Documentação

---

## 📈 Status do Projeto

**Versão Atual:** 1.0.0  
**Status:** Concluído  
**Última Atualização:** Março 2026

Este projeto foi desenvolvido como trabalho acadêmico para a disciplina de **Arquitetura de Software** na UNINASSAU, demonstrando a aplicação prática de conceitos modernos de desenvolvimento fullstack.
- Lombok
- Google API Client

### Frontend
- Angular 19
- TypeScript
- RxJS
- CSS Variables (tema)
- HttpClient

---

## 🚀 Como Executar

### Backend
```bash
cd plataforma-academica-spring
mvn clean install
mvn spring-boot:run
```
Acesse: http://localhost:8080

### Frontend
```bash
cd plataforma-academica-angular
npm install
ng serve
```
Acesse: http://localhost:4200

---

## 📋 Próximos Passos (Opcional)

### Para Ativar OAuth Real
1. Obter credenciais Google e Facebook
2. Configurar `application.properties`
3. Seguir guia: `INSTRUCOES_OAUTH_RAPIDO.md`

### Melhorias Futuras
- [ ] Sistema de notificações em tempo real
- [ ] Chat entre usuários
- [ ] Upload de arquivos (AWS S3)
- [ ] Sistema de badges/conquistas
- [ ] Relatórios de desempenho
- [ ] Calendário integrado
- [ ] Videoconferência
- [ ] App mobile (React Native)

---

## 🎨 Paleta de Cores

### Tema Claro
- Primary: `#5B21B6` (Roxo vibrante)
- Background: `#FFFFFF`
- Text: `#1F2937`

### Tema Escuro
- Primary: `#0A0412` (Roxo quase preto)
- Background: `#1F2937`
- Text: `#F9FAFB`

---

## 👥 Equipe

- **Rafael Victor** — Mat.: 03351641
- **Rhian Pablo** — Mat.: 03347356
- **Paulo Henrique Ferreira de Albuquerque** — Desenvolvedor Fullstack e Documentação

---

## 📄 Licença

Projeto acadêmico - UNINASSAU

---

**Última atualização:** 2024
**Versão:** 1.0.0
