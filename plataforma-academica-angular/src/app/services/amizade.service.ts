import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Amizade } from '../models/amizade.model';

@Injectable({
  providedIn: 'root'
})
export class AmizadeService {
  private apiUrl = 'http://localhost:8080/api/amizades';

  constructor(private http: HttpClient) {}

  enviarSolicitacao(solicitanteId: number, destinatarioId: number): Observable<Amizade> {
    const payload = { solicitanteId, destinatarioId };
    console.log('AmizadeService - Enviando:', payload);
    return this.http.post<Amizade>(this.apiUrl, payload);
  }

  responderSolicitacao(id: number, acao: 'aceitar' | 'recusar'): Observable<Amizade> {
    return this.http.patch<Amizade>(`${this.apiUrl}/${id}/resposta`, null, { params: { acao } });
  }

  aceitarSolicitacao(id: number): Observable<Amizade> {
    return this.responderSolicitacao(id, 'aceitar');
  }

  recusarSolicitacao(id: number): Observable<Amizade> {
    return this.responderSolicitacao(id, 'recusar');
  }

  removerAmizade(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  listarPendentes(usuarioId: number): Observable<Amizade[]> {
    return this.http.get<Amizade[]>(`${this.apiUrl}/pendentes/${usuarioId}`);
  }

  listarAmigos(usuarioId: number): Observable<Amizade[]> {
    return this.http.get<Amizade[]>(`${this.apiUrl}/amigos/${usuarioId}`);
  }
}
