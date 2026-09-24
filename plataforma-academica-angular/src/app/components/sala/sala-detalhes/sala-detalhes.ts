import { Component, OnInit, PLATFORM_ID, Inject } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { ActivatedRoute, RouterModule, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SalaService } from '../../../services/sala.service';
import { SalaContextService } from '../../../services/sala-context.service';
import { ComentarioService, Comentario } from '../../../services/comentario.service';
import { ForumService } from '../../../services/forum.service';
import { MaterialService } from '../../../services/material.service';
import { SubmissaoAtividadeService } from '../../../services/submissao-atividade.service';
import { SalaDeAula } from '../../../models/sala.model';
import { Atividade } from '../../../models/atividade.model';
import { Pergunta, Resposta } from '../../../models/forum.model';
import { Material, Semana } from '../../../models/material.model';
import { SubmissaoAtividadeResponse } from '../../../models/submissao-atividade.model';

@Component({
  selector: 'app-sala-detalhes',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './sala-detalhes.html',
  styleUrl: './sala-detalhes.css'
})
export class SalaDetalhesComponent implements OnInit {
  sala: SalaDeAula | null = null;
  membros: any[] = [];
  atividades: Atividade[] = [];
  carregando = true;
  erro = '';
  usuarioId = '';
  usuarioEhCriador = false;
  animando = false;
  comentarios: Comentario[] = [];
  novoComentario = '';
  isProfessor = false;
  isAdmin = false;
  // Forum properties
  perguntas: Pergunta[] = [];
  carregandoForum = false;
  erroForum = '';
  respostaSelecionadaId: string | null = null;
  novaRespostaConteudo = '';
  // Material properties
  semanas: Semana[] = [];
  materiaisPorSemana: { [semanaId: string]: Material[] } = {};
  carregandoMateriais = false;
  erroMateriais = '';
  semanaSelecionadaId: string | null = null;
  novoMaterialTitulo = '';
  novoMaterialDescricao = '';
  novoMaterialTipo: 'PDF' | 'VIDEO' | 'LINK' | 'CODIGO' | 'IMAGEM' | 'OUTRO' = 'PDF';
  novoMaterialUrl = '';
  arquivoSelecionado: File | null = null;
  novoMaterialSemanaId: string | null = null;

  // Assignments workflow properties
  atividadesDetalhadas: {
    [atividadeId: string]: {
      atividade: Atividade;
      minhaSubmissao: SubmissaoAtividadeResponse | null;
      submissoes: SubmissaoAtividadeResponse[];
      carregando: boolean;
      erro: string;
      enviandoSubmissao: boolean;
      urlDocumento: string;
      descricaoSubmissao: string;
      arquivosSubmissao: File[];
      linksGitHub: string[];
      nota: number | null;
      feedback: string;
    }
  } = {};

  constructor(
    private route: ActivatedRoute,
    private salaService: SalaService,
    private router: Router,
    private salaContext: SalaContextService,
    private comentarioService: ComentarioService,
    private forumService: ForumService,
    private materialService: MaterialService,
    private submissaoService: SubmissaoAtividadeService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) { }

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.usuarioId = this.getCurrentUserId();
      const isProfessorValue = localStorage.getItem('isProfessor');
      this.isProfessor = isProfessorValue === 'true';
      const isAdminValue = localStorage.getItem('isAdmin');
      this.isAdmin = isAdminValue === 'true';
    }

    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (!id) {
        this.erro = 'ID da sala inválido';
        this.carregando = false;
        return;
      }
      this.carregarDados(id);
    });
  }

  carregarDados(id: string): void {
    this.animando = true;
    this.carregando = true;
    this.erro = '';
    setTimeout(() => this.animando = false, 300);

    this.salaService.buscarPorId(id).subscribe({
      next: (s) => {
        this.sala = s;
        this.salaContext.setNomeSala(s.nome);
        this.usuarioEhCriador = s.criadorId === this.usuarioId;
      },
      error: (err: any) => {
        this.erro = err.error?.message || err.message || 'Erro ao carregar sala';
        console.error('Erro ao carregar sala:', err);
      }
    });

    this.salaService.listarMembros(id).subscribe({
      next: (m) => {
        this.membros = (m || []) as any[];
      },
      error: (err: any) => {
        console.warn('Erro ao listar membros:', err);
      }
    });

    this.salaService.listarAtividades(id).subscribe({
      next: (a) => {
        this.atividades = (a || []) as Atividade[];
      },
      error: (err: any) => {
        console.warn('Erro ao listar atividades:', err);
      }
    });

    this.comentarioService.listarPorSala(id).subscribe({
      next: (c: Comentario[]) => {
        console.log('Comentários da sala', id, 'carregados:', c);
        this.comentarios = (c || [])
          .filter(comentario => !comentario.atividadeId)
          .map((comentario: Comentario) => ({
            ...comentario,
            autorNome: comentario.autor?.nome || 'Usuário',
            autorId: comentario.autor?.id || comentario.autorId || ''
          }));
        console.log('Comentários da sala', id, 'processados:', this.comentarios);
      },
      error: (err: any) => {
        console.warn('Erro ao listar comentários da sala', id, ':', err);
      }
    });

    // Load forum questions for this sala
    this.carregarPerguntas(id);
    // Load materials for this sala
    this.carregarSemanasEMateriais(id);
    // Load assignments workflow for each atividade
    this.carregarDetalhesAtividades(id);

    this.carregando = false;
  }

  carregarPerguntas(salaId: string): void {
    this.carregandoForum = true;
    this.erroForum = '';
    this.forumService.getPerguntasBySala(salaId).subscribe({
      next: (perguntas) => {
        this.perguntas = perguntas;
        this.carregandoForum = false;
      },
      error: (err: any) => {
        console.error('Erro ao carregar perguntas:', err);
        this.erroForum = err.error?.message || err.message || 'Erro ao carregar perguntas';
        this.carregandoForum = false;
      }
    });
  }

  carregarSemanasEMateriais(salaId: string): void {
    this.carregandoMateriais = true;
    this.erroMateriais = '';
    this.materialService.getSemanasBySala(salaId).subscribe({
      next: (semanas) => {
        this.semanas = semanas;
        // Load materials for each week
        semanas.forEach(semana => {
          if (semana.id) {
            this.carregarMateriaisDaSemana(semana.id);
          }
        });
        this.carregandoMateriais = false;
      },
      error: (err: any) => {
        console.error('Erro ao carregar semanas:', err);
        this.erroMateriais = err.error?.message || err.message || 'Erro ao carregar semanas';
        this.carregandoMateriais = false;
      }
    });
  }

  carregarMateriaisDaSemana(semanaId: string): void {
    this.materialService.getMateriaisBySemana(semanaId).subscribe({
      next: (materiais) => {
        this.materiaisPorSemana[semanaId] = materiais;
      },
      error: (err: any) => {
        console.error('Erro ao carregar materiais da semana:', err);
      }
    });
  }

  adicionarComentario(): void {
    if (!this.novoComentario.trim() || !this.sala?.id) return;

    const comentario: Comentario = {
      conteudo: this.novoComentario,
      autorId: this.usuarioId,
      saladeAulaId: this.sala.id,
      tipoDestino: 'SALA'
    };
    console.log('Adicionando comentário na sala:', comentario);

    this.comentarioService.criar(comentario).subscribe({
      next: (c: Comentario) => {
        console.log('Comentário criado:', c);
        const novoComentario = {
          ...c,
          autorNome: c.autor?.nome || 'Você',
          autorId: c.autor?.id || this.usuarioId
        };
        this.comentarios.push(novoComentario);
        this.novoComentario = '';
        console.log('Comentários após adicionar:', this.comentarios);
      },
      error: (err: any) => {
        console.error('Erro ao adicionar comentário:', err);
      }
    });
  }

  deletarComentario(id: string): void {
    if (confirm('Tem certeza que deseja deletar este comentário?')) {
      this.comentarioService.deletar(id).subscribe({
        next: () => {
          this.comentarios = this.comentarios.filter(c => c.id !== id);
        },
        error: (err: any) => {
          console.error('Erro ao deletar comentário:', err);
        }
      });
    }
  }

  // Forum methods
  abrirNovaPergunta(): void {
    // This will be handled by a modal or by navigating to a new route
    // For simplicity, we'll just set a flag to show the form in the same component
    // But since we don't have a form for new question in the template yet, we'll implement later
    // For now, we'll just log
    console.log('Abrir nova pergunta');
    // TODO: Implement actual form for new question
  }

  criarPergunta(): void {
    // TODO: Implement creating a new question
    console.log('Criar pergunta');
  }

  abrirResposta(perguntaId: string): void {
    this.respostaSelecionadaId = perguntaId;
    this.novaRespostaConteudo = '';
  }

  cancelarResposta(): void {
    this.respostaSelecionadaId = null;
    this.novaRespostaConteudo = '';
  }

  enviarResposta(perguntaId: string): void {
    if (!this.novaRespostaConteudo.trim() || !this.sala?.id) return;

    const resposta: Resposta = {
      conteudo: this.novaRespostaConteudo,
      autorId: this.usuarioId,
      autorNome: this.getCurrentUserName(), // We'll need to get the user name
      dataCriacao: new Date().toISOString()
    };

    this.forumService.responderPergunta(perguntaId, resposta).subscribe({
      next: (respostaSalva) => {
        // Update the pergunta in the list with the new resposta
        const perguntaIndex = this.perguntas.findIndex(p => p.id === perguntaId);
        if (perguntaIndex !== -1) {
          if (!this.perguntas[perguntaIndex].respostas) {
            this.perguntas[perguntaIndex].respostas = [];
          }
          this.perguntas[perguntaIndex].respostas.push({
            ...respostaSalva,
            autorNome: this.getCurrentUserName()
          });
        }
        this.cancelarResposta();
      },
      error: (err: any) => {
        console.error('Erro ao responder pergunta:', err);
        // TODO: Show error to user
      }
    });
  }

  marcarRespostaComoAceita(perguntaId: string, respostaId: string): void {
    this.forumService.marcarRespostaComoAceita(perguntaId, respostaId).subscribe({
      next: () => {
        // Update the resposta in the list to mark as aceita
        const perguntaIndex = this.perguntas.findIndex(p => p.id === perguntaId);
        if (perguntaIndex !== -1 && this.perguntas[perguntaIndex].respostas) {
          const respostaIndex = this.perguntas[perguntaIndex].respostas.findIndex(r => r.id === respostaId);
          if (respostaIndex !== -1) {
            this.perguntas[perguntaIndex].respostas[respostaIndex].aceita = true;
          }
        }
      },
      error: (err: any) => {
        console.error('Erro ao marcar resposta como aceita:', err);
        // TODO: Show error to user
      }
    });
  }

  excluirPergunta(perguntaId: string): void {
    if (confirm('Tem certeza que deseja excluir esta pergunta?')) {
      this.forumService.excluirPergunta(perguntaId).subscribe({
        next: () => {
          this.perguntas = this.perguntas.filter(p => p.id !== perguntaId);
        },
        error: (err: any) => {
          console.error('Erro ao excluir pergunta:', err);
          // TODO: Show error to user
        }
      });
    }
  }

  excluirResposta(perguntaId: string, respostaId: string): void {
    if (confirm('Tem certeza que deseja excluir esta resposta?')) {
      this.forumService.excluirResposta(perguntaId, respostaId).subscribe({
        next: () => {
          const perguntaIndex = this.perguntas.findIndex(p => p.id === perguntaId);
          if (perguntaIndex !== -1 && this.perguntas[perguntaIndex].respostas) {
            this.perguntas[perguntaIndex].respostas = this.perguntas[perguntaIndex].respostas.filter(r => r.id !== respostaId);
          }
        },
        error: (err: any) => {
          console.error('Erro ao excluir resposta:', err);
          // TODO: Show error to user
        }
      });
    }
  }

  navegarAdicionarMembro(): void {
    if (this.sala?.id) {
      this.router.navigate([`/salas/${this.sala.id}/adicionar-membro`]);
    }
  }

  navegarCriarAtividade(): void {
    if (this.sala?.id) {
      this.router.navigate([`/salas/${this.sala.id}/atividades/criar`]);
    }
  }

  deletarSala(): void {
    if (!this.sala?.id || (!this.usuarioEhCriador && !this.isAdmin)) return;

    if (confirm(`Tem certeza que deseja deletar a sala "${this.sala.nome}"? Esta ação não pode ser desfeita.`)) {
      this.salaService.deletarSala(this.sala.id, this.usuarioId).subscribe({
        next: () => {
          alert('Sala deletada com sucesso!');
          this.router.navigate(['/salas']);
        },
        error: (err: any) => {
          alert('Erro ao deletar sala: ' + (err.message || 'Erro desconhecido'));
        }
      });
    }
  }

  copiarCodigoSala(): void {
    if (!this.sala?.codigoSala) return;
    if (isPlatformBrowser(this.platformId)) {
      navigator.clipboard.writeText(this.sala.codigoSala).then(() => {
        alert('Código da sala copiado: ' + this.sala!.codigoSala);
      }).catch(() => {
        alert('Código da sala: ' + this.sala!.codigoSala);
      });
    }
  }

  private getCurrentUserId(): string {
    return localStorage.getItem('usuarioId') || '';
  }

  private getCurrentUserName(): string {
    return localStorage.getItem('usuarioNome') || 'Usuário';
  }

  // Forum helper methods for template
  temRespostaAceita(pergunta: Pergunta): boolean {
    return pergunta.respostas ? pergunta.respostas.some(r => r.aceita) : false;
  }

  temRespostas(pergunta: Pergunta): boolean {
    return pergunta.respostas ? pergunta.respostas.length > 0 : false;
  }

  podeMarcarComoAceita(pergunta: Pergunta, resposta: Resposta): boolean {
    return this.usuarioId === pergunta.autorId && !resposta.aceita && (this.isProfessor || this.usuarioEhCriador || this.isAdmin);
  }

  podeResponder(pergunta: Pergunta): boolean {
    return this.usuarioId !== pergunta.autorId && !this.temRespostaAceita(pergunta);
  }

  // Material helper methods
  podeGerenciarMateriais(): boolean {
    return this.isProfessor || this.usuarioEhCriador || this.isAdmin;
  }

  selecionarSemana(semanaId: string | null): void {
    this.semanaSelecionadaId = this.semanaSelecionadaId === semanaId ? null : semanaId;
  }

  getMateriaisDaSemana(semanaId: string): Material[] {
    return this.materiaisPorSemana[semanaId] || [];
  }

  onArquivoSelecionado(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.arquivoSelecionado = input.files[0];
    }
  }

  criarSemana(): void {
    if (!this.sala?.id) return;

    const titulo = prompt('Título da semana/módulo:');
    if (!titulo) return;

    const descricao = prompt('Descrição (opcional):') || '';
    const numero = this.semanas.length + 1;

    const novaSemana: Semana = {
      titulo,
      descricao,
      numero,
      salaId: this.sala.id
    };

    this.materialService.criarSemana(novaSemana).subscribe({
      next: (semana) => {
        this.semanas.push(semana);
        this.semanas.sort((a, b) => a.numero - b.numero);
      },
      error: (err: any) => {
        console.error('Erro ao criar semana:', err);
        alert('Erro ao criar semana: ' + (err.error?.message || err.message));
      }
    });
  }

  criarMaterial(): void {
    if (!this.sala?.id || !this.novoMaterialSemanaId || !this.novoMaterialTitulo.trim()) {
      alert('Preencha todos os campos obrigatórios');
      return;
    }

    const material: Material = {
      titulo: this.novoMaterialTitulo,
      descricao: this.novoMaterialDescricao,
      tipo: this.novoMaterialTipo,
      url: this.novoMaterialTipo === 'LINK' || this.novoMaterialTipo === 'VIDEO' ? this.novoMaterialUrl : undefined,
      semanaId: this.novoMaterialSemanaId,
      salaId: this.sala.id,
      autorId: this.usuarioId,
      autorNome: this.getCurrentUserName()
    };

    if (this.arquivoSelecionado) {
      const uploadData = {
        arquivo: this.arquivoSelecionado,
        titulo: this.novoMaterialTitulo,
        descricao: this.novoMaterialDescricao,
        semanaId: this.novoMaterialSemanaId,
        salaId: this.sala.id
      };

      this.materialService.uploadMaterial(uploadData).subscribe({
        next: (materialSalvo) => {
          if (!this.materiaisPorSemana[this.novoMaterialSemanaId!]) {
            this.materiaisPorSemana[this.novoMaterialSemanaId!] = [];
          }
          this.materiaisPorSemana[this.novoMaterialSemanaId!].push(materialSalvo);
          this.limparFormularioMaterial();
        },
        error: (err: any) => {
          console.error('Erro ao fazer upload do material:', err);
          alert('Erro ao fazer upload: ' + (err.error?.message || err.message));
        }
      });
    } else {
      this.materialService.criarMaterial(material).subscribe({
        next: (materialSalvo) => {
          if (!this.materiaisPorSemana[this.novoMaterialSemanaId!]) {
            this.materiaisPorSemana[this.novoMaterialSemanaId!] = [];
          }
          this.materiaisPorSemana[this.novoMaterialSemanaId!].push(materialSalvo);
          this.limparFormularioMaterial();
        },
        error: (err: any) => {
          console.error('Erro ao criar material:', err);
          alert('Erro ao criar material: ' + (err.error?.message || err.message));
        }
      });
    }
  }

  excluirMaterial(materialId: string, semanaId: string): void {
    if (confirm('Tem certeza que deseja excluir este material?')) {
      this.materialService.excluirMaterial(materialId).subscribe({
        next: () => {
          this.materiaisPorSemana[semanaId] = this.materiaisPorSemana[semanaId].filter(m => m.id !== materialId);
        },
        error: (err: any) => {
          console.error('Erro ao excluir material:', err);
          alert('Erro ao excluir material: ' + (err.error?.message || err.message));
        }
      });
    }
  }

  excluirSemana(semanaId: string): void {
    if (confirm('Tem certeza que deseja excluir esta semana e todos os seus materiais?')) {
      this.materialService.excluirSemana(semanaId).subscribe({
        next: () => {
          this.semanas = this.semanas.filter(s => s.id !== semanaId);
          delete this.materiaisPorSemana[semanaId];
        },
        error: (err: any) => {
          console.error('Erro ao excluir semana:', err);
          alert('Erro ao excluir semana: ' + (err.error?.message || err.message));
        }
      });
    }
  }

  limparFormularioMaterial(): void {
    this.novoMaterialTitulo = '';
    this.novoMaterialDescricao = '';
    this.novoMaterialTipo = 'PDF';
    this.novoMaterialUrl = '';
    this.arquivoSelecionado = null;
    this.novoMaterialSemanaId = null;
  }

  getTipoIcon(tipo: string): string {
    switch (tipo) {
      case 'PDF': return '📄';
      case 'VIDEO': return '🎥';
      case 'LINK': return '🔗';
      case 'CODIGO': return '💻';
      case 'IMAGEM': return '🖼️';
      default: return '📎';
    }
  }

  abrirAdicionarMaterial(semanaId: string): void {
    this.novoMaterialSemanaId = semanaId;
  }

  visualizarMaterial(material: Material): void {
    if (material.tipo === 'PDF' && material.url) {
      window.open(material.url, '_blank');
    } else if (material.tipo === 'VIDEO' && material.url) {
      window.open(material.url, '_blank');
    } else if (material.tipo === 'LINK' && material.url) {
      window.open(material.url, '_blank');
    } else if (material.tipo === 'CODIGO' && material.url) {
      window.open(material.url, '_blank');
    } else if (material.tipo === 'IMAGEM' && material.url) {
      window.open(material.url, '_blank');
    } else if (material.arquivoId) {
      // For uploaded files, we'd need a download/view endpoint
      // Since we don't have environment injected, we'll use a relative path
      window.open(`/api/materiais/download/${material.arquivoId}`, '_blank');
    }
  }

  // Assignments workflow methods
  carregarDetalhesAtividades(salaId: string): void {
    if (!this.atividades || this.atividades.length === 0) return;

    this.atividades.forEach(atividade => {
      if (!atividade.id) return;
      if (!this.atividadesDetalhadas[atividade.id]) {
        this.atividadesDetalhadas[atividade.id] = {
          atividade: atividade,
          minhaSubmissao: null,
          submissoes: [],
          carregando: true,
          erro: '',
          enviandoSubmissao: false,
          urlDocumento: '',
          descricaoSubmissao: '',
          arquivosSubmissao: [],
          linksGitHub: [],
          nota: null,
          feedback: ''
        };
        this.carregarDetalhesAtividade(atividade.id);
      }
    });
  }

  carregarDetalhesAtividade(atividadeId: string | undefined): void {
    if (!atividadeId) return;
    const detalhe = this.atividadesDetalhadas[atividadeId];
    if (!detalhe) return;

    detalhe.carregando = true;
    detalhe.erro = '';

    // Load submission details
    this.submissaoService.buscarSubmissaoDoAluno(atividadeId, this.usuarioId).subscribe({
      next: (submissao) => {
        detalhe.minhaSubmissao = submissao;
        detalhe.urlDocumento = submissao.urlDocumento || '';
        detalhe.descricaoSubmissao = submissao.descricao || '';
        detalhe.nota = submissao.nota ?? null;
        detalhe.feedback = submissao.feedback || '';
        // Parse GitHub links from description if any
        if (detalhe.descricaoSubmissao) {
          const githubLinks = detalhe.descricaoSubmissao.match(/https:\/\/github\.com\/[^\s]+/g) || [];
          detalhe.linksGitHub = [...new Set(githubLinks)]; // Remove duplicates
        }
      },
      error: (err: any) => {
        console.error('Erro ao carregar submissão do aluno:', err);
        detalhe.erro = err.error?.message || err.message || 'Erro ao carregar submissão';
      }
    });

    // Load all submissions for this activity
    this.submissaoService.listarSubmissoesPorAtividade(atividadeId).subscribe({
      next: (submissoes) => {
        const detalhe = this.atividadesDetalhadas[atividadeId];
        if (detalhe) {
          detalhe.submissoes = submissoes;
          detalhe.carregando = false;
        }
      },
      error: (err: any) => {
        console.error('Erro ao listar submissões:', err);
        const detalhe = this.atividadesDetalhadas[atividadeId];
        if (detalhe) {
          detalhe.erro = err.error?.message || err.message || 'Erro ao listar submissões';
          detalhe.carregando = false;
        }
      }
    });
  }

  onArquivoSelecionadoParaAtividade(atividadeId: string, event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const detalhe = this.atividadesDetalhadas[atividadeId];
      if (detalhe) {
        detalhe.arquivosSubmissao = Array.from(input.files);
      }
    }
  }

  adicionarLinkGitHub(atividadeId: string): void {
    const link = prompt('Cole o link do GitHub:');
    if (link && link.trim()) {
      const detalhe = this.atividadesDetalhadas[atividadeId];
      if (detalhe) {
        if (!detalhe.linksGitHub.includes(link.trim())) {
          detalhe.linksGitHub.push(link.trim());
        }
      }
    }
  }

  removerLinkGitHub(atividadeId: string, index: number): void {
    const detalhe = this.atividadesDetalhadas[atividadeId];
    if (detalhe) {
      detalhe.linksGitHub.splice(index, 1);
    }
  }

  enviarSubmissaoAtividade(atividadeId: string): void {
    const detalhe = this.atividadesDetalhadas[atividadeId];
    if (!detalhe) return;

    if ((!detalhe.arquivosSubmissao || detalhe.arquivosSubmissao.length === 0) &&
      !detalhe.descricaoSubmissao.trim() &&
      (!detalhe.linksGitHub || detalhe.linksGitHub.length === 0)) {
      alert('Adicione uma descrição, anexe arquivos ou adicione links do GitHub');
      return;
    }

    detalhe.enviandoSubmissao = true;
    detalhe.erro = '';

    const formData = new FormData();
    if (detalhe.descricaoSubmissao.trim()) {
      formData.append('descricao', detalhe.descricaoSubmissao.trim());
    }
    if (detalhe.arquivosSubmissao && detalhe.arquivosSubmissao.length > 0) {
      detalhe.arquivosSubmissao.forEach(file => {
        formData.append('arquivos', file);
      });
    }
    if (detalhe.linksGitHub && detalhe.linksGitHub.length > 0) {
      // Store GitHub links in description for now
      const githubLinksText = detalhe.linksGitHub.join(' ');
      const currentDesc = formData.get('descricao') ? formData.get('descricao') as string : '';
      formData.set('descricao', (currentDesc + ' ' + githubLinksText).trim());
    }

    this.submissaoService.enviarSubmissaoComArquivo(atividadeId, this.usuarioId, formData).subscribe({
      next: (response) => {
        alert('Submissão enviada com sucesso!');
        detalhe.minhaSubmissao = response;
        detalhe.urlDocumento = response.urlDocumento || '';
        detalhe.descricaoSubmissao = response.descricao || '';
        detalhe.nota = response.nota ?? null;
        detalhe.feedback = response.feedback || '';
        detalhe.arquivosSubmissao = [];
        detalhe.linksGitHub = [];
        detalhe.enviandoSubmissao = false;
        detalhe.carregando = false;
        // Reload to get updated data
        this.carregarDetalhesAtividade(atividadeId);
      },
      error: (err: any) => {
        console.error('Erro ao enviar submissão:', err);
        detalhe.erro = err.error?.message || err.message || 'Erro ao enviar submissão';
        detalhe.enviandoSubmissao = false;
      }
    });
  }

  corrigirSubmissaoAtividade(submissaoId: string, nota: number, feedback: string): void {
    if (!confirm('Tem certeza que deseja corrigir esta submissão?')) return;

    this.submissaoService.corrigirSubmissao(submissaoId, nota, feedback).subscribe({
      next: (response) => {
        alert('Submissão corrigida com sucesso!');
        // Update the submission in the list
        const atividadeId = Object.keys(this.atividadesDetalhadas).find(id =>
          this.atividadesDetalhadas[id].minhaSubmissao?.id === submissaoId);
        if (atividadeId) {
          const detalhe = this.atividadesDetalhadas[atividadeId];
          if (detalhe) {
            detalhe.minhaSubmissao = response;
            detalhe.nota = response.nota ?? null;
            detalhe.feedback = response.feedback || '';
          }
        }
      },
      error: (err: any) => {
        console.error('Erro ao corrigir submissão:', err);
        alert('Erro ao corrigir submissão: ' + (err.error?.message || err.message));
      }
    });
  }

  podeEnviarSubmissao(atividadeId: string): boolean {
    const detalhe = this.atividadesDetalhadas[atividadeId];
    return detalhe && !detalhe.minhaSubmissao && this.usuarioId !== '';
  }

  podeCorrigirSubmissao(atividadeId: string): boolean {
    return this.isProfessor || this.isAdmin;
  }

  isPrazoFuturo(dataEntrega: string): boolean {
    if (!dataEntrega) return false;
    const data = new Date(dataEntrega);
    return data > new Date();
  }

  // Helper methods for template
  getIndex(i: number): number {
    return i;
  }

  getDetalheAtividade(atividadeId: string): any {
    return this.atividadesDetalhadas[atividadeId] || null;
  }
}
