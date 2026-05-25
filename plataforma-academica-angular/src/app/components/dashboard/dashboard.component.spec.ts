/// <reference types="jasmine" />

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { DashboardComponent } from './dashboard';
import { DashboardService } from '../../services/dashboard.service';
import { SalaContextService } from '../../services/sala-context.service';
import { NotificationService } from '../../services/notification.service';

describe('DashboardComponent', () => {
  let component: DashboardComponent;
  let fixture: ComponentFixture<DashboardComponent>;
  let dashboardServiceSpy: jasmine.SpyObj<DashboardService>;
  let notificationServiceSpy: jasmine.SpyObj<NotificationService>;

  beforeEach(async () => {
    const dashboardSpy = jasmine.createSpyObj('DashboardService', ['getDashboardAluno', 'getDashboardSala', 'getFrequencias']);
    const notificationSpy = jasmine.createSpyObj('NotificationService', ['getNotificacoesUsuario']);

    await TestBed.configureTestingModule({
      imports: [DashboardComponent],
      providers: [
        { provide: ActivatedRoute, useValue: { paramMap: of({ get: () => '1' }) } },
        { provide: DashboardService, useValue: dashboardSpy },
        { provide: SalaContextService, useValue: {} },
        { provide: NotificationService, useValue: notificationSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(DashboardComponent);
    component = fixture.componentInstance;
    dashboardServiceSpy = TestBed.inject(DashboardService) as jasmine.SpyObj<DashboardService>;
    notificationServiceSpy = TestBed.inject(NotificationService) as jasmine.SpyObj<NotificationService>;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load dashboard and prepare charts', async () => {
    const mockDashboard = {
      alunoId: 1,
      salaId: 1,
      alunoNome: 'Teste',
      salaNome: 'Sala 1',
      totalAtividades: 5,
      totalSubmissoes: 3,
      totalSubmissoesComNota: 2,
      mediaNota: 8.5,
      totalPresencas: 4,
      totalFaltas: 1,
      percentualPresenca: 80,
      submissoes: [
        { id: 1, atividadeId: 1, nota: 9, feedback: 'Bom', dataSubmissao: '2023-01-01' },
        { id: 2, atividadeId: 2, nota: 8, feedback: 'OK', dataSubmissao: '2023-01-02' }
      ]
    };

    dashboardServiceSpy.getDashboardAluno.and.returnValue(of(mockDashboard));
    dashboardServiceSpy.getFrequencias.and.returnValue(of([]));
    notificationServiceSpy.getNotificacoesUsuario.and.returnValue(of(''));

    spyOn(localStorage, 'getItem').and.returnValue('1');

    component.ngOnInit();
    fixture.detectChanges();
    await fixture.whenStable();

    expect(component.dashboard).toEqual(mockDashboard);
  });

  it('should load room dashboard by default for professor', async () => {
    const mockSalaDashboard = {
      salaId: 1,
      salaNome: 'Sala de POO',
      totalAtividades: 1,
      totalSubmissoes: 1,
      totalSubmissoesComNota: 1,
      mediaNotaSala: 8.5,
      totalPresencas: 1,
      totalFaltas: 0,
      percentualPresenca: 100.0,
      alunos: [
        {
          alunoId: 1,
          alunoNome: 'João Silva',
          totalSubmissoes: 1,
          totalSubmissoesComNota: 1,
          mediaNota: 8.5,
          percentualPresenca: 100.0
        }
      ]
    };

    dashboardServiceSpy.getDashboardSala.and.returnValue(of(mockSalaDashboard));
    dashboardServiceSpy.getFrequencias.and.returnValue(of([]));
    notificationServiceSpy.getNotificacoesUsuario.and.returnValue(of(''));
    spyOn(localStorage, 'getItem').and.callFake((key: string) => {
      if (key === 'isProfessor') {
        return 'true';
      }
      if (key === 'usuarioId') {
        return '1';
      }
      return null;
    });

    component.ngOnInit();
    fixture.detectChanges();
    await fixture.whenStable();

    expect(dashboardServiceSpy.getDashboardSala).toHaveBeenCalledWith(1, undefined, undefined);
    expect(component.dashboardSala).toEqual(mockSalaDashboard);
    expect(component.carregando).toBeFalse();
  });

  it('should load selected student dashboard for professor when alunoSelecionadoId is set', async () => {
    const mockSalaDashboard = {
      salaId: 1,
      salaNome: 'Sala de POO',
      totalAtividades: 1,
      totalSubmissoes: 1,
      totalSubmissoesComNota: 1,
      mediaNotaSala: 8.5,
      totalPresencas: 1,
      totalFaltas: 0,
      percentualPresenca: 100.0,
      alunos: [
        {
          alunoId: 1,
          alunoNome: 'João Silva',
          totalSubmissoes: 1,
          totalSubmissoesComNota: 1,
          mediaNota: 8.5,
          percentualPresenca: 100.0
        }
      ]
    };

    const mockAlunoDashboard = {
      alunoId: 1,
      salaId: 1,
      alunoNome: 'João Silva',
      salaNome: 'Sala de POO',
      totalAtividades: 1,
      totalSubmissoes: 1,
      totalSubmissoesComNota: 1,
      mediaNota: 8.5,
      totalPresencas: 1,
      totalFaltas: 0,
      percentualPresenca: 100,
      submissoes: []
    };

    dashboardServiceSpy.getDashboardSala.and.returnValue(of(mockSalaDashboard));
    dashboardServiceSpy.getDashboardAluno.and.returnValue(of(mockAlunoDashboard));
    dashboardServiceSpy.getFrequencias.and.returnValue(of([]));
    notificationServiceSpy.getNotificacoesUsuario.and.returnValue(of(''));
    spyOn(localStorage, 'getItem').and.callFake((key: string) => {
      if (key === 'isProfessor') {
        return 'true';
      }
      if (key === 'usuarioId') {
        return '1';
      }
      return null;
    });

    component.alunoSelecionadoId = 1;
    component.ngOnInit();
    fixture.detectChanges();
    await fixture.whenStable();

    expect(dashboardServiceSpy.getDashboardSala).toHaveBeenCalledWith(1, undefined, undefined);
    expect(dashboardServiceSpy.getDashboardAluno).toHaveBeenCalledWith(1, 1, undefined, undefined);
    expect(component.dashboard).toEqual(mockAlunoDashboard);
    expect(component.dashboardSala).toEqual(mockSalaDashboard);
  });

  it('should subscribe to notifications', () => {
    notificationServiceSpy.getNotificacoesUsuario.and.returnValue(of('Nova notificação'));

    spyOn(localStorage, 'getItem').and.returnValue('1');

    component.ngOnInit();

    expect(notificationServiceSpy.getNotificacoesUsuario).toHaveBeenCalledWith(1);
  });
});