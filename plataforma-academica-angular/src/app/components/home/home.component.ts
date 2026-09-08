import { AfterViewInit, Component, Inject, OnInit, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { RouterModule } from '@angular/router';
import { trigger, transition, style, animate } from '@angular/animations';
import { PerfilSectionComponent } from './sections/perfil-section.component';
import { SalasSectionComponent } from './sections/salas-section.component';
import { AtividadesSectionComponent } from './sections/atividades-section.component';
import { FeedSectionComponent } from './sections/feed-section.component';
import { AmigosSectionComponent } from './sections/amigos-section.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule, PerfilSectionComponent, SalasSectionComponent, AtividadesSectionComponent, FeedSectionComponent, AmigosSectionComponent],
  templateUrl: './home.component.html',
  styleUrl: './home-academic.component.css',
  animations: [
    trigger('fadeInUp', [
      transition(':enter', [
        style({ opacity: 0, transform: 'translateY(30px)' }),
        animate('600ms ease-out', style({ opacity: 1, transform: 'translateY(0)' }))
      ])
    ])
  ]
})
export class HomeComponent implements OnInit, AfterViewInit {
  title = 'Plataforma Acadêmica';

  constructor(@Inject(PLATFORM_ID) private platformId: Object) { }

  ngOnInit(): void { }

  ngAfterViewInit(): void {
    if (!isPlatformBrowser(this.platformId)) {
      return;
    }

    if (typeof IntersectionObserver === 'undefined') {
      document.querySelectorAll('.reveal').forEach((element) => element.classList.add('is-visible'));
      return;
    }

    const observer = new IntersectionObserver((entries) => {
      entries.filter((entry) => entry.isIntersecting).forEach((entry) => {
        entry.target.classList.add('is-visible');
        observer.unobserve(entry.target);
      });
    }, { threshold: 0.18 });

    document.querySelectorAll('.reveal').forEach((element) => observer.observe(element));
  }
}

