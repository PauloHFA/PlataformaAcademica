/**
 * Modelo de dados para Perfil do Usuário
 * Correspondente aos modelos Java:
 *   - com.plataforma_academica.plataforma.model.Perfil
 *   - com.plataforma_academica.plataforma.dto.PerfilDTO
 */

/**
 * DTO para criação e atualização de perfil
 * Contém apenas os campos necessários para o frontend enviar
 */
export interface PerfilDTO {
  usuarioId: number;
  bio: string;
  fotoPerfil?: string; // URL ou base64
  curso: string;
}

/**
 * Modelo completo de Perfil retornado pelo backend
 * Inclui dados adicionais como ID e datas de auditoria
 */
export interface Perfil {
  id: number;
  usuarioId: number;
  bio: string;
  fotoPerfil?: string;
  curso: string;
  dataCriacao?: string;
  dataAtualizacao?: string;
}

/**
 * Interface para resposta ao verificar existência de perfil
 */
export interface PerfilExistenceResponse {
  existe: boolean;
  perfil?: Perfil;
}
