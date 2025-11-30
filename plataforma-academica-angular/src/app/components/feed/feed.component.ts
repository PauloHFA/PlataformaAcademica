import { Component, OnInit, PLATFORM_ID, Inject } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PostagemService } from '../../services/postagem.service';
import { Postagem } from '../../models/postagem.model';

@Component({
  selector: 'app-feed',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './feed.component.html',
  styleUrl: './feed.component.css'
})
export class FeedComponent implements OnInit {
  postagens: Postagem[] = [];
  carregando = true;
  currentUserId: number | null = null;
  filtro: 'todas' | 'amigos' | 'curtidas' = 'todas';
  
  // Form nova postagem
  mostrarForm = false;
  novaPostagem: Postagem = { titulo: '', conteudo: '' };
  enviando = false;
  previewImagem: string | null = null;
  selectedFile: File | null = null;

  constructor(
    private postagemService: PostagemService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit(): void {
    this.currentUserId = this.getCurrentUserId();
    this.carregarPostagens();
  }

  getCurrentUserId(): number | null {
    if (isPlatformBrowser(this.platformId)) {
      const id = localStorage.getItem('usuarioId');
      return id ? Number(id) : null;
    }
    return null;
  }

  carregarPostagens(): void {
    this.carregando = true;
    let observable;
    
    if (this.filtro === 'amigos' && this.currentUserId) {
      observable = this.postagemService.listarDeAmigos(this.currentUserId);
    } else if (this.filtro === 'curtidas') {
      observable = this.postagemService.listarMaisCurtidas();
    } else {
      observable = this.postagemService.listarTodas();
    }
    
    observable.subscribe({
      next: (postagens) => {
        console.log('Postagens carregadas:', postagens);
        this.postagens = this.filtro === 'curtidas' ? postagens : postagens.sort((a, b) => (b.id || 0) - (a.id || 0));
        this.carregando = false;
      },
      error: (err) => {
        console.error('Erro ao carregar postagens:', err);
        this.carregando = false;
      }
    });
  }

  mudarFiltro(filtro: 'todas' | 'amigos' | 'curtidas'): void {
    this.filtro = filtro;
    this.carregarPostagens();
  }

  curtir(id: number | undefined): void {
    if (!id) return;
    
    this.postagemService.curtir(id).subscribe({
      next: () => {
        this.carregarPostagens();
      },
      error: () => {
        alert('Erro ao curtir');
      }
    });
  }

  toggleForm(): void {
    this.mostrarForm = !this.mostrarForm;
    console.log('Form visível:', this.mostrarForm);
    if (!this.mostrarForm) {
      this.novaPostagem = { titulo: '', conteudo: '' };
      this.previewImagem = null;
      this.selectedFile = null;
    }
  }

  onImagemSelecionada(event: any): void {
    const arquivo: File = event.target.files && event.target.files[0];
    if (arquivo) {
      this.selectedFile = arquivo;
      const leitor = new FileReader();
      leitor.onload = (e: any) => {
        this.previewImagem = e.target.result as string;
      };
      leitor.readAsDataURL(arquivo);
    } else {
      this.selectedFile = null;
      this.previewImagem = null;
    }
  }

  removerImagem(): void {
    this.previewImagem = null;
    this.selectedFile = null;
    this.novaPostagem.imagemUrl = undefined;
  }

  publicar(): void {
    if (!this.novaPostagem.titulo.trim() || !this.novaPostagem.conteudo.trim()) {
      alert('Preencha título e conteúdo');
      return;
    }

    if (!this.currentUserId) {
      alert('Você precisa estar logado');
      return;
    }

    this.enviando = true;
    this.novaPostagem.autorId = this.currentUserId;

    if (this.selectedFile) {
      const form = new FormData();
      form.append('imagem', this.selectedFile, this.selectedFile.name);
      form.append('titulo', this.novaPostagem.titulo);
      form.append('conteudo', this.novaPostagem.conteudo);
      form.append('autorId', String(this.novaPostagem.autorId));

      console.log('Publicando com imagem...');
      this.postagemService.publicarComImagem(form).subscribe({
        next: (res) => {
          console.log('Postagem criada:', res);
          alert('Postagem publicada com sucesso!');
          this.novaPostagem = { titulo: '', conteudo: '' };
          this.previewImagem = null;
          this.selectedFile = null;
          this.mostrarForm = false;
          this.enviando = false;
          setTimeout(() => this.carregarPostagens(), 500);
        },
        error: (err) => {
          console.error('Erro ao publicar:', err);
          alert('Erro ao publicar postagem');
          this.enviando = false;
        }
      });
      return;
    }

    console.log('Publicando sem imagem...');
    this.postagemService.publicar(this.novaPostagem).subscribe({
      next: (res) => {
        console.log('Postagem criada:', res);
        alert('Postagem publicada com sucesso!');
        this.novaPostagem = { titulo: '', conteudo: '' };
        this.previewImagem = null;
        this.mostrarForm = false;
        this.enviando = false;
        setTimeout(() => this.carregarPostagens(), 500);
      },
      error: (err) => {
        console.error('Erro ao publicar:', err);
        alert('Erro ao publicar postagem');
        this.enviando = false;
      }
    });
  }

  deletar(id: number | undefined): void {
    if (!id || !confirm('Deseja deletar esta postagem?')) return;

    this.postagemService.deletar(id).subscribe({
      next: () => {
        this.carregarPostagens();
      },
      error: () => {
        alert('Erro ao deletar');
      }
    });
  }
}
