import { Component, OnInit, PLATFORM_ID, Inject } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SubmissaoAtividadeService } from '../../services/submissao-atividade.service';
import { SubmissaoAtividade } from '../../models/submissao-atividade.model';

@Component({
  selector: 'app-submissoes-atividade',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './submissoes-atividade.component.html',
  styleUrl: './submissoes-atividade.component.css'
})
export class SubmissoesAtividadeComponent implements OnInit {
  submissoes: SubmissaoAtividade[] = [];
  carregando = true;
  atividadeId: number | null = null;
  salaId: number | null = null;
  submissaoSelecionada: SubmissaoAtividade | null = null;
  nota: number | null = null;
  feedback: string = '';

  constructor(
    private route: ActivatedRoute,
    private submissaoService: SubmissaoAtividadeService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.salaId = Number(params.get('id'));
      this.atividadeId = Number(params.get('atividadeId'));
      if (this.atividadeId) {
        this.carregarSubmissoes();
      }
    });
  }

  carregarSubmissoes(): void {
    if (!this.atividadeId) return;
    this.carregando = true;
    this.submissaoService.listarPorAtividade(this.atividadeId).subscribe({
      next: (data) => {
        this.submissoes = data;
        this.carregando = false;
      },
      error: (err) => {
        console.error('Erro ao carregar submissões:', err);
        this.carregando = false;
      }
    });
  }

  selecionarSubmissao(submissao: SubmissaoAtividade): void {
    this.submissaoSelecionada = submissao;
    this.nota = submissao.nota || null;
    this.feedback = submissao.feedback || '';
  }

  fecharModal(): void {
    this.submissaoSelecionada = null;
    this.nota = null;
    this.feedback = '';
  }

  salvarAvaliacao(): void {
    if (!this.submissaoSelecionada?.id || this.nota === null) return;
    
    this.submissaoService.corrigirSubmissao(this.submissaoSelecionada.id, this.nota, this.feedback).subscribe({
      next: () => {
        alert('Avaliação salva com sucesso!');
        this.fecharModal();
        this.carregarSubmissoes();
      },
      error: (err) => {
        alert('Erro ao salvar avaliação: ' + (err.error || err.message));
      }
    });
  }

  marcarComoRecebida(submissaoId: number): void {
    this.submissaoService.marcarComoRecebida(submissaoId).subscribe({
      next: () => {
        alert('Submissão marcada como recebida!');
        this.carregarSubmissoes();
      },
      error: (err) => {
        alert('Erro: ' + (err.error || err.message));
      }
    });
  }

  getDocumentoUrl(url: string): string {
    if (!url) return '';
    if (url.startsWith('http')) return url;
    return 'http://localhost:8080' + url;
  }
}
