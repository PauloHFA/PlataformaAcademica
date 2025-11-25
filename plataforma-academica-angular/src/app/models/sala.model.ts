/**
 * Modelo SalaDeAula
 */
export interface SalaDeAula {
  id?: number;
  nome: string;
  descricao?: string;
  codigoAcesso?: string;
  criadoPorId?: number;
  dataCriacao?: string;
  membros?: Array<number>;
}
