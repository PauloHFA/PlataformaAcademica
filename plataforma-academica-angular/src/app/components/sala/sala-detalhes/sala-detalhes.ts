import { Component, OnInit, PLATFORM_ID, Inject } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { ActivatedRoute, RouterModule, Router } from '@angular/router';
import { SalaService } from '../../../services/sala.service';
import { SalaDeAula } from '../../../models/sala.model';
import { Atividade } from '../../../models/atividade.model';

@Component({
  selector: 'app-sala-detalhes',
  standalone: true,
  imports: [CommonModule, RouterModule],
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

  constructor(
    private route: ActivatedRoute,
    private salaService: SalaService,
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.usuarioId = this.getCurrentUserId();
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
    this.carregando = true;
    this.erro = '';

    this.salaService.buscarPorId(id).subscribe({
      next: (s) => {
        this.sala = s;
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

    this.carregando = false;
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

  private getCurrentUserId(): number {
    const usuarioIdStr = localStorage.getItem('usuarioId');
    return usuarioIdStr ? parseInt(usuarioIdStr, 10) : 0;
  }
}
