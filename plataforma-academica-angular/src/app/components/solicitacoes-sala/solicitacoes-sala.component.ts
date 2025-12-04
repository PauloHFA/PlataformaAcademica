import { Component, OnInit, PLATFORM_ID, Inject } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { SolicitacaoEntradaService } from '../../services/solicitacao-entrada.service';
import { SolicitacaoEntrada } from '../../models/solicitacao-entrada.model';

@Component({
  selector: 'app-solicitacoes-sala',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './solicitacoes-sala.component.html',
  styleUrl: './solicitacoes-sala.component.css'
})
export class SolicitacoesSalaComponent implements OnInit {
  solicitacoes: SolicitacaoEntrada[] = [];
  carregando = true;
  salaId: number | null = null;
  professorId: number | null = null;

  constructor(
    private route: ActivatedRoute,
    private solicitacaoService: SolicitacaoEntradaService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      const uid = localStorage.getItem('usuarioId');
      this.professorId = uid ? parseInt(uid, 10) : null;
    }

    this.route.paramMap.subscribe(params => {
      const id = Number(params.get('id'));
      if (!isNaN(id)) {
        this.salaId = id;
        this.carregarSolicitacoes();
      }
    });
  }

  carregarSolicitacoes(): void {
    if (!this.salaId) return;
    this.carregando = true;
    this.solicitacaoService.listarPendentes(this.salaId).subscribe({
      next: (data) => {
        this.solicitacoes = data;
        this.carregando = false;
      },
      error: (err) => {
        console.error('Erro ao carregar solicitações:', err);
        this.carregando = false;
      }
    });
  }

  aprovar(solicitacaoId: number): void {
    if (!this.professorId) return;
    this.solicitacaoService.aprovar(solicitacaoId, this.professorId).subscribe({
      next: () => {
        this.carregarSolicitacoes();
      },
      error: (err) => {
        alert('Erro ao aprovar: ' + (err.error || err.message));
      }
    });
  }

  rejeitar(solicitacaoId: number): void {
    if (!this.professorId) return;
    this.solicitacaoService.rejeitar(solicitacaoId, this.professorId).subscribe({
      next: () => {
        this.carregarSolicitacoes();
      },
      error: (err) => {
        alert('Erro ao rejeitar: ' + (err.error || err.message));
      }
    });
  }
}
