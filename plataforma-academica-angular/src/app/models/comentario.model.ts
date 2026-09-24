export interface Comentario {
  id?: string;
  conteudo: string;
  autorId: string;
  autorNome?: string;
  autor?: { id: string; nome: string };
  saladeAulaId?: string;
  atividadeId?: string;
  postagemId?: string;
  tipoDestino?: string;
  dataCriacao?: string;
}
