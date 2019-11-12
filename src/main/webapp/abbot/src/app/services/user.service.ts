import { AuthService } from './auth-service';

import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, tap, catchError } from 'rxjs/operators';
import { UserModel } from '../models/UserModel';
import { ResultPage } from '../models/ResultPage';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient, private authService: AuthService ) { }


  getUsers$(pageNumber: number = 1, pageSize: number = 20 ): Observable<ResultPage<UserModel>>
  {
    return this.http
      .get<ResultPage<UserModel>>(
          'http://localhost:8080/userlist.json?page='
          + pageNumber.toString()
          + '&size=' + pageSize.toString());
  }

  addUser$(user: UserModel)
  {
    return this.http.post('http://localhost:8080/user.json', user);
  }
}

