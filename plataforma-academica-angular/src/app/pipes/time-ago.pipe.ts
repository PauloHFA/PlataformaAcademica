import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
    name: 'timeAgo'
})
export class TimeAgoPipe implements PipeTransform {

    transform(value: string | Date | undefined | null): string {
        if (!value) return '';

        const date = new Date(value);
        const now = new Date();
        const diffInSeconds = Math.floor((now.getTime() - date.getTime()) / 1000);

        if (diffInSeconds < 60) {
            return 'há pouco';
        }

        const diffInMinutes = Math.floor(diffInSeconds / 60);
        if (diffInMinutes < 60) {
            return `há ${diffInMinutes} ${diffInMinutes === 1 ? 'minuto' : 'minutos'}`;
        }

        const diffInHours = Math.floor(diffInMinutes / 60);
        if (diffInHours < 24) {
            return `há ${diffInHours} ${diffInHours === 1 ? 'hora' : 'horas'}`;
        }

        const diffInDays = Math.floor(diffInHours / 24);
        if (diffInDays < 30) {
            return `há ${diffInDays} ${diffInDays === 1 ? 'dia' : 'dias'}`;
        }

        const diffInMonths = Math.floor(diffInDays / 30);
        if (diffInMonths < 12) {
            return `há ${diffInMonths} ${diffInMonths === 1 ? 'mês' : 'meses'}`;
        }

        const diffInYears = Math.floor(diffInMonths / 12);
        return `há ${diffInYears} ${diffInYears === 1 ? 'ano' : 'anos'}`;
    }
}