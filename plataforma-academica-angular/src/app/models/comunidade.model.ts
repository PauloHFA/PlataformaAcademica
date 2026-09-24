export interface Comunidade {
    id?: string;
    nome: string;
    descricao: string;
    criadorId: string;
    criadorNome?: string;
    membros: string[]; // Array de IDs de usuários
    dataCriacao?: string | Date;
    ativo?: boolean;
    tema?: string; // Ex: Matemática, Programação, História
    tags?: string[]; // Tags para categorização
    fotoUrl?: string; // URL da foto da comunidade
    publico?: boolean; // Se a comunidade é pública ou requer convite para entrar
}