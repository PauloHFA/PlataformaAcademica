import { Component, OnInit, OnDestroy, PLATFORM_ID, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { PerfilService } from '../../services/perfil.service';
import { UsuarioService } from '../../services/usuario.service';
import { Perfil, PerfilDTO } from '../../models/perfil.model';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

/**
 * Componente para criar/editar o perfil de um usuário
 * Contém formulário com campos para bio, curso e foto de perfil
 */
@Component({
  selector: 'app-perfil-editar',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './perfil-editar.component.html',
  styleUrl: './perfil-editar.component.css'
})
export class PerfilEditarComponent implements OnInit, OnDestroy {
  formulario!: FormGroup;
  carregando = true;
  carregandoSalvar = false;
  mensagemErro = '';
  mensagemSucesso = '';
  modo: 'criar' | 'editar' = 'criar';
  usuarioLogado: any = null;
  perfilId?: number;
  previewFoto: string | null = null;
  private destroy$ = new Subject<void>();

  constructor(
    private formBuilder: FormBuilder,
    private perfilService: PerfilService,
    private usuarioService: UsuarioService,
    private route: ActivatedRoute,
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      const usuarioStr = localStorage.getItem('usuario');
      if (usuarioStr) {
        try {
          this.usuarioLogado = JSON.parse(usuarioStr);
          this.inicializarFormulario();
          this.verificarModo();
        } catch (e) {
          this.mensagemErro = 'Erro ao carregar dados do usuário';
          this.carregando = false;
        }
      } else {
        this.mensagemErro = 'Você precisa estar logado';
        this.carregando = false;
      }
    }
  }

  /**
   * Inicializa o formulário reativo
   */
  private inicializarFormulario(): void {
    this.formulario = this.formBuilder.group({
      bio: ['', [Validators.required, Validators.minLength(10)]],
      curso: ['', Validators.required],
      fotoPerfil: ['']
    });
  }

  /**
   * Verifica se está criando ou editando perfil
   */
  private verificarModo(): void {
    this.route.queryParams
      .pipe(takeUntil(this.destroy$))
      .subscribe(params => {
        if (params['id']) {
          this.perfilId = params['id'];
          this.modo = 'editar';
          this.carregarPerfil();
        } else {
          this.modo = 'criar';
          this.carregando = false;
        }
      });
  }

  /**
   * Carrega dados do perfil existente
   */
  private carregarPerfil(): void {
    if (!this.perfilId) return;

    this.perfilService.buscarPorId(this.perfilId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (perfil: Perfil) => {
          this.formulario.patchValue({
            bio: perfil.bio,
            curso: perfil.curso,
            fotoPerfil: perfil.fotoPerfil || ''
          });
          if (perfil.fotoPerfil) {
            this.previewFoto = perfil.fotoPerfil;
          }
          this.carregando = false;
        },
        error: (err: Error) => {
          this.mensagemErro = 'Erro ao carregar perfil para edição';
          this.carregando = false;
        }
      });
  }

  /**
   * Processa envio do formulário
   */
  salvarPerfil(): void {
    if (this.formulario.invalid) {
      this.mensagemErro = 'Por favor, preencha todos os campos obrigatórios corretamente';
      return;
    }

    this.carregandoSalvar = true;
    this.mensagemErro = '';
    this.mensagemSucesso = '';

    const dto: PerfilDTO = {
      usuarioId: this.usuarioLogado.id,
      bio: this.formulario.get('bio')?.value,
      curso: this.formulario.get('curso')?.value,
      fotoPerfil: this.formulario.get('fotoPerfil')?.value || undefined
    };

    const operacao$ = this.modo === 'criar'
      ? this.perfilService.criar(dto)
      : this.perfilService.atualizar(this.perfilId!, dto);

    operacao$
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (perfil: Perfil) => {
          this.mensagemSucesso = `Perfil ${this.modo === 'criar' ? 'criado' : 'atualizado'} com sucesso!`;
          this.carregandoSalvar = false;

          // Redirecionar para visualizar perfil após 2 segundos
          setTimeout(() => {
            this.router.navigate(['/perfil']);
          }, 2000);
        },
        error: (err: Error) => {
          this.mensagemErro = err.message || `Erro ao ${this.modo === 'criar' ? 'criar' : 'atualizar'} perfil`;
          this.carregandoSalvar = false;
        }
      });
  }

  /**
   * Processa upload de foto
   */
  onFotoSelecionada(event: any): void {
    const arquivo = event.target.files[0];
    if (arquivo) {
      const leitor = new FileReader();
      leitor.onload = (e: any) => {
        this.previewFoto = e.target.result;
        this.formulario.patchValue({ fotoPerfil: e.target.result });
      };
      leitor.readAsDataURL(arquivo);
    }
  }

  /**
   * Getters para validação
   */
  get bio() {
    return this.formulario.get('bio');
  }

  get curso() {
    return this.formulario.get('curso');
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
