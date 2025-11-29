# 🚀 Instruções Rápidas - Ativar Login Social

## ✅ O que já está implementado

- ✅ Dependências OAuth2 adicionadas no backend
- ✅ Arquivo de configuração criado
- ✅ Botões funcionais no frontend (modo demo)
- ✅ Estrutura completa pronta

## 🔑 O que VOCÊ precisa fazer (5 minutos)

### Passo 1: Obter Google Client ID

1. Acesse: https://console.cloud.google.com/
2. Crie um projeto novo
3. Vá em "APIs e Serviços" > "Credenciais"
4. Clique em "Criar Credenciais" > "ID do cliente OAuth"
5. Tipo: "Aplicativo da Web"
6. **Origens autorizadas:** `http://localhost:4200` e `http://localhost:8080`
7. **URIs de redirecionamento:** `http://localhost:8080/login/oauth2/code/google`
8. Copie o **Client ID** e **Client Secret**

### Passo 2: Obter Facebook App ID

1. Acesse: https://developers.facebook.com/
2. Crie um app novo (tipo: Consumidor)
3. Adicione produto "Facebook Login"
4. Em "Configurações" > "Básico", copie:
   - **ID do App**
   - **Chave Secreta do App**
5. Em "Facebook Login" > "Configurações":
   - **URIs de redirecionamento:** `http://localhost:8080/login/oauth2/code/facebook`

### Passo 3: Configurar Backend

Abra o arquivo:
```
plataforma-academica-spring/src/main/resources/application.properties
```

Adicione no final:

```properties
# Google OAuth2
spring.security.oauth2.client.registration.google.client-id=SEU_GOOGLE_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=SEU_GOOGLE_CLIENT_SECRET
spring.security.oauth2.client.registration.google.scope=profile,email
spring.security.oauth2.client.registration.google.redirect-uri={baseUrl}/login/oauth2/code/google

# Facebook OAuth2
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
