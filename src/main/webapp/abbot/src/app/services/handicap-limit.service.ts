import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HandicapLimit } from '../models/HandicapLimit';
import { ResultPage } from '../models/ResultPage';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class HandicapLimitService {

  constructor(private http: HttpClient) { }

  getHandicapLimits$(raceSeriesId: number, pageNumber: number = 1, pageSize: number = 20 ): Observable<ResultPage<HandicapLimit>>
  {
    return this.http.get<ResultPage<HandicapLimit>>(
          'http://localhost:8080/raceseries/' + raceSeriesId + '/handicaplimitlist.json?page='
          + pageNumber.toString()
          + '&size=' + pageSize.toString());
  }

  addHandicapLimit$(raceSeriesId: number, handicapLimit: HandicapLimit)
  {
    return this.http.post(
      'http://localhost:8080/raceseries/' + raceSeriesId + '/handicaplimit.json',
      handicapLimit
    );
  }

  deleteHandicapLimit$(raceSeriesId: number, handicapLimit: HandicapLimit)
  {
    return this.http.delete(
      'http://localhost:8080/raceseries/' + raceSeriesId + '/handicaplimit.json/' + handicapLimit.id);
  }

}
