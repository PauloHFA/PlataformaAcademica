import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, AbstractControl } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { UsuarioService } from '../../services/usuario.service';
import { Usuario } from '../../models/usuario.model';

/**
 * Validador customizado para verificar se as senhas são iguais
 */
function senhasIguaisValidator(control: AbstractControl): { [key: string]: boolean } | null {
  const senha = control.get('senha');
  const confirmarSenha = control.get('confirmarSenha');

  if (!senha || !confirmarSenha) {
    return null;
  }

  return senha.value === confirmarSenha.value ? null : { senhasNaoIguais: true };
}

/**
 * Componente de Cadastro de Usuários
 * Permite que novos usuários se registrem na plataforma
 */
@Component({
  selector: 'app-cadastro',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './cadastro.component.html',
  styleUrl: './cadastro.component.css'
})
export class CadastroComponent implements OnInit {
  formulario!: FormGroup;
  carregando = false;
  mensagemErro = '';
  mensagemSucesso = '';
  usuarioCadastrado: Usuario | null = null;

  constructor(
    private formBuilder: FormBuilder,
    private usuarioService: UsuarioService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.inicializarFormulario();
  }

  /**
   * Inicializa o formulário reativo com validações
   */
  private inicializarFormulario(): void {
    this.formulario = this.formBuilder.group({
      nome: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      senha: ['', [Validators.required, Validators.minLength(6)]],
      confirmarSenha: ['', Validators.required]
    }, { validators: senhasIguaisValidator });
  }

  /**
   * Processa o envio do formulário de cadastro
   */
  cadastrar(): void {
    if (this.formulario.invalid) {
      this.mensagemErro = 'Por favor, preencha todos os campos corretamente';
      return;
    }

    this.carregando = true;
    this.mensagemErro = '';
    this.mensagemSucesso = '';

    const { nome, email, senha } = this.formulario.value;
    const novoUsuario: Usuario = { nome, email, senha };

    this.usuarioService.cadastrarUsuario(novoUsuario).subscribe({
      next: (usuarioCadastrado: Usuario) => {
        this.usuarioCadastrado = usuarioCadastrado;
        this.mensagemSucesso = 'Cadastro realizado com sucesso! Redirecionando para login...';
        console.log('Usuário cadastrado:', usuarioCadastrado);

        // Redirecionar para login após 2 segundos
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 2000);
      },
      error: (erro) => {
        this.mensagemErro = erro.message || 'Erro ao realizar cadastro. Email pode já estar em uso.';
        this.carregando = false;
      }
    });
  }

  /**
   * Getters para validação dos campos
   */
  get nome() {
    return this.formulario.get('nome');
  }

  get email() {
    return this.formulario.get('email');
  }

  get senha() {
    return this.formulario.get('senha');
  }

  get confirmarSenha() {
    return this.formulario.get('confirmarSenha');
  }

  get senhasNaoIguais() {
    return this.formulario.hasError('senhasNaoIguais');
  }
}
