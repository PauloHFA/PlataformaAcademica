import { Component, OnInit, PLATFORM_ID, Inject } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PostagemService } from '../../services/postagem.service';
import { ComentarioService } from '../../services/comentario.service';
import { BadgeService } from '../../services/badge.service';
import { Postagem } from '../../models/postagem.model';
import { Comentario } from '../../models/comentario.model';
import { Badge } from '../../models/badge.model';
import { TimeAgoPipe } from '../../pipes/time-ago.pipe';
import { MarkdownPipe } from '../../pipes/markdown.pipe';
import { forkJoin, of, switchMap, map, catchError } from 'rxjs';

@Component({
  selector: 'app-feed',
  standalone: true,
  imports: [CommonModule, FormsModule, TimeAgoPipe, MarkdownPipe],
  templateUrl: './feed.component.html',
  styleUrl: './feed.component.css'
})
export class FeedComponent implements OnInit {
  postagens: Postagem[] = [];
  carregando = true;
  currentUserId: string | null = null;
  filtro: 'todas' | 'amigos' | 'curtidas' = 'todas';

  // Form nova postagem
  mostrarForm = false;
  novaPostagem: Postagem = { titulo: '', conteudo: '' };
  enviando = false;
  previewImagem: string | null = null;
  selectedFile: File | null = null;

  // Comentários
  mostrarComentarios: { [key: string]: boolean } = {};
  comentarios: { [key: string]: Comentario[] } = {};
  novoComentario: { [key: string]: string } = {};

  constructor(
    private postagemService: PostagemService,
    private comentarioService: ComentarioService,
    private badgeService: BadgeService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) { }

  ngOnInit(): void {
    this.currentUserId = this.getCurrentUserId();
    this.usuarioNome = this.getCurrentUserName();
    this.carregarPostagens();
  }

  usuarioNome: string | null = null;

  getCurrentUserId(): string | null {
    if (isPlatformBrowser(this.platformId)) {
      return localStorage.getItem('usuarioId');
    }
    return null;
  }

  getCurrentUserName(): string | null {
    if (isPlatformBrowser(this.platformId)) {
      return localStorage.getItem('usuarioNome');
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

    observable.pipe(
      map(postagens => postagens.map(p => ({
        ...p,
        imagemUrl: p.imagemUrl && !p.imagemUrl.startsWith('http')
          ? `http://localhost:8090${p.imagemUrl}`
          : p.imagemUrl
      }))),
      switchMap(postagensWithImage => {
        const uniqueAuthorIds = [...new Set(postagensWithImage.map(p => p.autorId).filter((id): id is string => id !== undefined && id !== null))];
        if (uniqueAuthorIds.length === 0) {
          return of(postagensWithImage);
        }
        const badgeRequests = uniqueAuthorIds.map(id =>
          this.badgeService.buscarBadgesDoUsuario(id).pipe(
            catchError(() => of([] as Badge[]))
          )
        );
        return forkJoin(badgeRequests).pipe(
          map(badgeArrays => {
            const badgeMap = new Map<string, Badge[]>();
            uniqueAuthorIds.forEach((id, index) => {
              badgeMap.set(id, badgeArrays[index]);
            });
            return postagensWithImage.map(post => ({
              ...post,
              autorBadges: badgeMap.get(post.autorId!) || []
            }));
          })
        );
      })
    ).subscribe({
      next: (postagens) => {
        console.log('Postagens carregadas com badges:', postagens);
        this.postagens = postagens;
        this.postagens = this.filtro === 'curtidas' ? this.postagens : this.postagens;
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

  curtir(id: string | undefined): void {
    if (!id || !this.currentUserId) return;

    this.postagemService.curtir(id, this.currentUserId).subscribe({
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

  deletar(id: string | undefined): void {
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

  toggleComentarios(postagemId: string): void {
    this.mostrarComentarios[postagemId] = !this.mostrarComentarios[postagemId];
    if (this.mostrarComentarios[postagemId] && !this.comentarios[postagemId]) {
      this.carregarComentarios(postagemId);
    }
  }

  carregarComentarios(postagemId: string): void {
    this.comentarioService.listarPorPostagem(postagemId).subscribe({
      next: (comentarios) => {
        this.comentarios[postagemId] = comentarios;
      },
      error: () => {
        alert('Erro ao carregar comentários');
      }
    });
  }

  adicionarComentario(postagemId: string): void {
    const conteudo = this.novoComentario[postagemId]?.trim();
    if (!conteudo || !this.currentUserId) return;

    const comentario: any = {
      conteudo,
      autor: { id: this.currentUserId },
      postagem: { id: postagemId },
      tipoDestino: 'POSTAGEM'
    };

    this.comentarioService.criar(comentario).subscribe({
      next: () => {
        this.novoComentario[postagemId] = '';
        this.carregarComentarios(postagemId);
      },
      error: (err) => {
        console.error('Erro ao adicionar comentário:', err);
        alert('Erro ao adicionar comentário');
      }
    });
  }

  deletarComentario(comentarioId: string, postagemId: string): void {
    if (!confirm('Deseja deletar este comentário?')) return;

    this.comentarioService.deletar(comentarioId).subscribe({
      next: () => {
        this.carregarComentarios(postagemId);
      },
      error: () => {
        alert('Erro ao deletar comentário');
      }
    });
  }
}
