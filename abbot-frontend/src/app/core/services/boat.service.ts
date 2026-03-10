import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { Boat, ValidationResponse } from '../models/race.model';
import { Page } from '../models/race-series.model';

@Injectable({ providedIn: 'root' })
export class BoatService {
  private readonly http = inject(HttpClient);

  getList(seriesId: number, page = 0, size = 20): Promise<Page<Boat>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return firstValueFrom(this.http.get<Page<Boat>>(`/raceseries/${seriesId}/boatlist.json`, { params }));
  }

  getById(seriesId: number, boatId: number): Promise<Boat> {
    return firstValueFrom(this.http.get<Boat>(`/raceseries/${seriesId}/boat.json/${boatId}`));
  }

  save(seriesId: number, boat: Boat): Promise<ValidationResponse> {
    return firstValueFrom(this.http.post<ValidationResponse>(`/raceseries/${seriesId}/boat.json`, boat));
  }

  delete(seriesId: number, boatId: number): Promise<ValidationResponse> {
    return firstValueFrom(this.http.delete<ValidationResponse>(`/raceseries/${seriesId}/boat.json/${boatId}`));
  }
}
