import { BoatDivisionModel } from './../models/BoatClassModel';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ResultPage } from '../models/ResultPage';
import { BoatClassModel } from '../models/BoatClassModel';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BoatClassService {

  constructor(private http: HttpClient) { }

  getBoatClasses$(raceSeriesId: number, pageNumber: number = 1, pageSize: number = 20 ): Observable<ResultPage<BoatClassModel>>
  {
    return this.http
      .get<ResultPage<BoatClassModel>>(
          'http://localhost:8080/raceseries/' + raceSeriesId + '/boatclasslist.json?page='
          + pageNumber.toString()
          + '&size=' + pageSize.toString());
  }

  getAllBoatClasses$(raceSeriesId: number): Observable<BoatClassModel[]>
  {
    return this.http
      .get<BoatClassModel[]>(
          'http://localhost:8080/raceseries/' + raceSeriesId + '/boatclasslist.json/all');
  }

  addBoatClass$(raceSeriesId: number, boatClass: BoatClassModel)
  {
    return this.http.post('http://localhost:8080/raceseries/' + raceSeriesId + '/boatclass.json', boatClass);
  }

  addDivision$(raceSeriesId: number, boatClassId: number, boatDivision: BoatDivisionModel )
  {
    return this.http.post(
      'http://localhost:8080/raceseries/' + raceSeriesId + '/boatclass.json/' + boatClassId + '/division.json',
      boatDivision);
  }

  deleteDivision$(raceSeriesId: number, boatClassId: number, divisionId: number)
  {
    return this.http.delete(
      'http://localhost:8080/raceseries/' + raceSeriesId + '/boatclass.json/' + boatClassId + '/division.json/' + divisionId );
  }
}
