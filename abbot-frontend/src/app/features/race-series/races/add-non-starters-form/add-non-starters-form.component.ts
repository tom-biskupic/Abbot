import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { RaceResultService } from '../../../../core/services/race-result.service';

@Component({
    selector: 'app-add-non-starters-form',
    imports: [FormsModule],
    templateUrl: './add-non-starters-form.component.html'
})
export class AddNonStartersFormComponent {
  readonly modal = inject(NgbActiveModal);
  private readonly service = inject(RaceResultService);

  seriesId!: number;
  raceId!: number;

  resultStatus = 'DNS';
  saving = false;
  error: string | null = null;

  readonly statusOptions = [
    { id: 'DNS', label: 'DNS' },
    { id: 'DNC', label: 'DNC' },
  ];

  async ok(): Promise<void> {
    this.saving = true;
    this.error = null;
    try {
      await this.service.addNonStarters(this.seriesId, this.raceId, this.resultStatus);
      this.modal.close();
    } catch {
      this.error = 'Failed to add non-starters.';
    } finally {
      this.saving = false;
    }
  }

  cancel(): void { this.modal.dismiss(); }
}
