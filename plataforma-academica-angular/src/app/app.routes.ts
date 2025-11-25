import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { CadastroComponent } from './components/cadastro/cadastro.component';
import { UsuarioListComponent } from './components/usuario/usuario-list/usuario-list';
import { PerfilComponent } from './components/perfil/perfil.component';
import { PerfilEditarComponent } from './components/perfil-editar/perfil-editar.component';
import { SalaListComponent } from './components/sala/sala-list/sala-list';
import { SalaCriarComponent } from './components/sala/sala-criar/sala-criar';
import { SalaDetalhesComponent } from './components/sala/sala-detalhes/sala-detalhes';
import { SalaAdicionarMembroComponent } from './components/sala/sala-adicionar-membro/sala-adicionar-membro';
import { AtividadeCriarComponent } from './components/sala/atividade-criar/atividade-criar';
import { AtividadeListComponent } from './components/sala/atividade-list/atividade-list';

/**
 * Rotas principais da aplicação SPA
 * Define os caminhos e componentes associados
 */
export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'home', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'cadastro', component: CadastroComponent },
  { path: 'usuarios', component: UsuarioListComponent },
  { path: 'salas', component: SalaListComponent },
  { path: 'salas/criar', component: SalaCriarComponent },
  { path: 'salas/:id', component: SalaDetalhesComponent },
  { path: 'salas/:id/adicionar-membro', component: SalaAdicionarMembroComponent },
  { path: 'salas/:id/atividades', component: AtividadeListComponent },
  { path: 'salas/:id/atividades/criar', component: AtividadeCriarComponent },
  { path: 'perfil', component: PerfilComponent },
  { path: 'perfil-criar', component: PerfilEditarComponent },
  { path: 'perfil-editar', component: PerfilEditarComponent },
  { path: '**', redirectTo: '' }
];
