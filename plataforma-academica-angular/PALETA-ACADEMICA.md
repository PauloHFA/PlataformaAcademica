# 🎨 Paleta de Cores Acadêmica Profissional

## Inspiração
Design inspirado em plataformas profissionais e acadêmicas:
- **LinkedIn** - Profissionalismo e networking
- **Google Classroom** - Clareza e organização educacional
- **GitHub** - Interface limpa e funcional

---

## 🌞 Tema Claro (Padrão)

### Cores Primárias
```css
--primary-color: #5B21B6      /* Roxo Escuro Vibrante */
--primary-hover: #6D28D9      /* Roxo Hover */
--primary-light: #7C3AED      /* Roxo Claro */
--primary-dark: #4C1D95       /* Roxo Mais Escuro */
```

### Backgrounds
```css
--bg-color: #F9FAFB           /* Fundo Principal (Cinza Muito Claro) */
--bg-secondary: #FFFFFF       /* Fundo Secundário (Branco) */
--card-bg: #FFFFFF            /* Cards */
```

### Textos
```css
--text-color: #111827         /* Texto Principal (Quase Preto) */
--text-secondary: #6B7280     /* Texto Secundário (Cinza Médio) */
--text-muted: #9CA3AF         /* Texto Esmaecido (Cinza Claro) */
```

### Bordas e Estados
```css
--border-color: #E5E7EB       /* Bordas */
--hover-color: #F3F4F6        /* Hover Neutro */
--hover-purple: #EDE9FE       /* Hover Roxo Suave */
```

### Acentos
```css
--accent-blue: #3B82F6        /* Azul (LinkedIn) */
--accent-green: #10B981       /* Verde (Sucesso) */
--accent-red: #EF4444         /* Vermelho (Erro) */
--accent-yellow: #F59E0B      /* Amarelo (Aviso) */
```

---

## 🌙 Tema Escuro

### Cores Primárias
```css
--primary-color: #A78BFA      /* Roxo Claro */
--primary-hover: #C4B5FD      /* Roxo Hover Claro */
--primary-light: #DDD6FE      /* Roxo Muito Claro */
```

### Backgrounds
```css
--bg-color: #111827           /* Fundo Principal (Escuro) */
--bg-secondary: #1F2937       /* Fundo Secundário */
--card-bg: #1F2937            /* Cards */
```

### Textos
```css
--text-color: #F9FAFB         /* Texto Principal (Claro) */
--text-secondary: #D1D5DB     /* Texto Secundário */
--text-muted: #9CA3AF         /* Texto Esmaecido */
```

### Bordas e Estados
```css
--border-color: #374151       /* Bordas */
--hover-color: #1F2937        /* Hover Neutro */
--hover-purple: #2D1B69       /* Hover Roxo Escuro */
```

---

## 📐 Uso Recomendado

### Botões Primários
```html
<button class="bg-[var(--primary-color)] hover:bg-[var(--primary-hover)]">
  Ação Principal
</button>
```

### Cards
```html
<div class="bg-[var(--card-bg)] border border-[var(--border-color)] shadow-md">
  Conteúdo do Card
</div>
```

### Textos
```html
<h1 class="text-[var(--text-color)]">Título</h1>
<p class="text-[var(--text-secondary)]">Descrição</p>
<span class="text-[var(--text-muted)]">Informação adicional</span>
```

### Estados de Sucesso/Erro
```html
<div class="text-[var(--accent-green)]">✓ Sucesso</div>
<div class="text-[var(--accent-red)]">✗ Erro</div>
<div class="text-[var(--accent-yellow)]">⚠ Aviso</div>
```

---

## 🎯 Princípios de Design

1. **Contraste Adequado**: Garantir legibilidade em ambos os temas
2. **Consistência**: Usar as variáveis CSS em vez de cores hardcoded
3. **Acessibilidade**: Seguir WCAG 2.1 para contraste de cores
4. **Profissionalismo**: Roxo transmite criatividade + seriedade acadêmica
5. **Clareza**: Hierarquia visual clara com tons de cinza

---

## 🔄 Transição de Temas

A transição entre temas é suave (0.3s) e afeta:
- Background colors
- Text colors
- Border colors
- Shadow intensities

```css
transition: background-color 0.3s ease, color 0.3s ease;
```

---

## 📱 Responsividade

As cores se adaptam automaticamente em:
- Desktop (1024px+)
- Tablet (768px - 1023px)
- Mobile (< 768px)

Sem necessidade de ajustes específicos por breakpoint.
