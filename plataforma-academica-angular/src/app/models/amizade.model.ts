export interface Amizade {
  id?: string;
  solicitanteId: string;
  destinatarioId: string;
  solicitanteNome?: string;
  destinatarioNome?: string;
  status?: 'PENDENTE' | 'ACEITO' | 'RECUSADO';
  criadoEm?: string;
}
