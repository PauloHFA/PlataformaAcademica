import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ThemeToggleComponent } from '../theme-toggle/theme-toggle.component';
import { SalaContextService } from '../../services/sala-context.service';

@Component({
  selector: 'app-sala-navbar',
  standalone: true,
  imports: [CommonModule, ThemeToggleComponent],
  templateUrl: './sala-navbar.component.html',
  styleUrl: './sala-navbar.component.css'
})
export class SalaNavbarComponent implements OnInit {
  nomeSala$;

  constructor(
    private router: Router,
    private salaContext: SalaContextService
  ) {
    this.nomeSala$ = this.salaContext.nomeSala$;
  }

  ngOnInit() {}

  voltarParaFeed() {
    this.router.navigate(['/feed']);
  }
}
