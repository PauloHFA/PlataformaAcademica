import { Component, OnInit, OnDestroy, PLATFORM_ID, Inject } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { UsuarioService } from '../../../services/usuario.service';
import { AmizadeService } from '../../../services/amizade.service';
import { Usuario } from '../../../models/usuario.model';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

@Component({
  selector: 'app-usuario-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './usuario-list.html',
  styleUrl: './usuario-list.css'
})
export class UsuarioListComponent implements OnInit, OnDestroy {
  usuarios: Usuario[] = [];
  carregando = false;
  mensagemErro = '';
  currentUserId: number | null = null;
  amigosIds: Set<number> = new Set();
  pendentesIds: Set<number> = new Set();
  private destroy$ = new Subject<void>();

  constructor(
    private usuarioService: UsuarioService,
    private amizadeService: AmizadeService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) { }

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      const usuarioId = localStorage.getItem('usuarioId');
      this.currentUserId = usuarioId ? parseInt(usuarioId) : null;
    }
    this.carregarDados();
  }

  carregarDados(): void {
    if (!this.currentUserId) return;
    
    this.carregarUsuarios();
    this.carregarAmigos();
    this.carregarPendentes();
  }

  listarUsuarios(): void {
    this.carregarDados();
  }

  carregarUsuarios(): void {
    this.carregando = true;
    this.mensagemErro = '';
    this.usuarios = [];

    this.usuarioService.listarUsuarios()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (data: Usuario[]) => {
          this.usuarios = data || [];
          this.carregando = false;
        },
        error: (err: Error) => {
          console.error('Erro ao listar usuários', err);
          if (err.message.includes('conectar ao servidor') || err.message.includes('localhost:8080')) {
            this.mensagemErro = 'O backend não está respondendo. Verifique se o servidor Spring Boot está rodando e acessível em http://localhost:8080';
          } else {
            this.mensagemErro = err.message || 'Erro ao carregar a lista de usuários. Tente novamente mais tarde.';
          }
          this.carregando = false;
          this.usuarios = [];
        }
      });
  }

  carregarAmigos(): void {
    if (!this.currentUserId) return;
    
    this.amizadeService.listarAmigos(this.currentUserId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (amigos) => {
          this.amigosIds.clear();
          amigos.forEach(a => {
            if (a.solicitanteId === this.currentUserId) {
              this.amigosIds.add(a.destinatarioId);
            } else {
              this.amigosIds.add(a.solicitanteId);
            }
          });
        },
        error: () => {}
      });
  }

  carregarPendentes(): void {
    if (!this.currentUserId) return;
    
    this.amizadeService.listarPendentes(this.currentUserId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (pendentes) => {
          this.pendentesIds.clear();
          pendentes.forEach(p => {
            if (p.solicitanteId === this.currentUserId) {
              this.pendentesIds.add(p.destinatarioId);
            } else {
              this.pendentesIds.add(p.solicitanteId);
            }
          });
        },
        error: () => {}
      });
  }

  temRelacao(usuarioId: number): boolean {
    return this.amigosIds.has(usuarioId) || this.pendentesIds.has(usuarioId);
  }

  getStatusBotao(usuarioId: number): string {
    if (this.amigosIds.has(usuarioId)) return 'Amigo';
    if (this.pendentesIds.has(usuarioId)) return 'Pendente';
    return 'Adicionar';
  }

  trackByUsuarioId(index: number, usuario: Usuario): any {
    return usuario.id || index;
  }

  solicitarAmizade(amigoId: number): void {
    if (!this.currentUserId) {
      alert('Você precisa estar logado');
      return;
    }

    if (this.temRelacao(amigoId)) {
      alert('Você já tem uma relação com este usuário');
      return;
    }

    this.amizadeService.enviarSolicitacao(this.currentUserId, amigoId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
          alert('Solicitação de amizade enviada!');
          this.pendentesIds.add(amigoId);
        },
        error: (err) => {
          const mensagem = err.error?.error || err.error?.message || 'Erro ao enviar solicitacao';
          alert(mensagem);
        }
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
