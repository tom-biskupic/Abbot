import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { Fleet, ValidationResponse } from '../models/race.model';
import { Page } from '../models/race-series.model';

@Injectable({ providedIn: 'root' })
export class FleetService {
  private readonly http = inject(HttpClient);

  getList(seriesId: number, page = 0, size = 20): Promise<Page<Fleet>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return firstValueFrom(this.http.get<Page<Fleet>>(`/raceseries/${seriesId}/fleetlist.json`, { params }));
  }

  getAll(seriesId: number): Promise<Fleet[]> {
    return firstValueFrom(this.http.get<Fleet[]>(`/raceseries/${seriesId}/fleetlist.json/all`));
  }

  getById(seriesId: number, fleetId: number): Promise<Fleet> {
    return firstValueFrom(this.http.get<Fleet>(`/raceseries/${seriesId}/fleet.json/${fleetId}`));
  }

  save(seriesId: number, fleet: Fleet): Promise<ValidationResponse> {
    return firstValueFrom(this.http.post<ValidationResponse>(`/raceseries/${seriesId}/fleet.json`, fleet));
  }

  delete(seriesId: number, fleetId: number): Promise<ValidationResponse> {
    return firstValueFrom(this.http.delete<ValidationResponse>(`/raceseries/${seriesId}/fleet.json/${fleetId}`));
  }
}
