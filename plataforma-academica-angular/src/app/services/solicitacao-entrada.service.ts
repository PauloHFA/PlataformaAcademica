import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SolicitacaoEntrada } from '../models/solicitacao-entrada.model';

@Injectable({
  providedIn: 'root'
})
export class SolicitacaoEntradaService {
  private baseUrl = 'http://localhost:8090/api/solicitacoes';

  constructor(private http: HttpClient) { }

  solicitarEntrada(salaId: string, usuarioId: string): Observable<SolicitacaoEntrada> {
    return this.http.post<SolicitacaoEntrada>(`${this.baseUrl}/solicitar/${salaId}/${usuarioId}`, {});
  }

  listarPendentes(salaId: string): Observable<SolicitacaoEntrada[]> {
    return this.http.get<SolicitacaoEntrada[]>(`${this.baseUrl}/sala/${salaId}/pendentes`);
  }

  aprovar(solicitacaoId: string, professorId: string): Observable<SolicitacaoEntrada> {
    return this.http.put<SolicitacaoEntrada>(`${this.baseUrl}/${solicitacaoId}/aprovar/${professorId}`, {});
  }

  rejeitar(solicitacaoId: string, professorId: string): Observable<SolicitacaoEntrada> {
    return this.http.put<SolicitacaoEntrada>(`${this.baseUrl}/${solicitacaoId}/rejeitar/${professorId}`, {});
  }

  minhasSolicitacoes(usuarioId: string): Observable<SolicitacaoEntrada[]> {
    return this.http.get<SolicitacaoEntrada[]>(`${this.baseUrl}/usuario/${usuarioId}/minhas`);
  }
}
