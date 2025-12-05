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
      const usuarioId = localStorage.getItem('usuarioId');
      const usuarioStr = localStorage.getItem('usuario');

      if (usuarioId) {
        try {
          this.usuarioLogado = usuarioStr ? JSON.parse(usuarioStr) : {};
          this.usuarioLogado.id = parseInt(usuarioId);
          this.inicializarFormulario();
          this.verificarPerfilExistente();
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
   * Verifica se o usuário já possui um perfil existente
   */
  private verificarPerfilExistente(): void {
    this.perfilService.buscarPorUsuarioId(this.usuarioLogado.id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (perfil: Perfil) => {
          // Perfil existe, redirecionar para editar
          this.router.navigate(['/perfil/editar'], { queryParams: { id: perfil.id } });
        },
        error: (err: Error) => {
          // Perfil não existe, verificar modo (criar)
          this.verificarModo();
        }
      });
  }

  /**
   * Inicializa o formulário reativo
   */
  private inicializarFormulario(): void {
    this.formulario = this.formBuilder.group({
      nome: ['', Validators.required],
      sobrenome: [''],
      email: ['', [Validators.required, Validators.email]],
      instituicaoEnsino: [''],
      cep: [''],
      pais: [''],
      cidade: [''],
      site: [''],
      telefone: [''],
      dataNascimento: [''],
      descricao: [''],
      avatar: [''],
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
          this.preencherDadosUsuario();
          this.carregando = false;
        }
      });
  }

  /**
   * Preenche o formulário com dados do usuário logado
   */
  private preencherDadosUsuario(): void {
    if (this.usuarioLogado) {
      this.formulario.patchValue({
        nome: this.usuarioLogado.nome || '',
        sobrenome: this.usuarioLogado.sobrenome || '',
        email: this.usuarioLogado.email || '',
        instituicaoEnsino: this.usuarioLogado.instituicaoEnsino || '',
        cep: this.usuarioLogado.cep || '',
        pais: this.usuarioLogado.pais || '',
        cidade: this.usuarioLogado.cidade || '',
        site: this.usuarioLogado.site || '',
        telefone: this.usuarioLogado.telefone || '',
        dataNascimento: this.usuarioLogado.dataNascimento || '',
        descricao: this.usuarioLogado.descricao || '',
        avatar: this.usuarioLogado.avatarBase64 || this.usuarioLogado.avatarUrl || ''
      });
      // Desabilitar email para edição
      this.formulario.get('email')?.disable();
    }
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
            nome: perfil.nome || '',
            sobrenome: perfil.sobrenome || '',
            email: perfil.email || '',
            instituicaoEnsino: perfil.instituicaoEnsino || '',
            cep: perfil.cep || '',
            pais: perfil.pais || '',
            cidade: perfil.cidade || '',
            site: perfil.site || '',
            telefone: perfil.telefone || '',
            dataNascimento: perfil.dataNascimento || '',
            descricao: perfil.descricao || '',
            avatar: perfil.avatarUrl || perfil.avatarBase64 || '',
            bio: perfil.bio || '',
            curso: perfil.curso || '',
            fotoPerfil: perfil.fotoPerfil || ''
          });
          if (perfil.fotoPerfil) {
            this.previewFoto = perfil.fotoPerfil;
          }
          // Desabilitar email para edição
          this.formulario.get('email')?.disable();
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
      ...(this.modo === 'editar' && { id: this.perfilId }),
      usuarioId: this.usuarioLogado.id,
      nome: this.formulario.get('nome')?.value || undefined,
      sobrenome: this.formulario.get('sobrenome')?.value || undefined,
      email: this.formulario.get('email')?.value || undefined,
      instituicaoEnsino: this.formulario.get('instituicaoEnsino')?.value || undefined,
      cep: this.formulario.get('cep')?.value || undefined,
      pais: this.formulario.get('pais')?.value || undefined,
      cidade: this.formulario.get('cidade')?.value || undefined,
      site: this.formulario.get('site')?.value || undefined,
      telefone: this.formulario.get('telefone')?.value || undefined,
      dataNascimento: this.formulario.get('dataNascimento')?.value || undefined,
      descricao: this.formulario.get('descricao')?.value || undefined,
      avatar: this.formulario.get('avatar')?.value || undefined,
      bio: this.formulario.get('bio')?.value,
      curso: this.formulario.get('curso')?.value,
      fotoPerfil: this.formulario.get('fotoPerfil')?.value || undefined
    };

    const operacao$ = this.modo === 'criar'
      ? this.perfilService.criar(dto)
      : this.perfilService.atualizar(dto);

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
  get nome() {
    return this.formulario.get('nome');
  }

  get email() {
    return this.formulario.get('email');
  }

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
