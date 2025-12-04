import { Component, OnInit, PLATFORM_ID, Inject } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
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
  descricaoSubmissao = '';
  arquivoSubmissao: File | null = null;
  enviandoSubmissao = false;
  
  comentarios: Comentario[] = [];
  novoComentario = '';
  usuarioId = 0;
  isProfessor = false;
  usuarioEhCriadorDaSala = false;

  getDocumentoTipo(url: string): 'pdf' | 'image' | 'video' | 'other' {
    const ext = url.split('.').pop()?.toLowerCase();
    if (ext === 'pdf') return 'pdf';
    if (['jpg', 'jpeg', 'png', 'gif', 'webp'].includes(ext || '')) return 'image';
    if (['mp4', 'webm', 'ogg'].includes(ext || '')) return 'video';
    return 'other';
  }

  getDocumentoPreviewUrl(url: string): string {
    const fullUrl = url.startsWith('http://localhost:8080') ? url : 'http://localhost:8080' + url;
    console.log('Preview URL:', fullUrl);
    return fullUrl;
  }

  getSafeUrl(url: string): SafeResourceUrl {
    return this.sanitizer.bypassSecurityTrustResourceUrl(this.getDocumentoPreviewUrl(url));
  }

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private salaService: SalaService,
    private submissaoService: SubmissaoAtividadeService,
    private comentarioService: ComentarioService,
    private salaContext: SalaContextService,
    private sanitizer: DomSanitizer,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      const usuarioIdStr = localStorage.getItem('usuarioId');
      this.usuarioId = usuarioIdStr ? parseInt(usuarioIdStr, 10) : 0;
      this.isProfessor = localStorage.getItem('isProfessor') === 'true';
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
      next: (s) => {
        this.salaContext.setNomeSala(s.nome);
        this.usuarioEhCriadorDaSala = s.criadorId === this.currentUserId;
      },
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
        this.urlDocumento = submissao.urlDocumento || '';
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
    if (!this.novoComentario.trim() || !this.atividadeId || !this.usuarioId) return;

    const comentario: any = {
      conteudo: this.novoComentario,
      autor: { id: this.usuarioId },
      atividade: { id: this.atividadeId },
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

  onArquivoSelecionado(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.arquivoSubmissao = file;
    }
  }

  enviarSubmissao(): void {
    if (!this.atividadeId || !this.currentUserId) {
      alert('Dados inválidos');
      return;
    }

    if (!this.arquivoSubmissao && !this.descricaoSubmissao.trim()) {
      alert('Adicione uma descrição ou anexe um arquivo');
      return;
    }

    this.enviandoSubmissao = true;
    
    const formData = new FormData();
    if (this.descricaoSubmissao.trim()) {
      formData.append('descricao', this.descricaoSubmissao.trim());
    }
    if (this.arquivoSubmissao) {
      formData.append('arquivo', this.arquivoSubmissao);
    }

    this.submissaoService.enviarSubmissaoComArquivo(this.atividadeId, this.currentUserId, formData).subscribe({
      next: (response) => {
        alert('Submissão enviada com sucesso!');
        this.minhaSubmissao = response;
        this.descricaoSubmissao = '';
        this.arquivoSubmissao = null;
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
