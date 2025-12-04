/**
 * Modelo de dados para Usuário da plataforma acadêmica
 * Correspondente ao modelo Java: com.plataforma_academica.plataforma.model.Usuario
 */
export interface Usuario {
  id?: number;
  nome: string;
  sobrenome?: string;
  email: string;
  senha?: string;
  dataNascimento?: string;
  telefone?: string;
  descricao?: string;
  instituicaoEnsino?: string;
  cep?: string;
  pais?: string;
  cidade?: string;
  site?: string;
  avatarUrl?: string;
  avatarBase64?: string;
  plataformaId?: number;
  matricula?: string; // Campo para professores
}

/**
 * Resposta de login contendo o usuário autenticado
 * Correspondente ao UsuarioResponseDTO do backend
 */
export interface LoginResponse {
  id: number;
  nome: string;
  email: string;
  avatarUrl?: string;
  avatarBase64?: string;
}

/**
 * Objeto para enviar credenciais no login
 */
export interface LoginRequest {
  email: string;
  senha: string;
}
