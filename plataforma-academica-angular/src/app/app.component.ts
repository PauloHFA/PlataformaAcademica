import { Component, OnInit, PLATFORM_ID, Inject } from '@angular/core';
import { RouterOutlet, RouterModule, Router, NavigationEnd } from '@angular/router';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { NavbarComponent } from "./components/navbar/navbar.component";
import { SidebarComponent } from "./components/sidebar/sidebar.component";
import { ChatFlutuanteComponent } from "./components/chat-flutuante/chat-flutuante.component";
import { HeaderModernComponent } from "./components/header-modern/header-modern.component";
import { ConnectionsPanelComponent } from "./components/connections-panel/connections-panel.component";
import { CommandPaletteComponent } from "./components/command-palette/command-palette.component";
import { LayoutService } from "./services/layout.service";
import { routeAnimations } from './animations/route-animations';

import { ThemeService } from "./services/theme.service";
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterModule, RouterOutlet, NavbarComponent, SidebarComponent, ChatFlutuanteComponent, HeaderModernComponent, ConnectionsPanelComponent, CommandPaletteComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
  animations: [routeAnimations]
})
export class AppComponent implements OnInit {
  title = 'ProjetoFinal';
  isLoggedIn = false;
  isAuthenticated = false;
  usuarioNome = '';
  isHomePage = true;
  isSalaPage = false;
  isFocusMode = false;

  constructor(
    private router: Router,
    private themeService: ThemeService,
    private layoutService: LayoutService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) { }

  ngOnInit() {
    this.checkLoginStatus();
    this.layoutService.focusMode$.subscribe(focusMode => {
      this.isFocusMode = focusMode;
    });

    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(() => {
        this.checkLoginStatus();
        this.isSalaPage = this.router.url.includes('/salas');
        this.isHomePage = this.router.url === '/' || this.router.url === '/home';
      });
  }

  checkLoginStatus() {
    if (isPlatformBrowser(this.platformId)) {
      const usuarioId = localStorage.getItem('usuarioId');
      this.usuarioNome = localStorage.getItem('usuarioNome') || 'U';
      const isAuthPage = this.router.url === '/' ||
        this.router.url === '/home' ||
        this.router.url === '/login' ||
        this.router.url === '/cadastro';

      this.isAuthenticated = !!usuarioId;
      this.isLoggedIn = !!usuarioId && !isAuthPage;

      // Se não está logado e tenta acessar página protegida, redireciona para login
      if (!usuarioId && !isAuthPage) {
        this.router.navigate(['/login']);
      }
    } else {
      this.isLoggedIn = false;
    }
  }
}
