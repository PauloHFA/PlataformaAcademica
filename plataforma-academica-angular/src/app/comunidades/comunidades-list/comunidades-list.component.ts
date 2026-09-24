import { Component, OnInit } from '@angular/core';
import { ComunidadeService } from '../../services/comunidade.service';
import { Comunidade } from '../../models/comunidade.model';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-comunidades-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './comunidades-list.component.html',
  styleUrl: './comunidades-list.component.css'
})
export class ComunidadesListComponent implements OnInit {
  comunidades: Comunidade[] = [];
  carregando = true;
  usuarioId: string | null = null;

  constructor(
    private comunidadeService: ComunidadeService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.usuarioId = this.getCurrentUserId();
    this.carregarComunidades();
  }

  getCurrentUserId(): string | null {
    if (typeof localStorage !== 'undefined') {
      return localStorage.getItem('usuarioId');
    }
    return null;
  }

  carregarComunidades(): void {
    this.comunidadeService.listarTodas().subscribe({
      next: (comunidades) => {
        this.comunidades = comunidades;
        this.carregando = false;
      },
      error: (err) => {
        console.error('Erro ao carregar comunidades:', err);
        this.carregando = false;
      }
    });
  }

  entrarComunidade(id: string): void {
    if (!this.usuarioId) {
      alert('Você precisa estar logado');
      return;
    }

    this.comunidadeService.entrar(id, this.usuarioId).subscribe({
      next: () => {
        alert('Você entrou na comunidade com sucesso!');
        this.carregarComunidades();
      },
      error: (err) => {
        console.error('Erro ao entrar na comunidade:', err);
        alert('Erro ao entrar na comunidade');
      }
    });
  }

  sairComunidade(id: string): void {
    if (!this.usuarioId) {
      alert('Você precisa estar logado');
      return;
    }

    this.comunidadeService.sair(id, this.usuarioId).subscribe({
      next: () => {
        alert('Você saiu da comunidade com sucesso!');
        this.carregarComunidades();
      },
      error: (err) => {
        console.error('Erro ao sair da comunidade:', err);
        alert('Erro ao sair da comunidade');
      }
    });
  }

  criarComunidade(): void {
    // TODO: Implementar modal de criação de comunidade
    alert('Funcionalidade de criação de comunidade em desenvolvimento');
  }

  verDetalhes(id: string): void {
    this.router.navigate(['/comunidades', id]);
  }

  isMembro(comunidade: Comunidade): boolean {
    return !!this.usuarioId && !!comunidade.membros && comunidade.membros.includes(this.usuarioId);
  }
}
