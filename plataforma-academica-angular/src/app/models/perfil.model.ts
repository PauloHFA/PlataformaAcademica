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
  id?: number;
  nome?: string;
  sobrenome?: string;
  email?: string;
  instituicaoEnsino?: string;
  cep?: string;
  pais?: string;
  cidade?: string;
  site?: string;
  telefone?: string;
  dataNascimento?: string;
  descricao?: string;
  avatar?: string; // base64
  bio?: string;
  curso?: string;
  fotoPerfil?: string;
  usuarioId?: number;
}

import { Usuario } from './usuario.model';

/**
 * Modelo completo de Perfil retornado pelo backend
 * Herda campos de Usuario e adiciona campos extras
 */
export interface Perfil extends Usuario {
  bio?: string;
  curso?: string;
  fotoPerfil?: string;
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
