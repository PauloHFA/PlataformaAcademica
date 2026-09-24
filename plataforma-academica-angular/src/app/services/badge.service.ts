import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError, of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { Badge } from '../models/badge.model';

/**
 * Serviço de integração com API de Badges do backend Spring Boot
 * Gerencia operações de busca, atribuição e listagem de badges
 */
@Injectable({
    providedIn: 'root'
})
export class BadgeService {

    private baseUrl = 'http://localhost:8090/api/badges'; // Endpoint do backend
    private badgeCache: Map<string, Badge[]> = new Map();

    constructor(private http: HttpClient) { }

    /**
     * Lista todos os badges disponíveis
     * @returns Observable com array de badges
     */
    listarBadges(): Observable<Badge[]> {
        return this.http.get<Badge[]>(this.baseUrl)
            .pipe(catchError(this.tratarErro));
    }

    /**
     * Busca badges de um usuário específico
     * @param usuarioId ID do usuário
     * @returns Observable com array de badges do usuário
     */
    buscarBadgesDoUsuario(usuarioId: string): Observable<Badge[]> {
        if (this.badgeCache.has(usuarioId)) {
            return of(this.badgeCache.get(usuarioId)!);
        }
        return this.http.get<Badge[]>(`${this.baseUrl}/usuario/${usuarioId}`)
            .pipe(
                tap(badges => this.badgeCache.set(usuarioId, badges)),
                catchError(this.tratarErro)
            );
    }

    /**
     * Atribui um badge a um usuário
     * @param usuarioId ID do usuário
     * @param badgeId ID do badge
     * @returns Observable com o badge atribuído
     */
    atribuirBadge(usuarioId: string, badgeId: string): Observable<Badge> {
        return this.http.post<Badge>(`${this.baseUrl}/usuario/${usuarioId}/badge/${badgeId}`, {})
            .pipe(catchError(this.tratarErro));
    }

    /**
     * Remove um badge de um usuário
     * @param usuarioId ID do usuário
     * @param badgeId ID do badge
     * @returns Observable vazio
     */
    removerBadge(usuarioId: string, badgeId: string): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/usuario/${usuarioId}/badge/${badgeId}`)
            .pipe(catchError(this.tratarErro));
    }

    /**
     * Verifica se um usuário possui um badge específico
     * @param usuarioId ID do usuário
     * @param badgeId ID do badge
     * @returns Observable com boolean
     */
    usuarioTemBadge(usuarioId: string, badgeId: string): Observable<boolean> {
        return this.http.get<boolean>(`${this.baseUrl}/usuario/${usuarioId}/badge/${badgeId}/existe`)
            .pipe(catchError(this.tratarErro));
    }

    /**
     * Limpa o cache de badges (útil quando um novo badge é conquistado)
     */
    limparCache(): void {
        this.badgeCache.clear();
    }

    /**
     * Tratamento de erros HTTP
     */
    private tratarErro = (error: HttpErrorResponse): Observable<never> => {
        let mensagemErro = 'Ocorreu um erro inesperado';
        if (error.error instanceof ErrorEvent) {
            // Erro do lado do cliente
            mensagemErro = `Erro: ${error.error.message}`;
        } else {
            // Erro do lado do servidor
            if (error.status === 404) {
                mensagemErro = 'Recurso não encontrado';
            } else if (error.status === 403) {
                mensagemErro = 'Acesso negado';
            } else if (error.status === 400) {
                mensagemErro = error.error?.message || 'Dados inválidos';
            } else if (error.status === 500) {
                mensagemErro = 'Erro interno do servidor';
            } else {
                mensagemErro = `Erro ${error.status}: ${error.message}`;
            }
        }
        return throwError(() => new Error(mensagemErro));
    };
}