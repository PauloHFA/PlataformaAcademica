import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ThemeService } from '../../services/theme.service';

@Component({
  selector: 'app-theme-toggle',
  standalone: true,
  imports: [CommonModule],
  template: `
    <button 
      (click)="toggleTheme()" 
      class="theme-toggle-btn"
      [attr.aria-label]="(themeService.isDarkMode$ | async) ? 'Ativar modo claro' : 'Ativar modo escuro'"
    >
      <span class="theme-icon">
        {{ (themeService.isDarkMode$ | async) ? '☀️' : '🌙' }}
      </span>
    </button>
  `,
  styles: [`
    .theme-toggle-btn {
      background: none;
      border: 2px solid var(--primary-color);
      border-radius: 50%;
      width: 45px;
      height: 45px;
      cursor: pointer;
      display: flex;
      align-items: center;
      justify-content: center;
      transition: all 0.3s ease;
      position: fixed;
      top: 20px;
      right: 20px;
      z-index: 1000;
      background-color: var(--bg-color);
    }

    .theme-toggle-btn:hover {
      transform: scale(1.1);
      box-shadow: 0 4px 12px rgba(24, 24, 35, 0.2);
    }

    .theme-icon {
      font-size: 20px;
      transition: transform 0.3s ease;
    }

    .theme-toggle-btn:hover .theme-icon {
      transform: rotate(20deg);
    }
  `]
})
export class ThemeToggleComponent {
  constructor(public themeService: ThemeService) {}

  toggleTheme() {
    this.themeService.toggleTheme();
  }
}