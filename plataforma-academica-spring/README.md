# 🔧 Plataforma Acadêmica - Backend Spring Boot

## 📋 Descrição

Este é o módulo backend da Plataforma Acadêmica Integrada, desenvolvido com **Spring Boot 3.5.6** e **Java 21**. Fornece APIs REST robustas e seguras para suportar as funcionalidades da plataforma acadêmica.

## 🚀 Tecnologias Utilizadas

- **Java** 21 (OpenJDK)
- **Spring Boot** 3.5.6
- **Spring Data JPA** para persistência
- **Spring Security** com OAuth2
- **PostgreSQL** como banco de dados
- **Maven** para gerenciamento de dependências
- **JUnit 5** e **Mockito** para testes

## 🏗️ Arquitetura

A aplicação segue os princípios de arquitetura limpa e Domain-Driven Design:

- **Controllers:** Endpoints REST e tratamento de requisições
- **Services:** Lógica de negócio e regras de aplicação
- **Repositories:** Acesso a dados via JPA
- **Models:** Entidades do domínio
- **DTOs:** Objetos de transferência de dados
- **Mappers:** Conversão entre entidades e DTOs

## 📦 Instalação e Execução

### Pré-requisitos
- Java 21 ou superior
- Maven 3.6+
- PostgreSQL 12+

### Configuração do Banco de Dados
```sql
-- Criar banco de dados
CREATE DATABASE ProjetoAcademy;

-- Configurar usuário (exemplo)
CREATE USER postgres WITH PASSWORD 'phfa1996';
GRANT ALL PRIVILEGES ON DATABASE ProjetoAcademy TO postgres;
```

### Instalação
```bash
# Clonar o repositório
git clone <repository-url>
cd plataforma-academica-spring

# Compilar o projeto
./mvnw clean compile
```

### Execução
```bash
# Executar aplicação
./mvnw spring-boot:run

# A API estará disponível em http://localhost:8080
```

### Build para Produção
```bash
# Gerar JAR executável
./mvnw clean package -DskipTests

# Executar JAR
java -jar target/plataforma-academica-0.0.1-SNAPSHOT.jar
```

## 🧪 Testes

### Testes Unitários
```bash
# Executar todos os testes
./mvnw test

# Executar testes com cobertura
./mvnw test jacoco:report
```

### Testes de Integração
```bash
# Executar testes de integração
./mvnw verify -Dspring.profiles.active=test
```

## 📁 Estrutura do Projeto

```
src/
├── main/
│   ├── java/
│   │   └── com/plataforma_academica/plataforma/
│   │       ├── controller/      # REST Controllers
│   │       ├── service/         # Business Logic
│   │       ├── repository/      # JPA Repositories
│   │       ├── model/           # Domain Entities
│   │       ├── dto/             # Data Transfer Objects
│   │       ├── mapper/          # Entity-DTO Mappers
│   │       ├── config/          # Configuration Classes
│   │       └── exception/       # Custom Exceptions
│   └── resources/
│       ├── application.properties     # Main config
│       ├── application-oauth.properties # OAuth config
│       └── data.sql                   # Initial data
└── test/
    └── java/
        └── com/plataforma_academica/plataforma/
            ├── controller/            # Controller Tests
            └── service/               # Service Tests
```

## 🔧 Configuração

### application.properties
```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/ProjetoAcademy
spring.datasource.username=postgres
spring.datasource.password=your_password

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Server
server.port=8080
```

### OAuth2 Configuration
Para configurar login social, consulte:
- `CONFIGURACAO_LOGIN_SOCIAL.md`
- `INSTRUCOES_OAUTH_RAPIDO.md`

## 🌐 APIs Principais

### Autenticação
- `POST /api/auth/login` - Login tradicional
- `GET /api/auth/user` - Informações do usuário

### Usuários
- `GET /api/usuarios` - Listar usuários
- `POST /api/usuarios` - Criar usuário
- `PUT /api/usuarios/{id}` - Atualizar usuário

### Salas de Aula
- `GET /api/salas` - Listar salas
- `POST /api/salas` - Criar sala
- `PUT /api/salas/{id}` - Atualizar sala

### Atividades
- `GET /api/salas/{id}/atividades` - Listar atividades
- `POST /api/salas/{id}/atividades` - Criar atividade

### Postagens
- `GET /api/postagens` - Feed de postagens
- `POST /api/postagens` - Criar postagem

## 🔒 Segurança

- **Autenticação:** JWT tokens
- **Autorização:** Baseada em papéis (Professor, Aluno, Admin)
- **OAuth2:** Integração com Google e Facebook
- **CORS:** Configurado para frontend local

## 📊 Monitoramento

- **Actuator:** Endpoints em `/actuator/health`, `/actuator/info`
- **Logs:** Configurados com SLF4J e Logback
- **Métricas:** Exposição de métricas via Actuator

## 👨‍💻 Equipe de Desenvolvimento

- **Rafael Victor** - Desenvolvedor Backend Principal
- **Paulo Henrique Ferreira de Albuquerque** - Desenvolvedor Backend e DevOps

## 📄 Licença

Este projeto é desenvolvido para fins acadêmicos como parte do curso de Arquitetura de Software da UNINASSAU.