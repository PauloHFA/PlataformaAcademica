# 🎨 Guia de Aplicação do Tema Global

## Como Aplicar o Tema em Qualquer Componente

### ✅ O que foi feito:
1. **Variáveis CSS globais** definidas em `styles.css`
2. **Classes utilitárias** disponíveis globalmente
3. **Tema automático** com suporte a dark mode

---

## 🔄 Migração de Componentes

### Substituições Básicas:

#### Cores Hardcoded → Variáveis CSS

```css
/* ❌ ANTES */
background: #667eea;
color: #333;
border: 1px solid #e5e7eb;

/* ✅ DEPOIS */
background: var(--primary-color);
color: var(--text-color);
border: 1px solid var(--border-color);
```

#### Gradientes → Cor Sólida

```css
/* ❌ ANTES */
background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);

/* ✅ DEPOIS */
background: var(--primary-color);
```

#### Backgrounds Fixos → Variáveis

```css
/* ❌ ANTES */
background: white;
background: #f5f7fa;

/* ✅ DEPOIS */
background: var(--card-bg);
background: var(--bg-color);
```

---

## 📋 Tabela de Substituições Rápidas

| Antes (Hardcoded) | Depois (Variável) | Uso |
|-------------------|-------------------|-----|
| `#667eea`, `#764ba2` | `var(--primary-color)` | Botões, links, destaques |
| `white`, `#ffffff` | `var(--card-bg)` | Cards, modais |
| `#f5f7fa`, `#f9fafb` | `var(--bg-color)` | Background principal |
| `#333`, `#1f2328` | `var(--text-color)` | Texto principal |
| `#666`, `#6b7280` | `var(--text-secondary)` | Texto secundário |
| `#999`, `#9ca3af` | `var(--text-muted)` | Texto esmaecido |
| `#e5e7eb`, `#d1d9e0` | `var(--border-color)` | Bordas |
| `#f3f4f6`, `#f6f8fa` | `var(--hover-color)` | Estados hover |

---

## 🎯 Classes Utilitárias Prontas

### Botões
```html
<button class="btn-primary">Ação Principal</button>
<button class="btn-secondary">Ação Secundária</button>
```

### Cards
```html
<div class="card">
  Conteúdo do card com tema automático
</div>
```

### Badges
```html
<span class="badge badge-success">Ativo</span>
<span class="badge badge-error">Erro</span>
<span class="badge badge-warning">Aviso</span>
<span class="badge badge-info">Info</span>
```

### Textos
```html
<h1 class="text-primary">Título</h1>
<p class="text-secondary">Descrição</p>
<span class="text-muted">Informação adicional</span>
```

### Sombras
```html
<div class="shadow-sm">Sombra pequena</div>
<div class="shadow-md">Sombra média</div>
<div class="shadow-lg">Sombra grande</div>
```

---

## 🌓 Suporte a Dark Mode

O tema escuro é aplicado automaticamente quando a classe `dark-theme` é adicionada ao `<body>`.

**Não é necessário fazer nada!** As variáveis CSS mudam automaticamente.

```typescript
// O ThemeService já gerencia isso
this.themeService.toggleTheme();
```

---

## 📝 Exemplo Completo de Migração

### Antes:
```css
.meu-componente {
  background: white;
  color: #333;
  border: 1px solid #e5e7eb;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.meu-botao {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.meu-botao:hover {
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}
```

### Depois:
```css
.meu-componente {
  background: var(--card-bg);
  color: var(--text-color);
  border: 1px solid var(--border-color);
  box-shadow: var(--shadow-md);
}

.meu-botao {
  background: var(--primary-color);
  color: white;
}

.meu-botao:hover {
  background: var(--primary-hover);
  box-shadow: var(--shadow-md);
}
```

---

## ⚡ Dicas Rápidas

1. **Use sempre variáveis CSS** em vez de cores hardcoded
2. **Prefira classes utilitárias** quando possível
3. **Teste em ambos os temas** (claro e escuro)
4. **Mantenha consistência** usando as mesmas variáveis
5. **Evite gradientes complexos** - use cores sólidas

---

## 🔍 Verificação Rápida

Para verificar se um componente está usando o tema corretamente:

```bash
# Procurar por cores hardcoded
grep -r "#[0-9a-fA-F]\{3,6\}" src/app/components/seu-componente/

# Deve retornar poucos ou nenhum resultado
```

---

## 📚 Variáveis Disponíveis

### Cores Principais
- `--primary-color`
- `--primary-hover`
- `--primary-light`
- `--primary-dark`

### Backgrounds
- `--bg-color`
- `--bg-secondary`
- `--card-bg`
- `--hover-color`

### Textos
- `--text-color`
- `--text-secondary`
- `--text-muted`

### Bordas
- `--border-color`
- `--border-light`
- `--divider-color`

### Acentos
- `--accent-blue`
- `--accent-green`
- `--accent-red`
- `--accent-yellow`

### Sombras
- `--shadow-sm`
- `--shadow-md`
- `--shadow-lg`
- `--shadow-xl`

---

## ✅ Checklist de Migração

- [ ] Substituir todas as cores hardcoded por variáveis
- [ ] Remover gradientes complexos
- [ ] Usar classes utilitárias quando possível
- [ ] Testar em tema claro
- [ ] Testar em tema escuro
- [ ] Verificar contraste de cores
- [ ] Validar responsividade
