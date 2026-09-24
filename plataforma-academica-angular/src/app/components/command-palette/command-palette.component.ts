import { Component, OnInit, HostListener } from '@angular/core';
import { CommandPaletteService } from '../../services/command-palette.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
    selector: 'app-command-palette',
    templateUrl: './command-palette.component.html',
    styleUrls: ['./command-palette.component.css'],
    standalone: true,
    imports: [CommonModule, FormsModule]
})
export class CommandPaletteComponent implements OnInit {
    searchTerm = '';
    resultados: any[] = [];
    isOpen = false;

    constructor(
        private commandPaletteService: CommandPaletteService,
        private router: Router
    ) { }

    ngOnInit(): void {
        // Subscribe to search term changes
        this.commandPaletteService.getResults().subscribe(resultados => {
            this.resultados = resultados;
        });
    }

    @HostListener('document:keydown.control.k', ['$event'])
    @HostListener('document:keydown.meta.k', ['$event'])
    openPalette(event: KeyboardEvent) {
        event.preventDefault();
        this.open();
    }

    open() {
        this.isOpen = true;
        this.searchTerm = '';
        this.commandPaletteService.open();
        // Focus the input after a short delay to ensure the DOM is ready
        setTimeout(() => {
            const input = document.getElementById('command-palette-input');
            if (input) {
                input.focus();
            }
        }, 100);
    }

    close() {
        this.isOpen = false;
        this.commandPaletteService.close();
    }

    onSearchChange() {
        this.commandPaletteService.setSearchTerm(this.searchTerm);
    }

    selectItem(item: any) {
        this.close();
        const actionResult = this.commandPaletteService.executeAction(item);

        if (actionResult) {
            if (actionResult.action === 'navigate' && actionResult.route) {
                this.router.navigate([actionResult.route]);
            }
            // For other actions, we might handle them here
            // For now, we'll just log them
            console.log('Action executed:', actionResult);
        }
    }

    // Helper methods for icons
    getItemIcon(type: string): string {
        switch (type) {
            case 'sala':
                return '🏫';
            case 'usuario':
                return '👤';
            case 'postagem':
                return '📝';
            case 'acao':
                return '⚡';
            default:
                return '❓';
        }
    }

    getItemIconClass(type: string): string {
        return type; // Returns 'sala', 'usuario', 'postagem', or 'acao' for CSS classes
    }

    @HostListener('document:keydown.escape', ['$event'])
    handleEscape(event: KeyboardEvent) {
        if (this.isOpen) {
            event.preventDefault();
            this.close();
        }
    }
}