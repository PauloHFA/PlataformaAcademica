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
    const dashboardSpy = jasmine.createSpyObj('DashboardService', ['getDashboardAluno', 'getFrequencias']);
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

  it('should subscribe to notifications', () => {
    notificationServiceSpy.getNotificacoesUsuario.and.returnValue(of('Nova notificação'));

    spyOn(localStorage, 'getItem').and.returnValue('1');

    component.ngOnInit();

    expect(notificationServiceSpy.getNotificacoesUsuario).toHaveBeenCalledWith(1);
  });
});