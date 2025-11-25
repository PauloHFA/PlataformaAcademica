import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators, FormGroup } from '@angular/forms';
import { ActivatedRoute, RouterModule, Router } from '@angular/router';
import { SalaService } from '../../../services/sala.service';

@Component({
  selector: 'app-sala-adicionar-membro',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './sala-adicionar-membro.html',
  styleUrl: './sala-adicionar-membro.css'
})
export class SalaAdicionarMembroComponent {
  form!: FormGroup;
  carregando = false;
  mensagem = '';

  private salaId: number | null = null;

  constructor(private fb: FormBuilder, private route: ActivatedRoute, private salaService: SalaService, private router: Router) {
    this.form = this.fb.group({ membroId: ['', [Validators.required]] });
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.salaId = isNaN(id) ? null : id;
  }

  submit(): void {
    if (!this.salaId || this.form.invalid) return;
    this.carregando = true;
    const membroId = Number(this.form.value.membroId);

    let criadorId = 0;
    try { criadorId = JSON.parse(localStorage.getItem('usuario') || 'null')?.id || 0; } catch(e) { }

    if (!criadorId) {
      this.mensagem = 'Você precisa estar logado como criador para adicionar membros.';
      this.carregando = false;
      return;
    }

    this.salaService.adicionarMembro(this.salaId, membroId, criadorId).subscribe({
      next: () => { this.mensagem = 'Membro adicionado com sucesso'; this.carregando = false; this.router.navigate([`/salas/${this.salaId}`]); },
      error: (err: Error) => { this.mensagem = err.message || 'Erro ao adicionar membro'; this.carregando = false; }
    });
  }
}
