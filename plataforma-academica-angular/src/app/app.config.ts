import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import { provideClientHydration, withEventReplay } from '@angular/platform-browser';
import { provideAnimations } from '@angular/platform-browser/animations';
import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
import { authAndLoggingInterceptor } from './interceptors/auth-and-logging.interceptor';
import { provideSocketIo, SocketIoConfig } from 'ngx-socket-io';

const config: SocketIoConfig = { url: 'http://localhost:8080', options: {} };

/**
 * Configuração global da aplicação Angular (Padrão Sênior)
 * Inclui providers para routing, HTTP com interceptor funcional, animações e WebSocket (Socket.io).
 */
export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideClientHydration(withEventReplay()),
    provideAnimations(),
    provideHttpClient(
      withFetch(),
      withInterceptors([authAndLoggingInterceptor])
    ),
    provideSocketIo(config)
  ]
};
