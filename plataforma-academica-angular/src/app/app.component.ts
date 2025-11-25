import { Component, OnInit, PLATFORM_ID, Inject } from '@angular/core';
import { RouterOutlet, RouterModule, Router, NavigationEnd } from '@angular/router';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { SidebarComponent } from "./components/sidebar/sidebar.component";
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterModule, RouterOutlet, SidebarComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  title = 'ProjetoFinal';
  isLoggedIn = false;
  isHomePage = true;

  constructor(
    private router: Router,
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
    // Verificar se está no navegador antes de acessar localStorage
    if (isPlatformBrowser(this.platformId)) {
      const usuarioId = localStorage.getItem('usuarioId');
      this.isLoggedIn = !!usuarioId;
    } else {
      this.isLoggedIn = false;
    }
  }
}

