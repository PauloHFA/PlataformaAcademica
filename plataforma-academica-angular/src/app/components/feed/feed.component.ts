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
        this.postagens = this.filtro === 'curtidas' ? postagens : postagens.sort((a, b) => (b.id || 0) - (a.id || 0));
        this.carregando = false;
      },
      error: () => {
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
    if (!this.mostrarForm) {
      this.novaPostagem = { titulo: '', conteudo: '' };
    }
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
    this.novaPostagem.plataformaId = 1; // ID fixo da plataforma

    this.postagemService.publicar(this.novaPostagem).subscribe({
      next: () => {
        this.novaPostagem = { titulo: '', conteudo: '' };
        this.mostrarForm = false;
        this.carregarPostagens();
        this.enviando = false;
      },
      error: () => {
        alert('Erro ao publicar');
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
