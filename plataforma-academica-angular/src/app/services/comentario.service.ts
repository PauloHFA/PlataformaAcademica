import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Comentario } from '../models/comentario.model';

export type { Comentario };

@Injectable({
  providedIn: 'root'
})
export class ComentarioService {
  private apiUrl = 'http://localhost:8090/comentario';

  constructor(private http: HttpClient) { }

  criar(comentario: Comentario): Observable<Comentario> {
    return this.http.post<Comentario>(this.apiUrl, comentario);
  }

  listarPorSala(salaId: string): Observable<Comentario[]> {
    return this.http.get<Comentario[]>(`${this.apiUrl}/sala/${salaId}`);
  }

  listarAtividadesGerais(salaId: string): Observable<Comentario[]> {
    return this.http.get<Comentario[]>(`${this.apiUrl}/sala/${salaId}/atividades-gerais`);
  }

  listarPorAtividade(atividadeId: string): Observable<Comentario[]> {
    return this.http.get<Comentario[]>(`${this.apiUrl}/atividade/${atividadeId}`);
  }

  listarPorPostagem(postagemId: string): Observable<Comentario[]> {
    return this.http.get<Comentario[]>(`${this.apiUrl}/postagem/${postagemId}`);
  }

  deletar(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
