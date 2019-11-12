import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ResultPage } from '../models/ResultPage';
import { CompetitionModel } from '../models/CompetitionModel';

@Injectable({
  providedIn: 'root'
})
export class CompetitionService {

  constructor(private http: HttpClient) { }

  getCompetitions$(raceSeriesId: number, pageNumber: number = 1, pageSize: number = 20 ): Observable<ResultPage<CompetitionModel>>
  {
    return this.http.get<ResultPage<CompetitionModel>>(
          'http://localhost:8080/raceseries/' + raceSeriesId + '/competitionlist.json?page='
          + pageNumber.toString()
          + '&size=' + pageSize.toString());
  }

  getAllCompetitions$(raceSeriesId: number)
  {
    return this.http.get<CompetitionModel[]>(
      'http://localhost:8080/raceseries/' + raceSeriesId + '/competitionlist.json/all');
  }

  addCompetition$(raceSeriesId: number, competition: CompetitionModel)
  {
    return this.http.post(
      'http://localhost:8080/raceseries/' + raceSeriesId + '/competition.json',
      competition
    );
  }

  deleteCompetition$(raceSeriesId: number, competition: CompetitionModel)
  {
    return this.http.delete(
      'http://localhost:8080/raceseries/' + raceSeriesId + '/competition.json/' + competition.id);
  }
}
