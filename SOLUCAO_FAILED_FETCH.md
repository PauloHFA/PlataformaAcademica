# ✅ Guia Rápido - Solucionar erro "Failed to fetch"

## **Passo 1: Verificar se o Backend está rodando**

Abra um terminal e execute:

```bash
# Verifique se algo está rodando na porta 8080
lsof -i :8080
```

Se retornar vazio, o backend NÃO está rodando.

## **Passo 2: Verificar em qual porta seu backend está**

Se você tiver um projeto Spring Boot, procure pelo arquivo:
- `application.properties` ou `application.yml`

Procure por:
```
server.port=8080
```

Anote a porta.

## **Passo 3: Atualizar a configuração do Angular**

### **3.1 Edite o arquivo de ambiente:**
`src/environments/environment.ts`

```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:SEU_PORTA_AQUI/api'  // ← Mude a porta aqui
};
```

### **3.2 Atualize os serviços para usar a configuração:**

**Para `src/app/services/usuario.service.ts`:**

No início do arquivo, adicione o import:
```typescript
import { environment } from '../../environments/environment';
```

Depois, mude a linha:
```typescript
// DE:
private baseUrl = 'http://localhost:8080/api/usuarios';

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
