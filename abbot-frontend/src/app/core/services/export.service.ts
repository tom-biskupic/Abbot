import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ExportService {
  private readonly http = inject(HttpClient);

  exportPoints(seriesId: number, competitionIds: number[]): Promise<Blob> {
    const params = competitionIds.map(id => `competition=${id}`).join('&');
    return firstValueFrom(this.http.get(
      `/raceseries/${seriesId}/exportPoints.json?${params}`,
      { responseType: 'blob' }
    ));
  }

  exportRaces(seriesId: number, fleetId: number): Promise<Blob> {
    return firstValueFrom(this.http.get(
      `/raceseries/${seriesId}/exportRaces.json/${fleetId}`,
      { responseType: 'blob' }
    ));
  }

  exportHandicaps(seriesId: number, fleetId: number): Promise<Blob> {
    return firstValueFrom(this.http.get(
      `/raceseries/${seriesId}/exportHandicaps.json/${fleetId}`,
      { responseType: 'blob' }
    ));
  }

  exportShortCourseHandicaps(seriesId: number, fleetId: number): Promise<Blob> {
    return firstValueFrom(this.http.get(
      `/raceseries/${seriesId}/exportShortCourseHandicaps.json/${fleetId}`,
      { responseType: 'blob' }
    ));
  }
}
