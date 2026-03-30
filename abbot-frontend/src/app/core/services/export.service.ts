import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { ExportConfig, ValidationResponse } from '../models/race.model';
import { Page } from '../models/race-series.model';

@Injectable({ providedIn: 'root' })
export class ExportService {
  private readonly http = inject(HttpClient);

  // -------------------------------------------------------------------------
  // ExportConfig CRUD
  // -------------------------------------------------------------------------

  getConfigList(seriesId: number, page = 0, size = 20): Promise<Page<ExportConfig>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return firstValueFrom(this.http.get<Page<ExportConfig>>(`/raceseries/${seriesId}/exportconfiglist.json`, { params }));
  }

  getConfigAll(seriesId: number): Promise<ExportConfig[]> {
    return firstValueFrom(this.http.get<ExportConfig[]>(`/raceseries/${seriesId}/exportconfiglist.json/all`));
  }

  getConfig(seriesId: number, id: number): Promise<ExportConfig> {
    return firstValueFrom(this.http.get<ExportConfig>(`/raceseries/${seriesId}/exportconfig.json/${id}`));
  }

  saveConfig(seriesId: number, config: ExportConfig): Promise<ValidationResponse> {
    return firstValueFrom(this.http.post<ValidationResponse>(`/raceseries/${seriesId}/exportconfig.json`, config));
  }

  deleteConfig(seriesId: number, id: number): Promise<ValidationResponse> {
    return firstValueFrom(this.http.delete<ValidationResponse>(`/raceseries/${seriesId}/exportconfig.json/${id}`));
  }

  // -------------------------------------------------------------------------
  // Export generation
  // -------------------------------------------------------------------------

  exportAll(seriesId: number, exportConfigId: number): Promise<Blob> {
    return firstValueFrom(this.http.get(
      `/raceseries/${seriesId}/exportAll.json/${exportConfigId}`,
      { responseType: 'blob' }
    ));
  }

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
