import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { SalaService } from '../../services/sala.service';
import { SalaDeAula } from '../../models/sala.model';

@Component({
  selector: 'app-sala-sidebar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './sala-sidebar.component.html',
  styleUrl: './sala-sidebar.component.css'
})
export class SalaSidebarComponent implements OnInit {
  salas: SalaDeAula[] = [];
  carregando = true;

  constructor(
    private salaService: SalaService,
    private router: Router
  ) {}

  ngOnInit() {
    this.carregarSalas();
  }

  carregarSalas() {
    this.salaService.listarSalas().subscribe({
      next: (data) => {
        this.salas = data || [];
        this.carregando = false;
      },
      error: () => {
        this.carregando = false;
      }
    });
  }

  criarSala() {
    this.router.navigate(['/salas/criar']);
  }

  entrarEmSala() {
    const salaId = prompt('Digite o ID da sala:');
    if (salaId) {
      this.router.navigate(['/salas', salaId]);
    }
  }

  selecionarSala(salaId: number) {
    this.router.navigate(['/salas', salaId]);
  }
}
