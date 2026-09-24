import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CalendarioAcademicoService } from '../../services/calendario-academico.service';
import { EventoAcademico, CalendarioFiltro } from '../../models/evento-academico.model';

@Component({
    selector: 'app-calendario-academico',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './calendario-academico.component.html',
    styleUrl: './calendario-academico.component.css'
})
export class CalendarioAcademicoComponent implements OnInit {
    eventos: EventoAcademico[] = [];
    eventosHoje: EventoAcademico[] = [];
    eventosProximos: EventoAcademico[] = [];
    carregando = true;
    erroCarregamento = '';

    constructor(private calendarioService: CalendarioAcademicoService) { }

    ngOnInit(): void {
        this.carregarEventos();
    }

    carregarEventos(): void {
        this.carregando = true;
        this.erroCarregamento = '';

        // Carrega eventos de hoje
        this.calendarioService.obterEventosHoje().subscribe({
            next: (eventos) => {
                this.eventosHoje = eventos;
            },
            error: (error) => {
                console.error('Erro ao carregar eventos de hoje:', error);
                this.erroCarregamento = 'Erro ao carregar eventos de hoje';
            }
        });

        // Carrega eventos próximos (próximos 7 dias)
        this.calendarioService.obterEventosProximos(7).subscribe({
            next: (eventos) => {
                this.eventosProximos = eventos;
                // Combina todos os eventos para a lista completa
                this.eventos = [...new Set([...this.eventosHoje, ...this.eventosProximos])];
                this.carregando = false;
            },
            error: (error) => {
                console.error('Erro ao carregar eventos próximos:', error);
                this.erroCarregamento = 'Erro ao carregar eventos';
                this.carregando = false;
            }
        });
    }

    /**
     * Formata a data para exibição (DD/MM/YYYY)
     */
    formatarData(dataString: string): string {
        const data = new Date(dataString);
        return data.toLocaleDateString('pt-BR');
    }

    /**
     * Formata a hora para exibição (HH:MM)
     */
    formatarHora(dataString: string): string {
        const data = new Date(dataString);
        return data.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' });
    }

    /**
     * Retorna a classe CSS baseada no tipo de evento
     */
    getTipoClasse(tipo: string): string {
        switch (tipo) {
            case 'prova':
                return 'tipo-prova';
            case 'entrega':
                return 'tipo-entrega';
            case 'evento':
                return 'tipo-evento';
            case 'feriado':
                return 'tipo-feriado';
            default:
                return 'tipo-outro';
        }
    }

    /**
     * Retorna o ícone baseado no tipo de evento
     */
    getTipoIcone(tipo: string): string {
        switch (tipo) {
            case 'prova':
                return '📝';
            case 'entrega':
                return '📤';
            case 'evento':
                return '📅';
            case 'feriado':
                return '🎉';
            default:
                return '⏰';
        }
    }

    /**
     * Verifica se o evento é de hoje
     */
    isEventoHoje(dataString: string): boolean {
        const dataEvento = new Date(dataString).toISOString().split('T')[0];
        const hoje = new Date().toISOString().split('T')[0];
        return dataEvento === hoje;
    }

    /**
     * Verifica se o evento é amanhã
     */
    isEventoAmanha(dataString: string): boolean {
        const dataEvento = new Date(dataString).toISOString().split('T')[0];
        const amanha = new Date(Date.now() + 24 * 60 * 60 * 1000).toISOString().split('T')[0];
        return dataEvento === amanha;
    }

    /**
     * Calcula os dias restantes para o evento
     */
    calcularDiasRestantes(dataString: string): number {
        const dataEvento = new Date(dataString);
        const hoje = new Date();
        const diffTime = dataEvento.getTime() - hoje.getTime();
        return Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    }

    /**
     * Filtra para mostrar apenas eventos de hoje
     */
    filtrarHoje(): void {
        this.carregando = true;
        this.calendarioService.obterEventosHoje().subscribe({
            next: (eventos) => {
                this.eventosHoje = eventos;
                this.eventosProximos = []; // Limpa os próximos quando filtrando por hoje
                this.carregando = false;
            },
            error: (error) => {
                console.error('Erro ao filtrar eventos de hoje:', error);
                this.erroCarregamento = 'Erro ao carregar eventos';
                this.carregando = false;
            }
        });
    }

    /**
     * Filtra para mostrar apenas eventos de amanhã
     */
    filtrarAmanha(): void {
        this.carregando = true;
        const amanha = new Date(Date.now() + 24 * 60 * 60 * 1000).toISOString().split('T')[0];
        const filtros: CalendarioFiltro = {
            dataInicio: amanha,
            dataFim: amanha,
            limite: 20
        };

        this.calendarioService.listarEventos(filtros).subscribe({
            next: (eventos) => {
                this.eventosHoje = []; // Limpa os de hoje quando filtrando por amanhã
                this.eventosProximos = eventos;
                this.carregando = false;
            },
            error: (error) => {
                console.error('Erro ao filtrar eventos de amanhã:', error);
                this.erroCarregamento = 'Erro ao carregar eventos';
                this.carregando = false;
            }
        });
    }

    /**
     * Filtra para mostrar apenas eventos desta semana
     */
    filtrarSemana(): void {
        this.carregando = true;
        const hoje = new Date();
        const inicioSemana = new Date(hoje.setDate(hoje.getDate() - hoje.getDay()));
        const fimSemana = new Date(hoje.setDate(hoje.getDate() - hoje.getDay() + 6));

        const filtros: CalendarioFiltro = {
            dataInicio: inicioSemana.toISOString().split('T')[0],
            dataFim: fimSemana.toISOString().split('T')[0],
            limite: 50
        };

        this.calendarioService.listarEventos(filtros).subscribe({
            next: (eventos) => {
                this.eventosHoje = []; // Reorganiza para exibição semanal
                this.eventosProximos = eventos;
                this.carregando = false;
            },
            error: (error) => {
                console.error('Erro ao filtrar eventos da semana:', error);
                this.erroCarregamento = 'Erro ao carregar eventos';
                this.carregando = false;
            }
        });
    }

    /**
     * Filtra para mostrar todos os eventos
     */
    filtrarTodos(): void {
        this.carregarEventos(); // Recarrega todos os eventos
    }
}