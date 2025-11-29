export interface SubmissaoAtividade {
  id?: number;
  atividadeId: number;
  alunoId: number;
  urlDocumento: string;
  dataSubmissao?: string;
  nota?: number;
  feedback?: string;
}

export interface SubmissaoAtividadeResponse {
  id: number;
  atividadeId: number;
  atividadeTitulo?: string;
  alunoId: number;
  alunoNome?: string;
  urlDocumento: string;
  dataSubmissao: string;
  nota?: number;
  feedback?: string;
}
