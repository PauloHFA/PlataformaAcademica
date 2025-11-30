import { Component, OnInit, PLATFORM_ID, Inject } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { UsuarioService } from '../../services/usuario.service';
import { NotificacoesComponent } from '../notificacoes/notificacoes.component';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterModule, NotificacoesComponent],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css'
})
export class SidebarComponent implements OnInit {
  isOpen = true;
  usuarioNome = '';
  usuarioEmail = '';

  constructor(
    private usuarioService: UsuarioService,
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit() {
    this.carregarDadosUsuario();
  }

  carregarDadosUsuario() {
    if (isPlatformBrowser(this.platformId)) {
      const usuarioId = localStorage.getItem('usuarioId');
      if (usuarioId) {
        this.usuarioService.buscarPorId(parseInt(usuarioId)).subscribe(
          usuario => {
            this.usuarioNome = usuario.nome;
            this.usuarioEmail = usuario.email;
          },
          error => console.error('Erro ao carregar usuário:', error)
        );
      }
    }
  }

  toggleSidebar() {
    this.isOpen = !this.isOpen;
  }

  logout() {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.removeItem('usuarioId');
      localStorage.removeItem('token');
    }
    this.router.navigate(['/home']);
  }

  navegarPara(rota: string) {
    this.router.navigate([rota]);
  }
}
