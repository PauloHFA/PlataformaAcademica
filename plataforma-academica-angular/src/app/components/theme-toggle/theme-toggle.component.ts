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
      background: var(--card-bg);
      border: 1px solid var(--border-color);
      border-radius: 50%;
      width: 40px;
      height: 40px;
      cursor: pointer;
      display: flex;
      align-items: center;
      justify-content: center;
      transition: all 0.2s ease;
      position: fixed;
      top: 16px;
      right: 16px;
      z-index: 1000;
      box-shadow: 0 1px 3px var(--shadow);
      color: var(--text-color);
    }

    .theme-toggle-btn:hover {
      transform: scale(1.05);
      background: var(--primary-color);
      border-color: var(--primary-color);
      color: var(--secondary-color);
      box-shadow: 0 4px 12px var(--shadow);
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