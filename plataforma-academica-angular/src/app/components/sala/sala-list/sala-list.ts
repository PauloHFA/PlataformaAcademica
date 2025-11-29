import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { SalaService } from '../../../services/sala.service';
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

  constructor(private salaService: SalaService) {}

  ngOnInit(): void {
    this.currentUserId = this.getCurrentUserId();
    console.log('Usuario ID carregado:', this.currentUserId);
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
        this.salas = []; // Limpar lista em caso de erro
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
    } catch (e) { /* ignore */ }

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
    const usuarioIdVerificado = this.getCurrentUserId();
    
    if (sala.criadorId && usuarioIdVerificado !== sala.criadorId) {
      alert('Apenas o criador da sala pode deletá-la.');
      return;
    }
    
    const confirmado = confirm(`Tem certeza que deseja deletar a sala "${sala.nome}"?`);
    if (!confirmado) return;
    
    let usuarioId = usuarioIdVerificado || sala.criadorId || 1;
    console.log(`Tentando deletar sala ${sala.id} com usuário ${usuarioId}`);

    this.salaService.deletarSala(sala.id!, usuarioId).subscribe({
      next: () => {
        this.salas = this.salas.filter(s => s.id !== sala.id);
        alert('Sala deletada com sucesso!');
      },
      error: (err: Error) => {
        console.error('Erro ao deletar sala:', err);
        if (sala.criadorId && sala.criadorId !== usuarioId) {
          console.log(`Tentando novamente com criadorId ${sala.criadorId}`);
          this.salaService.deletarSala(sala.id!, sala.criadorId!).subscribe({
            next: () => {
              this.salas = this.salas.filter(s => s.id !== sala.id);
              alert('Sala deletada com sucesso!');
            },
            error: (err2: Error) => {
              this.erro = err2.message || 'Erro ao deletar sala';
              alert('Erro ao deletar: ' + this.erro);
            }
          });
        } else {
          this.erro = err.message || 'Erro ao deletar sala';
          alert('Erro ao deletar: ' + this.erro);
        }
      }
    });
  }

  forcarDelecao(sala: SalaDeAula): void {
    if (!sala || !sala.id) return;
    
    const confirmado = confirm(`FORÇAR DELEÇÃO da sala "${sala.nome}"?\n\nIsso tentará deletar usando o criadorId da sala.`);
    if (!confirmado) return;
    
    const usuarioId = sala.criadorId || this.getCurrentUserId() || 1;
    console.log(`Forçando deleção da sala ${sala.id} com usuário ${usuarioId}`);

    this.salaService.deletarSala(sala.id!, usuarioId).subscribe({
      next: () => {
        this.salas = this.salas.filter(s => s.id !== sala.id);
        alert('Sala deletada com sucesso!');
      },
      error: (err: Error) => {
        console.error('Erro ao forçar deleção:', err);
        alert('Erro ao deletar: ' + (err.message || 'Erro desconhecido'));
      }
    });
  }

  private getCurrentUserId(): number {
    try {
      // checa várias chaves possíveis no localStorage para compatibilidade
      const usuarioStr = localStorage.getItem('usuario');
      if (usuarioStr) {
        const obj = JSON.parse(usuarioStr);
        if (obj && (obj.id || obj.id === 0)) return Number(obj.id) || 0;
      }

      const usuarioIdStr = localStorage.getItem('usuarioId') || localStorage.getItem('userId') || localStorage.getItem('user');
      if (usuarioIdStr) {
        // pode ser um número direto ou um JSON
        const parsed = Number(usuarioIdStr);
        if (!isNaN(parsed)) return parsed;
        try {
          const maybe = JSON.parse(usuarioIdStr);
          if (maybe && (maybe.id || maybe.id === 0)) return Number(maybe.id) || 0;
        } catch (e) { /* ignore */ }
      }
    } catch (e) {
      // ignore
    }
    return 1;
  }
}
