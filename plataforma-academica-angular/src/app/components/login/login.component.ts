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

    this.usuarioService.login(email, senha).subscribe({
      next: (resposta: LoginResponse) => {
        this.usuarioLogado = resposta;
        console.log('Login realizado com sucesso!', resposta);
        
        // Armazenar dados do usuário no localStorage (apenas no navegador)
        if (isPlatformBrowser(this.platformId)) {
          localStorage.setItem('usuario', JSON.stringify(resposta));
          localStorage.setItem('usuarioId', resposta.id.toString());
          localStorage.setItem('token', resposta.id.toString());
        }
        
        // Redirecionar para salas de aula após login
        this.router.navigate(['/salas']);
      },
      error: (erro) => {
        this.mensagemErro = erro.message || 'Email ou senha incorretos';
        this.carregando = false;
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
   * Login com Google (DEMO - sem OAuth real)
   */
  loginComGoogle(): void {
    // DEMO: Simula login com Google
    const demoUser = {
      id: 999,
      nome: 'Usuário Google (Demo)',
      email: 'google.demo@plataforma.com',
      provider: 'google'
    };
    
    if (isPlatformBrowser(this.platformId)) {
      localStorage.setItem('usuario', JSON.stringify(demoUser));
      localStorage.setItem('usuarioId', demoUser.id.toString());
      this.router.navigate(['/salas']);
    }
    
    // Para implementação real, veja: CONFIGURACAO_LOGIN_SOCIAL.md
  }

  /**
   * Login com Facebook (DEMO - sem OAuth real)
   */
  loginComFacebook(): void {
    // DEMO: Simula login com Facebook
    const demoUser = {
      id: 998,
      nome: 'Usuário Facebook (Demo)',
      email: 'facebook.demo@plataforma.com',
      provider: 'facebook'
    };
    
    if (isPlatformBrowser(this.platformId)) {
      localStorage.setItem('usuario', JSON.stringify(demoUser));
      localStorage.setItem('usuarioId', demoUser.id.toString());
      this.router.navigate(['/salas']);
    }
    
    // Para implementação real, veja: CONFIGURACAO_LOGIN_SOCIAL.md
  }
}
