export interface SubmissaoAtividadeResponseDTO {
  id: number;
  descricao?: string;
  nota?: number;
  feedback?: string;
  dataSubmissao?: string;
  dataCorrecao?: string;
  recebida?: boolean;
  alunoId?: number;
  atividadeId?: number;
}

export interface DashboardAluno {
  alunoId: number;
  alunoNome: string;
  salaId: number;
  salaNome: string;
  totalAtividades: number;
  totalSubmissoes: number;
  totalSubmissoesComNota: number;
  mediaNota: number;
  totalPresencas: number;
  totalFaltas: number;
  percentualPresenca: number;
  submissoes: SubmissaoAtividadeResponseDTO[];
}
