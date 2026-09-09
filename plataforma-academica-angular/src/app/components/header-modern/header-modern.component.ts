import { Component, OnInit, PLATFORM_ID, Inject } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { trigger, transition, style, animate } from '@angular/animations';

@Component({
    selector: 'app-header-modern',
    standalone: true,
    imports: [CommonModule, RouterModule],
    templateUrl: './header-modern.component.html',
    styleUrl: './header-academic.component.css',
    animations: [
        trigger('slideDown', [
            transition(':enter', [
                style({ transform: 'translateY(-100%)', opacity: 0 }),
                animate('300ms ease-out', style({ transform: 'translateY(0)', opacity: 1 }))
            ])
        ])
    ]
})
export class HeaderModernComponent implements OnInit {
    isLoggedIn = false;
    usuarioNome = '';
    mobileMenuOpen = false;

    constructor(
        private router: Router,
        @Inject(PLATFORM_ID) private platformId: Object
    ) { }

    ngOnInit() {
        this.checkLoginStatus();
    }

    checkLoginStatus() {
        if (isPlatformBrowser(this.platformId)) {
            const usuario = localStorage.getItem('usuario');
            if (usuario) {
                try {
                    const usuarioData = JSON.parse(usuario);
                    this.isLoggedIn = true;
                    this.usuarioNome = usuarioData.nome?.split(' ')[0] || 'Usuário';
                } catch (e) {
                    this.isLoggedIn = false;
                }
            }
        }
    }

    logout() {
        if (isPlatformBrowser(this.platformId)) {
            localStorage.removeItem('usuario');
            localStorage.removeItem('usuarioId');
            localStorage.removeItem('token');
            this.isLoggedIn = false;
            this.router.navigate(['/']);
        }
    }

    toggleMobileMenu() {
        this.mobileMenuOpen = !this.mobileMenuOpen;
    }

    navigate(path: string) {
        this.router.navigate([path]);
        this.mobileMenuOpen = false;
    }
}
