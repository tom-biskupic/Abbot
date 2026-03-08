import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { Race, RaceDay, ValidationResponse } from '../models/race.model';
import { Page } from '../models/race-series.model';

@Injectable({ providedIn: 'root' })
export class RaceService {
  private readonly http = inject(HttpClient);

  getList(seriesId: number, page = 0, size = 20): Promise<Page<Race>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return firstValueFrom(this.http.get<Page<Race>>(`/raceseries/${seriesId}/racelist.json`, { params }));
  }

  getById(seriesId: number, raceId: number): Promise<Race> {
    return firstValueFrom(this.http.get<Race>(`/raceseries/${seriesId}/race.json/${raceId}`));
  }

  save(seriesId: number, race: Race): Promise<ValidationResponse> {
    return firstValueFrom(this.http.post<ValidationResponse>(`/raceseries/${seriesId}/race.json`, race));
  }

  delete(seriesId: number, raceId: number): Promise<{ status: string }> {
    return firstValueFrom(this.http.delete<{ status: string }>(`/raceseries/${seriesId}/race.json/${raceId}`));
  }

  getRaceDays(seriesId: number): Promise<RaceDay[]> {
    return firstValueFrom(this.http.get<RaceDay[]>(`/raceseries/${seriesId}/racedays.json`));
  }
}
