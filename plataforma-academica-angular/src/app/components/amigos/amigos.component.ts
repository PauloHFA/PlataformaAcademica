import { Component, OnInit, PLATFORM_ID, Inject } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
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
    private router: Router,
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
        this.usuarios = usuarios.filter(u => {
          if (u.id === this.currentUserId) return false;
          return !this.jaTemRelacao(u.id!);
        });
        this.usuariosFiltrados = this.usuarios;
      },
      error: () => {}
    });
  }

  jaTemRelacao(usuarioId: number): boolean {
    const jaAmigo = this.amigos.some(a => 
      a.solicitanteId === usuarioId || a.destinatarioId === usuarioId
    );
    const jaPendente = this.pendentes.some(p => 
      p.solicitanteId === usuarioId || p.destinatarioId === usuarioId
    );
    return jaAmigo || jaPendente;
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
    if (aba === 'adicionar') {
      this.carregarDados();
      setTimeout(() => this.carregarUsuarios(), 500);
    }
  }

  irParaUsuarios(): void {
    this.router.navigate(['/usuarios']);
  }

  enviarSolicitacao(destinatarioId: number): void {
    if (!this.currentUserId) return;

    console.log('Enviando:', { solicitanteId: this.currentUserId, destinatarioId });
    this.amizadeService.enviarSolicitacao(this.currentUserId, destinatarioId).subscribe({
      next: (res) => {
        console.log('Sucesso:', res);
        alert('Solicitação enviada!');
        this.carregarDados();
        setTimeout(() => this.carregarUsuarios(), 500);
      },
      error: (err) => {
        console.error('Erro:', err);
        const mensagem = err.error?.error || err.error?.message || 'Erro ao enviar solicitação';
        alert(mensagem);
      }
    });
  }

  responder(id: number, acao: 'aceitar' | 'recusar'): void {
    this.amizadeService.responderSolicitacao(id, acao).subscribe({
      next: () => {
        this.carregarDados();
        if (this.usuarios.length) {
          setTimeout(() => this.carregarUsuarios(), 500);
        }
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
        if (this.usuarios.length) {
          setTimeout(() => this.carregarUsuarios(), 500);
        }
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
