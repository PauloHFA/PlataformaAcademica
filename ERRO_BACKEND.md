## 🔍 Diagnóstico do Erro "Failed to fetch"

### **O que está acontecendo:**
O Angular está tentando se conectar ao backend em `http://localhost:8080/api/usuarios` mas recebendo erro "Failed to fetch".

### **Possíveis causas:**

1. **Backend não está rodando**
   - Verifique se o Spring Boot está iniciado na porta 8080
   - Command: `java -jar seu-app.jar` ou `mvn spring-boot:run`

2. **Porta errada**
   - Se o backend está em outra porta, você precisa atualizar a configuração

3. **CORS bloqueado**
   - O backend precisa estar com CORS habilitado para `http://localhost:4200` (ou `33941`)

### **Solução rápida:**

#### **Opção A: Verificar e iniciar o backend**
```bash
# Verifique se o backend Spring Boot está rodando
lsof -i :8080

# Se não estiver, inicie o backend
cd seu-projeto-java
mvn clean spring-boot:run
```

#### **Opção B: Mudar a porta do backend na configuração**

Se o backend está em outra porta (ex: 9090), atualize o arquivo:
`src/environments/environment.ts`

```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:9090/api'  // Mude a porta aqui
};
```

Depois atualize os serviços:
- `src/app/services/usuario.service.ts` 
- `src/app/services/perfil.service.ts`

Mudar de:
```typescript
private baseUrl = 'http://localhost:8080/api/usuarios';
```

Para:
```typescript
import { environment } from '../../environments/environment';
private baseUrl = `${environment.apiUrl}/usuarios`;
```

#### **Opção C: Mock do backend (para testes)**

Se você ainda não tem o backend pronto, podemos criar um mock/stub dos dados para testar o frontend.

### **Como verificar se o backend está respondendo:**

```bash
# No terminal, teste a conexão
curl http://localhost:8080/api/usuarios

# Ou use o navegador
# Vá para: http://localhost:8080/api/usuarios
```

**Por favor, verifique qual porta seu backend está rodando e me avise!**
