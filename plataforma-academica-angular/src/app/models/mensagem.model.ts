export interface Mensagem {
  id?: number;
  remetenteId: number;
  destinatarioId: number;
  conteudo: string;
  criadoEm?: string;
  lida?: boolean;
}

export interface ConversaChat {
  usuarioId: number;
  usuarioNome: string;
  ultimaMensagem?: string;
  ultimaAtualizado?: string;
}
