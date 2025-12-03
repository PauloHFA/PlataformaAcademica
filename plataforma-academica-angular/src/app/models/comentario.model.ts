export interface Comentario {
  id?: number;
  conteudo: string;
  autorId: number;
  autorNome?: string;
  autor?: { id: number; nome: string };
  saladeAulaId?: number;
  atividadeId?: number;
  postagemId?: number;
  tipoDestino?: string;
  dataCriacao?: string;
}
