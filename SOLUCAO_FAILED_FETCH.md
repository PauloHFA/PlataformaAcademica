# ✅ Guia de Resolução - Erro "Failed to fetch"

## 📋 Problema

O erro "Failed to fetch" indica falha de conectividade entre o frontend Angular e o backend Spring Boot, geralmente causado por configuração incorreta de portas ou serviços não iniciados.

## 🔍 Diagnóstico Rápido

### 1. Verificar Status do Backend

Execute no terminal:
```bash
# Verificar processos na porta 8080
lsof -i :8080

# Ou para Windows
netstat -ano | findstr :8080
```

**Resultado esperado:** Deve aparecer um processo Java relacionado ao Spring Boot.

### 2. Testar Conectividade Básica

```bash
# Testar endpoint de saúde
curl http://localhost:8080/actuator/health

# Resultado esperado: {"status":"UP"}
```

## 🛠️ Solução Passo a Passo

### Passo 1: Iniciar o Backend

```bash
# Navegar para o projeto Spring Boot
cd plataforma-academica-spring

# Limpar e compilar
./mvnw clean compile

# Iniciar aplicação
./mvnw spring-boot:run
```

**Verificação:** Deve aparecer no console:
```
Tomcat started on port(s): 8080 (http) with context path ''
Started PlataformaAcademicaApplication
```

### Passo 2: Verificar Porta do Backend

**Arquivo:** `plataforma-academica-spring/src/main/resources/application.properties`

```properties
# Verificar configuração
server.port=8080
```

**Se precisar alterar a porta:**
```properties
server.port=8081  # ou outra porta disponível
```

### Passo 3: Atualizar Configuração do Frontend

**Arquivo:** `plataforma-academica-angular/src/environments/environment.ts`

```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api'  // Atualizar porta se necessário
};
```

### Passo 4: Atualizar Serviços Angular

Para cada serviço que faz chamadas HTTP, atualizar a configuração:

**Exemplo - `src/app/services/usuario.service.ts`:**

```typescript
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment'; // ← Adicionar import

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  private baseUrl = environment.apiUrl + '/usuarios'; // ← Usar configuração

  constructor(private http: HttpClient) { }

  getUsuarios(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(this.baseUrl);
  }

  // ... outros métodos
}
```

**Serviços a atualizar:**
- `src/app/services/usuario.service.ts`
- `src/app/services/perfil.service.ts`
- `src/app/services/postagem.service.ts`
- `src/app/services/sala.service.ts`
- `src/app/services/amizade.service.ts`
- Todos os outros serviços que fazem chamadas API

### Passo 5: Configurar CORS (se necessário)

**Arquivo:** `plataforma-academica-spring/src/main/resources/application.properties`

```properties
# Configuração CORS
spring.web.cors.allowed-origins=http://localhost:4200
spring.web.cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
spring.web.cors.allowed-headers=*
spring.web.cors.allow-credentials=true
```

### Passo 6: Reiniciar Aplicações

```bash
# Terminal 1 - Backend
cd plataforma-academica-spring
./mvnw spring-boot:run

# Terminal 2 - Frontend
cd plataforma-academica-angular
ng serve
```

### Passo 7: Verificar Funcionamento

1. **Abrir navegador:** http://localhost:4200
2. **Abrir DevTools:** F12 → Console
3. **Verificar logs:** Não deve haver erros "Failed to fetch"
4. **Testar funcionalidade:** Tentar login/cadastro

## 🔧 Verificações Adicionais

### Firewall/Antivírus
- Desabilitar temporariamente
- Verificar se porta 8080 está liberada

### Conflito de Portas
```bash
# Matar processo conflitante
lsof -ti:8080 | xargs kill -9
```

### Logs Detalhados
**Backend - application.properties:**
```properties
logging.level.org.springframework.web=DEBUG
logging.level.com.plataforma_academica=DEBUG
```

**Frontend - Console do navegador**

## 📊 Checklist de Verificação

- [ ] Backend iniciado na porta correta
- [ ] Frontend configurado com URL correta
- [ ] Serviços atualizados para usar environment
- [ ] CORS configurado
- [ ] Firewall não bloqueando
- [ ] Sem conflito de portas
- [ ] Aplicações reiniciadas

## 📞 Quando Pedir Ajuda

Se o problema persistir, fornecer:
- Logs completos do backend
- Configuração atual dos arquivos
- Resultado dos comandos de diagnóstico
- Versão do Java/Node.js

**Documentos relacionados:**
- `ERRO_BACKEND.md` - Diagnóstico detalhado
- `SOLUCAO_ERROS.md` - Outros problemas comuns
- `README.md` - Setup completo

Última atualização: Março 2026

// PARA:
private baseUrl = `${environment.apiUrl}/usuarios`;
```

**Para `src/app/services/perfil.service.ts`:**

Faça o mesmo! Adicione o import e mude a baseUrl.

## **Passo 4: Verificar se o Backend tem CORS habilitado**

Seu controller Java deve ter a anotação:
```java
@CrossOrigin(origins = "http://localhost:4200")
```

OU você pode liberar CORS globalmente adicionando:
```java
@CrossOrigin(origins = "*")
```

## **Passo 5: Teste de Conectividade**

Abra o navegador e tente acessar diretamente:
```
http://localhost:8080/api/usuarios
```

Se der erro "Cannot GET", é porque o backend está rodando mas você acessou errado a rota.
Se não abrir, é porque o backend não está respondendo.

## **Se ainda não funcionar:**

1. Verifique o console do navegador (F12 → Network/Rede)
2. Procure pela requisição que falha (vai estar vermelha)
3. Clique nela e veja o erro
4. Me mande uma screenshot da aba "Response"

---

**Qual é a porta do seu backend?** Me avise para eu atualizar os arquivos corretamente!
