import { Component } from '@angular/core';

@Component({
    selector: 'app-amigos-section',
    standalone: true,
    imports: [],
    templateUrl: './amigos-section.component.html',
    styleUrls: ['./amigos-section.component.css']
})
export class AmigosSectionComponent {
    requestSent = false;
}