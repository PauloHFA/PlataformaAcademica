import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Postagem } from '../models/postagem.model';

@Injectable({
  providedIn: 'root'
})
export class PostagemService {
  private apiUrl = 'http://localhost:8080/api/postagens';

  constructor(private http: HttpClient) {}

  listarTodas(): Observable<Postagem[]> {
    return this.http.get<Postagem[]>(this.apiUrl);
  }

  buscarPorId(id: number): Observable<Postagem> {
    return this.http.get<Postagem>(`${this.apiUrl}/${id}`);
  }

  buscarPorTitulo(titulo: string): Observable<Postagem[]> {
    return this.http.get<Postagem[]>(`${this.apiUrl}/titulo`, { params: { titulo } });
  }

  publicar(postagem: Postagem): Observable<Postagem> {
    return this.http.post<Postagem>(this.apiUrl, postagem);
  }

  atualizar(id: number, postagem: Postagem): Observable<Postagem> {
    return this.http.put<Postagem>(`${this.apiUrl}/${id}`, postagem);
  }

  deletar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
