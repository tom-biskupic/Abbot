import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { ValidationResponse } from '../models/race.model';
import { HandicapLimit } from '../models/settings.model';
import { Page } from '../models/race-series.model';

@Injectable({ providedIn: 'root' })
export class HandicapLimitService {
  private readonly http = inject(HttpClient);

  getList(seriesId: number, page = 0, size = 20): Promise<Page<HandicapLimit>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return firstValueFrom(this.http.get<Page<HandicapLimit>>(`/raceseries/${seriesId}/handicaplimitlist.json`, { params }));
  }

  getById(seriesId: number, id: number): Promise<HandicapLimit> {
    return firstValueFrom(this.http.get<HandicapLimit>(`/raceseries/${seriesId}/handicaplimit.json/${id}`));
  }

  save(seriesId: number, limit: HandicapLimit): Promise<ValidationResponse> {
    return firstValueFrom(this.http.post<ValidationResponse>(`/raceseries/${seriesId}/handicaplimit.json`, limit));
  }

  delete(seriesId: number, id: number): Promise<ValidationResponse> {
    return firstValueFrom(this.http.delete<ValidationResponse>(`/raceseries/${seriesId}/handicaplimit.json/${id}`));
  }
}
