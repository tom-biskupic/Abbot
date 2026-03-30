import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { HandicapLimitService } from '../../../../../core/services/handicap-limit.service';
import { FleetService } from '../../../../../core/services/fleet.service';
import { HandicapLimit } from '../../../../../core/models/settings.model';
import { Fleet, FieldValidationError } from '../../../../../core/models/race.model';

@Component({
    selector: 'app-handicap-form',
    imports: [FormsModule],
    templateUrl: './handicap-form.component.html'
})
export class HandicapFormComponent implements OnInit {
  readonly modal = inject(NgbActiveModal);
  private readonly service = inject(HandicapLimitService);
  private readonly fleetService = inject(FleetService);

  seriesId!: number;
  saving = false;
  fleets: Fleet[] = [];
  fieldErrors: Record<string, string> = {};
  generalError: string | null = null;

  private _limit: HandicapLimit = { fleet: null!, limit: 0 };

  set limit(v: HandicapLimit) { this._limit = { ...v }; }
  get limit(): HandicapLimit { return this._limit; }

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
      const response = await this.service.save(this.seriesId, this.limit);
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
