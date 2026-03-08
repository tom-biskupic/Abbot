import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { CompetitionService } from '../../../../../core/services/competition.service';
import { FleetService } from '../../../../../core/services/fleet.service';
import { Competition, Fleet, FieldValidationError } from '../../../../../core/models/race.model';

@Component({
  selector: 'app-competition-form',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './competition-form.component.html'
})
export class CompetitionFormComponent implements OnInit {
  readonly modal = inject(NgbActiveModal);
  private readonly service = inject(CompetitionService);
  private readonly fleetService = inject(FleetService);

  seriesId!: number;
  saving = false;
  fleets: Fleet[] = [];
  fieldErrors: Record<string, string> = {};
  generalError: string | null = null;

  readonly pointsSystemValues = [
    { id: 'LOW_POINTS', label: 'Low Points System' },
    { id: 'BONUS_POINTS', label: 'Bonus Points System' }
  ];

  readonly resultTypeValues = [
    { id: 'SCRATCH_RESULT', label: 'Use scratch placings' },
    { id: 'HANDICAP_RESULT', label: 'Use handicap placings' }
  ];

  private _competition: Competition = {
    name: '', fleet: null!, pointsSystem: 'LOW_POINTS',
    resultType: 'HANDICAP_RESULT', fleetSize: 0, drops: 0
  };

  set competition(c: Competition) { this._competition = { ...c }; }
  get competition(): Competition { return this._competition; }

  ngOnInit(): void {
    this.fleetService.getAll(this.seriesId).then(f => this.fleets = f);
  }

  compareById(a: { id: number }, b: { id: number }): boolean { return a?.id === b?.id; }
  clearFieldError(f: string): void { delete this.fieldErrors[f]; }

  async ok(): Promise<void> {
    this.fieldErrors = {};
    this.generalError = null;
    this.saving = true;
    try {
      const response = await this.service.save(this.seriesId, this.competition);
      if (response.status === 'SUCCESS') this.modal.close();
      else {
        this.applyErrors(response.errorMessageList ?? []);
        if (response.generalErrorText) this.generalError = response.generalErrorText;
      }
    } catch {
      this.generalError = 'An unexpected error occurred.';
    } finally {
      this.saving = false;
    }
  }

  cancel(): void { this.modal.dismiss(); }

  private applyErrors(errors: FieldValidationError[]): void {
    for (const e of errors) {
      if (e.field) this.fieldErrors[e.field] = e.defaultMessage;
      else if (!this.generalError) this.generalError = e.defaultMessage;
    }
  }
}
