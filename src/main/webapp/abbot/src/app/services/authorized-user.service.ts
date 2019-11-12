import { AuthorizedUserModel } from './../models/AuthorizedUserModel';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ResultPage } from '../models/ResultPage';

@Injectable({
  providedIn: 'root'
})
export class AuthorizedUserService
{

  constructor(private http: HttpClient) { }

  getAuthorizedUsers$( raceSeriesId: number, pageNumber: number = 1, pageSize: number = 20 )
  {
    return this.http.get<ResultPage<AuthorizedUserModel>>(
      'http://localhost:8080/raceseries/' + raceSeriesId + '/authorizeduserlist.json?page='
      + pageNumber.toString()
      + '&size=' + pageSize.toString());
  }

  addAuthorizedUser$(raceSeriesId: number, authorizedUser: AuthorizedUserModel)
  {
    return this.http.post<AuthorizedUserModel>(
      'http://localhost:8080/raceseries/' + raceSeriesId + '/authorizeduser.json',
      authorizedUser);
  }

  deleteAuthorizedUser$(raceSeriesId: number, authorizedUser: AuthorizedUserModel)
  {
    return this.http.delete<AuthorizedUserModel>(
      'http://localhost:8080/raceseries/' + raceSeriesId + '/authorizeduser.json/'+authorizedUser.id
    );
  }
}
