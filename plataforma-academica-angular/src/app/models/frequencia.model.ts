export interface Frequencia {
  id: number;
  aluno: { id: number; nome?: string };
  salaDeAula: { id: number; nome?: string };
  data: string;
  presente: boolean;
  justificativa?: string;
}

export interface FrequenciaRequest {
  alunoId: number;
  salaId: number;
  data: string;
  presente: boolean;
  justificativa?: string;
}
