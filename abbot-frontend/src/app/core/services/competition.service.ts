import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { Competition, ValidationResponse } from '../models/race.model';
import { Page } from '../models/race-series.model';

@Injectable({ providedIn: 'root' })
export class CompetitionService {
  private readonly http = inject(HttpClient);

  getList(seriesId: number, page = 0, size = 20): Promise<Page<Competition>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return firstValueFrom(this.http.get<Page<Competition>>(`/raceseries/${seriesId}/competitionlist.json`, { params }));
  }

  getAll(seriesId: number): Promise<Competition[]> {
    return firstValueFrom(this.http.get<Competition[]>(`/raceseries/${seriesId}/competitionlist.json/all`));
  }

  getById(seriesId: number, competitionId: number): Promise<Competition> {
    return firstValueFrom(this.http.get<Competition>(`/raceseries/${seriesId}/competition.json/${competitionId}`));
  }

  save(seriesId: number, competition: Competition): Promise<ValidationResponse> {
    return firstValueFrom(this.http.post<ValidationResponse>(`/raceseries/${seriesId}/competition.json`, competition));
  }

  delete(seriesId: number, competitionId: number): Promise<ValidationResponse> {
    return firstValueFrom(this.http.delete<ValidationResponse>(`/raceseries/${seriesId}/competition.json/${competitionId}`));
  }
}
