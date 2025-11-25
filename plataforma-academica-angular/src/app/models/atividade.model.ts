/**
 * Modelo Atividade
 */
export interface Atividade {
  id?: number;
  titulo: string;
  descricao: string;
  tipoDocumentoSubmissao?: string;
  dataEntrega: string;
  pontos?: number;
  dataCriacao?: string;
  criadoPorId?: number;
  salaId?: number;
  autorId?: number;
  autorNome?: string;
  salaNome?: string;
}

