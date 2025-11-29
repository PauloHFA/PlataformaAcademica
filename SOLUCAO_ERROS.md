# Soluções para Erros Comuns

## Erro: "A propriedade 'cadastroComGoogle' não existe"

Este erro ocorre devido ao cache do Angular. Para resolver:

### Solução 1: Limpar cache do Angular
```bash
cd plataforma-academica-angular
rm -rf .angular/cache
ng serve
```

### Solução 2: Reinstalar node_modules
```bash
cd plataforma-academica-angular
rm -rf node_modules
npm install
ng serve
```

### Solução 3: Limpar tudo
```bash
cd plataforma-academica-angular
rm -rf .angular node_modules
npm install
ng serve
```

## Verificação

Os métodos estão implementados em:
- `src/app/components/cadastro/cadastro.component.ts` (linhas 122 e 130)
- `src/app/components/login/login.component.ts`

Se o erro persistir após limpar o cache, reinicie o servidor de desenvolvimento.
