export interface Postagem {
  id?: number;
  titulo: string;
  conteudo: string;
  autorId?: number;
  autorNome?: string;
  plataformaId?: number;
  plataformaNome?: string;
}
