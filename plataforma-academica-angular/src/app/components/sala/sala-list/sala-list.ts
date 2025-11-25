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

  constructor(private salaService: SalaService) {}

  ngOnInit(): void {
    this.carregarSalas();
  }

  carregarSalas(): void {
    this.carregando = true;
    this.erro = ''; // Limpar erro anterior
    this.salaService.listarSalas().subscribe({
      next: (data) => {
        this.salas = data || [];
        this.carregando = false;
        this.erro = ''; // Limpar erro em caso de sucesso
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
      nome: 'Sala de Teste ' + new Date().toISOString().slice(0,19),
      descricao: 'Sala criada automaticamente para teste'
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
}
