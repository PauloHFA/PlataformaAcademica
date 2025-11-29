import { Component, OnInit, OnDestroy, PLATFORM_ID, Inject } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { AmizadeService } from '../../services/amizade.service';
import { UsuarioService } from '../../services/usuario.service';
import { Subject, interval } from 'rxjs';
import { takeUntil, switchMap } from 'rxjs/operators';

interface Notificacao {
  id: number;
  remetenteId: number;
  remetenteNome: string;
  tipo: 'AMIZADE';
  mensagem: string;
}

@Component({
  selector: 'app-notificacoes',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './notificacoes.component.html',
  styleUrl: './notificacoes.component.css'
})
export class NotificacoesComponent implements OnInit, OnDestroy {
  notificacoes: Notificacao[] = [];
  mostrarDropdown = false;
  currentUserId: number | null = null;
  private destroy$ = new Subject<void>();

  constructor(
    private amizadeService: AmizadeService,
    private usuarioService: UsuarioService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      const usuarioId = localStorage.getItem('usuarioId');
      this.currentUserId = usuarioId ? parseInt(usuarioId) : null;
      
      if (this.currentUserId) {
        this.carregarNotificacoes();
        // Atualizar notificações a cada 30 segundos
        interval(30000)
          .pipe(
            takeUntil(this.destroy$),
            switchMap(() => this.amizadeService.listarPendentes(this.currentUserId!))
          )
          .subscribe({
            next: (pendentes) => this.processarPendentes(pendentes),
            error: (err) => console.error('Erro ao atualizar notificações:', err)
          });
      }
    }
  }

  carregarNotificacoes(): void {
    if (!this.currentUserId) return;

    this.amizadeService.listarPendentes(this.currentUserId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (pendentes) => this.processarPendentes(pendentes),
        error: (err) => console.error('Erro ao carregar notificações:', err)
      });
  }

  private processarPendentes(pendentes: any[]): void {
    // Filtrar apenas solicitações onde o usuário atual é o destinatário
    this.notificacoes = pendentes
      .filter(p => p.destinatarioId === this.currentUserId)
      .map(p => ({
        id: p.id,
        remetenteId: p.solicitanteId,
        remetenteNome: p.solicitanteNome || 'Usuário',
        tipo: 'AMIZADE' as const,
        mensagem: `${p.solicitanteNome || 'Alguém'} enviou uma solicitação de amizade`
      }));
  }

  toggleDropdown(): void {
    this.mostrarDropdown = !this.mostrarDropdown;
  }

  aceitarAmizade(notificacao: Notificacao): void {
    this.amizadeService.aceitarSolicitacao(notificacao.id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
          this.notificacoes = this.notificacoes.filter(n => n.id !== notificacao.id);
        },
        error: (_err: any) => alert('Erro ao aceitar amizade')
      });
  }

  recusarAmizade(notificacao: Notificacao): void {
    this.amizadeService.recusarSolicitacao(notificacao.id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
          this.notificacoes = this.notificacoes.filter(n => n.id !== notificacao.id);
        },
        error: (_err: any) => alert('Erro ao recusar amizade')
      });
  }

  get totalNotificacoes(): number {
    return this.notificacoes.length;
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
