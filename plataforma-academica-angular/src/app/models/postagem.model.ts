import { Badge } from './badge.model';

export interface Postagem {
  id?: string;
  titulo: string;
  conteudo: string;
  imagemUrl?: string;
  autorId?: string;
  autorNome?: string;
  autorTipo?: string; // ALUNO, PROFESSOR, etc.
  dataPostagem?: string | Date; // ISO string or Date object
  area?: string; // e.g., Ciência da Computação, Matemática, História
  plataformaId?: string;
  plataformaNome?: string;
  curtidas?: number;
  curtiuAtual?: boolean;
  autorBadges?: Badge[]; // Badges do autor da postagem
}
