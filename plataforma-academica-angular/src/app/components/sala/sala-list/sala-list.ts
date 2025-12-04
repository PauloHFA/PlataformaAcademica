import { Component, OnInit, PLATFORM_ID, Inject } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { RouterModule } from '@angular/router';
import { SalaService } from '../../../services/sala.service';
import { SolicitacaoEntradaService } from '../../../services/solicitacao-entrada.service';
import { SalaDeAula } from '../../../models/sala.model';

@Component({
  selector: 'app-sala-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './sala-list.html',
  styleUrl: './sala-list.css'
})
export class SalaListComponent implements OnInit {
  salas: SalaDeAula[] = [];
  carregando = true;
  erro = '';
  criando = false;
  mensagemCriar = '';
  currentUserId: number | null = null;
  isProfessor = false;

  constructor(
    private salaService: SalaService,
    private solicitacaoService: SolicitacaoEntradaService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit(): void {
    this.currentUserId = this.getCurrentUserId();
    this.isProfessor = this.checkIsProfessor();
    console.log('Usuario ID carregado:', this.currentUserId, 'isProfessor:', this.isProfessor);
    this.carregarSalas();
  }

  carregarSalas(): void {
    this.carregando = true;
    this.erro = '';
    this.salaService.listarSalas().subscribe({
      next: (data) => {
        this.salas = data || [];
        console.log('=== DEBUG SALAS ===');
        console.log('Usuario atual ID:', this.currentUserId);
        this.salas.forEach(s => {
          console.log(`Sala: ${s.nome}, criadorId: ${s.criadorId}, mostra delete: ${s.criadorId === this.currentUserId}`);
        });
        this.carregando = false;
        this.erro = '';
      },
      error: (err: Error) => {
        console.error('Erro ao listar salas', err);
        this.erro = err.message || 'Erro ao listar salas';
        this.salas = [];
        this.carregando = false;
      }
    });
  }

  createTestSala(): void {
    this.mensagemCriar = '';
    this.criando = true;
    const exemplo: SalaDeAula = {
      nome: 'Sala de Teste ' + new Date().toISOString().slice(0,19)
    };

    let criadorId = 0;
    try {
      const usuarioStr = localStorage.getItem('usuario');
      if (usuarioStr) criadorId = JSON.parse(usuarioStr).id || 0;
    } catch (e) { }

    this.salaService.criarSala(exemplo, criadorId).subscribe({
      next: (created) => {
        this.salas.unshift(created);
        this.mensagemCriar = 'Sala de teste criada com sucesso.';
        this.criando = false;
      },
      error: (err: Error) => {
        console.error('Erro ao criar sala de teste', err);
        this.mensagemCriar = err.message || 'Erro ao criar sala de teste';
        this.criando = false;
      }
    });
  }

  deleteSala(sala: SalaDeAula): void {
    if (!sala || !sala.id) return;
    
    if (sala.criadorId !== this.currentUserId) {
      alert('Apenas o criador da sala pode deletá-la.');
      return;
    }
    
    const confirmado = confirm(`Tem certeza que deseja deletar a sala "${sala.nome}"?`);
    if (!confirmado) return;
    
    const usuarioId = this.currentUserId || sala.criadorId || 1;
    console.log(`Deletando sala ${sala.id} com usuário ${usuarioId}`);

    this.salaService.deletarSala(sala.id!, usuarioId).subscribe({
      next: () => {
        this.salas = this.salas.filter(s => s.id !== sala.id);
        alert('Sala deletada com sucesso!');
      },
      error: (err: Error) => {
        console.error('Erro ao deletar sala:', err);
        alert('Erro ao deletar: ' + (err.message || 'Erro desconhecido'));
      }
    });
  }

  private getCurrentUserId(): number | null {
    if (!isPlatformBrowser(this.platformId)) return null;
    
    try {
      const usuarioId = localStorage.getItem('usuarioId');
      if (usuarioId) {
        const parsed = parseInt(usuarioId);
        return !isNaN(parsed) ? parsed : null;
      }
    } catch (e) {
      console.error('Erro ao obter usuarioId:', e);
    }
    return null;
  }

  private checkIsProfessor(): boolean {
    if (!isPlatformBrowser(this.platformId)) return false;
    return localStorage.getItem('isProfessor') === 'true';
  }

  solicitarEntrada(sala: SalaDeAula): void {
    if (!sala.id || !this.currentUserId) return;
    this.solicitacaoService.solicitarEntrada(sala.id, this.currentUserId).subscribe({
      next: () => {
        alert('Solicitação enviada com sucesso! Aguarde aprovação do professor.');
      },
      error: (err: Error) => {
        alert(err.message || 'Erro ao enviar solicitação');
      }
    });
  }
}
