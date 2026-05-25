export interface AlunoDashboardResumo {
  alunoId: number;
  alunoNome: string;
  totalSubmissoes: number;
  totalSubmissoesComNota: number;
  mediaNota: number;
  percentualPresenca: number;
}

export interface DashboardSala {
  salaId: number;
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
