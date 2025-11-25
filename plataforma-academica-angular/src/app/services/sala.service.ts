import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { environment } from '../../environments/environment';
import { SalaDeAula } from '../models/sala.model';
import { Atividade } from '../models/atividade.model';

@Injectable({
  providedIn: 'root'
})
export class SalaService {
  private baseUrl = `${environment.apiUrl}/saladeaula`;

  constructor(private http: HttpClient, @Inject(PLATFORM_ID) private platformId: Object) {}

  listarSalas(): Observable<SalaDeAula[]> {
    return this.http.get<SalaDeAula[]>(this.baseUrl).pipe(catchError(this.handleError));
  }

  buscarPorId(id: number): Observable<SalaDeAula> {
    return this.http.get<SalaDeAula>(`${this.baseUrl}/${id}`).pipe(catchError(this.handleError));
  }

  criarSala(sala: SalaDeAula, criadorId: number): Observable<SalaDeAula> {
    return this.http.post<SalaDeAula>(`${this.baseUrl}/criar/${criadorId}`, sala).pipe(catchError(this.handleError));
  }

  deletarSala(id: number, usuarioId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}/usuario/${usuarioId}`).pipe(catchError(this.handleError));
  }

  adicionarMembro(salaId: number, membroId: number, criadorId: number): Observable<SalaDeAula> {
    return this.http.post<SalaDeAula>(`${this.baseUrl}/${salaId}/add-membro/${membroId}/criador/${criadorId}`, {}).pipe(catchError(this.handleError));
  }

  listarMembros(salaId: number): Observable<number[]> {
    return this.http.get<number[]>(`${this.baseUrl}/${salaId}/membros`).pipe(catchError(this.handleError));
  }

  removerMembro(salaId: number, membroId: number, criadorId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${salaId}/remover-membro/${membroId}/criador/${criadorId}`).pipe(catchError(this.handleError));
  }

  // Atividades
  criarAtividade(salaId: number, criadorId: number, atividade: Atividade): Observable<Atividade> {
    return this.http.post<Atividade>(`${this.baseUrl}/${salaId}/atividade/criar/${criadorId}`, atividade).pipe(catchError(this.handleError));
  }

  listarAtividades(salaId: number): Observable<Atividade[]> {
    return this.http.get<Atividade[]>(`${this.baseUrl}/${salaId}/atividades`).pipe(catchError(this.handleError));
  }

  buscarAtividade(atividadeId: number): Observable<Atividade> {
    return this.http.get<Atividade>(`${this.baseUrl}/atividade/${atividadeId}`).pipe(catchError(this.handleError));
  }

  atualizarAtividade(salaId: number, criadorId: number, atividade: Atividade): Observable<Atividade> {
    return this.http.put<Atividade>(`${this.baseUrl}/${salaId}/atividade/atualizar/${criadorId}`, atividade).pipe(catchError(this.handleError));
  }

  deletarAtividade(atividadeId: number, criadorId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/atividade/${atividadeId}/criador/${criadorId}`).pipe(catchError(this.handleError));
  }

  private handleError(err: any) {
    let errorMessage = 'Erro desconhecido ao processar requisição';
    
    // Verificar se é HttpErrorResponse
    if (err instanceof HttpErrorResponse) {
      // Erro de parsing (resposta não é JSON válido)
      if (err.error instanceof ErrorEvent || err.name === 'HttpErrorResponse') {
        // Verificar se é erro de conexão
        if (err.status === 0 || err.statusText === 'Unknown Error') {
          errorMessage = 'Não foi possível conectar ao servidor. Verifique se o backend está rodando em http://localhost:8080';
        } else if (err.status >= 500) {
          errorMessage = `Erro no servidor (${err.status}). Tente novamente mais tarde.`;
        } else if (err.status === 404) {
          errorMessage = 'Recurso não encontrado';
        } else if (err.status === 401 || err.status === 403) {
          errorMessage = 'Acesso não autorizado';
        } else {
          // Tentar extrair mensagem do erro
          if (err.error && typeof err.error === 'string') {
            errorMessage = err.error;
          } else if (err.error?.message) {
            errorMessage = err.error.message;
          } else if (err.message) {
            errorMessage = err.message;
          } else {
            errorMessage = `Erro na requisição: ${err.status} ${err.statusText}`;
          }
        }
      } else {
        // Erro de parsing JSON
        errorMessage = 'Resposta inválida do servidor. Verifique se o backend está funcionando corretamente.';
      }
    } else if (err.message) {
      // Erro genérico
      errorMessage = err.message;
    }
    
    console.error('Erro na requisição de sala:', errorMessage, err);
    return throwError(() => new Error(errorMessage));
  }
}
