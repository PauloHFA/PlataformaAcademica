import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DashboardAluno } from '../models/dashboard-aluno.model';
import { DashboardSala } from '../models/dashboard-sala.model';
import { Frequencia, FrequenciaRequest } from '../models/frequencia.model';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  getDashboardAluno(alunoId: number, salaId: number, inicio?: string, fim?: string): Observable<DashboardAluno> {
    let params = new HttpParams().set('salaId', String(salaId));
    if (inicio) params = params.set('inicio', inicio);
    if (fim) params = params.set('fim', fim);
    return this.http.get<DashboardAluno>(`${this.apiUrl}/dashboard/aluno/${alunoId}`, { params });
  }

  getDashboardSala(salaId: number, inicio?: string, fim?: string): Observable<DashboardSala> {
    let params = new HttpParams();
    if (inicio) params = params.set('inicio', inicio);
    if (fim) params = params.set('fim', fim);
    return this.http.get<DashboardSala>(`${this.apiUrl}/dashboard/sala/${salaId}`, { params });
  }

  registrarFrequencia(payload: FrequenciaRequest): Observable<Frequencia> {
    return this.http.post<Frequencia>(`${this.apiUrl}/frequencia/registrar`, payload);
  }

  getFrequencias(alunoId: number, salaId: number, inicio?: string, fim?: string): Observable<Frequencia[]> {
    let params = new HttpParams().set('alunoId', String(alunoId)).set('salaId', String(salaId));
    if (inicio) params = params.set('inicio', inicio);
    if (fim) params = params.set('fim', fim);
    return this.http.get<Frequencia[]>(`${this.apiUrl}/frequencia`, { params });
  }

  getPercentualPresenca(alunoId: number, salaId: number, inicio: string, fim: string): Observable<number> {
    const params = new HttpParams().set('alunoId', String(alunoId)).set('salaId', String(salaId)).set('inicio', inicio).set('fim', fim);
    return this.http.get<number>(`${this.apiUrl}/frequencia/percentual`, { params });
  }
}
