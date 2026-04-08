# 🔐 Configuração de Autenticação Social - Google e Facebook

## 📋 Visão Geral

Este documento fornece instruções detalhadas para configurar a autenticação social via Google OAuth 2.0 e Facebook Login na Plataforma Acadêmica. A implementação permite login simplificado para usuários, melhorando a experiência de acesso à plataforma.

## 🎯 Objetivos

- Configurar provedores de identidade externos
- Implementar fluxo OAuth2 seguro
- Integrar autenticação social no backend e frontend
- Testar funcionalidade completa

---

## 🔵 Google OAuth 2.0

### 1. Criação do Projeto no Google Cloud Console

1. **Acesse o Console:** Navegue para [Google Cloud Console](https://console.cloud.google.com/)
2. **Novo Projeto:** Clique em "Criar Projeto"
3. **Detalhes:**
   - Nome: `Plataforma Academica`
   - Organização: (se aplicável)
4. **Confirmação:** Clique em "Criar" e aguarde a criação

### 2. Ativação da Google+ API

1. **Menu Lateral:** APIs e Serviços → Biblioteca
2. **Busca:** Procure por "Google+ API"
3. **Ativação:** Clique em "Ativar" para habilitar a API

### 3. Criação de Credenciais OAuth 2.0

1. **Navegação:** APIs e Serviços → Credenciais
2. **Nova Credencial:** Criar Credenciais → ID do cliente OAuth
3. **Configuração:**
   - Tipo: Aplicativo da Web
   - Nome: `Plataforma Academica Web`
4. **Origens Autorizadas:**
   ```
   http://localhost:4200
   http://localhost:8080
   ```
5. **URIs de Redirecionamento:**
   ```
   http://localhost:4200/auth/google/callback
   http://localhost:8080/api/auth/google/callback
   ```
6. **Finalização:** Clique em "Criar"
7. **Credenciais:** Salve com segurança:
   - **Client ID:** `SEU_GOOGLE_CLIENT_ID`
   - **Client Secret:** `SEU_GOOGLE_CLIENT_SECRET`

### 4. Configuração da Tela de Consentimento OAuth

1. **Acesso:** Tela de consentimento OAuth
2. **Tipo de Usuário:** Externo
3. **Informações da Aplicação:**
   - Nome do app: `Plataforma Academica`
   - E-mail de suporte: [seu-email@dominio.com]
   - Domínio da página inicial: `http://localhost:4200`
4. **Escopos:** Adicionar `email` e `profile`
5. **Salvar:** Confirme as alterações

---

## 🔵 Facebook Login

### 1. Criação da Aplicação no Facebook Developers

1. **Acesso:** [Facebook Developers](https://developers.facebook.com/)
2. **Novo App:** Meus Apps → Criar App
3. **Configuração:**
   - Tipo: Consumidor
   - Nome: `Plataforma Academica`
   - E-mail: [seu-email@dominio.com]
4. **Criação:** Clique em "Criar App"

### 2. Adição do Produto Facebook Login

1. **Painel do App:** Adicionar Produto
2. **Seleção:** Facebook Login → Configurar
3. **Plataforma:** Web
4. **URL do Site:** `http://localhost:4200`

### 3. Configuração do Facebook Login

1. **Navegação:** Facebook Login → Configurações
2. **URIs de Redirecionamento:**
   ```
   http://localhost:4200/auth/facebook/callback
   http://localhost:8080/api/auth/facebook/callback
   ```
3. **Salvar:** Aplicar as alterações

### 4. Obtenção das Credenciais

1. **Configurações:** Básico
2. **Credenciais:** Salve com segurança:
   - **App ID:** `SEU_FACEBOOK_APP_ID`
   - **App Secret:** `SEU_FACEBOOK_APP_SECRET`

### 5. Modo de Desenvolvimento

1. **Status:** Alterar de "Desenvolvimento" para "Ativo"
2. **Informações:** Preencher dados obrigatórios
3. **Termos:** Aceitar termos e condições
2. Tipo de usuário: "Externo"
3. Preencha:
   - Nome do app: `Plataforma Academica`
   - Email de suporte: seu email
   - Domínio da página inicial: `http://localhost:4200`
4. Escopos: adicione `email` e `profile`
5. Salve

---

## 🔵 Facebook Login

### Passo 1: Criar App no Facebook Developers

1. Acesse: https://developers.facebook.com/
2. Clique em "Meus Apps" > "Criar App"
3. Tipo: "Consumidor"
4. Nome do app: `Plataforma Academica`
5. Email de contato: seu email
6. Clique em "Criar App"

### Passo 2: Adicionar Produto Facebook Login

1. No painel do app, clique em "Adicionar Produto"
2. Selecione "Facebook Login" > "Configurar"
3. Plataforma: "Web"
4. URL do site: `http://localhost:4200`

### Passo 3: Configurar Facebook Login

1. Vá em "Facebook Login" > "Configurações"
2. **URIs de redirecionamento OAuth válidos:**
   ```
   http://localhost:4200/auth/facebook/callback
   http://localhost:8080/api/auth/facebook/callback
   ```
3. Salve as alterações

### Passo 4: Obter Credenciais

1. Vá em "Configurações" > "Básico"
2. **COPIE e SALVE:**
   - ID do App: `SEU_FACEBOOK_APP_ID`
   - Chave Secreta do App: `SEU_FACEBOOK_APP_SECRET`

### Passo 5: Modo de Desenvolvimento

1. No topo da página, mude de "Desenvolvimento" para "Ativo"
2. Preencha as informações necessárias
3. Aceite os termos

---

## 🔧 Backend (Spring Boot)

### Passo 1: Adicionar Dependências

Adicione no `pom.xml`:

```xml
<!-- OAuth2 Client -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-client</artifactId>
</dependency>

<!-- Spring Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

### Passo 2: Configurar application.properties

Adicione em `src/main/resources/application.properties`:

```properties
# Google OAuth2
spring.security.oauth2.client.registration.google.client-id=SEU_GOOGLE_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=SEU_GOOGLE_CLIENT_SECRET
spring.security.oauth2.client.registration.google.scope=profile,email
spring.security.oauth2.client.registration.google.redirect-uri={baseUrl}/api/auth/google/callback

# Facebook OAuth2
spring.security.oauth2.client.registration.facebook.client-id=SEU_FACEBOOK_APP_ID
spring.security.oauth2.client.registration.facebook.client-secret=SEU_FACEBOOK_APP_SECRET
spring.security.oauth2.client.registration.facebook.scope=email,public_profile
spring.security.oauth2.client.registration.facebook.redirect-uri={baseUrl}/api/auth/facebook/callback

# CORS
spring.web.cors.allowed-origins=http://localhost:4200
spring.web.cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
spring.web.cors.allowed-headers=*
```

### Passo 3: Criar Controller de Autenticação OAuth

Crie `OAuthController.java`:

```java
package com.plataforma_academica.plataforma.controller;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class OAuthController {

    @GetMapping("/google/callback")
    public ResponseEntity<?> googleCallback(OAuth2AuthenticationToken token) {
        // Processar dados do Google
        Map<String, Object> attributes = token.getPrincipal().getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        
        // Criar ou buscar usuário no banco
        // Gerar token JWT
        // Retornar para o frontend
        
        return ResponseEntity.ok(Map.of(
            "email", email,
            "name", name,
            "provider", "google"
        ));
    }

    @GetMapping("/facebook/callback")
    public ResponseEntity<?> facebookCallback(OAuth2AuthenticationToken token) {
        // Processar dados do Facebook
        Map<String, Object> attributes = token.getPrincipal().getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        
        return ResponseEntity.ok(Map.of(
            "email", email,
            "name", name,
            "provider", "facebook"
        ));
    }
}
```

---

## 🎨 Frontend (Angular)

### Passo 1: Instalar Biblioteca OAuth

```bash
cd plataforma-academica-angular
npm install @abacritt/angularx-social-login
```

### Passo 2: Configurar no app.config.ts

```typescript
import { SocialAuthServiceConfig, GoogleLoginProvider, FacebookLoginProvider } from '@abacritt/angularx-social-login';

export const appConfig: ApplicationConfig = {
  providers: [
    {
      provide: 'SocialAuthServiceConfig',
      useValue: {
        autoLogin: false,
        providers: [
          {
            id: GoogleLoginProvider.PROVIDER_ID,
            provider: new GoogleLoginProvider('SEU_GOOGLE_CLIENT_ID')
          },
          {
            id: FacebookLoginProvider.PROVIDER_ID,
            provider: new FacebookLoginProvider('SEU_FACEBOOK_APP_ID')
          }
        ]
      } as SocialAuthServiceConfig,
    }
  ]
};
```

### Passo 3: Atualizar login.component.ts

```typescript
import { SocialAuthService, GoogleLoginProvider, FacebookLoginProvider } from '@abacritt/angularx-social-login';

constructor(
  private socialAuthService: SocialAuthService,
  // ... outros
) {}

loginComGoogle(): void {
  this.socialAuthService.signIn(GoogleLoginProvider.PROVIDER_ID).then(user => {
    console.log('Google user:', user);
    // Enviar para backend
    this.usuarioService.loginSocial(user.email, user.name, 'google').subscribe({
      next: (response) => {
        localStorage.setItem('usuario', JSON.stringify(response));
        localStorage.setItem('usuarioId', response.id.toString());
        this.router.navigate(['/salas']);
      }
    });
  });
}

loginComFacebook(): void {
  this.socialAuthService.signIn(FacebookLoginProvider.PROVIDER_ID).then(user => {
    console.log('Facebook user:', user);
    // Enviar para backend
    this.usuarioService.loginSocial(user.email, user.name, 'facebook').subscribe({
      next: (response) => {
        localStorage.setItem('usuario', JSON.stringify(response));
        localStorage.setItem('usuarioId', response.id.toString());
        this.router.navigate(['/salas']);
      }
    });
  });
}
```

---

## ✅ Testes

### Testar Google Login

1. Inicie o backend: `mvn spring-boot:run`
2. Inicie o frontend: `ng serve`
3. Acesse: http://localhost:4200/login
4. Clique em "Continuar com Google"
5. Faça login com sua conta Google
6. Verifique se foi redirecionado para /salas

### Testar Facebook Login

1. Clique em "Continuar com Facebook"
2. Faça login com sua conta Facebook
3. Verifique se foi redirecionado para /salas

---

## 🔒 Segurança

### Variáveis de Ambiente

**NÃO COMMITE** as credenciais no Git! Use variáveis de ambiente:

```properties
# application.properties
spring.security.oauth2.client.registration.google.client-id=${GOOGLE_CLIENT_ID}
spring.security.oauth2.client.registration.google.client-secret=${GOOGLE_CLIENT_SECRET}
spring.security.oauth2.client.registration.facebook.client-id=${FACEBOOK_APP_ID}
spring.security.oauth2.client.registration.facebook.client-secret=${FACEBOOK_APP_SECRET}
```

Crie arquivo `.env` (adicione ao .gitignore):

```
GOOGLE_CLIENT_ID=seu_client_id_aqui
GOOGLE_CLIENT_SECRET=seu_secret_aqui
FACEBOOK_APP_ID=seu_app_id_aqui
FACEBOOK_APP_SECRET=seu_secret_aqui
```

---

## 📚 Recursos Adicionais

- [Google OAuth 2.0 Docs](https://developers.google.com/identity/protocols/oauth2)
- [Facebook Login Docs](https://developers.facebook.com/docs/facebook-login)
- [Spring Security OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
- [Angular Social Login](https://www.npmjs.com/package/@abacritt/angularx-social-login)

---

## ⚠️ Notas Importantes

1. **Produção**: Substitua `localhost` pelos domínios reais
2. **HTTPS**: Em produção, use HTTPS obrigatoriamente
3. **Privacidade**: Revise políticas de privacidade antes de publicar
4. **Testes**: Teste com múltiplas contas antes de lançar
5. **Logs**: Implemente logs para debug de autenticação

---

## 🐛 Troubleshooting

### Erro: "redirect_uri_mismatch"
- Verifique se as URIs de redirecionamento estão corretas no console
- Certifique-se de que não há espaços ou caracteres extras

### Erro: "invalid_client"
- Verifique se o Client ID e Secret estão corretos
- Confirme que o projeto está ativo no console

### Erro: CORS
- Adicione `http://localhost:4200` nas origens permitidas
- Verifique configuração CORS no Spring Boot

---

**Última atualização:** 2024
**Versão:** 1.0
