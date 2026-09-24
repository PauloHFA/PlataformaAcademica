export interface Material {
    id?: string;
    titulo: string;
    descricao?: string;
    tipo: 'PDF' | 'VIDEO' | 'LINK' | 'CODIGO' | 'IMAGEM' | 'OUTRO';
    url?: string;
    arquivoId?: string;
    semanaId: string;
    salaId: string;
    autorId: string;
    autorNome: string;
    dataCriacao?: string;
    ordem?: number;
}

export interface Semana {
    id?: string;
    titulo: string;
    descricao?: string;
    numero: number;
    salaId: string;
    dataInicio?: string;
    dataFim?: string;
    materiais?: Material[];
}

export interface MaterialUpload {
    arquivo: File;
    titulo: string;
    descricao?: string;
    semanaId: string;
    salaId: string;
}