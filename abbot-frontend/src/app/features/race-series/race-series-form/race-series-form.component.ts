import { Component, inject, input, output, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { RaceSeries, RaceSeriesType } from '../../../core/models/race-series.model';

@Component({
  selector: 'app-race-series-form',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './race-series-form.component.html'
})
export class RaceSeriesFormComponent {
  readonly modal = inject(NgbActiveModal);

  series: RaceSeries = { name: '', seriesType: 'SEASON', comment: '' };

  readonly seriesTypes: { id: RaceSeriesType; label: string }[] = [
    { id: 'SEASON', label: 'Season' },
    { id: 'REGATTA', label: 'Regatta' }
  ];

  ok(): void {
    this.modal.close(this.series);
  }

  cancel(): void {
    this.modal.dismiss();
  }
}
