import { Component, PLATFORM_ID, Inject } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators, FormGroup } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { SalaService } from '../../../services/sala.service';
import { SalaDeAula } from '../../../models/sala.model';

@Component({
  selector: 'app-sala-criar',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './sala-criar.html',
  styleUrl: './sala-criar.css'
})
export class SalaCriarComponent {
  form!: FormGroup;
  carregando = false;
  mensagem = '';

  constructor(
    private fb: FormBuilder,
    private salaService: SalaService,
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    this.form = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(3)]]
    });
  }

  submit(): void {
    if (this.form.invalid) return;
    this.carregando = true;
    const sala: SalaDeAula = {
      nome: this.form.value.nome
    };

    let criadorId = 0;
    if (isPlatformBrowser(this.platformId)) {
      const usuarioId = localStorage.getItem('usuarioId');
      if (usuarioId) {
        criadorId = parseInt(usuarioId);
      }
    }

    console.log('Criando sala com criadorId:', criadorId);
    this.salaService.criarSala(sala, criadorId).subscribe({
      next: (created) => {
        console.log('Sala criada:', created);
        alert('Sala criada com sucesso!');
        this.carregando = false;
        this.router.navigate(['/salas']);
      },
      error: (err: Error) => {
        console.error('Erro ao criar sala:', err);
        this.mensagem = err.message || 'Erro ao criar sala';
        this.carregando = false;
      }
    });
  }
}
