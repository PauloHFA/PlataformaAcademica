import { Component, PLATFORM_ID, Inject } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators, FormGroup } from '@angular/forms';
import { ActivatedRoute, RouterModule, Router } from '@angular/router';
import { SalaService } from '../../../services/sala.service';
import { Atividade } from '../../../models/atividade.model';

@Component({
  selector: 'app-atividade-criar',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './atividade-criar.html',
  styleUrl: './atividade-criar.css'
})
export class AtividadeCriarComponent {
  form!: FormGroup;
  carregando = false;
  mensagem = '';
  private salaId: number | null = null;
  selectedFile: File | null = null;
  previewDocumento: string | null = null;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private salaService: SalaService,
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    this.form = this.fb.group({
      titulo: ['', [Validators.required, Validators.minLength(3)]],
      descricao: ['', Validators.required],
      tipoDocumentoSubmissao: ['PDF'],
      dataEntrega: ['', Validators.required],
      pontos: [0, [Validators.required, Validators.min(0)]]
    });
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.salaId = isNaN(id) ? null : id;
  }

  submit(): void {
    if (!this.salaId || this.form.invalid) {
      this.mensagem = 'Preencha todos os campos obrigatórios';
      return;
    }

    this.carregando = true;
    
    let criadorId = 0;
    if (isPlatformBrowser(this.platformId)) {
      criadorId = parseInt(localStorage.getItem('usuarioId') || '0', 10);
    }

    if (!criadorId) {
      this.mensagem = 'Você precisa estar logado para criar atividades.';
      this.carregando = false;
      return;
    }

    if (this.selectedFile) {
      const formData = new FormData();
      formData.append('documento', this.selectedFile);
      formData.append('titulo', this.form.value.titulo);
      formData.append('descricao', this.form.value.descricao);
      formData.append('tipoDocumentoSubmissao', this.form.value.tipoDocumentoSubmissao || 'PDF');
      formData.append('dataEntrega', this.form.value.dataEntrega);
      formData.append('pontos', this.form.value.pontos || '0');
      formData.append('salaId', this.salaId.toString());
      formData.append('autorId', criadorId.toString());

      this.salaService.criarAtividadeComDocumento(this.salaId, criadorId, formData).subscribe({
        next: () => {
          this.mensagem = 'Atividade criada com sucesso!';
          this.carregando = false;
          setTimeout(() => this.router.navigate([`/salas/${this.salaId}`]), 1500);
        },
        error: (err: any) => {
          this.mensagem = err.error?.message || 'Erro ao criar atividade';
          this.carregando = false;
        }
      });
      return;
    }

    const atividade: Atividade = {
      titulo: this.form.value.titulo,
      descricao: this.form.value.descricao,
      tipoDocumentoSubmissao: this.form.value.tipoDocumentoSubmissao || 'PDF',
      dataEntrega: this.form.value.dataEntrega,
      pontos: parseFloat(this.form.value.pontos) || 0
    };

    this.salaService.criarAtividade(this.salaId, criadorId, atividade).subscribe({
      next: () => {
        this.mensagem = 'Atividade criada com sucesso!';
        this.carregando = false;
        setTimeout(() => {
          this.router.navigate([`/salas/${this.salaId}`]);
        }, 1500);
      },
      error: (err: any) => {
        this.mensagem = err.error?.message || err.message || 'Erro ao criar atividade';
        this.carregando = false;
      }
    });
  }

  onDocumentoSelecionado(event: any): void {
    const arquivo: File = event.target.files && event.target.files[0];
    if (arquivo) {
      this.selectedFile = arquivo;
      this.previewDocumento = arquivo.name;
    }
  }

  removerDocumento(): void {
    this.selectedFile = null;
    this.previewDocumento = null;
  }
}
