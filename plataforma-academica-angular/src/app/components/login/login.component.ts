import { Component, OnInit, PLATFORM_ID, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { UsuarioService } from '../../services/usuario.service';
import { LoginResponse } from '../../models/usuario.model';

/**
 * Componente de Login da plataforma
 * Permite que usuários se autentiquem com email e senha
 */
@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {
  formulario!: FormGroup;
  carregando = false;
  mensagemErro = '';
  usuarioLogado: LoginResponse | null = null;

  constructor(
    private formBuilder: FormBuilder,
    private usuarioService: UsuarioService,
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit(): void {
    this.inicializarFormulario();
  }

  /**
   * Inicializa o formulário reativo com validações
   */
  private inicializarFormulario(): void {
    this.formulario = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      senha: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  /**
   * Processa o envio do formulário de login
   */
  fazerLogin(): void {
    if (this.formulario.invalid) {
      this.mensagemErro = 'Por favor, preença todos os campos corretamente';
      return;
    }

    this.carregando = true;
    this.mensagemErro = '';

    const { email, senha } = this.formulario.value;

    // Tenta login como professor primeiro
    this.usuarioService.loginProfessor(email, senha).subscribe({
      next: (resposta: LoginResponse) => {
        console.log('Login como PROFESSOR bem-sucedido');
        this.salvarUsuarioENavegar(resposta);
      },
      error: () => {
        // Se falhar, tenta login como usuário normal
        this.usuarioService.login(email, senha).subscribe({
          next: (resposta: LoginResponse) => {
            console.log('Login como USUARIO bem-sucedido');
            this.salvarUsuarioENavegar(resposta);
          },
          error: (erro) => {
            this.mensagemErro = erro.message || 'Email ou senha incorretos';
            this.carregando = false;
          }
        });
      }
    });
  }

  /**
   * Getter para validação do campo email
   */
  get email() {
    return this.formulario.get('email');
  }

  /**
   * Getter para validação do campo senha
   */
  get senha() {
    return this.formulario.get('senha');
  }

  /**
   * Login com Google (Desabilitado)
   */
  loginComGoogle(): void {
    // Desabilitado - não fazer nada
  }

  /**
   * Login com Facebook (Desabilitado)
   */
  loginComFacebook(): void {
    // Desabilitado - não fazer nada
  }

  /**
   * Salva usuário no localStorage e navega para feed
   */
  private salvarUsuarioENavegar(resposta: any): void {
    this.usuarioLogado = resposta;
    console.log('Login realizado com sucesso!', resposta);
    console.log('Matricula presente?', resposta.matricula);
    console.log('isProfessor será:', resposta.matricula ? 'true' : 'false');
    
    if (isPlatformBrowser(this.platformId)) {
      localStorage.setItem('usuario', JSON.stringify(resposta));
      localStorage.setItem('usuarioId', resposta.id.toString());
      localStorage.setItem('token', resposta.id.toString());
      localStorage.setItem('isProfessor', resposta.matricula ? 'true' : 'false');
      console.log('localStorage isProfessor:', localStorage.getItem('isProfessor'));
    }
    
    this.router.navigate(['/feed']);
  }
}
