export interface Pergunta {
    id?: string;
    titulo: string;
    conteudo: string;
    autorId: string;
    autorNome: string;
    dataCriacao?: string;
    salaId: string;
    respostas?: Resposta[];
}

export interface Resposta {
    id?: string;
    conteudo: string;
    autorId: string;
    autorNome: string;
    dataCriacao?: string;
    aceita?: boolean;
}