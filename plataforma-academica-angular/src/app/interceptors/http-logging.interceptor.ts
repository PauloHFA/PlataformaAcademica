import { Injectable } from '@angular/core';
import {
  HttpEvent,
  HttpInterceptor,
  HttpHandler,
  HttpRequest,
  HttpErrorResponse
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';

/**
 * Interceptador HTTP para logging e tratamento de erros
 * Ajuda no debug das requisições
 */
@Injectable()
export class HttpLoggingInterceptor implements HttpInterceptor {
  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    console.log(`[HTTP] ${req.method} ${req.url}`);

    return next.handle(req).pipe(
      tap(event => {
        console.log(`[HTTP] Response: ${req.method} ${req.url}`, event);
      }),
      catchError((error: HttpErrorResponse) => {
        console.error(`[HTTP] Error: ${req.method} ${req.url}`, error);
        return throwError(() => error);
      })
    );
  }
}
