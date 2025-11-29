import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SalaService } from '../../../services/sala.service';
import { SubmissaoAtividadeService } from '../../../services/submissao-atividade.service';
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
  
  // Form de submissão
  urlDocumento = '';
  enviandoSubmissao = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private salaService: SalaService,
    private submissaoService: SubmissaoAtividadeService
  ) {}

  ngOnInit(): void {
    this.currentUserId = this.getCurrentUserId();
    this.atividadeId = Number(this.route.snapshot.paramMap.get('atividadeId'));
    this.salaId = Number(this.route.snapshot.paramMap.get('id'));
    
    if (this.atividadeId) {
      this.carregarAtividade();
      this.carregarSubmissoes();
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
