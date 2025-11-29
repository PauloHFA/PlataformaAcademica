import { Component, OnInit, PLATFORM_ID, Inject } from '@angular/core';
import { RouterOutlet, RouterModule, Router, NavigationEnd } from '@angular/router';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { SidebarComponent } from "./components/sidebar/sidebar.component";
import { ThemeToggleComponent } from "./components/theme-toggle/theme-toggle.component";
import { NotificacoesComponent } from "./components/notificacoes/notificacoes.component";
import { ThemeService } from "./services/theme.service";
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterModule, RouterOutlet, SidebarComponent, ThemeToggleComponent, NotificacoesComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  title = 'ProjetoFinal';
  isLoggedIn = false;
  isHomePage = true;

  constructor(
    private router: Router,
    private themeService: ThemeService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit() {
    // Verificar login inicial (apenas no navegador)
    this.checkLoginStatus();
    
    // Verificar login a cada navegação
    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(() => {
        this.checkLoginStatus();
        this.isHomePage = this.router.url === '/' || this.router.url === '/home';
      });
  }

  checkLoginStatus() {
    if (isPlatformBrowser(this.platformId)) {
      const usuarioId = localStorage.getItem('usuarioId');
      const isAuthPage = this.router.url === '/' || 
                         this.router.url === '/home' || 
                         this.router.url === '/login' || 
                         this.router.url === '/cadastro';
      this.isLoggedIn = !!usuarioId && !isAuthPage;
    } else {
      this.isLoggedIn = false;
    }
  }
}

