import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Comunidade } from '../models/comunidade.model';

@Injectable({
    providedIn: 'root'
})
export class ComunidadeService {
    private apiUrl = 'http://localhost:8090/api/comunidades';

    constructor(private http: HttpClient) { }

    listarTodas(): Observable<Comunidade[]> {
        return this.http.get<Comunidade[]>(this.apiUrl);
    }

    buscarPorId(id: string): Observable<Comunidade> {
        return this.http.get<Comunidade>(`${this.apiUrl}/${id}`);
    }

    criar(comunidade: Comunidade): Observable<Comunidade> {
        return this.http.post<Comunidade>(this.apiUrl, comunidade);
    }

    atualizar(id: string, comunidade: Comunidade): Observable<Comunidade> {
        return this.http.put<Comunidade>(`${this.apiUrl}/${id}`, comunidade);
    }

    deletar(id: string): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }

    entrar(id: string, usuarioId: string): Observable<void> {
        return this.http.post<void>(`${this.apiUrl}/${id}/entrar`, { usuarioId });
    }

    sair(id: string, usuarioId: string): Observable<void> {
        return this.http.post<void>(`${this.apiUrl}/${id}/sair`, { usuarioId });
    }

    listarMembros(id: string): Observable<string[]> {
        return this.http.get<string[]>(`${this.apiUrl}/${id}/membros`);
    }
}