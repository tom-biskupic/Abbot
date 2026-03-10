import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { Boat, Handicap, RaceResult, ValidationResponse } from '../models/race.model';
import { Page } from '../models/race-series.model';

@Injectable({ providedIn: 'root' })
export class RaceResultService {
  private readonly http = inject(HttpClient);

  getList(seriesId: number, raceId: number, page = 0, size = 20): Promise<Page<RaceResult>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return firstValueFrom(this.http.get<Page<RaceResult>>(
      `/raceseries/${seriesId}/race/${raceId}/resultlist.json`, { params }));
  }

  getById(seriesId: number, raceId: number, resultId: number): Promise<RaceResult> {
    return firstValueFrom(this.http.get<RaceResult>(
      `/raceseries/${seriesId}/race/${raceId}/result.json/${resultId}`));
  }

  save(seriesId: number, raceId: number, result: RaceResult): Promise<ValidationResponse> {
    return firstValueFrom(this.http.post<ValidationResponse>(
      `/raceseries/${seriesId}/race/${raceId}/result.json`, result));
  }

  delete(seriesId: number, raceId: number, resultId: number): Promise<ValidationResponse> {
    return firstValueFrom(this.http.delete<ValidationResponse>(
      `/raceseries/${seriesId}/race/${raceId}/result.json/${resultId}`));
  }

  updateHandicaps(seriesId: number, raceId: number): Promise<ValidationResponse> {
    return firstValueFrom(this.http.post<ValidationResponse>(
      `/raceseries/${seriesId}/race/${raceId}/updatehandicaps.json`, '{}'));
  }

  addNonStarters(seriesId: number, raceId: number, resultStatus: string): Promise<ValidationResponse> {
    return firstValueFrom(this.http.post<ValidationResponse>(
      `/raceseries/${seriesId}/race/${raceId}/addnonstarters.json`, resultStatus));
  }

  getBoatsNotSelected(seriesId: number, raceId: number): Promise<Boat[]> {
    return firstValueFrom(this.http.get<Boat[]>(
      `/raceseries/${seriesId}/race/${raceId}/boatsnotselected.json`));
  }

  getHandicapsForFleet(seriesId: number, fleetId: number, raceId: number): Promise<Handicap[]> {
    return firstValueFrom(this.http.get<Handicap[]>(
      `/raceseries/${seriesId}/fleet/${fleetId}/${raceId}/handicaplist.json`));
  }
}
