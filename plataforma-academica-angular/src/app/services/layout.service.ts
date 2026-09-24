import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class LayoutService {
    private focusModeSubject = new BehaviorSubject<boolean>(false);
    focusMode$ = this.focusModeSubject.asObservable();

    toggleFocusMode(): void {
        this.focusModeSubject.next(!this.focusModeSubject.value);
    }

    setFocusMode(enabled: boolean): void {
        this.focusModeSubject.next(enabled);
    }

    isFocusMode(): boolean {
        return this.focusModeSubject.value;
    }
}