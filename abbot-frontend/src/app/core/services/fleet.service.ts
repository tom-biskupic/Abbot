import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { Fleet } from '../models/race.model';

@Injectable({ providedIn: 'root' })
export class FleetService {
  private readonly http = inject(HttpClient);

  getAll(seriesId: number): Promise<Fleet[]> {
    return firstValueFrom(this.http.get<Fleet[]>(`/raceseries/${seriesId}/fleetlist.json/all`));
  }
}
