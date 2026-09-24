export interface Frequencia {
  id: string;
  aluno: { id: string; nome?: string };
  salaDeAula: { id: string; nome?: string };
  data: string;
  presente: boolean;
  justificativa?: string;
}

export interface FrequenciaRequest {
  alunoId: string;
  salaId: string;
  data: string;
  presente: boolean;
  justificativa?: string;
}
