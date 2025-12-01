import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ThemeToggleComponent } from '../theme-toggle/theme-toggle.component';

@Component({
  selector: 'app-sala-navbar',
  standalone: true,
  imports: [CommonModule, ThemeToggleComponent],
  templateUrl: './sala-navbar.component.html',
  styleUrl: './sala-navbar.component.css'
})
export class SalaNavbarComponent {
  constructor(private router: Router) {}

  voltarParaFeed() {
    this.router.navigate(['/feed']);
  }
}
