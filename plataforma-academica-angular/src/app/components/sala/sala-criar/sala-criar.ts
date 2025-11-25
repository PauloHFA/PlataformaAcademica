import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
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

  constructor(private fb: FormBuilder, private salaService: SalaService, private router: Router) {
    this.form = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(3)]],
      descricao: [''],
      codigoAcesso: ['']
    });
  }

  submit(): void {
    if (this.form.invalid) return;
    this.carregando = true;
    const sala: SalaDeAula = {
      nome: this.form.value.nome,
      descricao: this.form.value.descricao,
      codigoAcesso: this.form.value.codigoAcesso
    };

    // Para teste inicial, usamos id do usuário logado se existir
    let criadorId = 0;
    try {
      const usuarioStr = localStorage.getItem('usuario');
      if (usuarioStr) criadorId = JSON.parse(usuarioStr).id || 0;
    } catch (e) { /* ignore */ }

    this.salaService.criarSala(sala, criadorId).subscribe({
      next: (created) => {
        this.mensagem = 'Sala criada com sucesso.';
        this.carregando = false;
        this.router.navigate(['/salas']);
      },
      error: (err: Error) => {
        this.mensagem = err.message || 'Erro ao criar sala';
        this.carregando = false;
      }
    });
  }
}
