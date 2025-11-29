export interface Amizade {
  id?: number;
  solicitanteId: number;
  destinatarioId: number;
  solicitanteNome?: string;
  destinatarioNome?: string;
  status?: 'PENDENTE' | 'ACEITO' | 'RECUSADO';
  criadoEm?: string;
}
