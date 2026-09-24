/**
 * Modelo SalaDeAula
 */
export interface SalaDeAula {
  id?: string;
  nome: string;
  codigoSala?: string;
  criadorId?: string;
  criadorNome?: string;
  membrosIds?: string[];
  membrosNomes?: string[];
  atividadesIds?: string[];
}
