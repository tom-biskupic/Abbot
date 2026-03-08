import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { RaceSeries, Page } from '../models/race-series.model';

@Injectable({ providedIn: 'root' })
export class RaceSeriesService {
  private readonly http = inject(HttpClient);

  getList(page = 0, size = 10): Promise<Page<RaceSeries>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return firstValueFrom(this.http.get<Page<RaceSeries>>('/raceserieslist.json', { params }));
  }

  getById(id: number): Promise<RaceSeries> {
    return firstValueFrom(this.http.get<RaceSeries>(`/raceseries.json/${id}`));
  }

  save(series: RaceSeries): Promise<{ status: string }> {
    return firstValueFrom(this.http.post<{ status: string }>('/raceseries.json', series));
  }
}
