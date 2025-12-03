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
  comentarios: Comentario[] = [];
  novoComentario = '';
  usuarioId: number | null = null;
  carregandoComentarios = false;

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
    if (!this.salaId) { this.carregando = false; return; }
    this.salaService.buscarPorId(this.salaId).subscribe({
      next: (s) => this.salaContext.setNomeSala(s.nome),
      error: () => {}
    });
    this.salaService.listarAtividades(this.salaId).subscribe({
      next: (a) => { this.atividades = a || []; this.carregando = false; },
      error: (err: Error) => { console.warn('Erro ao listar atividades', err); this.carregando = false; }
    });
    this.carregarComentarios();
  }

  carregarComentarios(): void {
    if (!this.salaId) return;
    console.log('Carregando comentários de atividades gerais da sala:', this.salaId);
    this.carregandoComentarios = true;
    this.comentarioService.listarAtividadesGerais(this.salaId).subscribe({
      next: (c: Comentario[]) => { 
        console.log('Comentários recebidos:', c);
        this.comentarios = c || [];
        this.carregandoComentarios = false; 
      },
      error: (err: any) => { 
        console.error('Erro ao carregar comentários:', err); 
        this.carregandoComentarios = false; 
      }
    });
  }

  adicionarComentario(): void {
    if (!this.novoComentario.trim() || !this.salaId || !this.usuarioId) return;
    const comentario: any = {
      conteudo: this.novoComentario,
      autor: { id: this.usuarioId },
      saladeAula: { id: this.salaId },
      tipoDestino: 'ATIVIDADES_GERAIS'
    };
    console.log('Enviando comentário:', comentario);
    this.comentarioService.criar(comentario).subscribe({
      next: (c) => { 
        console.log('Comentário criado:', c);
        this.novoComentario = ''; 
        this.carregarComentarios(); 
      },
      error: (err: any) => {
        console.error('Erro ao adicionar comentário:', err);
        alert('Erro ao adicionar comentário: ' + (err.error?.message || err.message));
      }
    });
  }

  deletarComentario(id: number): void {
    this.comentarioService.deletar(id).subscribe({
      next: () => this.carregarComentarios(),
      error: (err: Error) => console.warn('Erro ao deletar comentário', err)
    });
  }
}
