import { Component, OnInit, PLATFORM_ID, Inject } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { ActivatedRoute, RouterModule, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SalaService } from '../../../services/sala.service';
import { SalaContextService } from '../../../services/sala-context.service';
import { ComentarioService, Comentario } from '../../../services/comentario.service';
import { SalaDeAula } from '../../../models/sala.model';
import { Atividade } from '../../../models/atividade.model';

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
  usuarioId = 0;
  usuarioEhCriador = false;
  animando = false;
  comentarios: Comentario[] = [];
  novoComentario = '';
  isProfessor = false;

  constructor(
    private route: ActivatedRoute,
    private salaService: SalaService,
    private router: Router,
    private salaContext: SalaContextService,
    private comentarioService: ComentarioService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.usuarioId = this.getCurrentUserId();
      const isProfessorValue = localStorage.getItem('isProfessor');
      this.isProfessor = isProfessorValue === 'true';
    }

    this.route.paramMap.subscribe(params => {
      const id = Number(params.get('id'));
      if (!id) {
        this.erro = 'ID da sala inválido';
        this.carregando = false;
        return;
      }
      this.carregarDados(id);
    });
  }

  carregarDados(id: number): void {
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
            autorId: comentario.autor?.id || comentario.autorId || 0
          }));
        console.log('Comentários da sala', id, 'processados:', this.comentarios);
      },
      error: (err: any) => {
        console.warn('Erro ao listar comentários da sala', id, ':', err);
      }
    });

    this.carregando = false;
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

  deletarComentario(id: number): void {
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
    if (!this.sala?.id || !this.usuarioEhCriador) return;
    
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

  private getCurrentUserId(): number {
    const usuarioIdStr = localStorage.getItem('usuarioId');
    return usuarioIdStr ? parseInt(usuarioIdStr, 10) : 0;
  }
}
