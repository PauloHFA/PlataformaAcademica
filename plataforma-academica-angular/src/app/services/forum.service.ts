import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Pergunta, Resposta } from '../models/forum.model';

@Injectable({
    providedIn: 'root'
})
export class ForumService {
    private baseUrl = `${environment.apiUrl}/forum`;

    constructor(private http: HttpClient) { }

    getPerguntasBySala(salaId: string): Observable<Pergunta[]> {
        return this.http.get<Pergunta[]>(`${this.baseUrl}/perguntas?salaId=${salaId}`);
    }

    criarPergunta(pergunta: Pergunta): Observable<Pergunta> {
        return this.http.post<Pergunta>(`${this.baseUrl}/perguntas`, pergunta);
    }

    responderPergunta(perguntaId: string, resposta: Resposta): Observable<Resposta> {
        return this.http.post<Resposta>(`${this.baseUrl}/perguntas/${perguntaId}/respostas`, resposta);
    }

    marcarRespostaComoAceita(perguntaId: string, respostaId: string): Observable<void> {
        return this.http.put<void>(`${this.baseUrl}/perguntas/${perguntaId}/respostas/${respostaId}/aceita`, {});
    }

    excluirPergunta(perguntaId: string): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/perguntas/${perguntaId}`);
    }

    excluirResposta(perguntaId: string, respostaId: string): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/perguntas/${perguntaId}/respostas/${respostaId}`);
    }
}