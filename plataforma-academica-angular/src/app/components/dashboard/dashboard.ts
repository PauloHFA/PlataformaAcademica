import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { ActivatedRoute } from '@angular/router';
import { DashboardService } from '../../services/dashboard.service';
import { DashboardAluno } from '../../models/dashboard-aluno.model';
import { DashboardSala, AlunoDashboardResumo } from '../../models/dashboard-sala.model';
import { Frequencia } from '../../models/frequencia.model';
import { SalaContextService } from '../../services/sala-context.service';
import { NotificationService } from '../../services/notification.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, MatIconModule, MatButtonModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class DashboardComponent implements OnInit, OnDestroy {
  dashboard: DashboardAluno | null = null;
  dashboardSala: DashboardSala | null = null;
  alunosResumo: AlunoDashboardResumo[] = [];
  alunoSelecionadoId: number | null = null;
  frequencias: Frequencia[] = [];
  erro: string = '';
  carregando = true;
  salaId = 0;
  isProfessor = false;
  dataInicio: string = '';
  dataFim: string = '';

  // Dados para gráficos (simplificado)
  // chartNotasData: any;
  // chartFrequenciaData: any;
  // chartOptions: any = {
  //   responsive: true,
  //   plugins: {
  //     legend: {
  //       position: 'top',
  //     },
  //   },
  // };

  // Notificações
  notificacoes: string[] = [];
  private notificationSub: Subscription | null = null;

  constructor(
    private route: ActivatedRoute,
    private dashboardService: DashboardService,
    private salaContext: SalaContextService,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    this.isProfessor = localStorage.getItem('isProfessor') === 'true';

    this.route.paramMap.subscribe(params => {
      this.salaId = Number(params.get('id'));
      this.carregarDashboard();
    });

    // Inscrever nas notificações
    const usuarioId = Number(localStorage.getItem('usuarioId') || '0');
    if (usuarioId) {
      this.notificationSub = this.notificationService.getNotificacoesUsuario(usuarioId).subscribe((mensagem: string) => {
        this.notificacoes.push(mensagem);
        // Auto-remover após 5 segundos
        setTimeout(() => {
          this.notificacoes.shift();
        }, 5000);
      });
    }
  }

  ngOnDestroy(): void {
    if (this.notificationSub) {
      this.notificationSub.unsubscribe();
    }
  }

  carregarDashboard(): void {
    this.carregando = true;
    this.erro = '';
    const inicio = this.dataInicio ? this.dataInicio : undefined;
    const fim = this.dataFim ? this.dataFim : undefined;

    if (this.isProfessor) {
      this.dashboard = null;
      this.frequencias = [];
      this.carregarDashboardSala(inicio, fim);
      if (this.alunoSelecionadoId) {
        this.carregarDashboardAluno(this.alunoSelecionadoId, inicio, fim);
      } else {
        this.carregando = false;
      }
      return;
    }

    const alunoId = Number(localStorage.getItem('usuarioId') || '0');
    if (!alunoId || !this.salaId) {
      this.erro = 'Aluno ou sala inválidos.';
      this.carregando = false;
      return;
    }

    this.carregarDashboardAluno(alunoId, inicio, fim);
  }

  carregarDashboardSala(inicio?: string, fim?: string): void {
    if (!this.salaId) {
      this.erro = 'Sala inválida.';
      this.carregando = false;
      return;
    }

    this.dashboardService.getDashboardSala(this.salaId, inicio, fim).subscribe({
      next: (data) => {
        this.dashboardSala = data;
        this.alunosResumo = data.alunos || [];
        this.carregando = false;
      },
      error: (err) => {
        this.erro = err.error?.message || err.message || 'Erro ao carregar dados da sala';
        this.carregando = false;
      }
    });
  }

  carregarDashboardAluno(alunoId: number, inicio?: string, fim?: string): void {
    if (!alunoId || !this.salaId) {
      this.erro = 'Aluno ou sala inválidos.';
      this.carregando = false;
      return;
    }

    this.carregando = true;
    this.dashboardService.getDashboardAluno(alunoId, this.salaId, inicio, fim).subscribe({
      next: (data) => {
        this.dashboard = data;
        this.carregando = false;
        this.carregarFrequencias(alunoId, inicio, fim);
      },
      error: (err) => {
        this.erro = err.error?.message || err.message || 'Erro ao carregar dashboard';
        this.carregando = false;
      }
    });
  }

  carregarFrequencias(alunoId: number, inicio?: string, fim?: string): void {
    this.dashboardService.getFrequencias(alunoId, this.salaId, inicio, fim).subscribe({
      next: (freqs) => {
        this.frequencias = freqs;
      },
      error: (err) => {
        console.error('Erro ao carregar frequências:', err);
      }
    });
  }

  limparFiltros(): void {
    this.dataInicio = '';
    this.dataFim = '';
    this.carregarDashboard();
  }
}
