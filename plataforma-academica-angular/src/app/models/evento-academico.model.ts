/**
 * Modelo de Evento Acadêmico para o Calendário/Timeline
 * Representa exames, entregas de trabalhos e eventos da instituição
 */
export interface EventoAcademico {
    id?: string;
    titulo: string;
    descricao?: string;
    dataInicio: string; // ISO date string
    dataFim?: string; // ISO date string (opcional para eventos de duração)
    tipo: 'prova' | 'entrega' | 'evento' | 'feriado'; // Tipo do evento
    salaId?: string; // ID da sala relacionada (se aplicável)
    salaNome?: string; // Nome da sala (para exibição)
    disciplina?: string; // Nome da disciplina
    cor?: string; // Cor para exibição no calendario (hex)
    icone?: string; // Classe de ícone ou nome do ícone
    link?: string; // Link para mais informações ou materiais
    todosOsDia?: boolean; // Se o evento dura o dia inteiro
    criadoEm?: string;
    atualizadoEm?: string;
}

/**
 * Filtro para consulta de eventos do calendário
 */
export interface CalendarioFiltro {
    salaId?: string;
    tipo?: 'prova' | 'entrega' | 'evento' | 'feriado';
    dataInicio?: string; // Data de início para filtro (ISO)
    dataFim?: string; // Data de fim para filtro (ISO)
    limite?: number; // Número máximo de eventos a retornar
}