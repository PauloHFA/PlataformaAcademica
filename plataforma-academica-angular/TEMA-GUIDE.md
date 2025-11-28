# 🎨 Guia do Sistema de Temas

## Cores do Tema

### Tema Claro (Padrão)
- **Cor Primária**: `#181823` (Azul escuro)
- **Cor Secundária**: `#FFFFFF` (Branco)
- **Fundo**: `#FFFFFF` (Branco)
- **Texto**: `#181823` (Azul escuro)

### Tema Escuro
- **Cor Primária**: `#FFFFFF` (Branco)
- **Cor Secundária**: `#181823` (Azul escuro)
- **Fundo**: `#181823` (Azul escuro)
- **Texto**: `#FFFFFF` (Branco)

## Variáveis CSS Disponíveis

```css
:root {
  --primary-color: #181823;
  --secondary-color: #FFFFFF;
  --bg-color: #FFFFFF;
  --text-color: #181823;
  --border-color: #e5e7eb;
  --hover-color: #f3f4f6;
  --shadow: rgba(24, 24, 35, 0.1);
}
```

## Como Usar

### 1. Botão de Toggle
O botão de toggle do tema está sempre visível no canto superior direito da tela. Clique para alternar entre os temas claro e escuro.

### 2. Em Componentes CSS
Use as variáveis CSS em seus estilos:

```css
.meu-componente {
  background-color: var(--bg-color);
  color: var(--text-color);
  border: 1px solid var(--border-color);
}

.botao {
  background-color: var(--primary-color);
  color: var(--secondary-color);
}

.botao:hover {
  background-color: var(--hover-color);
}
```

### 3. No TypeScript
Para acessar o estado do tema no código:

```typescript
import { ThemeService } from './services/theme.service';

constructor(private themeService: ThemeService) {}

// Verificar se está no modo escuro
get isDarkMode() {
  return this.themeService.isDarkMode;
}

// Observar mudanças de tema
ngOnInit() {
  this.themeService.isDarkMode$.subscribe(isDark => {
    console.log('Tema alterado:', isDark ? 'Escuro' : 'Claro');
  });
}
```

## Componentes Atualizados

Os seguintes componentes já foram atualizados para usar o sistema de temas:

- ✅ **App Component** - Container principal
- ✅ **Sidebar** - Menu lateral
- ✅ **Login** - Página de login
- ✅ **Home** - Página inicial
- ✅ **Feed** - Feed de posts
- ✅ **Perfil** - Página de perfil
- ✅ **Theme Toggle** - Botão de alternância

## Persistência

O tema escolhido pelo usuário é salvo no `localStorage` e será mantido entre as sessões.

## Acessibilidade

- O botão de toggle possui `aria-label` apropriado
- As cores mantêm contraste adequado em ambos os temas
- Transições suaves entre os temas

## Adicionando Novos Componentes

Para novos componentes, sempre use as variáveis CSS:

```css
/* ❌ Não faça assim */
.componente {
  background: white;
  color: black;
}

/* ✅ Faça assim */
.componente {
  background: var(--bg-color);
  color: var(--text-color);
}
```