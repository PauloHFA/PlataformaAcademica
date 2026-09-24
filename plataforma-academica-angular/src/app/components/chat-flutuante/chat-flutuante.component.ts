import { Component, OnInit, PLATFORM_ID, Inject } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ChatService, ChatContact } from '../../services/chat.service';
import { AmizadeService } from '../../services/amizade.service';
import { Mensagem, ConversaChat } from '../../models/mensagem.model';

@Component({
  selector: 'app-chat-flutuante',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './chat-flutuante.component.html',
  styleUrl: './chat-flutuante.component.css'
})
export class ChatFlutuanteComponent implements OnInit {
  aberto = false;
  chatsAbertos: ChatContact[] = [];
  minimizados = new Set<string>();
  conversas: ConversaChat[] = [];
  conversaSelecionada: ConversaChat | null = null;
  mensagens: Mensagem[] = [];
  novaMensagem = '';
  usuarioId = '';
  carregando = false;

  constructor(
    private chatService: ChatService,
    private amizadeService: AmizadeService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) { }

  ngOnInit() {
    this.chatService.chatsAbertos$.subscribe(chats => {
      this.chatsAbertos = chats;
      this.aberto = chats.length > 0;
      if (chats.length > 0 && !this.conversaSelecionada) {
        this.selecionarContato(chats[chats.length - 1]);
      }
    });
    if (isPlatformBrowser(this.platformId)) {
      this.usuarioId = localStorage.getItem('usuarioId') || '';
      if (this.usuarioId) {
        this.carregarConversas();
      }
    }
  }

  carregarConversas() {
    this.amizadeService.listarAmigos(this.usuarioId).subscribe({
      next: (amizades) => {
        this.conversas = amizades
          .filter(a => a.solicitanteId === this.usuarioId || a.destinatarioId === this.usuarioId)
          .map(amizade => {
            const amigoId = amizade.solicitanteId === this.usuarioId
              ? amizade.destinatarioId
              : amizade.solicitanteId;
            const amigoNome = amizade.solicitanteId === this.usuarioId
              ? amizade.destinatarioNome || 'Usuário'
              : amizade.solicitanteNome || 'Usuário';

            return {
              usuarioId: amigoId,
              usuarioNome: amigoNome
            };
          });
      },
      error: () => console.error('Erro ao carregar amigos')
    });
  }

  selecionarConversa(conversa: ConversaChat) {
    this.conversaSelecionada = conversa;
    this.carregarMensagens();
  }

  selecionarContato(contato: ChatContact): void {
    this.conversaSelecionada = { usuarioId: contato.usuarioId, usuarioNome: contato.usuarioNome };
    this.minimizados.delete(contato.usuarioId);
    this.carregarMensagens();
  }

  minimizar(contato: ChatContact): void {
    this.minimizados.add(contato.usuarioId);
  }

  fechar(contato: ChatContact): void {
    this.chatService.fecharChat(contato.usuarioId);
    if (this.conversaSelecionada?.usuarioId === contato.usuarioId) this.conversaSelecionada = null;
  }

  trackByChat(_: number, chat: ChatContact): string { return chat.usuarioId; }

  carregarMensagens() {
    if (!this.conversaSelecionada) return;
    this.chatService.obterMensagens(this.usuarioId, this.conversaSelecionada.usuarioId).subscribe({
      next: (msgs) => {
        this.mensagens = msgs;
        setTimeout(() => this.scrollParaBaixo(), 100);
      },
      error: () => console.error('Erro ao carregar mensagens')
    });
  }

  enviarMensagem() {
    if (!this.novaMensagem.trim() || !this.conversaSelecionada) return;

    const mensagem: Mensagem = {
      remetenteId: this.usuarioId,
      destinatarioId: this.conversaSelecionada.usuarioId,
      conteudo: this.novaMensagem
    };

    this.chatService.enviarMensagem(mensagem).subscribe({
      next: (msg) => {
        this.mensagens.push(msg);
        this.novaMensagem = '';
        setTimeout(() => this.scrollParaBaixo(), 100);
      },
      error: () => console.error('Erro ao enviar mensagem')
    });
  }

  handleKeyDown(event: Event) {
    const keyboardEvent = event as KeyboardEvent;
    if (keyboardEvent.shiftKey) {
      // Allow line break when Shift+Enter is pressed
      return;
    } else {
      // Send message when Enter is pressed without Shift
      event.preventDefault();
      this.enviarMensagem();
    }
  }

  scrollParaBaixo() {
    const container = document.querySelector('.mensagens-container');
    if (container) {
      container.scrollTop = container.scrollHeight;
    }
  }

  toggleChat() {
    this.aberto = !this.aberto;
  }

  fecharChat() {
    this.aberto = false;
    this.conversaSelecionada = null;
  }
}
