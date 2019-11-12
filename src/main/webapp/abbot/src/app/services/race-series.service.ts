import { RaceSeriesModel } from './../models/RaceSeriesModel';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ResultPage } from '../models/ResultPage';
import { UserModel } from '../models/UserModel';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class RaceSeriesService {

  constructor(private http: HttpClient) { }

  getRaceSeries(raceSeriesId: number): Observable<RaceSeriesModel>
  {
    return this.http.get<RaceSeriesModel>('http://localhost:8080/raceseries.json/' + raceSeriesId);
  }

  getRaceSeries$(pageNumber: number = 1, pageSize: number = 20 ): Observable<ResultPage<RaceSeriesModel>>
  {
    return this.http
      .get<ResultPage<RaceSeriesModel>>(
          'http://localhost:8080/raceserieslist.json?page='
          + pageNumber.toString()
          + '&size=' + pageSize.toString());
  }

  addRaceSeries$(raceSeries: RaceSeriesModel)
  {
    return this.http.post('http://localhost:8080/raceseries.json', raceSeries);
  }
}
