import { Component, OnInit, OnDestroy, PLATFORM_ID, Inject } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PerfilService } from '../../services/perfil.service';
import { BadgeService } from '../../services/badge.service';
import { Perfil } from '../../models/perfil.model';
import { Badge } from '../../models/badge.model';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

/**
 * Componente para visualizar o perfil de um usuário
 * Exibe informações como bio, curso e foto de perfil
 */
@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './perfil.component.html',
  styleUrl: './perfil.component.css'
})
export class PerfilComponent implements OnInit, OnDestroy {
  perfil: Perfil | null = null;
  carregando = true;
  mensagemErro = '';
  usuarioLogado: any = null;
  hasProfile = false;
  isEditing = false;
  salvando = false;
  dadosPerfil = {
    nomeCompleto: '',
    nomeExibicao: '',
    bio: '',
    papel: 'Aluno',
    cursoDepartamento: '',
    matricula: '',
    github: '',
    linkedin: '',
    lattes: ''
  };
  badges: Badge[] = [];
  private destroy$ = new Subject<void>();

  constructor(
    private perfilService: PerfilService,
    private badgeService: BadgeService,
    private route: ActivatedRoute,
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object
  ) { }

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

    // Carregar perfil e badges em paralelo
    this.perfilService.buscarPorUsuarioId(this.usuarioLogado.id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (perfil: Perfil) => {
          this.perfil = perfil;
          this.hasProfile = true;
          this.isEditing = false;
          this.preencherDadosPerfil(perfil);
          // Carregar badges do usuário
          this.carregarBadgesUsuario(this.usuarioLogado.id);
          this.carregando = false;
        },
        error: (err: Error) => {
          console.error('Erro ao carregar perfil:', err);
          // Mensagem mais amigável para erro 404 (perfil não encontrado)
          if (err.message.includes('não encontrado') || err.message.includes('404')) {
            this.hasProfile = false;
            this.mensagemErro = '';
          } else {
            this.mensagemErro = err.message || 'Erro ao carregar o perfil. Tente novamente mais tarde.';
          }
          this.carregando = false;
        }
      });
  }

  /**
   * Carrega os badges do usuário
   */
  private carregarBadgesUsuario(usuarioId: string): void {
    this.badgeService.buscarBadgesDoUsuario(usuarioId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (badges: Badge[]) => {
          this.badges = badges;
        },
        error: (err: Error) => {
          console.error('Erro ao carregar badges:', err);
          // Não interrompe o fluxo se falhar ao carregar badges
          this.badges = [];
        }
      });
  }

  /**
   * Preenche os dados do perfil para edição
   */
  private preencherDadosPerfil(perfil: Perfil): void {
    this.dadosPerfil = {
      nomeCompleto: `${perfil.nome || ''} ${perfil.sobrenome || ''}`.trim(),
      nomeExibicao: perfil.nome || '',
      bio: perfil.bio || perfil.descricao || '',
      papel: perfil.tipoUsuario?.toLowerCase().includes('prof') ? 'Professor' : 'Aluno',
      cursoDepartamento: perfil.curso || perfil.instituicaoEnsino || '',
      matricula: perfil.matricula || '',
      github: '',
      linkedin: '',
      lattes: ''
    };
  }

  /**
   * Navega para o formulário de edição do perfil
   */
  editarPerfil(): void {
    this.isEditing = true;
  }

  salvarPerfil(): void {
    if (!this.usuarioLogado?.id) return;
    this.salvando = true;
    const partesNome = this.dadosPerfil.nomeCompleto.trim().split(/\\s+/);
    const dto = {
      id: this.perfil?.id,
      usuarioId: this.usuarioLogado.id,
      nome: partesNome.shift() || this.dadosPerfil.nomeExibicao,
      sobrenome: partesNome.join(' '),
      bio: this.dadosPerfil.bio,
      curso: this.dadosPerfil.cursoDepartamento
    };
    const request$ = this.hasProfile ? this.perfilService.atualizar(dto) : this.perfilService.criar(dto);
    request$.pipe(takeUntil(this.destroy$)).subscribe({
      next: (perfil) => {
        this.perfil = perfil;
        this.hasProfile = true;
        this.isEditing = false;
        this.salvando = false;
        this.preencherDadosPerfil(perfil);
      },
      error: (err: Error) => {
        this.mensagemErro = err.message || 'Não foi possível salvar o perfil.';
        this.salvando = false;
      }
    });
  }

  cancelarEdicao(): void {
    if (this.perfil) this.preencherDadosPerfil(this.perfil);
    this.isEditing = false;
  }

  getAvatarSrc(): string {
    if (this.perfil?.fotoPerfil) {
      return 'data:image/png;base64,' + this.perfil.fotoPerfil;
    }
    if (this.usuarioLogado?.avatarUrl) {
      return this.usuarioLogado.avatarUrl;
    }
    if (this.usuarioLogado?.avatarBase64) {
      return 'data:image/png;base64,' + this.usuarioLogado.avatarBase64;
    }
    return '';
  }

  /**
   * Limpa recursos ao destruir o componente
   */
  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
