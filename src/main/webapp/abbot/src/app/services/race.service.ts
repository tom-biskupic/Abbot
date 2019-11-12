import { RaceSeriesModel } from './../models/RaceSeriesModel';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ResultPage } from '../models/ResultPage';
import { RaceModel } from '../models/RaceModel';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { RaceDayModel } from '../models/RaceDayModel';

@Injectable({
  providedIn: 'root'
})
export class RaceService {

  constructor(private http: HttpClient) { }

  getRaces$(raceSeriesId: number, pageNumber: number = 1, pageSize: number = 20 ): Observable<ResultPage<RaceModel>>
  {
    return this.http
      .get<ResultPage<RaceModel>>(
          'http://localhost:8080/raceseries/' + raceSeriesId + '/racelist.json?page='
          + pageNumber.toString()
          + '&size=' + pageSize.toString()).pipe(
            map( result => this.fixRacesDates(result))
          );
  }

  fixRacesDates(result: ResultPage<RaceModel>): ResultPage<RaceModel>
  {
    result.content = result.content.map( race => this.fixRaceDate(race));
    return result;
  }

  fixRaceDate(race: RaceModel ): RaceModel
  {
    race.raceDate = new Date(race.raceDate);
    return race;
  }

  addRace$(raceSeriesId: number, race: RaceModel)
  {
    return this.http.post('http://localhost:8080/raceseries/' + raceSeriesId + '/race.json', race);
  }

  deleteRace$(raceSeriesId: number, race: RaceModel)
  {
    return this.http.delete('http://localhost:8080/raceseries/' + raceSeriesId + '/race.json/' + race.id);
  }

  raceDay$(raceSeriesId: number)
  {
    return this.http.get<RaceDayModel[]>('http://localhost:8080/raceseries/' + raceSeriesId + '/racedays.json');
  }
}
