export interface SubmissaoAtividadeResponseDTO {
  id: string;
  descricao?: string;
  nota?: number;
  feedback?: string;
  dataSubmissao?: string;
  dataCorrecao?: string;
  recebida?: boolean;
  alunoId?: string;
  atividadeId?: string;
}

export interface DashboardAluno {
  alunoId: string;
  alunoNome: string;
  salaId: string;
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
