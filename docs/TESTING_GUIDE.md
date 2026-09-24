# 📋 Guia Completo de Testes - Plataforma Acadêmica

Este documento abrange toda a estratégia de testes da Plataforma Acadêmica, incluindo:
- Testes de unidade para camada de serviço e controller (backend)
- Testes de integração (backend)
- Testes end-to-end (frontend) com Cypress
- Mocks e estratégias de teste
- Boas práticas de nível sênior/especialista

## 🎯 Estratégia de Testes Geral

A Plataforma Acadêmica adota uma abordagem de teste em camadas (Test Pyramid):

```
                    ┌─────────────┐
                    │   E2E Tests │  ← Cypress (Frontend)
                    └─────────────┘
                            ▲
                    ┌─────────────┐
                    │ Integration │  ← Spring Boot Test
                    └─────────────┘
                            ▲
                    ┌─────────────┐
                    │  Unit Tests │  ← JUnit + Mockito
                    └─────────────┘
```

### Distribuição Ideal:
- **70%** Testes de Unidade (Unit Tests)
- **20%** Testes de Integração (Integration Tests)
- **10%** Testes End-to-End (E2E Tests)

## 🔧 Configuração de Ambiente de Teste

### Backend (Spring Boot)
- **Framework**: JUnit 5 + Mockito
- **Gerenciamento de Dependências**: Maven (pom.xml)
- **Banco de Dados de Teste**: H2 (em memória)
- **Application Properties**: `src/test/resources/application-test.properties`

### Frontend (Angular)
- **Framework**: Jasmine + Karma (unit) + Cypress (E2E)
- **Gerenciamento de Dependências**: npm (package.json)
- **Environment**: `src/environments/environment.test.ts`

## 🧪 Camada de Serviço (Service Layer) - Testes de Unidade

### Estratégia:
- Testar cada método de serviço isoladamente
- Mockar dependências (repositórios, outros serviços)
- Cobrir cenários de sucesso, falha e exceções
- Verificar interações com mocks quando apropriado

### Exemplo de Estrutura de Teste de Serviço:

```java
@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    
    @Mock
    private PerfilRepository perfilRepository;
    
    @InjectMocks
    private UsuarioService usuarioService;
    
    @Test
    void deveCriarUsuarioComSucesso() {
        // Arrange
        UsuarioDTO dto = new UsuarioDTO("test@email.com", "senha123", "João");
        Usuario usuario = new Usuario();
        // ... configurar usuario
        
        when(usuarioRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        
        // Act
        Usuario resultado = usuarioService.criarUsuario(dto);
        
        // Assert
        assertNotNull(resultado);
        assertEquals(dto.getEmail(), resultado.getEmail());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }
    
    @Test
    void naoDeveCriarUsuarioComEmailDuplicado() {
        // Arrange
        UsuarioDTO dto = new UsuarioDTO("test@email.com", "senha123", "João");
        when(usuarioRepository.existsByEmail(dto.getEmail())).thenReturn(true);
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
                   () -> usuarioService.criarUsuario(dto));
    }
}
```

### Cobertura Esperada para Serviços:
- **UsuarioService**: 100%
- **PostagemService**: 100%
- **ComunidadeService**: 100%
- **AmizadeService**: 100%
- **NotificacaoService**: 100%
- **DashboardService**: 100%
- **ArquivoService**: 100%
- **AtividadeService**: 100%
- **ProfessorService**: 100%
- **SaladeAulaService**: 100%

## 🎯 Camada de Controller - Testes de Unidade e Integração

### Testes de Unidade (com MockMvc):
- Testar endpoints isoladamente
- Mockar serviços
- Validar request/response (JSON, status codes, headers)
- Testar validação de entrada (@Valid)
- Testar tratamento de exceções (@ControllerAdvice)

### Exemplo de Teste de Controller com MockMvc:

```java
@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UsuarioService usuarioService;
    
    @Test
    void deveRetornarUsuarioQuandoBuscarPorIdExistente() throws Exception {
        // Arrange
        Usuario usuario = new Usuario(1L, "joao@email.com", "João Silva");
        when(usuarioService.buscarPorId(1L)).thenReturn(usuario);
        
        // Act & Assert
        mockMvc.perform(get("/api/usuarios/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1))
               .andExpect(jsonPath("$.email").value("joao@email.com"))
               .andExpect(jsonPath("$.nome").value("João Silva"));
    }
    
    @Test
    void deveRetornar404QuandoUsuarioNaoEncontrado() throws Exception {
        // Arrange
        when(usuarioService.buscarPorId(999L)).thenThrow(EntidadeNaoEncontradaException.class);
        
        // Act & Assert
        mockMvc.perform(get("/api/usuarios/999"))
               .andExpect(status().isNotFound());
    }
}
```

### Testes de Integração (@SpringBootTest):
- Testar fluxo completo controller → service → repository
- Usar banco de dados em memória (H2)
- Testar configurações de segurança, filtros, interceptors
- Validar comportamento com dados reais

### Exemplo de Teste de Integração:

```java
@SpringBootTest
@AutoConfigureMockMvc
class UsuarioControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Test
    void deveCriarUsuarioERetornar201() throws Exception {
        // Arrange
        UsuarioDTO dto = new UsuarioDTO("novo@email.com", "senha123", "Maria");
        
        // Act & Assert
        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
               .andExpect(status().isCreated())
               .andExpect(header().string("Location", containsString("/api/usuarios/")))
               .andExpect(jsonPath("$.email").value("novo@email.com"));
               
        // Verificar no banco
        assertTrue(usuarioRepository.existsByEmail("novo@email.com"));
    }
}
```

### Cobertura Esperada para Controllers:
- **UsuarioController**: 95%+ (unidade + integração)
- **PostagemController**: 95%+
- **ComunidadeController**: 95%+
- **AmizadeController**: 95%+
- **NotificacaoController**: 95%+
- **DashboardController**: 95%+
- **ArquivoController**: 95%+
- **AtividadeController**: 95%+
- **ProfessorController**: 95%+
- **SaladeAulaController**: 95%+
- **Controllers de Integração**: 85%+ (foco em fluxos críticos)

## 🧩 Estratégia de Mocks

### Quando Mockar:
1. **Dependências externas**: Bancos de dados, APIs de terceiros (OAuth, email)
2. **Serviços complexos**: Quando o teste unitário foca na lógica do serviço atual
3. **Componentes lentos ou não-determinísticos**: Geradores de UUID, timestamps

### Quando NÃO Mockar:
1. **Classes simples de domínio (DTOs, Entities)**: Testar comportamento real
2. **Utilitários puros**: Quando não têm efeitos colaterais
3. **Integração crítica**: Quando validar a interação real é essencial

### Bibliotecas de Mock:
- **Mockito**: Framework principal para mocks
- **MockBean**: Para mocks em testes de integração Spring
- **Spy**: Quando precisar chamar métodos reais parcialmente
- **ArgumentCaptor**: Para capturar argumentos passados para métodos mockados

### Exemplo de Uso Avançado de Mocks:

```java
@Test
void deveProcessarPostagemComMentions() {
    // Arrange
    Postagem postagem = new Postagem();
    postagem.setConteudo("Olá @joao e @maria, veja isso!");
    
    // Mock do serviço de menções
    when(mentionService.extractMentions(anyString()))
        .thenReturn(Arrays.asList("joao", "maria"));
    
    // Mock do serviço de notificação
    doNothing().when(notificationService).sendMentionNotifications(
        eq(postagem.getAutorId()), 
        anyList()
    );
    
    // Act
    postagemService.processarPostagem(postagem);
    
    // Assert
    verify(mentionService).extractMentions("Olá @joao e @maria, veja isso!");
    verify(notificationService).sendMentionNotifications(
        eq(postagem.getAutorId()), 
        eq(Arrays.asList("joao", "maria"))
    );
}
```

## 🖥️ Testes End-to-End (Frontend) - Cypress

### Estrutura de Pastas:
```
cypress/
├── e2e/                 # Testes funcionais
│   ├── auth/            # Testes de autenticação
│   │   ├── login.cy.js
│   │   ├── registro.cy.js
│   │   └── oauth.cy.js
│   ├── dashboard/       # Testes do dashboard
│   │   ├── visitante.cy.js
│   │   ├── aluno.cy.js
│   │   └── professor.cy.js
│   ├── postagens/       # Testes de postagens
│   │   ├── criar.cy.js
│   │   ├── editar.cy.js
│   │   ├── comentar.cy.js
│   │   └── curtir.cy.js
│   ├── comunidades/     # Testes de comunidades
│   │   ├── criar.cy.js
│   │   ├── entrar.cy.js
│   │   ├── sair.cy.js
│   │   └── moderar.cy.js
│   ├── atividades/      # Testes de atividades
│   │   ├── entregar.cy.js
│   │   ├── avaliar.cy.js
│   │   └── historico.cy.js
│   └── perfil/          # Testes de perfil
│       ├── editar.cy.js
│       ├── foto.cy.js
│       └── privacidade.cy.js
├── support/             # Comandos customizados
│   ├── commands.js      # Comandos reutilizáveis
│   └── e2e.js           # Configurações globais
├── fixtures/            # Dados de teste
│   ├── usuarios.json
│   ├── postagens.json
│   └── comunidades.json
└── plugins/             # Plugins do Cypress
    └── index.js
```

### Comandos Customizados (support/commands.js):

```javascript
// Login customizado (bypass OAuth para testes)
Cypress.Commands.add('loginViaMock', (usuario = 'aluno') => {
  const usuarios = {
    aluno: { id: 1, email: 'aluno@test.com', nome: 'Aluno Teste', papel: 'ALUNO' },
    professor: { id: 2, email: 'prof@test.com', nome: 'Prof Teste', papel: 'PROFESSOR' },
    admin: { id: 3, email: 'admin@test.com', nome: 'Admin Teste', papel: 'ADMIN' }
  };
  
  const user = usuarios[usuario] || usuarios.aluno;
  
  // Simular login armazenando token no localStorage
  cy.window().then((win) => {
    win.localStorage.setItem('token', 'fake-jwt-token');
    win.localStorage.setItem('usuario', JSON.stringify(user));
  });
  
  return cy;
});

// Logout
Cypress.Commands.add('logout', () => {
  cy.window().then((win) => {
    win.localStorage.removeItem('token');
    win.localStorage.removeItem('usuario');
  });
  cy.visit('/login');
});

// Criar dados de teste via API (quando necessário)
Cypress.Commands.add('criarPostagemViaApi', (conteudo) => {
  return cy.request({
    method: 'POST',
    url: '/api/postagens',
    body: { conteudo },
    headers: {
      Authorization: `Bearer ${Cypress.env('token')}`
    }
  });
});

// Limpar dados de teste
Cypress.Commands.add('limparDadosTeste', () => {
  cy.request({
    method: 'DELETE',
    url: '/api/teste/limpar-tudo',
    headers: {
      Authorization: `Bearer ${Cypress.env('token')}`
    }
  });
});
```

### Testes de Autenticação (auth/login.cy.js):

```javascript
describe('Fluxo de Autenticação', () => {
  beforeEach(() => {
    cy.visit('/login');
    cy.limparDadosTeste(); // Começar com estado limpo
  });

  it('deve permitir login com credenciais válidas', () => {
    // Arrange - criar usuário de teste via API (se endpoint disponível)
    // ou usar usuário pré-cadastrado no banco de teste
    
    // Act
    cy.get('input[formControlName="email"]').digitar('aluno@test.com');
    cy.get('input[formControlName="senha"]').digitar('senha123');
    cy.get('button[type="submit"]').clicar();
    
    // Assert
    cy.url().should('include', '/dashboard');
    cy.get('[data-cy="usuario-nome"]').should('conter.text', 'Aluno Teste');
    cy.get('[data-cy="papel-usuario"]').should('conter.text', 'ALUNO');
  });

  it('deve mostrar erro para credenciais inválidas', () => {
    // Act
    cy.get('input[formControlName="email"]').digitar('errado@test.com');
    cy.get('input[formControlName="senha"]').digitar('senhaerrada');
    cy.get('button[type="submit"]').clicar();
    
    // Assert
    cy.get('[data-cy="mensagem-erro"]').should('conter.text', 
      'Email ou senha inválidos');
    cy.url().should('include', '/login');
  });

  it('deve redirecionar para página solicitada após login', () => {
    // Arrange
    cy.visit('/comunidades'); // Tentar acessar página protegida
    
    // Act - fazer login
    cy.get('input[formControlName="email"]').digitar('aluno@test.com');
    cy.get('input[formControlName="senha"]').digitar('senha123');
    cy.get('button[type="submit"]').clicar();
    
    // Assert
    cy.url().should('include', '/comunidades');
  });
});
```

### Testes de Funcionalidades Principais:

#### Postagens (postagens/criar.cy.js):
```javascript
describe('Criação de Postagens', () => {
  beforeEach(() => {
    cy.loginViaMock('aluno');
    cy.visit('/feed');
  });

  it('deve permitir criar postagem de texto', () => {
    const conteudo = 'Este é um teste de postagem criada via Cypress ' + 
                     Date.now();
    
    // Act
    cy.get('[data-cy="campo-postagem"]').digitar(conteudo);
    cy.get('[data-cy="botao-publicar"]').clicar();
    
    // Assert
    cy.get('[data-cy="postagem-item"]').first()
      .should('conter.text', conteudo);
      
    cy.get('[data-cy="campo-postagem"]').should('estar.vazio');
  });

  it('deve permitir criar postagem com imagem', () => {
    // Arrange
    const nomeArquivo = 'test-image.png';
    const conteudo = 'Postagem com imagem';
    
    // Act
    cy.get('[data-cy="campo-postagem"]').digitar(conteudo);
    cy.get('[data-cy="upload-imagem"]').selecionaArquivo(nomeArquivo);
    cy.get('[data-cy="botao-publicar"]').clicar();
    
    // Assert
    cy.get('[data-cy="postagem-item"]').first()
      .should('conter.text', conteudo)
      .and('conter.imagem', nomeArquivo);
  });

  it('não deve permitir postagem vazia', () => {
    // Act
    cy.get('[data-cy="botao-publicar"]').clicar();
    
    // Assert
    cy.get('[data-cy="mensagem-erro"]').should('conter.text', 
      'A postagem não pode estar vazia');
    cy.get('[data-cy="botao-publicar"]').should('estar.desabilitado');
  });
});
```

#### Comunidades (comunidades/criar.cy.js):
```javascript
describe('Gerenciamento de Comunidades', () => {
  beforeEach(() => {
    cy.loginViaMock('aluno');
    cy.visit('/comunidades');
  });

  it('deve permitir criar nova comunidade', () => {
    const nome = 'Comunidade de Teste ' + Date.now();
    const descricao = 'Esta é uma comunidade criada para testes automatizados';
    
    // Act
    cy.get('[data-cy="botao-nova-comunidade"]').clicar();
    cy.get('[data-cy="campo-nome"]').digitar(nome);
    cy.get('[data-cy="campo-descricao"]').digitar(descricao);
    cy.get('[data-cy="botao-criar-comunidade"]').clicar();
    
    // Assert
    cy.get('[data-cy="comunidade-card"]').first()
      .should('conter.text', nome)
      .and('conter.text', descricao);
      
    cy.get('[data-cy="botao-entrar-comunidade"]').first()
      .should('estar.visivel');
  });

  it('deve permitir entrar e sair de comunidades', () => {
    // Assume que já existe uma comunidade disponível
    // (criada em teste anterior ou fixture)
    
    // Act - entrar
    cy.get('[data-cy="botao-entrar-comunidade"]').first().clicar();
    
    // Assert
    cy.get('[data-cy="botao-sair-comunidade"]').first()
      .should('estar.visivel');
    cy.get('[data-cy="botao-entrar-comunidade"]').first()
      .should('não.existir');
      
    // Act - sair
    cy.get('[data-cy="botao-sair-comunidade"]').first().clicar();
    
    // Assert
    cy.get('[data-cy="botao-entrar-comunidade"]').first()
      .should('estar.visivel');
    cy.get('[data-cy="botao-sair-comunidade"]').first()
      .should('não.existir');
  });
});
```

#### Atividades (atividades/entregar.cy.js):
```javascript
describe('Entrega de Atividades', () => {
  beforeEach(() => {
    cy.loginViaMock('aluno');
    cy.visit('/atividades');
  });

  it('deve permitir entregar atividade com arquivo', () => {
    // Assume que existe uma atividade pendente
    const nomeArquivo = 'resposta-teste.pdf';
    const comentario = 'Minha resposta para a atividade';
    
    // Act
    cy.get('[data-cy="botao-entregar-atividade"]').first().clicar();
    cy.get('[data-cy="campo-comentario"]').digitar(comentario);
    cy.get('[data-cy="upload-arquivo"]').selecionaArquivo(nomeArquivo);
    cy.get('[data-cy="botao-submeter-entrega"]').clicar();
    
    // Assert
    cy.get('[data-cy="mensagem-sucesso"]').should('conter.text', 
      'Atividade entregue com sucesso');
      
    cy.get('[data-cy="status-entrega"]').first()
      .should('conter.text', 'Entregue');
  });

  it('deve mostrar atividades vencidas', () => {
    // Assert
    cy.get('[data-cy="atividade-vencida"]').should('existir');
    cy.get('[data-cy="atividade-vencida"]')
      .should('ter.classe', 'vencida');
  });
});
```

### Comandos Customizados Adicionais:

```javascript
// Digitar com delay realista
Cypress.Commands.add('digitar', { prevSubject: 'element' }, (subject, texto) => {
  cy.wrap(subject)
    .clear()
    .type(texto, { delay: 50 });
});

// Clicar com espera por animação
Cypress.Commands.add('clicar', { prevSubject: 'element' }, (subject) => {
  cy.wrap(subject)
    .click({ force: true })
    .wait(300); // Esperar animações
});

// Verificar se elemento contém texto
Cypress.Commands.add('conter.text', { prevSubject: 'element' }, (subject, texto) => {
  cy.wrap(subject).should('contain', texto);
});

// Verificar se elemento está vazio (para inputs)
Cypress.Commands.add('estar.vazio', { prevSubject: 'element' }, (subject) => {
  cy.wrap(subject).should('have.value', '');
});

// Verificar se elemento está desabilitado
Cypress.Commands.add('estar.desabilitado', { prevSubject: 'element' }, (subject) => {
  cy.wrap(subject).should('be.disabled');
});

// Verificar se elemento está visível
Cypress.Commands.add('estar.visivel', { prevSubject: 'element' }, (subject) => {
  cy.wrap(subject).should('be.visible');
});

// Verificar se elemento não existe
Cypress.Commands.add('não.existir', { prevSubject: 'subject' }, (subject) => {
  cy.wrap(subject).should('not.exist');
});

// Verificar se elemento contém imagem
Cypress.Commands.add('conter.imagem', { prevSubject: 'element' }, (subject, nomeArquivo) => {
  cy.wrap(subject)
    .find('img')
    .should('have.attr', 'src')
    .and('contain', nomeArquivo);
});

// Selecionar arquivo para upload
Cypress.Commands.add('selecionaArquivo', { prevSubject: 'element' }, (subject, nomeArquivo) => {
  const arquivo = cypressPath + '/fixtures/' + nomeArquivo;
  cy.wrap(subject).attachFile(arquivo);
});
```

### Fixtures de Teste (fixtures/usuarios.json):
```json
{
  "usuarios": [
    {
      "id": 1,
      "email": "aluno@test.com",
      "nome": "Aluno Teste",
      "senha": "senha123",
      "papel": "ALUNO",
      "ativo": true
    },
    {
      "id": 2,
      "email": "prof@test.com",
      "nome": "Professor Teste",
      "senha": "senha123",
      "papel": "PROFESSOR",
      "ativo": true
    },
    {
      "id": 3,
      "email": "admin@test.com",
      "nome": "Admin Teste",
      "senha": "senha123",
      "papel": "ADMIN",
      "ativo": true
    }
  ],
  "comunidades": [
    {
      "id": 1,
      "nome": "Comunidade de Teste",
      "descricao": "Comunidade criada para testes",
      "criadoPor": 1,
      "ativo": true
    }
  ],
  "postagens": [
    {
      "id": 1,
      "conteudo": "Postagem de teste inicial",
      "autorId": 1,
      "comunidadeId": 1,
      "curtiidas": 0,
      "dataCriacao": "2026-09-24T10:00:00Z"
    }
  ]
}
```

## 📊 Métricas e Relatórios de Cobertura

### Backend (Jacoco):
- **Arquivo de configuração**: `pom.xml` (plugin jacoco-maven-plugin)
- **Relatório gerado**: `target/site/jacoco/index.html`
- **Meta de cobertura**: 85% geral, 90%+ para camadas críticas

### Frontend (Istanbul/Cypress):
- **Configuração**: `cypress.config.js` com cobertura ativada
- **Relatório gerado**: `coverage/lcov-report/index.html`
- **Meta de cobertura**: 80%+ para componentes críticos

### Scripts de Relatório:
```bash
# Backend
mvn clean test jacoco:report

# Frontend
npm run test:coverage
npm run cypress:run -- --env coverage=true
```

## 🚀 Integração com CI/CD

### GitHub Actions Workflow (`.github/workflows/test.yml`):
```yaml
name: Test Suite

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  test:
    runs-on: ubuntu-latest
    strategy:
      matrix:
        java-version: [21]
        node-version: [20]
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK ${{ matrix.java-version }}
      uses: actions/setup-java@v3
      with:
        java-version: ${{ matrix.java-version }}
        distribution: 'temurin'
        
    - name: Set up Node.js ${{ matrix.node-version }}
      uses: actions/setup-node@v3
      with:
        node-version: ${{ matrix.node-version }}
        cache: 'npm'
        
    - name: Build and Test Backend
      run: |
        cd plataforma-academica-spring
        mvn -B clean verify
        
    - name: Install Frontend Dependencies
      run: |
        cd plataforma-academica-angular
        npm ci
        
    - name: Run Frontend Unit Tests
      run: |
        cd plataforma-academica-angular
        npm run test -- --watch=false --browsers=ChromeHeadless
        
    - name: Run Cypress Tests
      uses: cypress-io/github-action@v6
      with:
        start: npm start
        wait-on: 'http://localhost:4200'
        wait-on-timeout: 120
        browser: chrome
        headless: true
        
    - name: Upload Coverage Reports
      if: always()
      uses: actions/upload-artifact@v3
      with:
        name: coverage-reports
        path: |
          plataforma-academica-spring/target/site/jacoco/
          plataforma-academica-angular/coverage/
          plataforma-academica-angular/cypress/reports/
```

## 📋 Checklist de Qualidade de Testes

### Antes de Commit:
- [ ] Todos os novos métodos têm testes de unidade
- [ ] Testes cobrem cenários de sucesso e falha
- [ ] Mocks são usados apropriadamente (não excessivamente)
- [ ] Nomes de testes são descritivos e seguem padrão Given/When/Then
- [ ] Testes são independentes (não dependem de estado de outros testes)
- [ ] Dados de teste vêm de fixtures ou são gerados dinamicamente
- [ ] Testes de integração testam fluxos críticos reais
- [ ] Testes E2E cobrem jornadas de usuário importantes
- [ ] Testes são rápidos (evitar sleeps desnecessários, usar waits inteligentes)
- [ ] Testes são determinísticos (mesmo código sempre produz mesmo resultado)

### Revisão de Código:
- [ ] Testes estão no pacote/teste correto (mesmo nome do pacote da classe)
- [ ] Testes seguem convenções de nomeamento da equipe
- [ ] Testes não testam detalhes de implementação (foco no comportamento)
- [ ] Testes de unidade não fazem chamadas reais a banco/serviços externos
- [ ] Testes de integração usam @SpringBootTest quando necessário
- [ ] Testes E2E usam seletores data-cy (não CSS ou XPath frágil)
- [ ] Testes limpam após si mesmos quando modificam estado persistente

## 🔧 Troubleshooting Comum

### Problemas de Teste de Unidade:
1. **NullPointerException em mocks**: Verificar se @InjectMocks está configurado corretamente
2. **MockitoException: Wrong type of return value**: Verificar tipos de retorno nos when()
3. **UnnecessaryStubbingException**: Remover stubs não utilizados
4. **Tests dependendo da ordem**: Garantir que testes não compartilham estado mutável

### Problemas de Teste de Integração:
1. **BeanCreationException**: Verificar configuração de teste no application-test.properties
2. **Transaction rollback issues**: Usar @Transactional quando apropriado
3. **Database locked errors**: Garantir que conexões sejam fechadas corretamente
4. **Port already in use**: Usar portas aleatórias em testes paralelos (@SpringBootTest(webEnvironment = RANDOM_PORT))

### Problemas de Cypress:
1. **Element not found**: Usar seletores data-cy, aguardar elementos com cy.wait() ou intercept()
2. **Tests flaky**: Usar cy.wait() com aliases de rota, não waits fixos
3. **401/403 errors**: Verificar se tokens estão sendo definidos corretamente nos comandos customizados
4. **File upload not working**: Verificar caminho do fixture e permissões
5. **Cross-origin errors**: Configurar baseUrl corretamente no cypress.config.js

## 📈 Melhoria Contínua

### Processo de Revisão de Testes:
1. **Durante desenvolvimento**: Escrever testes junto com o código (TDD quando apropriado)
2. **Durante code review**: Revisor verifica cobertura e qualidade dos testes
3. **Semanalmente**: Revisar relatórios de cobertura e identificar lacunas
4. **Mensalmente**: Refatorar testes difíceis de manter ou lentos
5. **Trimestralmente**: Revisar estratégia de teste inteira e ajustar metas

### Métricas a Acompanhar:
- **Cobertura de linha por módulo**
- **Tempo de execução da suite de testes**
- **Porcentagem de testes flaky** (que falham/passam aleatoriamente)
- **Distribuição de tipos de teste** (unit/integração/e2e)
- **Taxa de falha de testes em CI**
- **Tempo médio para corrigir testes quebrados**

## 📚 Recursos e Referências

### Livros:
- "Growing Object-Oriented Software, Guided by Tests" - Steve Freeman & Nat Pryce
- "Effective Unit Testing" - Lasse Koskela
- "Testing Java Microservices" - Alex Soto Bueno & Jason Porter
- "Cypress.io Testing Practice" - Various authors

### Blogs/Sites:
- https://martinfowler.com/articles/mocksArentStubs.html
- https://www.baeldung.com/testing-java
- https://docs.cypress.io/guides/overview/why-cypress.html
- https://spring.io/guides/gs/testing-web/

### Vídeos/Cursos:
- "Testing Spring Boot: Beginner to Guru" (Udemy)
- "Advanced Testing with JUnit 5" (Pluralsight)
- "Cypress.io: End-to-End Testing Framework" (YouTube - Cypress.io channel)

---

*Este documento deve ser revisado e atualizado a cada sprint ou sempre que houver mudanças significativas na arquitetura ou nas práticas de teste da equipe.*