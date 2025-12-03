import { Component, OnInit, PLATFORM_ID, Inject } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SalaService } from '../../../services/sala.service';
import { SubmissaoAtividadeService } from '../../../services/submissao-atividade.service';
import { ComentarioService, Comentario } from '../../../services/comentario.service';
import { SalaContextService } from '../../../services/sala-context.service';
import { Atividade } from '../../../models/atividade.model';
import { SubmissaoAtividadeResponse } from '../../../models/submissao-atividade.model';

@Component({
  selector: 'app-atividade-detalhes',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './atividade-detalhes.component.html',
  styleUrl: './atividade-detalhes.component.css'
})
export class AtividadeDetalhesComponent implements OnInit {
  atividade: Atividade | null = null;
  submissoes: SubmissaoAtividadeResponse[] = [];
  minhaSubmissao: SubmissaoAtividadeResponse | null = null;
  carregando = true;
  atividadeId: number | null = null;
  salaId: number | null = null;
  currentUserId: number | null = null;
  
  urlDocumento = '';
  enviandoSubmissao = false;
  
  comentarios: Comentario[] = [];
  novoComentario = '';
  usuarioId = 0;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private salaService: SalaService,
    private submissaoService: SubmissaoAtividadeService,
    private comentarioService: ComentarioService,
    private salaContext: SalaContextService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      const usuarioIdStr = localStorage.getItem('usuarioId');
      this.usuarioId = usuarioIdStr ? parseInt(usuarioIdStr, 10) : 0;
    }
    
    this.currentUserId = this.getCurrentUserId();
    this.atividadeId = Number(this.route.snapshot.paramMap.get('atividadeId'));
    this.salaId = Number(this.route.snapshot.paramMap.get('id'));
    
    if (this.atividadeId) {
      this.carregarAtividade();
      this.carregarSubmissoes();
      this.carregarComentarios();
      if (this.currentUserId) {
        this.carregarMinhaSubmissao();
      }
    }
  }

  getCurrentUserId(): number | null {
    const id = localStorage.getItem('usuarioId');
    return id ? Number(id) : null;
  }

  carregarAtividade(): void {
    if (!this.salaId || !this.atividadeId) return;
    
    this.salaService.buscarPorId(this.salaId).subscribe({
      next: (s) => this.salaContext.setNomeSala(s.nome),
      error: () => {}
    });
    
    this.salaService.listarAtividades(this.salaId).subscribe({
      next: (atividades) => {
        this.atividade = atividades.find(a => a.id === this.atividadeId) || null;
        this.carregando = false;
      },
      error: () => {
        this.carregando = false;
      }
    });
  }

  carregarSubmissoes(): void {
    if (!this.atividadeId) return;
    
    this.submissaoService.listarSubmissoesPorAtividade(this.atividadeId).subscribe({
      next: (submissoes) => {
        this.submissoes = submissoes;
      },
      error: () => {}
    });
  }

  carregarMinhaSubmissao(): void {
    if (!this.atividadeId || !this.currentUserId) return;
    
    this.submissaoService.buscarSubmissaoDoAluno(this.atividadeId, this.currentUserId).subscribe({
      next: (submissao) => {
        this.minhaSubmissao = submissao;
        this.urlDocumento = submissao.urlDocumento;
      },
      error: () => {}
    });
  }

  carregarComentarios(): void {
    if (!this.atividadeId) return;
    
    console.log('Carregando comentários da atividade:', this.atividadeId);
    this.comentarioService.listarPorAtividade(this.atividadeId).subscribe({
      next: (c: Comentario[]) => {
        console.log('Comentários da atividade', this.atividadeId, 'carregados:', c);
        this.comentarios = (c || []).map((comentario: Comentario) => ({
          ...comentario,
          autorNome: comentario.autor?.nome || 'Usuário',
          autorId: comentario.autor?.id || comentario.autorId || 0
        }));
        console.log('Comentários processados:', this.comentarios);
      },
      error: (err: any) => console.error('Erro ao listar comentários da atividade', this.atividadeId, ':', err)
    });
  }

  adicionarComentario(): void {
    if (!this.novoComentario.trim() || !this.atividadeId) return;

    const comentario: Comentario = {
      conteudo: this.novoComentario,
      autorId: this.usuarioId,
      atividadeId: this.atividadeId,
      tipoDestino: 'ATIVIDADE'
    };
    console.log('Adicionando comentário na atividade:', comentario);

    this.comentarioService.criar(comentario).subscribe({
      next: (c: Comentario) => {
        console.log('Comentário criado na atividade:', c);
        const novoComentario = {
          ...c,
          autorNome: c.autor?.nome || 'Você',
          autorId: c.autor?.id || this.usuarioId
        };
        this.comentarios.push(novoComentario);
        this.novoComentario = '';
        console.log('Comentários após adicionar:', this.comentarios);
      },
      error: (err: any) => console.error('Erro ao adicionar comentário na atividade:', err)
    });
  }

  deletarComentario(id: number): void {
    if (confirm('Tem certeza que deseja deletar este comentário?')) {
      this.comentarioService.deletar(id).subscribe({
        next: () => {
          this.comentarios = this.comentarios.filter(c => c.id !== id);
        },
        error: () => console.error('Erro ao deletar comentário')
      });
    }
  }

  enviarSubmissao(): void {
    if (!this.atividadeId || !this.currentUserId || !this.urlDocumento.trim()) {
      alert('Preencha a URL do documento');
      return;
    }

    this.enviandoSubmissao = true;
    
    const submissao = {
      atividadeId: this.atividadeId,
      alunoId: this.currentUserId,
      urlDocumento: this.urlDocumento.trim()
    };

    this.submissaoService.enviarSubmissao(this.atividadeId, this.currentUserId, submissao).subscribe({
      next: (response) => {
        alert('Submissão enviada com sucesso!');
        this.minhaSubmissao = response;
        this.carregarSubmissoes();
        this.enviandoSubmissao = false;
      },
      error: (err) => {
        alert('Erro ao enviar submissão: ' + (err.error?.message || 'Erro desconhecido'));
        this.enviandoSubmissao = false;
      }
    });
  }

  voltar(): void {
    if (this.salaId) {
      this.router.navigate(['/salas', this.salaId, 'atividades']);
    } else {
      this.router.navigate(['/salas']);
    }
  }
}
