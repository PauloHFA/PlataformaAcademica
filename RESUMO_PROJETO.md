# 📚 Resumo do Projeto - Plataforma Acadêmica

## 🎯 Visão Geral

Plataforma digital fullstack para interação acadêmica entre professores e alunos da UNINASSAU.

**Stack:**
- Backend: Spring Boot (Java 21)
- Frontend: Angular 19
- Banco de Dados: PostgreSQL

---

## ✅ Funcionalidades Implementadas

### 🔐 Autenticação e Usuários
- ✅ Sistema de login e cadastro
- ✅ Login social (Google e Facebook) - modo demo
- ✅ Gerenciamento de perfil com foto
- ✅ Sistema de amizades (enviar, aceitar, recusar)
- ✅ Lista de usuários

### 📚 Salas de Aula
- ✅ Criar, editar e deletar salas
- ✅ Adicionar membros (busca por nome/email)
- ✅ Visualizar detalhes da sala
- ✅ Apenas criador pode deletar sala
- ✅ Sistema de permissões

### 📝 Atividades
- ✅ Criar atividades com prazo e pontos
- ✅ Listar atividades da sala
- ✅ Ver detalhes da atividade
- ✅ Submeter atividades (URL do documento)
- ✅ Ver submissões enviadas
- ✅ Sistema de correção (nota e feedback)

### 📰 Feed de Postagens
- ✅ Criar postagens
- ✅ Curtir postagens
- ✅ Deletar próprias postagens
- ✅ Filtros: Todas / Amigos / Mais Curtidas
- ✅ Exibir autor e contador de curtidas

### 👥 Sistema de Amizades
- ✅ Enviar solicitação de amizade
- ✅ Aceitar/recusar solicitações
- ✅ Remover amizades
- ✅ Ver postagens de amigos no feed
- ✅ Buscar usuários para adicionar

### 🎨 Interface e Tema
- ✅ Tema claro/escuro adaptativo
- ✅ Paleta de cores roxa profissional
- ✅ Design responsivo (mobile/desktop)
- ✅ Sidebar com navegação
- ✅ Avatares com iniciais
- ✅ Animações e transições suaves

---

## 📁 Estrutura do Projeto

```
ProjetoFinal-nassau/
├── plataforma-academica-spring/     # Backend
│   ├── src/main/java/
│   │   └── com.plataforma_academica.plataforma/
│   │       ├── controller/          # REST Controllers
│   │       ├── service/             # Lógica de negócio
│   │       ├── repository/          # JPA Repositories
│   │       ├── model/               # Entidades
│   │       ├── dto/                 # Data Transfer Objects
│   │       └── mapper/              # Conversores DTO/Entity
│   └── pom.xml
│
├── plataforma-academica-angular/    # Frontend
│   ├── src/app/
│   │   ├── components/              # Componentes Angular
│   │   ├── services/                # Serviços HTTP
│   │   ├── models/                  # Interfaces TypeScript
│   │   └── styles.css               # Tema global
│   └── package.json
│
└── Documentação/
    ├── README.md
    ├── CONFIGURACAO_LOGIN_SOCIAL.md
    ├── INSTRUCOES_OAUTH_RAPIDO.md
    └── SOLUCAO_ERROS.md
```

---

## 🔧 Tecnologias e Bibliotecas

### Backend
- Spring Boot 3.5.6
- Spring Data JPA
- Spring Security (OAuth2)
- PostgreSQL Driver
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

---

## 📄 Licença

Projeto acadêmico - UNINASSAU

---

**Última atualização:** 2024
**Versão:** 1.0.0
