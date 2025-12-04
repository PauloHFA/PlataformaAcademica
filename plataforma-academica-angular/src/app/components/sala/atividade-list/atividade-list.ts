import { Component, OnInit, PLATFORM_ID, Inject } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SalaService } from '../../../services/sala.service';
import { ComentarioService } from '../../../services/comentario.service';
import { SalaContextService } from '../../../services/sala-context.service';
import { Atividade } from '../../../models/atividade.model';
import { Comentario } from '../../../models/comentario.model';

@Component({
  selector: 'app-atividade-list',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './atividade-list.html',
  styleUrl: './atividade-list.css'
})
export class AtividadeListComponent implements OnInit {
  atividades: Atividade[] = [];
  carregando = true;
  salaId: number | null = null;
  comentariosPorAtividade: { [atividadeId: number]: Comentario[] } = {};
  novoComentarioPorAtividade: { [atividadeId: number]: string } = {};
  usuarioId: number | null = null;
  atividadeExpandida: number | null = null;
  isProfessor = false;

  constructor(
    private route: ActivatedRoute,
    private salaService: SalaService,
    private comentarioService: ComentarioService,
    private salaContext: SalaContextService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.salaId = isNaN(id) ? null : id;
    if (isPlatformBrowser(this.platformId)) {
      const uid = localStorage.getItem('usuarioId');
      this.usuarioId = uid ? parseInt(uid, 10) : null;
    }
  }

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.isProfessor = localStorage.getItem('isProfessor') === 'true';
    }
    if (!this.salaId) { this.carregando = false; return; }
    this.salaService.buscarPorId(this.salaId).subscribe({
      next: (s) => this.salaContext.setNomeSala(s.nome),
      error: () => {}
    });
    this.salaService.listarAtividades(this.salaId).subscribe({
      next: (a) => { 
        this.atividades = a || [];
        this.atividades.forEach(ativ => this.carregarComentarios(ativ.id!));
        this.carregando = false;
      },
      error: (err: Error) => { console.warn('Erro ao listar atividades', err); this.carregando = false; }
    });
  }

  carregarComentarios(atividadeId: number): void {
    this.comentarioService.listarPorAtividade(atividadeId).subscribe({
      next: (c: Comentario[]) => { 
        this.comentariosPorAtividade[atividadeId] = c || [];
      },
      error: (err: any) => { 
        console.error('Erro ao carregar comentários da atividade', atividadeId, err);
      }
    });
  }

  adicionarComentario(atividadeId: number): void {
    const texto = this.novoComentarioPorAtividade[atividadeId];
    if (!texto || !texto.trim() || !this.usuarioId) return;
    const comentario: any = {
      conteudo: texto,
      autor: { id: this.usuarioId },
      atividade: { id: atividadeId },
      tipoDestino: 'ATIVIDADE'
    };
    this.comentarioService.criar(comentario).subscribe({
      next: () => { 
        this.novoComentarioPorAtividade[atividadeId] = '';
        this.carregarComentarios(atividadeId);
      },
      error: (err: any) => {
        console.error('Erro ao adicionar comentário:', err);
      }
    });
  }

  deletarComentario(id: number, atividadeId: number): void {
    this.comentarioService.deletar(id).subscribe({
      next: () => this.carregarComentarios(atividadeId),
      error: (err: Error) => console.warn('Erro ao deletar comentário', err)
    });
  }

  toggleComentarios(atividadeId: number): void {
    this.atividadeExpandida = this.atividadeExpandida === atividadeId ? null : atividadeId;
  }

  getComentarios(atividadeId: number): Comentario[] {
    return this.comentariosPorAtividade[atividadeId] || [];
  }
}
