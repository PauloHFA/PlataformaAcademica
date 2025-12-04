import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SolicitacaoEntrada } from '../models/solicitacao-entrada.model';

@Injectable({
  providedIn: 'root'
})
export class SolicitacaoEntradaService {
  private baseUrl = 'http://localhost:8080/api/solicitacoes';

  constructor(private http: HttpClient) {}

  solicitarEntrada(salaId: number, usuarioId: number): Observable<SolicitacaoEntrada> {
    return this.http.post<SolicitacaoEntrada>(`${this.baseUrl}/solicitar/${salaId}/${usuarioId}`, {});
  }

  listarPendentes(salaId: number): Observable<SolicitacaoEntrada[]> {
    return this.http.get<SolicitacaoEntrada[]>(`${this.baseUrl}/sala/${salaId}/pendentes`);
  }

  aprovar(solicitacaoId: number, professorId: number): Observable<SolicitacaoEntrada> {
    return this.http.put<SolicitacaoEntrada>(`${this.baseUrl}/${solicitacaoId}/aprovar/${professorId}`, {});
  }

  rejeitar(solicitacaoId: number, professorId: number): Observable<SolicitacaoEntrada> {
    return this.http.put<SolicitacaoEntrada>(`${this.baseUrl}/${solicitacaoId}/rejeitar/${professorId}`, {});
  }

  minhasSolicitacoes(usuarioId: number): Observable<SolicitacaoEntrada[]> {
    return this.http.get<SolicitacaoEntrada[]>(`${this.baseUrl}/usuario/${usuarioId}/minhas`);
  }
}
