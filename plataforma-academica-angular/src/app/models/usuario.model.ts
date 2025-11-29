/**
 * Modelo de dados para Usuário da plataforma acadêmica
 * Correspondente ao modelo Java: com.plataforma_academica.plataforma.model.Usuario
 */
export interface Usuario {
  id?: number;
  nome: string;
  email: string;
  senha?: string;
  avatarUrl?: string;      // URL do avatar para exibição
  avatarBase64?: string;   // Base64 do avatar para upload
  plataformaId?: number;
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
