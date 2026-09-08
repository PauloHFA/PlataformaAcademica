export interface SubmissaoAtividade {
  id?: string;
  atividadeId: string;
  alunoId: string;
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
  id: string;
  atividadeId: string;
  atividadeTitulo?: string;
  alunoId: string;
  alunoNome?: string;
  urlDocumento?: string;
  descricao?: string;
  dataSubmissao: string;
  nota?: number;
  feedback?: string;
}
