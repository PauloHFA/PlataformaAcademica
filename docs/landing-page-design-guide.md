# Guia de Direção Visual — Plataforma Acadêmica

**Status:** guia aprovado para implementação
**Escopo:** landing page pública, single-page com scroll longo
**Produto:** rede social acadêmica + gestão de salas de aula
**Referência de personalidade:** campus contemporâneo, biblioteca viva e comunidade de estudo

## 1. Direção criativa

A página deve parecer o primeiro contato com uma plataforma usada por pessoas que estudam juntas, e não uma vitrine genérica de software.

A linguagem visual combina:

- a estrutura editorial de uma publicação acadêmica;
- a precisão de uma ferramenta de organização;
- a energia humana de uma rede de colegas;
- sinais materiais discretos de papel, fichário, margem e anotação.

A interface não deve imitar uma biblioteca antiga nem parecer institucional. O universo acadêmico aparece na organização da informação, nos nomes das matérias, nas referências a prazos e colaboração e em pequenos detalhes editoriais. Não será usado como decoração temática excessiva.

## 2. Paleta cromática

A paleta foi escolhida para criar contraste entre concentração, leitura e interação. Nenhuma cor existe apenas para enfeitar.

| Token | Hex | Nome | Uso |
|---|---|---|---|
| `--night-study` | `#17212B` | Noite de estudo | Fundo de hero, navegação e seções de maior concentração |
| `--paper-warm` | `#F5F1E8` | Papel quente | Fundo principal claro e áreas de leitura |
| `--ink` | `#263238` | Tinta | Texto principal, títulos e elementos de alta prioridade |
| `--copper` | `#B86B3E` | Cobre | Ação principal, prazos, seleção e pontos de energia |
| `--sage` | `#728C69` | Sálvia | Estados positivos, presença, progresso e colaboração |
| `--dust-blue` | `#9AAFC1` | Azul poeira | Metadados, divisores, informação secundária e equilíbrio visual |

### Regras de harmonia

- `--night-study` e `--paper-warm` formam a alternância estrutural da página.
- `--ink` nunca deve competir com o `--night-study`; será usado em superfícies claras.
- `--copper` aparece em pequenas doses, principalmente em CTA e informação importante.
- `--sage` comunica atividade e pertencimento, não deve ser usado como cor decorativa geral.
- `--dust-blue` serve para respiro e hierarquia secundária, nunca como azul de marca dominante.
- O cobre e a sálvia não devem aparecer simultaneamente em todos os componentes. Cada demo terá uma cor de ênfase dominante.
- Gradientes ficam restritos a transições atmosféricas muito discretas do hero. Nenhum conteúdo dependerá de gradiente para ter contraste.
- Sombras serão curtas e quentes, com baixa opacidade. Não haverá sombra cinza pesada aplicada a todos os blocos.

## 3. Tipografia

### Famílias

- **Fraunces:** títulos, nome da marca e frases de destaque. Tem personalidade editorial sem parecer uma página institucional antiga.
- **DM Sans:** navegação, corpo, metadados, controles e conteúdo de demonstração. Mantém leitura rápida em telas densas.

As fontes devem ser carregadas no `index.html` e declaradas somente nos tokens globais.

### Escala

| Token | Tamanho | Papel |
|---|---:|---|
| `--type-hero` | `clamp(3.5rem, 8vw, 7.5rem)` | Título de abertura |
| `--type-display` | `clamp(2.5rem, 5vw, 4.75rem)` | Título de seção |
| `--type-heading` | `1.4rem` | Títulos dentro de demos |
| `--type-body` | `1.05rem` | Texto explicativo |
| `--type-meta` | `0.78rem` | Datas, estados e contadores |
| `--type-label` | `0.68rem` | Rótulos editoriais e categorias |

O hero terá uma quebra de linha intencional. Os títulos de funcionalidade serão curtos e diretos, sem frases de marketing infladas.

## 4. Estrutura da página

```text
[ navegação fixa: marca ]                         [ Fazer Login ] [ Criar Conta ]

┌──────────────────────────────────────────────────────────────────────────────┐
│ HERO: uma frase forte + CTA único + sinal visual de atividade acadêmica       │
│       altura de viewport, sem cards empilhados                               │
└──────────────────────────────────────────────────────────────────────────────┘

┌──────────────────────────┬───────────────────────────────────────────────────┐
│ DEMO DE SALA             │ SALAS DE AULA                                      │
│ interface de uma matéria │ entrar, acompanhar membros e tópicos               │
└──────────────────────────┴───────────────────────────────────────────────────┘

┌───────────────────────────────────────────────────┬──────────────────────────┐
│ ATIVIDADES                                        │ DEMO DE PRAZO             │
│ acompanhar trabalho, prioridade e andamento      │ checklist e progresso     │
└───────────────────────────────────────────────────┴──────────────────────────┘

┌──────────────────────────┬───────────────────────────────────────────────────┐
│ DEMO DO FEED             │ FEED SOCIAL                                        │
│ postagem com respostas   │ publicar, reagir e continuar uma conversa          │
└──────────────────────────┴───────────────────────────────────────────────────┘

┌───────────────────────────────────────────────────┬──────────────────────────┐
│ SISTEMA DE AMIZADE                                │ DEMO DE PEDIDO            │
│ encontrar colegas e formar grupos                 │ pedido -> aceito          │
└───────────────────────────────────────────────────┴──────────────────────────┘

┌──────────────────────────┬───────────────────────────────────────────────────┐
│ DEMO DE PERFIL           │ PERFIL PERSONALIZADO                              │
│ capa, bio e trajetória   │ mostrar interesses e participação                  │
└──────────────────────────┴───────────────────────────────────────────────────┘

[ rodapé mínimo: marca, frase curta, contato/termos essenciais ]
```

## 5. Barra de navegação

- Fixa no topo, com fundo `--night-study` translúcido e uma linha inferior fina.
- Ao rolar, reduz levemente a altura e aumenta a opacidade; não desaparece.
- À esquerda: marca textual **Plataforma Acadêmica**, sem foguete ou emoji.
- À direita: apenas `Fazer Login` e `Criar Conta`.
- `Criar Conta` é o único botão preenchido da barra.
- Em mobile, os dois comandos permanecem acessíveis em menu compacto; a marca não pode ser esmagada.
- A navegação não exibirá links internos de produto na landing pública.

## 6. Hero

O hero é o único momento mais abstrato da página. Deve abrir a narrativa, não listar funcionalidades.

Conteúdo:

- título curto e memorável sobre estudar em conjunto;
- subtítulo com uma promessa concreta e sem jargão;
- um CTA principal, `Começar agora`;
- uma composição visual lateral ou inferior que sugira uma sala ativa: nome da matéria, avatares, uma tarefa próxima e uma atualização do feed.

A composição visual deve parecer uma janela do produto, mas não um painel cheio de cards. Ela será construída com uma superfície editorial assimétrica, linhas de informação e um pequeno marcador de atividade.

## 7. Demonstrações por funcionalidade

Cada demonstração deve ser um mini fluxo visual, com hierarquia e estado, não um cartão com ícone.

### 7.1 Salas de Aula

Mostrar uma sala chamada `História Contemporânea` com:

- matéria e professora responsável;
- estado `12 pessoas estudando agora`;
- três membros visíveis com avatar;
- tópicos recentes;
- uma ação de entrada ou acompanhamento.

A demo terá estrutura de painel lateral de sala, com navegação interna e conteúdo principal. A ênfase será `--dust-blue`.

### 7.2 Atividades

Mostrar uma tarefa realista chamada `Ensaio: memória e cidade` com:

- prazo destacado;
- prioridade;
- barra de progresso;
- checklist com itens concluídos e pendentes;
- indicação de revisão ou entrega.

A interação visual será um checklist clicável ou um estado de progresso controlado localmente. A ênfase será `--copper`, pois prazo é informação de decisão.

### 7.3 Feed Social

Mostrar uma postagem acadêmica com:

- autor e contexto de uma sala;
- trecho de texto ou imagem editorial;
- contador de curtidas;
- comentários visíveis;
- campo de resposta ou estado de conversa.

A interação visual será a curtida ou expansão de comentários. O feed deve parecer uma conversa entre colegas, não uma lista de anúncios. A ênfase será `--sage`.

### 7.4 Sistema de Amizade

Mostrar o fluxo explícito de conexão:

1. colega encontrado por nome ou matéria;
2. botão `Adicionar`;
3. estado `Pedido enviado`;
4. aceite e confirmação de conexão.

A mudança de estado deve ser demonstrável por clique, com texto direto. A ênfase será `--copper` apenas no comando e `--sage` na confirmação.

### 7.5 Perfil Personalizado

Mostrar um perfil acadêmico completo, não apenas estatísticas:

- avatar e nome;
- bio curta;
- áreas de interesse;
- salas atuais;
- atividades concluídas;
- pequenos sinais de trajetória e participação.

O layout terá composição de perfil editorial, com cabeçalho amplo e uma coluna de informações. A ênfase será `--dust-blue` com detalhes em `--copper`.

## 8. Ritmo visual e layouts

As seções alternarão entre fundo claro e escuro, mas não de maneira mecânica. A mudança de fundo deve acompanhar o tipo de uso:

- hero escuro: concentração e entrada;
- salas claro: orientação e descoberta;
- atividades escuro: foco e prazo;
- feed claro: troca e leitura;
- amizade escuro: pertencimento e conexão;
- perfil claro: identidade e trajetória.

As demos não terão todas o mesmo raio, cabeçalho ou sombra. A unidade virá dos tokens, da tipografia e da disciplina de espaçamento, não da repetição de um componente.

## 9. Animação e interação

As animações começam quando a seção entra na viewport, usando `IntersectionObserver` ou equivalente compatível com Angular. Cada seção terá uma entrada própria:

| Seção | Entrada | Motivo |
|---|---|---|
| Hero | revelação de máscara/linha + leve deslocamento lateral | Apresentar a identidade editorial |
| Salas | painel desliza de dentro para fora, com membros aparecendo em cascata curta | Imita a abertura de uma sala ativa |
| Atividades | checklist desenha progresso e itens entram em sequência | Reforça avanço e conclusão |
| Feed | postagem principal estabiliza e uma resposta surge depois | Simula continuidade de conversa |
| Amizade | conexão visual entre dois perfis e troca de estado do botão | Torna o fluxo pedido/aceite compreensível |
| Perfil | cabeçalho revela primeiro, depois informações sobem em camadas | Mostra construção gradual de identidade |

Regras:

- nenhuma seção usará o mesmo `fade-up` padrão;
- duração entre `450ms` e `750ms`, com deslocamentos pequenos;
- nenhuma animação deve esconder informação essencial;
- `prefers-reduced-motion: reduce` desativa deslocamentos e mantém apenas mudanças instantâneas de opacidade/estado;
- interações devem ter feedback visual e textual, sem depender apenas de cor;
- não haverá animação infinita decorativa.

## 10. Espaçamento e responsividade

Tokens obrigatórios:

- escala de espaço baseada em `4px`;
- largura máxima de conteúdo entre `1120px` e `1240px`;
- seções com altura mínima próxima de `min(860px, 100svh)` em desktop;
- padding lateral fluido entre `24px` e `7vw`;
- demos com largura e altura estáveis para evitar saltos durante a animação;
- breakpoint principal em `768px`, com ajustes adicionais para telas muito estreitas.

No mobile:

- a alternância vira fluxo vertical intencional, mantendo demo e texto como blocos distintos;
- o conteúdo mais importante aparece antes da parte decorativa;
- demos não serão reduzidas a miniaturas ilegíveis;
- a navegação mantém os dois comandos de autenticação acessíveis;
- nenhum título ou botão pode quebrar de maneira acidental.

## 11. Princípios anti-template

1. **Informação antes de ornamentação:** cada detalhe visual deve explicar estado, relação ou ação.
2. **Produto visível:** a página mostra como a plataforma funciona, não apenas o que ela promete.
3. **Variação com sistema:** seções diferentes terão formas diferentes, mas compartilharão tokens e regras de leitura.
4. **Academia contemporânea:** usar ritmo editorial, contexto de matéria e trajetória, sem clichês de livros, capelos ou quadros-negros.
5. **Comunidade real:** nomes, comentários, presença e pedidos de amizade devem sugerir pessoas colaborando.
6. **Contraste com intenção:** fundo escuro representa foco; claro representa leitura; cobre representa decisão; sálvia representa conexão/progresso.
7. **Poucos comandos:** a landing não compete com o produto. A ação principal é começar, e a navegação pública é mínima.

## 12. Autocrítica obrigatória

Este plano poderia ser aplicado a qualquer landing de rede social? **Não completamente.**

A diferenciação está em:

- tratar a unidade central como sala por matéria, não como comunidade abstrata;
- representar atividades, prazo, checklist e revisão como elementos de estudo;
- mostrar feed contextualizado por produção acadêmica;
- representar amizade como formação de rede de colegas e grupos de estudo;
- mostrar perfil como trajetória de participação, não como perfil social genérico;
- usar a linguagem editorial de leitura e organização como base visual, em vez de roxo, gradientes e cards de métricas.

Se durante a implementação uma seção puder ser trocada por uma seção de qualquer aplicativo social sem perder sentido, ela deverá ser revisada.

## 13. Critérios de aceite visual

Antes de considerar a landing pronta, verificar:

- a barra permanece legível e estável durante o scroll;
- o hero ocupa a primeira tela e tem CTA único;
- login e cadastro aparecem apenas na barra superior;
- as cinco funcionalidades aparecem na ordem definida;
- o layout alterna demonstração e texto;
- cada demo tem pelo menos um estado ou interação observável;
- cada seção tem animação de entrada diferente;
- a página funciona em desktop e mobile;
- `prefers-reduced-motion` é respeitado;
- não há emoji como ícone estrutural;
- não há paleta roxa/azul genérica;
- não há repetição de cards idênticos;
- não há overflow horizontal ou texto sobreposto;
- screenshots são capturados após a implementação em cada seção, em desktop e mobile;
- o build Angular termina sem erros de template ou TypeScript.

## 14. Ordem de implementação

1. Consolidar tokens e tipografia.
2. Reescrever navegação pública e hero.
3. Criar o shell comum das seções e o comportamento de entrada por viewport.
4. Reescrever a demo de Salas.
5. Reescrever a demo de Atividades.
6. Reescrever a demo de Feed.
7. Reescrever a demo de Amizade.
8. Reescrever a demo de Perfil.
9. Criar rodapé mínimo e regras responsivas.
10. Executar build, validar acessibilidade básica e capturar screenshots de cada seção.
