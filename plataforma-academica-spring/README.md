# 🔧 Plataforma Acadêmica - Backend API

[![Java](https://img.shields.io/badge/Java-21-orange)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.0-brightgreen)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)](https://www.postgresql.org/)
[![Maven](https://img.shields.io/badge/Maven-3.9+-red)](https://maven.apache.org/)

API REST desenvolvida em Spring Boot para a Plataforma Acadêmica Integrada da UNINASSAU. Fornece endpoints seguros e escaláveis para gestão acadêmica, comunicação em tempo real e analytics educacionais.

## 📋 Visão Geral

Este módulo backend implementa uma arquitetura RESTful robusta, utilizando as melhores práticas do ecossistema Spring. Suporta autenticação OAuth2, comunicação WebSocket para notificações em tempo real, e persistência de dados com PostgreSQL.

### 🎯 Principais Responsabilidades
- ✅ **Gestão de Usuários**: Perfis de alunos, professores e administradores
- ✅ **Sistema Acadêmico**: Atividades, submissões, avaliações e frequência
- ✅ **Comunicação**: Notificações em tempo real via WebSocket
- ✅ **Analytics**: Métricas de desempenho e relatórios acadêmicos
- ✅ **Segurança**: Autenticação JWT e controle de acesso baseado em roles

## 🏗️ Arquitetura Técnica

### Tecnologias Core
- **Java 21** - Linguagem de programação com recursos modernos
- **Spring Boot 3.4.0** - Framework para aplicações Java enterprise
- **Spring Data JPA/Hibernate** - ORM para persistência de dados
- **Spring Security** - Framework de segurança com OAuth2/JWT
- **Spring WebSocket/STOMP** - Comunicação bidirecional em tempo real
- **PostgreSQL** - Banco de dados relacional robusto

### Padrões Arquiteturais
- **RESTful API** - Design de APIs seguindo princípios REST
- **Domain-Driven Design (DDD)** - Separação clara de responsabilidades
- **Repository Pattern** - Abstração do acesso a dados
- **Service Layer** - Lógica de negócio isolada
- **DTO Pattern** - Transferência segura de dados

### Estrutura de Pacotes
```
src/main/java/com/plataforma_academica/plataforma/
├── config/                    # Configurações (WebSocket, Security, CORS)
├── controller/                # Endpoints REST (@RestController)
├── dto/                       # Data Transfer Objects
├── model/                     # Entidades JPA (@Entity)
├── repository/                # Interfaces de persistência (@Repository)
├── service/                   # Regras de negócio (@Service)
└── PlataformaAcademicaApplication.java
```

## 🚀 Instalação e Configuração

### Pré-requisitos
- **Java 21** ou superior ([Download OpenJDK](https://adoptium.net/))
- **Maven 3.9+** ou wrapper incluído (`./mvnw`)
- **PostgreSQL 15+** ([Download PostgreSQL](https://www.postgresql.org/download/))

### 1. Clonagem e Setup
```bash
# Clonar repositório
git clone https://github.com/rhianpb/ProjetoFinal.git
cd ProjetoFinal-nassau/plataforma-academica-spring

# Compilar projeto
./mvnw clean compile
```

### 2. Configuração do Banco de Dados
```bash
# Criar banco de dados
sudo -u postgres createdb plataforma_academica

# Executar migrações SQL (se aplicável)
# Scripts localizados na raiz do projeto
psql -U postgres -d plataforma_academica -f add_tipo_usuario.sql
```

### 3. Configuração da Aplicação
O arquivo `src/main/resources/application.properties` contém as configurações:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/plataforma_academica
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

# Server
server.port=8080

# WebSocket
spring.websocket.enabled=true
```

### 4. Execução
```bash
# Executar aplicação
./mvnw spring-boot:run

# Ou com Maven instalado
mvn spring-boot:run

# Build para produção
./mvnw clean package -DskipTests
java -jar target/plataforma-academica-0.0.1-SNAPSHOT.jar
```

**API disponível em:** `http://localhost:8080`

## 📚 Endpoints da API

### Autenticação
- `POST /api/auth/login` - Login de usuário
- `POST /api/auth/register` - Registro de novo usuário
- `POST /api/auth/refresh` - Refresh token JWT

### Gestão de Usuários
- `GET /api/usuarios` - Listar usuários
- `GET /api/usuarios/{id}` - Buscar usuário por ID
- `POST /api/usuarios` - Criar usuário
- `PUT /api/usuarios/{id}` - Atualizar usuário
- `DELETE /api/usuarios/{id}` - Remover usuário

### Sistema Acadêmico
- `GET /api/dashboard/aluno/{alunoId}/{salaId}` - Dashboard do aluno
- `GET /api/atividades` - Listar atividades
- `POST /api/atividades` - Criar atividade
- `GET /api/submissoes` - Listar submissões
- `POST /api/submissoes` - Submeter atividade

### Notificações
- `GET /api/notificacoes/usuario/{usuarioId}` - Notificações do usuário (WebSocket)
- `POST /api/notificacoes` - Enviar notificação

### Documentação da API
A documentação completa da API está disponível via Swagger UI em:
`http://localhost:8080/swagger-ui.html`

## 🧪 Testes

### Executar Testes Unitários
```bash
# Todos os testes
./mvnw test

# Testes específicos
./mvnw test -Dtest=UsuarioServiceTest

# Com relatório de cobertura
./mvnw test jacoco:report
```

### Tipos de Teste
- **Unitários**: Testes isolados de classes e métodos
- **Integração**: Testes de endpoints REST
- **Repository**: Testes de persistência de dados

## 🔒 Segurança

### Autenticação e Autorização
- **JWT (JSON Web Tokens)** para autenticação stateless
- **Spring Security** com configuração baseada em roles
- **OAuth2** preparado para integração com provedores externos
- **CORS** configurado para comunicação com frontend

### Boas Práticas de Segurança
- Validação de entrada com Bean Validation
- Proteção contra SQL Injection via JPA
- Controle de acesso baseado em perfis de usuário
- Logs de auditoria para operações sensíveis

## 📊 Monitoramento

### Health Checks
- `GET /actuator/health` - Status geral da aplicação
- `GET /actuator/info` - Informações da aplicação
- `GET /actuator/metrics` - Métricas de performance

### Logs
- Configuração de logging com SLF4J/Logback
- Níveis configuráveis (DEBUG, INFO, WARN, ERROR)
- Logs estruturados para análise

## 🚀 Deploy

### Docker (Planejado)
```dockerfile
FROM openjdk:21-jdk-slim
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
```

### Build para Produção
```bash
./mvnw clean package -DskipTests -Pproduction
```

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch (`git checkout -b feature/nova-funcionalidade`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova funcionalidade'`)
4. Push para a branch (`git push origin feature/nova-funcionalidade`)
5. Abra um Pull Request

### Padrões de Código
- Seguir convenções Java (Google Java Style Guide)
- Usar Lombok para reduzir boilerplate
- Documentar métodos públicos com JavaDoc
- Manter cobertura de testes > 80%

## 📝 Licença

Este projeto é distribuído sob a licença MIT. Ver arquivo LICENSE para detalhes.

---

**Desenvolvido como parte do projeto de Arquitetura de Software - UNINASSAU**

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