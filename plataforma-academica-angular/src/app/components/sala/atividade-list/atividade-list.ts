import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { SalaService } from '../../../services/sala.service';
import { Atividade } from '../../../models/atividade.model';

@Component({
  selector: 'app-atividade-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './atividade-list.html',
  styleUrl: './atividade-list.css'
})
export class AtividadeListComponent implements OnInit {
  atividades: Atividade[] = [];
  carregando = true;
  salaId: number | null = null;

  constructor(private route: ActivatedRoute, private salaService: SalaService) {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.salaId = isNaN(id) ? null : id;
  }

  ngOnInit(): void {
    if (!this.salaId) { this.carregando = false; return; }
    this.salaService.listarAtividades(this.salaId).subscribe({
      next: (a) => { this.atividades = a || []; this.carregando = false; },
      error: (err: Error) => { console.warn('Erro ao listar atividades', err); this.carregando = false; }
    });
  }
}
