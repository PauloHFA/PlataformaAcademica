import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Amizade } from '../models/amizade.model';

@Injectable({
  providedIn: 'root'
})
export class AmizadeService {
  private apiUrl = 'http://localhost:8090/api/amizades';

  constructor(private http: HttpClient) { }

  enviarSolicitacao(solicitanteId: string, destinatarioId: string): Observable<Amizade> {
    const payload = { solicitanteId, destinatarioId };
    console.log('AmizadeService - Enviando:', payload);
    return this.http.post<Amizade>(this.apiUrl, payload);
  }

  responderSolicitacao(id: string, acao: 'aceitar' | 'recusar'): Observable<Amizade> {
    return this.http.patch<Amizade>(`${this.apiUrl}/${id}/resposta`, null, { params: { acao } });
  }

  aceitarSolicitacao(id: string): Observable<Amizade> {
    return this.responderSolicitacao(id, 'aceitar');
  }

  recusarSolicitacao(id: string): Observable<Amizade> {
    return this.responderSolicitacao(id, 'recusar');
  }

  removerAmizade(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  listarPendentes(usuarioId: string): Observable<Amizade[]> {
    return this.http.get<Amizade[]>(`${this.apiUrl}/pendentes/${usuarioId}`);
  }

  listarAmigos(usuarioId: string): Observable<Amizade[]> {
    return this.http.get<Amizade[]>(`${this.apiUrl}/amigos/${usuarioId}`);
  }
}
