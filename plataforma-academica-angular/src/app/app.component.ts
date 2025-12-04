import { Component, OnInit, PLATFORM_ID, Inject } from '@angular/core';
import { RouterOutlet, RouterModule, Router, NavigationEnd } from '@angular/router';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { NavbarComponent } from "./components/navbar/navbar.component";
import { SalaSidebarComponent } from "./components/sala-sidebar/sala-sidebar.component";
import { ChatFlutuanteComponent } from "./components/chat-flutuante/chat-flutuante.component";
import { routeAnimations } from './animations/route-animations';

import { ThemeService } from "./services/theme.service";
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterModule, RouterOutlet, NavbarComponent, SalaSidebarComponent, ChatFlutuanteComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
  animations: [routeAnimations]
})
export class AppComponent implements OnInit {
  title = 'ProjetoFinal';
  isLoggedIn = false;
  isHomePage = true;
  isSalaPage = false;

  constructor(
    private router: Router,
    private themeService: ThemeService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit() {
    this.checkLoginStatus();
    
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
      const isAuthPage = this.router.url === '/' || 
                         this.router.url === '/home' || 
                         this.router.url === '/login' || 
                         this.router.url === '/cadastro';
      
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
