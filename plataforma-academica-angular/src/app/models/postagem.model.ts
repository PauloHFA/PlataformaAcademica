export interface Postagem {
  id?: number;
  titulo: string;
  conteudo: string;
  imagemUrl?: string;
  autorId?: number;
  autorNome?: string;
  plataformaId?: number;
  plataformaNome?: string;
  curtidas?: number;
}
