import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SubmissaoAtividade, SubmissaoAtividadeResponse } from '../models/submissao-atividade.model';

@Injectable({
  providedIn: 'root'
})
export class SubmissaoAtividadeService {
  private apiUrl = 'http://localhost:8090/api/submissaoatividade';

  constructor(private http: HttpClient) { }

  enviarSubmissao(atividadeId: string, alunoId: string, submissao: SubmissaoAtividade): Observable<SubmissaoAtividadeResponse> {
    return this.http.post<SubmissaoAtividadeResponse>(
      `${this.apiUrl}/atividade/${atividadeId}/aluno/${alunoId}`,
      submissao
    );
  }

  listarSubmissoesPorAtividade(atividadeId: string): Observable<SubmissaoAtividadeResponse[]> {
    return this.http.get<SubmissaoAtividadeResponse[]>(`${this.apiUrl}/atividade/${atividadeId}`);
  }

  listarPorAtividade(atividadeId: string): Observable<SubmissaoAtividade[]> {
    return this.http.get<SubmissaoAtividade[]>(`${this.apiUrl}/atividade/${atividadeId}`);
  }

  buscarSubmissaoDoAluno(atividadeId: string, alunoId: string): Observable<SubmissaoAtividadeResponse> {
    return this.http.get<SubmissaoAtividadeResponse>(
      `${this.apiUrl}/atividade/${atividadeId}/aluno/${alunoId}`
    );
  }

  listarSubmissoesPorAlunoESala(alunoId: string, salaId: string): Observable<SubmissaoAtividadeResponse[]> {
    return this.http.get<SubmissaoAtividadeResponse[]>(`${this.apiUrl}/aluno/${alunoId}/sala/${salaId}`);
  }

  enviarSubmissaoComArquivo(atividadeId: string, alunoId: string, formData: FormData): Observable<SubmissaoAtividadeResponse> {
    return this.http.post<SubmissaoAtividadeResponse>(
      `${this.apiUrl}/atividade/${atividadeId}/aluno/${alunoId}`,
      formData
    );
  }

  corrigirSubmissao(submissaoId: string, nota: number, feedback?: string): Observable<SubmissaoAtividadeResponse> {
    const params: any = { nota };
    if (feedback) params.feedback = feedback;
    return this.http.put<SubmissaoAtividadeResponse>(
      `${this.apiUrl}/corrigir/${submissaoId}`,
      null,
      { params }
    );
  }

  marcarComoRecebida(submissaoId: string): Observable<SubmissaoAtividade> {
    return this.http.put<SubmissaoAtividade>(`${this.apiUrl}/${submissaoId}/recebida`, {});
  }
}
