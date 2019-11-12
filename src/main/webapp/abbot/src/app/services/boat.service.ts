import { Injectable } from '@angular/core';
import { ResultPage } from '../models/ResultPage';
import { Observable } from 'rxjs';
import { BoatModel } from '../models/BoatModel';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class BoatService
{
  constructor(private http: HttpClient) { }

  getBoats$(raceSeriesId: number, pageNumber: number = 1, pageSize: number = 20 ): Observable<ResultPage<BoatModel>>
  {
    return this.http.get<ResultPage<BoatModel>>(
          'http://localhost:8080/raceseries/' + raceSeriesId + '/boatlist.json?page='
          + pageNumber.toString()
          + '&size=' + pageSize.toString());
  }

  addBoat$(raceSeriesId: number, boat: BoatModel)
  {
    return this.http.post(
      'http://localhost:8080/raceseries/' + raceSeriesId + '/boat.json',
      boat
    );
  }

  deleteBoat$(raceSeriesId: number, boat: BoatModel)
  {
    return this.http.delete(
      'http://localhost:8080/raceseries/' + raceSeriesId + '/boat.json/' + boat.id);
  }
}
