import { Component, inject, signal, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NgbNavModule } from '@ng-bootstrap/ng-bootstrap';
import { RaceSeriesService } from '../../../core/services/race-series.service';
import { RaceSeries } from '../../../core/models/race-series.model';
import { RaceListComponent } from '../races/race-list/race-list.component';
import { RaceDaysComponent } from '../races/race-days/race-days.component';
import { SettingsPanelComponent } from '../settings/settings-panel/settings-panel.component';

@Component({
  selector: 'app-race-series-detail',
  standalone: true,
  imports: [NgbNavModule, RaceListComponent, RaceDaysComponent, SettingsPanelComponent],
  templateUrl: './race-series-detail.component.html'
})
export class RaceSeriesDetailComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly service = inject(RaceSeriesService);

  raceSeries = signal<RaceSeries | null>(null);
  loading = signal(false);
  error = signal<string | null>(null);
  activeTab = 'results';

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.load(id);
  }

  async load(id: number): Promise<void> {
    this.loading.set(true);
    this.error.set(null);
    try {
      const series = await this.service.getById(id);
      this.raceSeries.set(series);
    } catch {
      this.error.set('Failed to load race series.');
    } finally {
      this.loading.set(false);
    }
  }
}
