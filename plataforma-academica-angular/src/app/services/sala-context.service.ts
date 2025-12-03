import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SalaContextService {
  private nomeSalaSubject = new BehaviorSubject<string>('');
  nomeSala$ = this.nomeSalaSubject.asObservable();

  setNomeSala(nome: string) {
    this.nomeSalaSubject.next(nome);
  }

  getNomeSala() {
    return this.nomeSalaSubject.value;
  }
}
