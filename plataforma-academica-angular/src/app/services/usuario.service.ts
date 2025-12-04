import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Usuario, LoginRequest, LoginResponse } from '../models/usuario.model';

/**
 * Serviço de integração com API de Usuários do backend Spring Boot
 * Gerencia operações de cadastro, login, busca e listagem de usuários
 */
@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  private baseUrl = 'http://localhost:8080/api/usuarios'; // Endpoint do backend

  constructor(private http: HttpClient) { }

  /**
   * Cadastra um novo usuário na plataforma
   * @param usuario Dados do usuário a cadastrar (nome, email, senha, etc)
   * @param endpoint Tipo de usuário ('usuarios' ou 'professores')
   * @returns Observable com o usuário criado ou erro
   */
  cadastrarUsuario(usuario: Usuario, endpoint: string = 'usuarios'): Observable<Usuario> {
    const url = `http://localhost:8080/api/${endpoint}/cadastro`;
    return this.http.post<Usuario>(url, usuario)
      .pipe(catchError(this.tratarErro));
  }

  /**
   * Realiza o login de um usuário com email e senha
   * @param email Email do usuário
   * @param senha Senha do usuário
   * @returns Observable com dados do usuário autenticado ou erro
   */
  login(email: string, senha: string): Observable<LoginResponse> {
    const credenciais: LoginRequest = { email, senha };
    return this.http.post<LoginResponse>(`${this.baseUrl}/login`, credenciais)
      .pipe(catchError(this.tratarErro));
  }

  /**
   * Realiza o login de um professor com email e senha
   * @param email Email do professor
   * @param senha Senha do professor
   * @returns Observable com dados do professor autenticado ou erro
   */
  loginProfessor(email: string, senha: string): Observable<LoginResponse> {
    const credenciais: LoginRequest = { email, senha };
    return this.http.post<LoginResponse>('http://localhost:8080/api/professores/login', credenciais)
      .pipe(catchError(this.tratarErro));
  }

  /**
   * Busca um usuário específico pelo seu ID
   * @param id ID do usuário
   * @returns Observable com dados do usuário ou erro 404
   */
  buscarPorId(id: number): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.baseUrl}/buscarporid?id=${id}`)
      .pipe(catchError(this.tratarErro));
  }

  /**
   * Lista todos os usuários cadastrados (se houver endpoint público)
   * @returns Observable com lista de usuários
   */
  listarUsuarios(): Observable<Usuario[]> {
    console.log('Fazendo requisição para:', this.baseUrl);
    return this.http.get<Usuario[]>(this.baseUrl)
      .pipe(
        catchError((error) => {
          console.error('Erro na requisição de listar usuários:', error);
          return this.tratarErro(error);
        })
      );
  }

  /**
   * Tratamento centralizado de erros HTTP
   * @param erro Erro capturado
   * @returns Observable com erro formatado
   */
  private tratarErro(erro: HttpErrorResponse) {
    let mensagem = 'Erro desconhecido';

    // Verificar se é erro do cliente (apenas no navegador)
    if (typeof ErrorEvent !== 'undefined' && erro.error instanceof ErrorEvent) {
      mensagem = `Erro: ${erro.error.message}`;
    } else {
      // Erro do servidor
      if (erro.status === 0) {
        mensagem = 'Não foi possível conectar ao servidor. Verifique se o backend está rodando em http://localhost:8080';
      } else if (erro.status === 404) {
        mensagem = 'Endpoint não encontrado. Verifique se o backend está configurado corretamente.';
      } else if (erro.status === 500) {
        mensagem = 'Erro no servidor. Tente novamente mais tarde.';
      } else if (erro.error?.message) {
        mensagem = erro.error.message;
      } else {
        mensagem = `Erro ao processar requisição (${erro.status}). Tente novamente.`;
      }
    }

    console.error('Erro na requisição:', mensagem);
    return throwError(() => new Error(mensagem));
  }
}