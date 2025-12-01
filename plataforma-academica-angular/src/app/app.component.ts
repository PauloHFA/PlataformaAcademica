import { Component, OnInit, PLATFORM_ID, Inject } from '@angular/core';
import { RouterOutlet, RouterModule, Router, NavigationEnd } from '@angular/router';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { NavbarComponent } from "./components/navbar/navbar.component";
import { SalaSidebarComponent } from "./components/sala-sidebar/sala-sidebar.component";
import { SalaNavbarComponent } from "./components/sala-navbar/sala-navbar.component";
import { ThemeToggleComponent } from "./components/theme-toggle/theme-toggle.component";
import { ThemeService } from "./services/theme.service";
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterModule, RouterOutlet, NavbarComponent, SalaSidebarComponent, SalaNavbarComponent, ThemeToggleComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
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
    } else {
      this.isLoggedIn = false;
    }
  }
}
