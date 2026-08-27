import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { catchError, tap } from 'rxjs/operators';
import { throwError } from 'rxjs';

/**
 * Interceptor funcional moderno para a Plataforma Acadêmica.
 * Responsável por:
 *  1. Injetar o token JWT de autenticação em requisições protegidas.
 *  2. Realizar logging detalhado de requisições e respostas.
 *  3. Capturar e tratar erros HTTP globalmente (ex: 401 Unauthorized, 500 Server Error).
 */
export const authAndLoggingInterceptor: HttpInterceptorFn = (req, next) => {
    // Recupera o token do localStorage (se disponível no ambiente browser)
    let token: string | null = null;
    if (typeof window !== 'undefined' && localStorage) {
        token = localStorage.getItem('auth_token');
    }

    // Clona a requisição para injetar o header de autorização se o token existir
    let authReq = req;
    if (token) {
        authReq = req.clone({
            setHeaders: {
                Authorization: `Bearer ${token}`
            }
        });
    }

    console.log(`[HTTP Request] ${authReq.method} --> ${authReq.url}`);

    return next(authReq).pipe(
        tap(event => {
            // Log opcional de sucesso em desenvolvimento
        }),
        catchError((error: HttpErrorResponse) => {
            console.error(`[HTTP Error] ${authReq.method} ${authReq.url} | Status: ${error.status}`, error.error);

            if (error.status === 401) {
                console.warn('[Security] Sessão expirada ou não autorizada. Redirecionando...');
                // Aqui poderá ser acionado o serviço de logout ou redirecionamento para login
            }

            return throwError(() => error);
        })
    );
};
