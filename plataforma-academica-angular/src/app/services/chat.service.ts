import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { Mensagem, ConversaChat } from '../models/mensagem.model';

export interface ChatContact {
  usuarioId: string;
  usuarioNome: string;
  usuarioRole?: string;
  avatar?: string;
  status?: string;
}

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private apiUrl = 'http://localhost:8090/api/mensagens';
  private mensagensSubject = new BehaviorSubject<Mensagem[]>([]);
  mensagens$ = this.mensagensSubject.asObservable();
  private chatsAbertosSubject = new BehaviorSubject<ChatContact[]>([]);
  chatsAbertos$ = this.chatsAbertosSubject.asObservable();
  private readonly limiteChats = 3;

  constructor(private http: HttpClient) { }

  abrirChat(contact: ChatContact): void {
    const atuais = this.chatsAbertosSubject.value.filter(chat => chat.usuarioId !== contact.usuarioId);
    this.chatsAbertosSubject.next([...atuais, contact].slice(-this.limiteChats));
  }

  fecharChat(usuarioId: string): void {
    this.chatsAbertosSubject.next(this.chatsAbertosSubject.value.filter(chat => chat.usuarioId !== usuarioId));
  }

  alternarChatMinimizado(usuarioId: string): void {
    // O estado visual de minimizar é controlado pelo componente da caixa.
    const contato = this.chatsAbertosSubject.value.find(chat => chat.usuarioId === usuarioId);
    if (contato) this.abrirChat(contato);
  }

  enviarMensagem(mensagem: Mensagem): Observable<Mensagem> {
    return this.http.post<Mensagem>(`${this.apiUrl}/enviar`, mensagem);
  }

  obterMensagens(usuarioId: string, amigoId: string): Observable<Mensagem[]> {
    return this.http.get<Mensagem[]>(`${this.apiUrl}/${usuarioId}/${amigoId}`);
  }

  obterConversas(usuarioId: string): Observable<ConversaChat[]> {
    return this.http.get<ConversaChat[]>(`${this.apiUrl}/conversas/${usuarioId}`);
  }
}
