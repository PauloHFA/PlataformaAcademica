import { Component, OnInit, PLATFORM_ID, Inject } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ChatService } from '../../services/chat.service';

export interface Connection {
    id: string;
    name: string;
    role: 'Professor' | 'Aluno';
    avatar: string;
    status: 'online' | 'offline' | 'away';
    lastSeen?: string;
    sala?: string;
}

@Component({
    selector: 'app-connections-panel',
    standalone: true,
    imports: [CommonModule, RouterModule, FormsModule],
    templateUrl: './connections-panel.component.html',
    styleUrl: './connections-panel.component.css'
})
export class ConnectionsPanelComponent implements OnInit {
    connections: Connection[] = [];
    onlineCount = 0;
    searchTerm = '';
    isLoading = false;
    isDrawerOpen = true;

    constructor(
        private chatService: ChatService,
        @Inject(PLATFORM_ID) private platformId: Object
    ) { }

    ngOnInit(): void {
        this.carregarConexoes();
    }

    get conexoesFiltradas(): Connection[] {
        let resultado = this.connections;

        if (this.searchTerm && this.searchTerm.trim() !== '') {
            const termo = this.searchTerm.toLowerCase().trim();
            resultado = resultado.filter(c =>
                c.name.toLowerCase().includes(termo) ||
                c.role.toLowerCase().includes(termo) ||
                (c.sala && c.sala.toLowerCase().includes(termo))
            );
        }

        return resultado;
    }

    get onlineConnections(): Connection[] {
        return this.conexoesFiltradas.filter(c => c.status === 'online');
    }

    get offlineConnections(): Connection[] {
        return this.conexoesFiltradas.filter(c => c.status !== 'online');
    }

    private carregarConexoes(): void {
        this.isLoading = true;

        const mockConnections: Connection[] = [
            { id: '1', name: 'Prof. Ana Silva', role: 'Professor', avatar: 'AS', status: 'online', sala: 'Spring Boot Advanced' },
            { id: '2', name: 'Carlos Mendes', role: 'Aluno', avatar: 'CM', status: 'online', sala: 'Engenharia de Software' },
            { id: '3', name: 'Mariana Costa', role: 'Aluno', avatar: 'MC', status: 'away', sala: 'Banco de Dados' },
            { id: '4', name: 'Dr. João Pereira', role: 'Professor', avatar: 'JP', status: 'online', sala: 'Programação Web' },
            { id: '5', name: 'Lucas Ferreira', role: 'Aluno', avatar: 'LF', status: 'offline', lastSeen: 'há 2 horas', sala: 'Spring Boot Advanced' },
            { id: '6', name: 'Sofia Alves', role: 'Aluno', avatar: 'SA', status: 'online', sala: 'Banco de Dados' },
            { id: '7', name: 'Rafael Souza', role: 'Professor', avatar: 'RS', status: 'online', sala: 'Engenharia de Software' },
            { id: '8', name: 'Camila Rocha', role: 'Aluno', avatar: 'CR', status: 'away', sala: 'Programação Web' },
            { id: '9', name: 'Pedro Lima', role: 'Aluno', avatar: 'PL', status: 'offline', lastSeen: 'ontem', sala: 'Spring Boot Advanced' },
            { id: '10', name: 'Juliana Martins', role: 'Professor', avatar: 'JM', status: 'online', sala: 'Banco de Dados' }
        ];

        setTimeout(() => {
            this.connections = mockConnections;
            this.onlineCount = mockConnections.filter(c => c.status === 'online').length;
            this.isLoading = false;
        }, 800);
    }

    abrirChat(usuario: Connection): void {
        this.chatService.abrirChat({
            usuarioId: usuario.id,
            usuarioNome: usuario.name,
            usuarioRole: usuario.role,
            avatar: usuario.avatar,
            status: usuario.status
        });
    }

    toggleDrawer(): void {
        this.isDrawerOpen = !this.isDrawerOpen;
    }

    getStatusColor(status: string): string {
        switch (status) {
            case 'online': return '#10b981';
            case 'away': return '#f59e0b';
            case 'offline': return '#9ca3af';
            default: return '#9ca3af';
        }
    }

    getStatusLabel(status: string): string {
        switch (status) {
            case 'online': return 'Online';
            case 'away': return 'Ausente';
            case 'offline': return 'Offline';
            default: return 'Desconectado';
        }
    }
}
