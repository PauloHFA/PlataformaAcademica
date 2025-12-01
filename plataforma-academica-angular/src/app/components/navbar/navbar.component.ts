import { Component, OnInit, PLATFORM_ID, Inject } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { UsuarioService } from '../../services/usuario.service';
import { NotificacoesComponent } from '../notificacoes/notificacoes.component';
import { ThemeToggleComponent } from '../theme-toggle/theme-toggle.component';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule, NotificacoesComponent, ThemeToggleComponent],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent implements OnInit {
  usuarioNome = '';
  usuarioEmail = '';
  dropdownAberto = false;
  menuDropdownAberto = false;

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

  toggleDropdown() {
    this.dropdownAberto = !this.dropdownAberto;
  }

  fecharDropdown() {
    this.dropdownAberto = false;
  }

  toggleMenuDropdown() {
    this.menuDropdownAberto = !this.menuDropdownAberto;
  }

  fecharMenuDropdown() {
    this.menuDropdownAberto = false;
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
    this.fecharDropdown();
    this.fecharMenuDropdown();
  }
}
