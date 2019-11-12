import { Injectable } from '@angular/core';
import { RaceResultModel, ResultStatus } from '../models/RaceResultModel';
import { ResultPage } from '../models/ResultPage';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { RaceModel } from '../models/RaceModel';
import { BoatModel } from '../models/BoatModel';
import { map } from 'rxjs/operators';
import { HandicapModel } from '../models/Handicap';

@Injectable({
  providedIn: 'root'
})
export class RaceResultService
{
  constructor(private http: HttpClient) { }

  getResults$(raceSeriesId: number, race: RaceModel, pageNumber: number = 1, pageSize: number = 20 )
    : Observable<ResultPage<RaceResultModel>>
  {
    return this.http
      .get<ResultPage<RaceResultModel>>(
          'http://localhost:8080/raceseries/' + raceSeriesId + '/race/' + race.id + '/resultlist.json?page='
          + pageNumber.toString()
          + '&size=' + pageSize.toString()).pipe(
            map( result => this.fixResultDates(result))
          );
  }

  fixResultDates(result: ResultPage<RaceResultModel>): ResultPage<RaceResultModel>
  {
    result.content = result.content.map( raceResult => this.fixRaceResultDate(raceResult));
    return result;
  }

  fixRaceResultDate(raceResult: RaceResultModel ): RaceResultModel
  {
    raceResult.startTime = new Date(raceResult.startTime);
    raceResult.finishTime = new Date(raceResult.finishTime);
    return raceResult;
  }

  addResult$(raceSeriesId: number, race: RaceModel, result: RaceResultModel)
  {
    return this.http.post(
          'http://localhost:8080/raceseries/' + raceSeriesId + '/race/' + race.id + '/result.json', result);
  }

  deleteResult$(raceSeriesId: number, race: RaceModel, result: RaceResultModel)
  {
    return this.http.delete(
      'http://localhost:8080/raceseries/' + raceSeriesId + '/race/' + race.id + '/result.json/' + result.id );
  }

  getUnAddedBoats$(raceSeriesId: number, race: RaceModel): Observable<BoatModel[]>
  {
    return this.http.get<BoatModel[]>(
      'http://localhost:8080/raceseries/' + raceSeriesId + '/race/' + race.id + '/boatsnotselected.json');
  }

  getHandicapsForRace$(raceSeriesId:number, race: RaceModel): Observable<HandicapModel[]>
  {
    return this.http.get<HandicapModel[]>(
      'http://localhost:8080/raceseries/'+raceSeriesId+'/fleet/'+race.fleet.id+'/'+race.id+'/handicaplist.json');
  }

  isFinished(raceResultStatus: ResultStatus): boolean
  {
    return raceResultStatus == ResultStatus.FINISHED;
  }

  isStarted(raceResultStatus: ResultStatus): boolean
  {
    return  raceResultStatus != ResultStatus.DNS
            &&
            raceResultStatus != ResultStatus.DNC;
  }
}
