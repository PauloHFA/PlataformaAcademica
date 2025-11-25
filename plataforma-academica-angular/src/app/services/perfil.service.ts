import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Perfil, PerfilDTO } from '../models/perfil.model';

/**
 * Serviço de integração com API de Perfis do backend Spring Boot
 * Gerencia operações CRUD de perfis de usuários
 */
@Injectable({
  providedIn: 'root'
})
export class PerfilService {
  private baseUrl = 'http://localhost:8080/api/perfis';

  constructor(private http: HttpClient) { }

  /**
   * Lista todos os perfis cadastrados
   * @returns Observable com lista de perfis
   */
  listarTodos(): Observable<Perfil[]> {
    return this.http.get<Perfil[]>(this.baseUrl)
      .pipe(catchError(this.tratarErro));
  }

  /**
   * Cria um novo perfil para um usuário
   * @param dto Dados do perfil (bio, curso, fotoPerfil, usuarioId)
   * @returns Observable com o perfil criado
   */
  criar(dto: PerfilDTO): Observable<Perfil> {
    return this.http.post<Perfil>(this.baseUrl, dto)
      .pipe(catchError(this.tratarErro));
  }

  /**
   * Busca um perfil específico pelo ID
   * @param id ID do perfil
   * @returns Observable com o perfil ou erro 404
   */
  buscarPorId(id: number): Observable<Perfil> {
    return this.http.get<Perfil>(`${this.baseUrl}/${id}`)
      .pipe(catchError(this.tratarErro));
  }

  /**
   * Atualiza um perfil existente
   * @param id ID do perfil
   * @param dto Novos dados do perfil
   * @returns Observable com o perfil atualizado
   */
  atualizar(id: number, dto: PerfilDTO): Observable<Perfil> {
    return this.http.put<Perfil>(`${this.baseUrl}/${id}`, dto)
      .pipe(catchError(this.tratarErro));
  }

  /**
   * Busca perfis por curso específico
   * @param curso Nome do curso
   * @returns Observable com lista de perfis do curso
   */
  buscarPorCurso(curso: string): Observable<Perfil[]> {
    return this.http.get<Perfil[]>(`${this.baseUrl}/curso/${curso}`)
      .pipe(catchError(this.tratarErro));
  }

  /**
   * Busca o perfil de um usuário pelo ID do usuário
   * @param usuarioId ID do usuário
   * @returns Observable com o perfil do usuário
   */
  buscarPorUsuarioId(usuarioId: number): Observable<Perfil> {
    return this.http.get<Perfil>(`${this.baseUrl}/usuario/${usuarioId}`)
      .pipe(catchError(this.tratarErro));
  }

  /**
   * Verifica se um usuário possui perfil
   * @param usuarioId ID do usuário
   * @returns Observable com boolean indicando se existe
   */
  existePerfil(usuarioId: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.baseUrl}/existe/${usuarioId}`)
      .pipe(catchError(this.tratarErro));
  }

  /**
   * Tratamento centralizado de erros HTTP
   * @param erro Erro capturado
   * @returns Observable com erro formatado
   */
  private tratarErro(erro: HttpErrorResponse) {
    let mensagem = 'Erro desconhecido ao processar perfil';

    // Verificar se é erro do cliente (apenas no navegador)
    if (typeof ErrorEvent !== 'undefined' && erro.error instanceof ErrorEvent) {
      mensagem = `Erro: ${erro.error.message}`;
    } else {
      // Erro do servidor
      if (erro.status === 404) {
        mensagem = 'Perfil não encontrado. Você ainda não possui um perfil cadastrado. Clique em "Criar Perfil" para começar!';
      } else if (erro.status === 0) {
        mensagem = 'Não foi possível conectar ao servidor. Verifique se o backend está rodando.';
      } else if (erro.status === 500) {
        mensagem = 'Erro no servidor. Tente novamente mais tarde.';
      } else if (erro.error?.message) {
        mensagem = erro.error.message;
      } else {
        mensagem = `Erro ao processar requisição (${erro.status}). Tente novamente.`;
      }
    }

    console.error('Erro na requisição de perfil:', mensagem);
    return throwError(() => new Error(mensagem));
  }
}
