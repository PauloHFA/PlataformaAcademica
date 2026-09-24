import { Injectable } from '@angular/core';
import { SalaService } from '../services/sala.service';
import { UsuarioService } from '../services/usuario.service';
import { PostagemService } from '../services/postagem.service';
import { SalaDeAula } from '../models/sala.model';
import { Usuario } from '../models/usuario.model';
import { Postagem } from '../models/postagem.model';
import { Observable, combineLatest, of } from 'rxjs';
import { map, catchError } from 'rxjs/operators';

export interface CommandPaletteItem {
    type: 'sala' | 'usuario' | 'postagem' | 'acao';
    id?: string;
    title: string;
    subtitle?: string;
    action?: string; // route for navigation actions
}

@Injectable({
    providedIn: 'root'
})
export class CommandPaletteService {
    private openState = false;
    private searchTerm = '';
    private resultados: CommandPaletteItem[] = [];

    constructor(
        private salaService: SalaService,
        private usuarioService: UsuarioService,
        private postagemService: PostagemService
    ) { }

    open() {
        this.openState = true;
        this.searchTerm = '';
        this.resultados = [];
    }

    close() {
        this.openState = false;
    }

    isOpen(): boolean {
        return this.openState;
    }

    setSearchTerm(term: string) {
        this.searchTerm = term;
        this.updateResults();
    }

    getSearchTerm(): string {
        return this.searchTerm;
    }

    getResults(): Observable<CommandPaletteItem[]> {
        return of(this.resultados);
    }

    private updateResults() {
        if (!this.searchTerm) {
            this.resultados = this.getDefaultActions();
            return;
        }

        const term = this.searchTerm.toLowerCase();

        // We'll fetch data from all three sources and combine
        combineLatest([
            this.salaService.listarSalas().pipe(
                map(salas => salas.filter(sala =>
                    sala.nome.toLowerCase().includes(term)
                ).map(sala => ({
                    type: 'sala' as const,
                    id: sala.id,
                    title: sala.nome,
                    subtitle: `Sala: ${sala.nome}`
                }))),
                catchError(() => of([]))
            ),
            this.usuarioService.listarUsuarios().pipe(
                map(usuarios => usuarios.filter(usuario =>
                    usuario.nome.toLowerCase().includes(term)
                ).map(usuario => ({
                    type: 'usuario' as const,
                    id: usuario.id,
                    title: usuario.nome,
                    subtitle: `Usuário: ${usuario.nome}`
                }))),
                catchError(() => of([]))
            ),
            this.postagemService.listarTodas().pipe(
                map(postagens => postagens.filter(postagem =>
                    postagem.titulo.toLowerCase().includes(term)
                ).map(postagem => ({
                    type: 'postagem' as const,
                    id: postagem.id,
                    title: postagem.titulo,
                    subtitle: `Postagem: ${postagem.titulo}`
                }))),
                catchError(() => of([]))
            )
        ]).pipe(
            map(([salas, usuarios, postagens]) => {
                const items = [...salas, ...usuarios, ...postagens];
                // If we have search results, we don't show the default actions
                if (items.length > 0) {
                    return items;
                } else {
                    return this.getDefaultActions();
                }
            })
        ).subscribe(results => {
            this.resultados = results;
        });
    }

    private getDefaultActions(): CommandPaletteItem[] {
        return [
            {
                type: 'acao',
                title: 'Ir para Perfil',
                subtitle: 'Visualizar seu perfil',
                action: '/perfil'
            },
            {
                type: 'acao',
                title: 'Ir para Salas',
                subtitle: 'Ver todas as salas de aula',
                action: '/salas'
            },
            {
                type: 'acao',
                title: 'Ir para Feed',
                subtitle: 'Ver o feed acadêmico',
                action: '/feed'
            },
            {
                type: 'acao',
                title: 'Ir para Amigos',
                subtitle: 'Ver seus amigos',
                action: '/amigos'
            },
            {
                type: 'acao',
                title: 'Ir para Comunidades',
                subtitle: 'Ver grupos de estudo',
                action: '/comunidades'
            }
        ];
    }

    // This method will be called by the component when an item is selected
    executeAction(item: CommandPaletteItem) {
        if (item.type === 'acao' && item.action) {
            // action is now a string (route)
            return { action: 'navigate', route: item.action };
        }

        if (item.type === 'sala' && item.id) {
            return { action: 'navigate', route: `/salas/${item.id}` };
        }

        if (item.type === 'usuario' && item.id) {
            // For now, we'll just show an alert with the user info
            // In the future, we might navigate to a user profile page
            alert(`Usuário selecionado: ${item.title}\nID: ${item.id}`);
            return null; // No navigation
        }

        if (item.type === 'postagem' && item.id) {
            return { action: 'navigate', route: `/feed/postagem/${item.id}` };
            // Note: We don't have a route for a single postagem yet. We might need to create one.
            // For now, we'll just navigate to the feed and hope the post is visible? Not ideal.
            // We'll leave it as a TODO.
        }

        return null;
    }
}