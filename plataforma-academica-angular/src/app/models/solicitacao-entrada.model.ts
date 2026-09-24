export interface SolicitacaoEntrada {
  id?: string;
  sala?: {
    id: string;
    nome: string;
  };
  usuario?: {
    id: string;
    nome: string;
    email: string;
  };
  status: 'PENDENTE' | 'APROVADA' | 'REJEITADA';
  dataSolicitacao?: string;
  dataResposta?: string;
}
