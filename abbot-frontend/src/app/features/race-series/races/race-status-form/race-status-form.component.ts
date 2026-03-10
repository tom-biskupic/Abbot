import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { RaceService } from '../../../../core/services/race.service';
import { Race } from '../../../../core/models/race.model';

@Component({
  selector: 'app-race-status-form',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './race-status-form.component.html'
})
export class RaceStatusFormComponent {
  readonly modal = inject(NgbActiveModal);
  private readonly raceService = inject(RaceService);

  seriesId!: number;

  private _race!: Race;
  set race(r: Race) {
    this._race = r;
    this.raceStatus = r.raceStatus ?? 'NOT_RUN';
  }
  get race(): Race { return this._race; }

  raceStatus: 'NOT_RUN' | 'COMPLETED' | 'ABANDONED' = 'NOT_RUN';
  addDNSBoats = true;
  resultStatusForNonStarters = 'DNS';
  updateHandicaps = true;

  saving = false;
  error: string | null = null;

  readonly raceStatusOptions = [
    { id: 'NOT_RUN',   label: 'Not run' },
    { id: 'COMPLETED', label: 'Completed' },
    { id: 'ABANDONED', label: 'Abandoned' },
  ] as const;

  readonly nonStarterStatusOptions = [
    { id: 'DNS', label: 'DNS' },
    { id: 'DNC', label: 'DNC' },
  ];

  async ok(): Promise<void> {
    this.saving = true;
    this.error = null;
    try {
      await this.raceService.updateRaceStatus(this.seriesId, this._race.id!, {
        raceStatus: this.raceStatus,
        addDNSBoats: this.addDNSBoats,
        resultStatusForNonStarters: this.resultStatusForNonStarters,
        updateHandicaps: this.updateHandicaps,
      });
      this.modal.close(this.raceStatus);
    } catch {
      this.error = 'Failed to update race status.';
    } finally {
      this.saving = false;
    }
  }

  cancel(): void { this.modal.dismiss(); }
}
