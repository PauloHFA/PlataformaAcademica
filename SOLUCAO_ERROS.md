# 🔧 Guia de Resolução de Problemas - Plataforma Acadêmica

## 📋 Visão Geral

Este documento contém soluções para problemas comuns encontrados durante o desenvolvimento e execução da Plataforma Acadêmica. Organize os problemas por categoria para facilitar a localização.

## 🎯 Problemas Comuns e Soluções

### 🔄 Erro: "A propriedade 'cadastroComGoogle' não existe"

**Sintomas:**
- Erro de compilação TypeScript
- Método não reconhecido em componentes
- Cache desatualizado do Angular

**Causas:**
- Cache do compilador Angular corrompido
- Arquivos temporários desatualizados

**Soluções:**

#### 1. Limpeza do Cache Angular
```bash
cd plataforma-academica-angular
rm -rf .angular/cache
ng serve
```

#### 2. Reinstalação de Dependências
```bash
cd plataforma-academica-angular
rm -rf node_modules package-lock.json
npm install
ng serve
```

#### 3. Limpeza Completa
```bash
cd plataforma-academica-angular
rm -rf .angular node_modules dist
npm install
ng serve
```

**Verificação:**
- Métodos implementados em:
  - `src/app/components/cadastro/cadastro.component.ts` (linhas 122, 130)
  - `src/app/components/login/login.component.ts`
- Servidor reiniciado após limpeza

---

### 🗄️ Erro: Conexão com Banco de Dados PostgreSQL

**Sintomas:**
- `Connection refused` ou `FATAL: database does not exist`
- Aplicação falha ao iniciar
- Erros de JPA/Hibernate

**Causas:**
- PostgreSQL não iniciado
- Banco de dados não criado
- Credenciais incorretas

**Soluções:**

#### 1. Verificar Status do PostgreSQL
```bash
# Linux
sudo systemctl status postgresql

# macOS
brew services list | grep postgresql

# Windows
# Verificar serviços do Windows
```

#### 2. Criar Banco de Dados
```sql
-- Conectar como superusuário
psql -U postgres

-- Criar banco e usuário
CREATE DATABASE ProjetoAcademy;
CREATE USER postgres WITH PASSWORD 'phfa1996';
GRANT ALL PRIVILEGES ON DATABASE ProjetoAcademy TO postgres;
```

#### 3. Verificar Configuração
Arquivo: `application.properties`
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ProjetoAcademy
spring.datasource.username=postgres
spring.datasource.password=phfa1996
```

---

### 🔐 Erro: OAuth2 - Credenciais Inválidas

**Sintomas:**
- `401 Unauthorized` no login social
- Redirecionamento falha
- Erro "invalid_client" ou "invalid_grant"

**Causas:**
- Credenciais incorretas nos provedores
- URIs de redirecionamento incorretas
- Aplicação em modo desenvolvimento

**Soluções:**

#### 1. Verificar Credenciais Google
- Console Cloud: APIs e Serviços → Credenciais
- Confirmar Client ID e Secret
- Verificar URIs autorizadas

#### 2. Verificar Credenciais Facebook
- Developers Facebook: Configurações → Básico
- Confirmar App ID e Secret
- Verificar URIs de redirecionamento

#### 3. Configuração do Backend
Arquivo: `application.properties`
```properties
# Google
spring.security.oauth2.client.registration.google.client-id=CORRETO_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=CORRETO_CLIENT_SECRET

# Facebook
spring.security.oauth2.client.registration.facebook.client-id=CORRETO_APP_ID
spring.security.oauth2.client.registration.facebook.client-secret=CORRETO_APP_SECRET
```

---

### 🌐 Erro: CORS - Bloqueio de Requisições

**Sintomas:**
- `CORS error` no navegador
- Requisições bloqueadas
- `Access-Control-Allow-Origin` header missing

**Causas:**
- Configuração CORS incorreta
- Origens não autorizadas

**Solução:**
Arquivo: `application.properties`
```properties
# CORS Configuration
spring.web.cors.allowed-origins=http://localhost:4200,http://localhost:8080
spring.web.cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
spring.web.cors.allowed-headers=*
spring.web.cors.allow-credentials=true
```

---

### 📦 Erro: Dependências Maven Não Resolvidas

**Sintomas:**
- Build falha com "Could not resolve dependencies"
- Erro de rede ou repositório

**Causas:**
- Problemas de conectividade
- Cache Maven corrompido
- Repositórios indisponíveis

**Soluções:**

#### 1. Limpar Cache Maven
```bash
cd plataforma-academica-spring
./mvnw clean
rm -rf ~/.m2/repository
./mvnw dependency:resolve
```

#### 2. Forçar Download
```bash
./mvnw clean install -U
```

#### 3. Verificar Conectividade
```bash
ping repo.maven.apache.org
```

---

### 🖥️ Erro: Porta Já em Uso

**Sintomas:**
- `Port 8080 already in use`
- Aplicação não inicia

**Causas:**
- Instância anterior rodando
- Outra aplicação usando a porta

**Solução:**
```bash
# Encontrar processo
lsof -i :8080

# Matar processo (substitua PID)
kill -9 PID

# Ou alterar porta
# application.properties
server.port=8081
```

---

### 🔍 Erro: 404 - Endpoint Não Encontrado

**Sintomas:**
- `404 Not Found` em requisições API
- Funcionalidades não funcionam

**Causas:**
- URL incorreta
- Controller não mapeado
- Método HTTP errado

**Verificação:**
- Base URL: `http://localhost:8080/api`
- Endpoints disponíveis em controllers
- Logs do Spring Boot

---

### 📊 Erro: Testes Falhando

**Sintomas:**
- Testes unitários falham
- `BUILD FAILURE` no Maven

**Causas:**
- Dependências de teste ausentes
- Configuração incorreta
- Código não testável

**Solução:**
```bash
# Executar testes específicos
./mvnw test -Dtest=NomeDaClasseTest

# Ver relatório
./mvnw surefire-report:report
```

---

## 🛠️ Ferramentas de Diagnóstico

### Logs da Aplicação
```bash
# Backend - Ver logs em tempo real
./mvnw spring-boot:run

# Frontend - Ver console do navegador
# DevTools → Console
```

### Verificação de Conectividade
```bash
# Testar API
curl http://localhost:8080/actuator/health

# Testar frontend
curl http://localhost:4200
```

### Monitoramento de Recursos
```bash
# Uso de memória
free -h

# Processos Java
ps aux | grep java

# Portas abertas
netstat -tlnp | grep :8080
```

---

## 📞 Suporte Adicional

Para problemas não cobertos:

1. **Verificar Logs Detalhados:**
   - Backend: `logging.level.com.plataforma_academica=DEBUG`
   - Frontend: Console do navegador

2. **Documentação Técnica:**
   - README.md - Visão geral
   - CONFIGURACAO_LOGIN_SOCIAL.md - OAuth2
   - RESUMO_PROJETO.md - Arquitetura

3. **Comunidade:**
   - Issues no repositório GitHub
   - Documentação da UNINASSAU

---

## 📈 Prevenção de Problemas

- **Versionamento:** Manter dependências atualizadas
- **Configuração:** Usar variáveis de ambiente para credenciais
- **Testes:** Executar suite completa antes de commits
- **Documentação:** Atualizar docs após mudanças
- **Backup:** Manter backup de configurações funcionais

Última atualização: Março 2026
