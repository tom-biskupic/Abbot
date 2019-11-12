import { AuthService } from './auth-service';
import { HttpInterceptor, HttpRequest, HttpHandler } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable()
export class BasicAuthHtppInterceptorService implements HttpInterceptor {

  constructor(private authService: AuthService) { }

  intercept(req: HttpRequest<any>, next: HttpHandler)
  {
    const principal = this.authService.getLoggonOnUser();

    if (principal != null )
    {
      req = req.clone({
        setHeaders: { authorization: 'Basic ' + btoa(principal.name + ':' + principal.password) }
      })
    }

    return next.handle(req);

  }
}
