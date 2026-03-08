import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { ValidationResponse } from '../models/race.model';
import { UserSummary } from '../models/settings.model';
import { Page } from '../models/race-series.model';

@Injectable({ providedIn: 'root' })
export class AuthorizedUserService {
  private readonly http = inject(HttpClient);

  getList(seriesId: number, page = 0, size = 20): Promise<Page<UserSummary>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return firstValueFrom(this.http.get<Page<UserSummary>>(`/raceseries/${seriesId}/authorizeduserlist.json`, { params }));
  }

  authorize(seriesId: number, emailAddress: string): Promise<ValidationResponse> {
    return firstValueFrom(this.http.post<ValidationResponse>(`/raceseries/${seriesId}/authorizeduser.json`, { emailAddress }));
  }

  delete(seriesId: number, userId: number): Promise<ValidationResponse> {
    return firstValueFrom(this.http.delete<ValidationResponse>(`/raceseries/${seriesId}/authorizeduser.json/${userId}`));
  }
}
