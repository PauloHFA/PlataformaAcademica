import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

/**
 * Componente Home - Página inicial da plataforma
 * Exibe login e cadastro com fundo acadêmico
 */
@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  title = 'Plataforma Acadêmica';

  constructor() {}

  ngOnInit(): void {
    // Home simples - apenas login e cadastro
  }
}

