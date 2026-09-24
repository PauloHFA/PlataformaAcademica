import { Component, OnInit, PLATFORM_ID, Inject, Input } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { trigger, transition, style, animate } from '@angular/animations';
import { LayoutService } from '../../services/layout.service';

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
export class HeaderModernComponent {
    @Input() isLoggedIn = false;
    @Input() usuarioNome = '';
    mobileMenuOpen = false;
    isFocusMode = false;

    constructor(
        private router: Router,
        private layoutService: LayoutService,
        @Inject(PLATFORM_ID) private platformId: Object
    ) { }

    ngOnInit() {
        this.layoutService.focusMode$.subscribe(focusMode => {
            this.isFocusMode = focusMode;
        });
    }

    navigate(path: string) {
        this.router.navigate([path]);
        this.mobileMenuOpen = false;
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

    toggleFocusMode() {
        this.layoutService.toggleFocusMode();
    }
}
