/**
 * Modelo Atividade
 */
export interface Atividade {
  id?: string;
  titulo: string;
  descricao: string;
  tipoDocumentoSubmissao?: string;
  dataEntrega: string;
  pontos?: number;
  dataCriacao?: string;
  criadoPorId?: string;
  salaId?: string;
  autorId?: string;
  autorNome?: string;
  salaNome?: string;
  documentoUrl?: string;
}

