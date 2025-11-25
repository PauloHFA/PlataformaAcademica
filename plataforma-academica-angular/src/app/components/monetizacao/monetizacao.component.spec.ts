import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MonetizacaoComponent } from './monetizacao.component';

describe('MonetizacaoComponent', () => {
  let component: MonetizacaoComponent;
  let fixture: ComponentFixture<MonetizacaoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MonetizacaoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MonetizacaoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
