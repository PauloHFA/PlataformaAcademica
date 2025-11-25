import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UsuarioService } from '../../../services/usuario.service';
import { Usuario } from '../../../models/usuario.model';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

/**
 * Componente para listar todos os usuários cadastrados
 * Exibe uma tabela com os dados dos usuários e permite gerenciar a listagem
 */
@Component({
  selector: 'app-usuario-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './usuario-list.html',
  styleUrl: './usuario-list.css'
})
export class UsuarioListComponent implements OnInit, OnDestroy {
  usuarios: Usuario[] = []; // Lista de usuários
  carregando = false;
  mensagemErro = '';
  private destroy$ = new Subject<void>();

  constructor(private usuarioService: UsuarioService) { }

  ngOnInit(): void {
    this.listarUsuarios();
  }

  /**
   * Carrega a lista de todos os usuários do backend
   */
  listarUsuarios(): void {
    this.carregando = true;
    this.mensagemErro = '';
    this.usuarios = []; // Limpar lista anterior

    this.usuarioService.listarUsuarios()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (data: Usuario[]) => {
          console.log('Resposta do backend:', data);
          console.log('Tipo da resposta:', typeof data);
          console.log('É array?', Array.isArray(data));
          this.usuarios = data || [];
          this.carregando = false;
          console.log('Usuários carregados:', this.usuarios.length);
          console.log('Dados dos usuários:', this.usuarios);
          if (this.usuarios.length === 0) {
            console.log('Nenhum usuário encontrado no backend');
          }
        },
        error: (err: Error) => {
          console.error('Erro ao listar usuários', err);
          // Mensagem mais amigável para erro de conexão
          if (err.message.includes('conectar ao servidor') || err.message.includes('localhost:8080')) {
            this.mensagemErro = 'O backend não está respondendo. Verifique se o servidor Spring Boot está rodando e acessível em http://localhost:8080';
          } else {
            this.mensagemErro = err.message || 'Erro ao carregar a lista de usuários. Tente novamente mais tarde.';
          }
          this.carregando = false;
          this.usuarios = []; // Garantir que a lista está vazia em caso de erro
        }
      });
  }

  /**
   * Função de trackBy para melhor performance do ngFor
   */
  trackByUsuarioId(index: number, usuario: Usuario): any {
    return usuario.id || index;
  }

  /**
   * Limpa recursos ao destruir o componente
   */
  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}