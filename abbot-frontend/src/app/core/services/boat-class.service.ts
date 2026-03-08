import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { BoatClass, BoatDivision, ValidationResponse } from '../models/race.model';
import { Page } from '../models/race-series.model';

@Injectable({ providedIn: 'root' })
export class BoatClassService {
  private readonly http = inject(HttpClient);

  getList(seriesId: number, page = 0, size = 20): Promise<Page<BoatClass>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return firstValueFrom(this.http.get<Page<BoatClass>>(`/raceseries/${seriesId}/boatclasslist.json`, { params }));
  }

  getAll(seriesId: number): Promise<BoatClass[]> {
    return firstValueFrom(this.http.get<BoatClass[]>(`/raceseries/${seriesId}/boatclasslist.json/all`));
  }

  getById(seriesId: number, boatClassId: number): Promise<BoatClass> {
    return firstValueFrom(this.http.get<BoatClass>(`/raceseries/${seriesId}/boatclass.json/${boatClassId}`));
  }

  save(seriesId: number, boatClass: BoatClass): Promise<ValidationResponse> {
    return firstValueFrom(this.http.post<ValidationResponse>(`/raceseries/${seriesId}/boatclass.json`, boatClass));
  }

  delete(seriesId: number, boatClassId: number): Promise<ValidationResponse> {
    return firstValueFrom(this.http.delete<ValidationResponse>(`/raceseries/${seriesId}/boatclass.json/${boatClassId}`));
  }

  getDivision(seriesId: number, boatClassId: number, divisionId: number): Promise<BoatDivision> {
    return firstValueFrom(this.http.get<BoatDivision>(`/raceseries/${seriesId}/boatclass.json/${boatClassId}/division.json/${divisionId}`));
  }

  saveDivision(seriesId: number, boatClassId: number, division: BoatDivision): Promise<ValidationResponse> {
    return firstValueFrom(this.http.post<ValidationResponse>(`/raceseries/${seriesId}/boatclass.json/${boatClassId}/division.json`, division));
  }

  deleteDivision(seriesId: number, boatClassId: number, divisionId: number): Promise<ValidationResponse> {
    return firstValueFrom(this.http.delete<ValidationResponse>(`/raceseries/${seriesId}/boatclass.json/${boatClassId}/division.json/${divisionId}`));
  }
}
