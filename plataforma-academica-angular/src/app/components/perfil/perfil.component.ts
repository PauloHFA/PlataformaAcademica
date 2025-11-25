import { Component, OnInit, OnDestroy, PLATFORM_ID, Inject } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { PerfilService } from '../../services/perfil.service';
import { Perfil } from '../../models/perfil.model';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

/**
 * Componente para visualizar o perfil de um usuário
 * Exibe informações como bio, curso e foto de perfil
 */
@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './perfil.component.html',
  styleUrl: './perfil.component.css'
})
export class PerfilComponent implements OnInit, OnDestroy {
  perfil: Perfil | null = null;
  carregando = true;
  mensagemErro = '';
  usuarioLogado: any = null;
  private destroy$ = new Subject<void>();

  constructor(
    private perfilService: PerfilService,
    private route: ActivatedRoute,
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit(): void {
    // Obter usuário logado do localStorage
    if (isPlatformBrowser(this.platformId)) {
      const usuarioStr = localStorage.getItem('usuario');
      if (usuarioStr) {
        try {
          this.usuarioLogado = JSON.parse(usuarioStr);
          this.carregarPerfil();
        } catch (e) {
          this.mensagemErro = 'Erro ao carregar dados do usuário logado';
          this.carregando = false;
        }
      } else {
        this.mensagemErro = 'Você precisa estar logado para visualizar seu perfil';
        this.carregando = false;
      }
    }
  }

  /**
   * Carrega o perfil do usuário logado
   */
  private carregarPerfil(): void {
    if (!this.usuarioLogado?.id) {
      this.mensagemErro = 'ID do usuário não encontrado';
      this.carregando = false;
      return;
    }

    this.perfilService.buscarPorUsuarioId(this.usuarioLogado.id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (perfil: Perfil) => {
          this.perfil = perfil;
          this.carregando = false;
        },
        error: (err: Error) => {
          console.error('Erro ao carregar perfil:', err);
          // Mensagem mais amigável para erro 404 (perfil não encontrado)
          if (err.message.includes('não encontrado') || err.message.includes('404')) {
            this.mensagemErro = 'Você ainda não possui um perfil cadastrado. Clique no botão abaixo para criar seu perfil e personalizar suas informações!';
          } else {
            this.mensagemErro = err.message || 'Erro ao carregar o perfil. Tente novamente mais tarde.';
          }
          this.carregando = false;
        }
      });
  }

  /**
   * Navega para o formulário de edição do perfil
   */
  editarPerfil(): void {
    if (this.perfil) {
      this.router.navigate(['/perfil-editar'], { queryParams: { id: this.perfil.id } });
    } else {
      this.router.navigate(['/perfil-criar']);
    }
  }

  /**
   * Limpa recursos ao destruir o componente
   */
  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
