import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComunidadesListComponent } from './comunidades-list.component';

describe('ComunidadesListComponent', () => {
  let component: ComunidadesListComponent;
  let fixture: ComponentFixture<ComunidadesListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ComunidadesListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ComunidadesListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
