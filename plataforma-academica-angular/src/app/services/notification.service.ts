import { Injectable } from '@angular/core';
import { Socket } from 'ngx-socket-io';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  constructor(private socket: Socket) {
    this.socket.connect();
  }

  getNotificacoesUsuario(usuarioId: number): Observable<string> {
    return this.socket.fromEvent('/topic/notificacoes/' + usuarioId);
  }

  desconectar() {
    this.socket.disconnect();
  }
}