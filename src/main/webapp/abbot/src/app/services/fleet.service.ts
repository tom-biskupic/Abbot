import { FleetModel } from 'src/app/models/FleetModel';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ResultPage } from '../models/ResultPage';

@Injectable({
  providedIn: 'root'
})
export class FleetService {

  constructor(private http: HttpClient) { }

  getFleets$(raceSeriesId: number, pageNumber: number = 1, pageSize: number = 20 ): Observable<ResultPage<FleetModel>>
  {
    return this.http
      .get<ResultPage<FleetModel>>(
          'http://localhost:8080/raceseries/' + raceSeriesId + '/fleetlist.json?page='
          + pageNumber.toString()
          + '&size=' + pageSize.toString());
  }

  getAllFleets$(raceSeriesId: number): Observable<FleetModel[]>
  {
    return this.http.get<FleetModel[]>(
          'http://localhost:8080/raceseries/' + raceSeriesId + '/fleetlist.json/all');
  }

  addFleet$(raceSeriesId: number, fleet: FleetModel)
  {
    return this.http.post(
          'http://localhost:8080/raceseries/' + raceSeriesId + '/fleet.json', fleet);
  }

  deleteFleet$(raceSeriesId: number, fleet: FleetModel)
  {
    return this.http.delete(
      'http://localhost:8080/raceseries/'+raceSeriesId+'/fleet.json/' + fleet.id);
  }
}
