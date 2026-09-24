export interface Badge {
    id?: string;
    nome: string;
    descricao?: string;
    cor?: string; // CSS color class or hex
    icone?: string; // icon class name (e.g., 'bi bi-star')
    dataConquista?: string; // date earned
}