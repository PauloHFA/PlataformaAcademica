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
  formularioEtapa2!: FormGroup;
  carregando = false;
  mensagemErro = '';
  mensagemSucesso = '';
  usuarioCadastrado: Usuario | null = null;
  etapaAtual = 1;
  dadosEtapa1: any = null;

  constructor(
    private formBuilder: FormBuilder,
    private usuarioService: UsuarioService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.inicializarFormulario();
    this.inicializarFormularioEtapa2();
  }

  /**
   * Inicializa o formulário reativo com validações
   */
  private inicializarFormulario(): void {
    this.formulario = this.formBuilder.group({
      nome: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      senha: ['', [Validators.required, Validators.minLength(6)]],
      confirmarSenha: ['', Validators.required],
      tipoUsuario: ['normal', Validators.required],
      matricula: ['']
    }, { validators: senhasIguaisValidator });

    // Adiciona validação condicional para matrícula
    this.formulario.get('tipoUsuario')?.valueChanges.subscribe(tipo => {
      const matriculaControl = this.formulario.get('matricula');
      if (tipo === 'professor') {
        matriculaControl?.setValidators([Validators.required, Validators.minLength(8), Validators.maxLength(8)]);
      } else {
        matriculaControl?.clearValidators();
      }
      matriculaControl?.updateValueAndValidity();
    });
  }

  private inicializarFormularioEtapa2(): void {
    this.formularioEtapa2 = this.formBuilder.group({
      sobrenome: ['', Validators.required],
      dataNascimento: ['', Validators.required],
      telefone: ['', Validators.required],
      instituicaoEnsino: ['', Validators.required],
      cep: ['', Validators.required],
      pais: ['', Validators.required],
      cidade: ['', Validators.required],
      descricao: [''],
      site: ['']
    });
  }

  /**
   * Processa o envio do formulário de cadastro
   */
  avancarParaEtapa2(): void {
    if (this.formulario.invalid) {
      this.mensagemErro = 'Por favor, preencha todos os campos corretamente';
      return;
    }
    this.dadosEtapa1 = this.formulario.value;
    this.etapaAtual = 2;
    this.mensagemErro = '';
  }

  voltarParaEtapa1(): void {
    this.etapaAtual = 1;
    this.mensagemErro = '';
  }

  cadastrar(): void {
    if (this.formularioEtapa2.invalid) {
      this.mensagemErro = 'Por favor, preencha todos os campos obrigatórios';
      return;
    }

    this.carregando = true;
    this.mensagemErro = '';
    this.mensagemSucesso = '';

    const dadosCompletos: any = {
      ...this.dadosEtapa1,
      ...this.formularioEtapa2.value
    };
    delete dadosCompletos.confirmarSenha;
    const tipoUsuario = dadosCompletos.tipoUsuario;
    delete dadosCompletos.tipoUsuario;

    const endpoint = tipoUsuario === 'professor' ? 'professores' : 'usuarios';
    
    this.usuarioService.cadastrarUsuario(dadosCompletos, endpoint).subscribe({
      next: (usuarioCadastrado: Usuario) => {
        this.usuarioCadastrado = usuarioCadastrado;
        this.mensagemSucesso = 'Cadastro realizado com sucesso! Redirecionando para login...';
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

  get tipoUsuario() {
    return this.formulario.get('tipoUsuario');
  }

  get matricula() {
    return this.formulario.get('matricula');
  }

  cadastroComGoogle(): void {
    // Desabilitado
  }

  cadastroComFacebook(): void {
    // Desabilitado
  }
}
