/**
 * Modelo de dados para Usuário da plataforma acadêmica
 * Correspondente ao modelo Java: com.plataforma_academica.plataforma.model.Usuario
 */
export interface Usuario {
  id?: number;
  nome: string;
  email: string;
  senha?: string;
  dataCadastro?: string;
  ativo?: boolean;
  [key: string]: any; // Permite propriedades adicionais se necessário
}

/**
 * Resposta de login contendo o usuário autenticado
 */
export interface LoginResponse {
  id: number;
  nome: string;
  email: string;
  dataCadastro?: string;
  ativo?: boolean;
}

/**
 * Objeto para enviar credenciais no login
 */
export interface LoginRequest {
  email: string;
  senha: string;
}
