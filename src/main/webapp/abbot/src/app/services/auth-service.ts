import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { map } from 'rxjs/operators';
import { Principal } from '../models/Principal';


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  static ADMIN_ROLE = 'ROLE_ADMIN';
  static LOCAL_STORAGE_CRED_NAME = 'user';

  error: any;

  constructor(private http: HttpClient, private router: Router ) { }

  isAuthenticated()
  {
    const storedCred = localStorage.getItem(AuthService.LOCAL_STORAGE_CRED_NAME);
    return !( storedCred === null);
  }

  getLoggonOnUser(): Principal
  {
    const storedCred = localStorage.getItem(AuthService.LOCAL_STORAGE_CRED_NAME);
    if ( storedCred === null )
    {
      return null;
    }
    else
    {
      return JSON.parse(storedCred);
    }
  }

  isAdministrator(): boolean
  {
    const storedUser = localStorage.getItem(AuthService.LOCAL_STORAGE_CRED_NAME);
    if ( storedUser != null )
    {
      return JSON.parse(storedUser).isAdmin;
    }
    else
    {
      return false;
    }
  }

  login(username: string, password: string)
  {
    const headers = new HttpHeaders(
      { authorization : 'Basic ' + btoa(username + ':' + password) }
    );

    return this.http.get<Principal>(
      'http://localhost:8080/user',
      { headers}).pipe( map( principal  =>
        {
          if (! principal.name)
          {
            throw new Error('Login failed');
          }

          for ( const authority of principal.authorities )
          {
            if ( authority.authority === AuthService.ADMIN_ROLE )
            {
              principal.isAdmin = true;
            }
          }

          principal.password = password;

          localStorage.setItem(
            AuthService.LOCAL_STORAGE_CRED_NAME,
            JSON.stringify(principal)
            );

          console.log('Returned response as ' + principal.toString());

          return principal;

        }));
  }

  logout()
  {
    localStorage.removeItem(AuthService.LOCAL_STORAGE_CRED_NAME);
  }

}
