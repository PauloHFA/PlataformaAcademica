import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { Mensagem, ConversaChat } from '../models/mensagem.model';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private apiUrl = 'http://localhost:8080/api/mensagens';
  private mensagensSubject = new BehaviorSubject<Mensagem[]>([]);
  mensagens$ = this.mensagensSubject.asObservable();

  constructor(private http: HttpClient) {}

  enviarMensagem(mensagem: Mensagem): Observable<Mensagem> {
    return this.http.post<Mensagem>(`${this.apiUrl}/enviar`, mensagem);
  }

  obterMensagens(usuarioId: number, amigoId: number): Observable<Mensagem[]> {
    return this.http.get<Mensagem[]>(`${this.apiUrl}/${usuarioId}/${amigoId}`);
  }

  obterConversas(usuarioId: number): Observable<ConversaChat[]> {
    return this.http.get<ConversaChat[]>(`${this.apiUrl}/conversas/${usuarioId}`);
  }
}
