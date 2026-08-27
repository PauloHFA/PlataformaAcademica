# 🎓 Guia de Arquitetura e Padrões de Frontend — Plataforma Acadêmica

> **Nível:** Sênior / Especialista (Staff)  
> **Escopo:** Arquitetura do Frontend Angular (Padrão SPA / Standalone Components), Design System (Paleta Acadêmica & Social), Gerenciamento de Estado, Comunicação com API (HTTP Interceptors / Sagas) e Padrões de Hooks/Reatividade (Signals e RxJS).

---

## 1. Visão Geral da Arquitetura

O frontend da **Plataforma Acadêmica** é construído em **Angular** (arquitetura moderna baseada em **Standalone Components** e **Signals**, sem módulos legados `NgModule`), combinando conceitos de **Redes Sociais Acadêmicas** (feed, postagens, curtidas, comentários, conexões/amizades, chat em tempo real) com **Ambiente Virtual de Aprendizagem (AVA)** (salas de aula, envio de atividades, chamadas de frequência, submissões e marketplace de conteúdos).

### 🏛️ Estrutura de Camadas (Clean Architecture no Frontend)
```
src/app/
├── core/                  # Singleton services, interceptors, guards, global state
│   ├── interceptors/      # JWT, Auth, Error Handling, Loader Interceptors
│   ├── guards/            # AuthGuard, RoleGuard (Aluno/Professor/Admin)
│   └── services/          # WebsocketService, AuthService, ThemeService
├── shared/                # Componentes reutilizáveis, pipes, diretivas e design system
│   ├── components/        # Botões, modais, cards de feed, avatares
│   └── directives/        # Máscaras, tooltips, lazy load
├── features/              # Módulos de domínio (Lazy Loaded Routes)
│   ├── feed/              # Mural social, postagens, curtidas, comentários
│   ├── academic/          # Salas de aula, atividades, frequência, submissões
│   ├── social/            # Amizades, chat em tempo real, comunidades
│   ├── profile/           # Perfil do usuário, bio, portfólio acadêmico
│   └── marketplace/       # Compra e venda de conteúdos educacionais
└── app.routes.ts          # Roteamento principal com Lazy Loading
```

---

## 2. Design System & Paleta de Cores (Identidade Visual)

A identidade visual funde a **seriedade e autoridade acadêmica** (tons de azul institucional e cinza neutro) com a **dinâmica e engajamento de uma rede social moderna** (tons de destaque vibrantes, modo escuro/claro nativo).

### 🎨 Paleta de Cores Oficial
| Token / Nome | Hexadecimal | Uso Principal |
| :--- | :--- | :--- |
| `--color-primary` | `#0f2b46` | Azul Acadêmico Profundo (Navbar, Headers, Títulos Principais) |
| `--color-primary-light` | `#1d4ed8` | Azul Interativo (Botões de Ação, Links, Estados Hover) |
| `--color-accent` | `#38bdf8` | Azul Celeste / Ciano (Badges, Destaques, Notificações) |
| `--color-social-bg` | `#f8fafc` | Fundo geral do feed e páginas sociais (Modo Claro) |
| `--color-card-bg` | `#ffffff` | Fundo de cartões de postagens, salas e atividades |
| `--color-text-main` | `#1e293b` | Texto principal (Alta legibilidade) |
| `--color-text-muted` | `#64748b` | Texto secundário (Datas, subtítulos, metadados) |
| `--color-success` | `#10b981` | Aprovação de atividades, presença confirmada, conexões aceitas |
| `--color-warning` | `#f59e0b` | Prazos de entrega próximos, alertas de pendência |
| `--color-danger` | `#ef4444` | Exclusões, prazos vencidos, erros de validação |

### 📐 Tipografia e Layout
* **Fonte Primária:** Inter / Roboto (-apple-system, sans-serif) para máxima clareza em leitura longa e dashboards.
* **Border Radius Padrão:** `12px` (Cards e Modais) para um visual moderno e amigável (Arredondamento suave).
* **Sombras (Elevation):** Sombras sutis (`box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1)`) para profundidade em cards de feed e salas de aula.

---

## 3. Comunicação com a API (Backend Spring Boot)

A comunicação é centralizada através de serviços tipados com **RxJS Observables** e **Angular Signals**, utilizando **HTTP Interceptors** para injeção automática de credenciais e tratamento de erros.

### 🔌 Padrão de Interceptors
1. **`AuthInterceptor`**: Injeta o token JWT no cabeçalho `Authorization: Bearer <token>` em todas as requisições protegidas.
2. **`ErrorInterceptor`**: Captura globalmente erros HTTP (401 Unauthorized, 403 Forbidden, 404 Not Found, 500 Internal Server Error) e dispara toasts/notificações amigáveis ao usuário.
3. **`LoaderInterceptor`**: Gerencia o estado global de carregamento (spinners/skeletons) durante chamadas assíncronas.

### 🌐 Exemplo de Serviço de Comunicação (Client Service)
```typescript
@Injectable({ providedIn: 'root' })
export class PostagemService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiBaseUrl}/api/postagens`;

  // Utilizando Signals para reatividade moderna
  private _postagens = signal<PostagemResponseDTO[]>([]);
  public postagens = this._postagens.asReadonly();

  carregarFeed(): void {
    this.http.get<PostagemResponseDTO[]>(this.apiUrl).subscribe({
      next: (data) => this._postagens.set(data),
      error: (err) => console.error('Erro ao carregar feed', err)
    });
  }
}
```

---

## 4. Gerenciamento de Estado e Reatividade (Hooks & Signals)

Diferente de abordagens legadas baseadas exclusivamente em RxJS complexo para estado local, o frontend adota **Angular Signals** combinados com o padrão **Repository/Service State**.

### 🎣 O que substitui os "Hooks" do React no Angular?
No Angular moderno, a reatividade é gerida por:
* **`signal()`**: Estado reativo local ou global mutável de forma controlada (`.set()`, `.update()`).
* **`computed()`**: Valores derivados automáticos (ex: contagem total de curtidas ou filtro de postagens por termo de busca).
* **`effect()`**: Efeitos colaterais reativos (ex: salvar preferências no `localStorage` sempre que o tema mudar).

### 🔄 Exemplo Prático de Componente Reativo (Estilo Sênior)
```typescript
@Component({
  selector: 'app-feed-postagem',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="post-card">
      <h3>{{ postagem().titulo }}</h3>
      <p>{{ postagem().conteudo }}</p>
      <button (click)="curtir()">Curtir ({{ curtidasCount() }})</button>
    </div>
  `
})
export class FeedPostagemComponent {
  // Input reativo
  postagem = input.required<PostagemResponseDTO>();

  // Estado local com Signal
  private localCurtidas = signal(0);

  // Valor derivado (Computed)
  curtidasCount = computed(() => this.postagem().curtidas + this.localCurtidas());

  curtir() {
    this.localCurtidas.update(c => c + 1);
    // Chamada ao backend...
  }
}
```

---

## 5. Fluxos de Interação (Rede Social + Acadêmico)

### 📱 1. Fluxo de Rede Social (Feed / Postagens / Comentários)
* **Timeline Infinita / Paginação:** Carregamento otimizado de postagens com suporte a anexos de imagens e links.
* **Curtidas Instantâneas (Optimistic Updates):** O contador de curtidas atualiza imediatamente na tela do usuário antes mesmo da resposta do servidor (`CurtidaRepository`), revertendo em caso de falha na rede.
* **Comentários Aninhados:** Suporte a comentários em postagens, atividades e salas de aula gerenciados pelo `TipoDestinoComentario`.

### 📚 2. Fluxo Acadêmico (Salas de Aula / Atividades / Frequência)
* **Painel do Aluno / Professor:** Visualização de prazos de entrega (`dataEntrega`), pontuações e submissões (`SubmissaoAtividade`).
* **Chamada de Frequência:** Professores registram presença em tempo real por sala de aula.
* **Chat em Tempo Real:** Comunicação via WebSocket (`/ws` com STOMP) para mensagens diretas entre alunos e professores.

---

## 6. Próximos Passos Recomendados para o Frontend

1. **Configuração do Projeto:** Inicializar o projeto Angular na pasta `plataforma-academica-angular/` com rotas e layout base.
2. **Implementação do Design System:** Criar variáveis globais em `styles.css` baseadas na paleta de cores definida no item 2.
3. **Criação dos Guards e Interceptors:** Garantir segurança nas rotas e injeção de JWT.
4. **Desenvolvimento das Features:** Construir os componentes do Feed Social e do AVA (Salas de Aula) utilizando Standalone Components e Signals.
