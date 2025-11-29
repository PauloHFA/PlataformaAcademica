import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterModule, Router } from '@angular/router';
import { SalaService } from '../../../services/sala.service';
import { UsuarioService } from '../../../services/usuario.service';
import { Usuario } from '../../../models/usuario.model';

@Component({
  selector: 'app-sala-adicionar-membro',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './sala-adicionar-membro.html',
  styleUrl: './sala-adicionar-membro.css'
})
export class SalaAdicionarMembroComponent implements OnInit {
  usuarios: Usuario[] = [];
  usuariosFiltrados: Usuario[] = [];
  busca = '';
  carregando = false;
  mensagem = '';
  salaId: number | null = null;

  constructor(
    private route: ActivatedRoute,
    private salaService: SalaService,
    private usuarioService: UsuarioService,
    private router: Router
  ) {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.salaId = isNaN(id) ? null : id;
  }

  ngOnInit(): void {
    this.carregarUsuarios();
  }

  carregarUsuarios(): void {
    this.carregando = true;
    this.usuarioService.listarUsuarios().subscribe({
      next: (usuarios) => {
        this.usuarios = usuarios;
        this.usuariosFiltrados = usuarios;
        this.carregando = false;
      },
      error: () => {
        this.mensagem = 'Erro ao carregar usuários';
        this.carregando = false;
      }
    });
  }

  filtrarUsuarios(): void {
    const termo = this.busca.toLowerCase().trim();
    if (!termo) {
      this.usuariosFiltrados = this.usuarios;
    } else {
      this.usuariosFiltrados = this.usuarios.filter(u =>
        u.nome?.toLowerCase().includes(termo) ||
        u.email?.toLowerCase().includes(termo) ||
        u.id?.toString().includes(termo)
      );
    }
  }

  adicionarMembro(membroId: number): void {
    if (!this.salaId) return;

    const criadorId = Number(localStorage.getItem('usuarioId'));
    if (!criadorId) {
      this.mensagem = 'Você precisa estar logado';
      return;
    }

    this.carregando = true;
    this.salaService.adicionarMembro(this.salaId, membroId, criadorId).subscribe({
      next: () => {
        this.mensagem = 'Membro adicionado com sucesso';
        setTimeout(() => this.router.navigate([`/salas/${this.salaId}`]), 1500);
      },
      error: (err: Error) => {
        this.mensagem = err.message || 'Erro ao adicionar membro';
        this.carregando = false;
      }
    });
  }
}
