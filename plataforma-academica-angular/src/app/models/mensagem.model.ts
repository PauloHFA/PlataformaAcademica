export interface Mensagem {
  id?: string;
  remetenteId: string;
  destinatarioId: string;
  conteudo: string;
  criadoEm?: string;
  lida?: boolean;
}

export interface ConversaChat {
  usuarioId: string;
  usuarioNome: string;
  ultimaMensagem?: string;
  ultimaAtualizado?: string;
}
