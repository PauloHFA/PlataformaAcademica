export interface SolicitacaoEntrada {
  id?: number;
  sala?: {
    id: number;
    nome: string;
  };
  usuario?: {
    id: number;
    nome: string;
    email: string;
  };
  status: 'PENDENTE' | 'APROVADA' | 'REJEITADA';
  dataSolicitacao?: string;
  dataResposta?: string;
}
