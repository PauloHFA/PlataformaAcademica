export interface SubmissaoAtividade {
  id?: number;
  atividadeId: number;
  alunoId: number;
  alunoNome?: string;
  urlDocumento?: string;
  descricao?: string;
  dataSubmissao?: string;
  nota?: number;
  feedback?: string;
  recebida?: boolean;
  dataRecebimento?: string;
}

export interface SubmissaoAtividadeResponse {
  id: number;
  atividadeId: number;
  atividadeTitulo?: string;
  alunoId: number;
  alunoNome?: string;
  urlDocumento?: string;
  descricao?: string;
  dataSubmissao: string;
  nota?: number;
  feedback?: string;
}
