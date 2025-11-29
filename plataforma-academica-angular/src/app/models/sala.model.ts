/**
 * Modelo SalaDeAula
 */
export interface SalaDeAula {
  id?: number;
  nome: string;
  criadorId?: number;
  criadorNome?: string;
  membrosIds?: number[];
  membrosNomes?: string[];
  atividadesIds?: number[];
}
