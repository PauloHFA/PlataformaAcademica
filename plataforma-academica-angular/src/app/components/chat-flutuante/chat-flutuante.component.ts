import { Component, OnInit, PLATFORM_ID, Inject } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ChatService } from '../../services/chat.service';
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
  conversas: ConversaChat[] = [];
  conversaSelecionada: ConversaChat | null = null;
  mensagens: Mensagem[] = [];
  novaMensagem = '';
  usuarioId = 0;
  carregando = false;

  constructor(
    private chatService: ChatService,
    private amizadeService: AmizadeService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit() {
    if (isPlatformBrowser(this.platformId)) {
      const usuarioIdStr = localStorage.getItem('usuarioId');
      this.usuarioId = usuarioIdStr ? parseInt(usuarioIdStr, 10) : 0;
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
