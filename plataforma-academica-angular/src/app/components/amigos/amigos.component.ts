import { Component, OnInit, PLATFORM_ID, Inject } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AmizadeService } from '../../services/amizade.service';
import { UsuarioService } from '../../services/usuario.service';
import { Amizade } from '../../models/amizade.model';
import { Usuario } from '../../models/usuario.model';

@Component({
  selector: 'app-amigos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './amigos.component.html',
  styleUrl: './amigos.component.css'
})
export class AmigosComponent implements OnInit {
  currentUserId: number | null = null;
  amigos: Amizade[] = [];
  pendentes: Amizade[] = [];
  usuarios: Usuario[] = [];
  usuariosFiltrados: Usuario[] = [];
  busca = '';
  carregando = true;
  abaAtiva: 'amigos' | 'pendentes' | 'adicionar' = 'amigos';

  constructor(
    private amizadeService: AmizadeService,
    private usuarioService: UsuarioService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit(): void {
    this.currentUserId = this.getCurrentUserId();
    if (this.currentUserId) {
      this.carregarDados();
    }
  }

  getCurrentUserId(): number | null {
    if (isPlatformBrowser(this.platformId)) {
      const id = localStorage.getItem('usuarioId');
      return id ? Number(id) : null;
    }
    return null;
  }

  carregarDados(): void {
    if (!this.currentUserId) return;
    
    this.carregando = true;
    this.amizadeService.listarAmigos(this.currentUserId).subscribe({
      next: (amigos) => {
        this.amigos = amigos;
        this.carregando = false;
      },
      error: () => {
        this.carregando = false;
      }
    });

    this.amizadeService.listarPendentes(this.currentUserId).subscribe({
      next: (pendentes) => {
        this.pendentes = pendentes;
      },
      error: () => {}
    });
  }

  carregarUsuarios(): void {
    this.usuarioService.listarUsuarios().subscribe({
      next: (usuarios) => {
        this.usuarios = usuarios.filter(u => u.id !== this.currentUserId);
        this.usuariosFiltrados = this.usuarios;
      },
      error: () => {}
    });
  }

  filtrarUsuarios(): void {
    const termo = this.busca.toLowerCase().trim();
    if (!termo) {
      this.usuariosFiltrados = this.usuarios;
    } else {
      this.usuariosFiltrados = this.usuarios.filter(u =>
        u.nome?.toLowerCase().includes(termo) ||
        u.email?.toLowerCase().includes(termo)
      );
    }
  }

  mudarAba(aba: 'amigos' | 'pendentes' | 'adicionar'): void {
    this.abaAtiva = aba;
    if (aba === 'adicionar' && !this.usuarios.length) {
      this.carregarUsuarios();
    }
  }

  enviarSolicitacao(destinatarioId: number): void {
    if (!this.currentUserId) return;

    this.amizadeService.enviarSolicitacao(this.currentUserId, destinatarioId).subscribe({
      next: () => {
        alert('Solicitação enviada!');
        this.carregarDados();
      },
      error: () => {
        alert('Erro ao enviar solicitação');
      }
    });
  }

  responder(id: number, acao: 'aceitar' | 'recusar'): void {
    this.amizadeService.responderSolicitacao(id, acao).subscribe({
      next: () => {
        this.carregarDados();
      },
      error: () => {
        alert('Erro ao responder solicitação');
      }
    });
  }

  remover(id: number): void {
    if (!confirm('Deseja remover esta amizade?')) return;

    this.amizadeService.removerAmizade(id).subscribe({
      next: () => {
        this.carregarDados();
      },
      error: () => {
        alert('Erro ao remover amizade');
      }
    });
  }

  getAmigoNome(amizade: Amizade): string {
    return amizade.solicitanteId === this.currentUserId
      ? amizade.destinatarioNome || 'Usuário'
      : amizade.solicitanteNome || 'Usuário';
  }

  isRecebida(amizade: Amizade): boolean {
    return amizade.destinatarioId === this.currentUserId;
  }
}
