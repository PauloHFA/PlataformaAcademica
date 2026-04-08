# 🚀 Guia Rápido - Configuração de Autenticação Social

## ✅ Status da Implementação

A infraestrutura completa para autenticação social já está implementada:

- ✅ Dependências OAuth2 configuradas no backend
- ✅ Arquivo de propriedades preparado
- ✅ Componentes frontend funcionais (modo demonstração)
- ✅ Estrutura de segurança estabelecida

## 🔑 Configuração Necessária (5 minutos)

### 1. Credenciais Google OAuth2

#### Obtenção das Credenciais
1. **Acesso:** [Google Cloud Console](https://console.cloud.google.com/)
2. **Projeto:** Criar novo projeto ou selecionar existente
3. **Navegação:** APIs e Serviços → Credenciais
4. **Criação:** Criar Credenciais → ID do cliente OAuth
5. **Configuração:**
   - Tipo: Aplicativo da Web
   - Origens autorizadas: `http://localhost:4200`, `http://localhost:8080`
   - URIs de redirecionamento: `http://localhost:8080/login/oauth2/code/google`
6. **Credenciais:** Copiar Client ID e Client Secret

#### Ativação da API
- APIs e Serviços → Biblioteca → Google+ API → Ativar

### 2. Credenciais Facebook Login

#### Criação da Aplicação
1. **Acesso:** [Facebook Developers](https://developers.facebook.com/)
2. **Aplicação:** Criar App (tipo: Consumidor)
3. **Produto:** Adicionar Facebook Login
4. **Configuração:** Básico → Copiar App ID e App Secret

#### Configuração do Login
- Facebook Login → Configurações
- URIs de redirecionamento: `http://localhost:8080/login/oauth2/code/facebook`

### 3. Configuração do Backend

**Arquivo:** `plataforma-academica-spring/src/main/resources/application.properties`

Adicionar ao final do arquivo:

```properties
# Google OAuth2 Configuration
spring.security.oauth2.client.registration.google.client-id=SEU_GOOGLE_CLIENT_ID_AQUI
spring.security.oauth2.client.registration.google.client-secret=SEU_GOOGLE_CLIENT_SECRET_AQUI
spring.security.oauth2.client.registration.google.scope=profile,email
spring.security.oauth2.client.registration.google.redirect-uri={baseUrl}/login/oauth2/code/google

# Facebook OAuth2 Configuration
spring.security.oauth2.client.registration.facebook.client-id=SEU_FACEBOOK_APP_ID_AQUI
spring.security.oauth2.client.registration.facebook.client-secret=SEU_FACEBOOK_APP_SECRET_AQUI
spring.security.oauth2.client.registration.facebook.scope=email,public_profile
spring.security.oauth2.client.registration.facebook.redirect-uri={baseUrl}/login/oauth2/code/facebook

# CORS Configuration
spring.web.cors.allowed-origins=http://localhost:4200
spring.web.cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
spring.web.cors.allowed-headers=*
spring.web.cors.allow-credentials=true
```

### 4. Configuração do Frontend

**Arquivo:** `plataforma-academica-angular/src/environments/environment.ts`

```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api',
  googleClientId: 'SEU_GOOGLE_CLIENT_ID_AQUI',
  facebookAppId: 'SEU_FACEBOOK_APP_ID_AQUI'
};
```

### 5. Teste da Configuração

1. **Reiniciar Backend:** `./mvnw spring-boot:run`
2. **Reiniciar Frontend:** `ng serve`
3. **Testar Login:**
   - Acesse: http://localhost:4200
   - Clique em "Login com Google" ou "Login com Facebook"
   - Verificar redirecionamento e autenticação

## 🔍 Verificação de Funcionamento

### Logs do Backend
Verificar no console se aparecem mensagens como:
```
OAuth2 Login successful for user: [user_id]
```

### Rede (DevTools)
- Requests para `/oauth2/authorization/google` ou `/facebook`
- Redirecionamento para provedor
- Callback com código de autorização

## ⚠️ Notas Importantes

- **Segurança:** Nunca commite credenciais reais no repositório
- **Produção:** Configure domínios reais nos provedores OAuth
- **HTTPS:** Em produção, use HTTPS para redirecionamentos OAuth
- **Variáveis de Ambiente:** Considere usar variáveis de ambiente para credenciais

## 📞 Suporte

Para problemas específicos, consulte:
- `CONFIGURACAO_LOGIN_SOCIAL.md` - Guia detalhado
- `SOLUCAO_ERROS.md` - Resolução de problemas comuns
spring.security.oauth2.client.registration.facebook.client-id=SEU_FACEBOOK_APP_ID
spring.security.oauth2.client.registration.facebook.client-secret=SEU_FACEBOOK_APP_SECRET
spring.security.oauth2.client.registration.facebook.scope=email,public_profile
spring.security.oauth2.client.registration.facebook.redirect-uri={baseUrl}/login/oauth2/code/facebook
```

### Passo 4: Reiniciar Backend

```bash
cd plataforma-academica-spring
mvn clean install
mvn spring-boot:run
```

### Passo 5: Testar

1. Acesse: http://localhost:4200/login
2. Clique em "Continuar com Google" ou "Continuar com Facebook"
3. Faça login
4. Pronto! ✅

## 🎯 Modo Atual (Demo)

Enquanto você não configurar as credenciais, o sistema funciona em **modo demo**:
- Clica em Google → Loga como "Usuário Google (Demo)"
- Clica em Facebook → Loga como "Usuário Facebook (Demo)"

## ⚠️ Importante

- **NÃO COMMITE** as credenciais no Git
- Use variáveis de ambiente em produção
- Em produção, use HTTPS obrigatoriamente

## 📚 Guia Completo

Para mais detalhes, veja: `CONFIGURACAO_LOGIN_SOCIAL.md`

---

**Tempo estimado:** 5-10 minutos
**Dificuldade:** Fácil
