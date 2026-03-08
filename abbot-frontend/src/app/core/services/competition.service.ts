import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { Competition } from '../models/race.model';

@Injectable({ providedIn: 'root' })
export class CompetitionService {
  private readonly http = inject(HttpClient);

  getAll(seriesId: number): Promise<Competition[]> {
    return firstValueFrom(this.http.get<Competition[]>(`/raceseries/${seriesId}/competitionlist.json/all`));
  }
}
