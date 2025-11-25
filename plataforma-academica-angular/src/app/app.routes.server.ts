import { RenderMode, ServerRoute } from '@angular/ssr';

export const serverRoutes: ServerRoute[] = [
  {
    path: 'salas/:id',
    renderMode: RenderMode.Server
  },
  {
    path: 'salas/:id/atividades',
    renderMode: RenderMode.Server
  },
  {
    path: 'salas/:id/adicionar-membro',
    renderMode: RenderMode.Server
  },
  {
    path: 'salas/:id/atividades/criar',
    renderMode: RenderMode.Server
  },
  {
    path: '**',
    renderMode: RenderMode.Prerender
  }
];
