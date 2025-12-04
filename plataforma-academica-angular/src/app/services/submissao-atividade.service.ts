import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SubmissaoAtividade, SubmissaoAtividadeResponse } from '../models/submissao-atividade.model';

@Injectable({
  providedIn: 'root'
})
export class SubmissaoAtividadeService {
  private apiUrl = 'http://localhost:8080/api/submissaoatividade';

  constructor(private http: HttpClient) {}

  enviarSubmissao(atividadeId: number, alunoId: number, submissao: SubmissaoAtividade): Observable<SubmissaoAtividadeResponse> {
    return this.http.post<SubmissaoAtividadeResponse>(
      `${this.apiUrl}/atividade/${atividadeId}/aluno/${alunoId}`,
      submissao
    );
  }

  listarSubmissoesPorAtividade(atividadeId: number): Observable<SubmissaoAtividadeResponse[]> {
    return this.http.get<SubmissaoAtividadeResponse[]>(`${this.apiUrl}/atividade/${atividadeId}`);
  }

  listarPorAtividade(atividadeId: number): Observable<SubmissaoAtividade[]> {
    return this.http.get<SubmissaoAtividade[]>(`${this.apiUrl}/atividade/${atividadeId}`);
  }

  buscarSubmissaoDoAluno(atividadeId: number, alunoId: number): Observable<SubmissaoAtividadeResponse> {
    return this.http.get<SubmissaoAtividadeResponse>(
      `${this.apiUrl}/atividade/${atividadeId}/aluno/${alunoId}`
    );
  }

  enviarSubmissaoComArquivo(atividadeId: number, alunoId: number, formData: FormData): Observable<SubmissaoAtividadeResponse> {
    return this.http.post<SubmissaoAtividadeResponse>(
      `${this.apiUrl}/atividade/${atividadeId}/aluno/${alunoId}`,
      formData
    );
  }

  corrigirSubmissao(submissaoId: number, nota: number, feedback?: string): Observable<SubmissaoAtividadeResponse> {
    const params: any = { nota };
    if (feedback) params.feedback = feedback;
    return this.http.put<SubmissaoAtividadeResponse>(
      `${this.apiUrl}/corrigir/${submissaoId}`,
      null,
      { params }
    );
  }

  marcarComoRecebida(submissaoId: number): Observable<SubmissaoAtividade> {
    return this.http.put<SubmissaoAtividade>(
      `${this.apiUrl}/marcar-recebida/${submissaoId}`,
      null
    );
  }
}
