import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EventoAcademico, CalendarioFiltro } from '../models/evento-academico.model';

@Injectable({
    providedIn: 'root'
})
export class CalendarioAcademicoService {
    private apiUrl = 'http://localhost:8090/api';

    constructor(private http: HttpClient) { }

    /**
     * Lista eventos do calendário acadêmico com filtros opcionais
     * @param filtros Filtros para consulta dos eventos
     * @returns Observable com lista de eventos acadêmicos
     */
    listarEventos(filtros?: CalendarioFiltro): Observable<EventoAcademico[]> {
        let params = new HttpParams();

        if (filtros) {
            if (filtros.salaId) {
                params = params.set('salaId', filtros.salaId);
            }
            if (filtros.tipo) {
                params = params.set('tipo', filtros.tipo);
            }
            if (filtros.dataInicio) {
                params = params.set('dataInicio', filtros.dataInicio);
            }
            if (filtros.dataFim) {
                params = params.set('dataFim', filtros.dataFim);
            }
            if (filtros.limite) {
                params = params.set('limite', String(filtros.limite));
            }
        }

        return this.http.get<EventoAcademico[]>(`${this.apiUrl}/calendario/eventos`, { params });
    }

    /**
     * Busca um evento específico pelo ID
     * @param id ID do evento
     * @returns Observable com o evento acadêmico
     */
    buscarPorId(id: string): Observable<EventoAcademico> {
        return this.http.get<EventoAcademico>(`${this.apiUrl}/calendario/eventos/${id}`);
    }

    /**
     * Cria um novo evento acadêmico
     * @param evento Dados do evento a ser criado
     * @returns Observable com o evento criado
     */
    criarEvento(evento: EventoAcademico): Observable<EventoAcademico> {
        return this.http.post<EventoAcademico>(`${this.apiUrl}/calendario/eventos`, evento);
    }

    /**
     * Atualiza um evento acadêmico existente
     * @param id ID do evento a ser atualizado
     * @param evento Dados atualizados do evento
     * @returns Observable com o evento atualizado
     */
    atualizarEvento(id: string, evento: EventoAcademico): Observable<EventoAcademico> {
        return this.http.put<EventoAcademico>(`${this.apiUrl}/calendario/eventos/${id}`, evento);
    }

    /**
     * Remove um evento acadêmico
     * @param id ID do evento a ser removido
     * @returns Observable vazio indicando sucesso
     */
    deletarEvento(id: string): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/calendario/eventos/${id}`);
    }

    /**
     * Obtém eventos próximos (próximos 7 dias por padrão)
     * @param dias Número de dias à frente para buscar eventos (padrão: 7)
     * @returns Observable com lista de eventos próximos
     */
    obterEventosProximos(dias: number = 7): Observable<EventoAcademico[]> {
        const dataInicio = new Date().toISOString().split('T')[0];
        const dataFim = new Date(Date.now() + dias * 24 * 60 * 60 * 1000).toISOString().split('T')[0];

        const filtros: CalendarioFiltro = {
            dataInicio: dataInicio,
            dataFim: dataFim,
            limite: 50 // Limite razoável para próximos eventos
        };

        return this.listarEventos(filtros);
    }

    /**
     * Obtém eventos de hoje
     * @returns Observable com lista de eventos de hoje
     */
    obterEventosHoje(): Observable<EventoAcademico[]> {
        const hoje = new Date().toISOString().split('T')[0];

        const filtros: CalendarioFiltro = {
            dataInicio: hoje,
            dataFim: hoje,
            limite: 20
        };

        return this.listarEventos(filtros);
    }

    /**
     * Obtém eventos desta semana
     * @returns Observable com lista de eventos desta semana
     */
    obterEventosSemana(): Observable<EventoAcademico[]> {
        const hoje = new Date();
        const inicioSemana = new Date(hoje.setDate(hoje.getDate() - hoje.getDay()));
        const fimSemana = new Date(hoje.setDate(hoje.getDate() - hoje.getDay() + 6));

        const filtros: CalendarioFiltro = {
            dataInicio: inicioSemana.toISOString().split('T')[0],
            dataFim: fimSemana.toISOString().split('T')[0],
            limite: 50
        };

        return this.listarEventos(filtros);
    }
}