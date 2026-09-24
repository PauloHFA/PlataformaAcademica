export interface AlunoDashboardResumo {
  alunoId: string;
  alunoNome: string;
  totalSubmissoes: number;
  totalSubmissoesComNota: number;
  mediaNota: number;
  percentualPresenca: number;
}

export interface DashboardSala {
  salaId: string;
  salaNome: string;
  totalAtividades: number;
  totalSubmissoes: number;
  totalSubmissoesComNota: number;
  mediaNotaSala: number;
  totalPresencas: number;
  totalFaltas: number;
  percentualPresenca: number;
  alunos: AlunoDashboardResumo[];
}
