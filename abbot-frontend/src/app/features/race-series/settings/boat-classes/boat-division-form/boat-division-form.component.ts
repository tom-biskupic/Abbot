import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { BoatClassService } from '../../../../../core/services/boat-class.service';
import { BoatDivision, FieldValidationError } from '../../../../../core/models/race.model';

@Component({
    selector: 'app-boat-division-form',
    imports: [FormsModule],
    templateUrl: './boat-division-form.component.html'
})
export class BoatDivisionFormComponent {
  readonly modal = inject(NgbActiveModal);
  private readonly service = inject(BoatClassService);

  seriesId!: number;
  boatClassId!: number;
  saving = false;
  fieldErrors: Record<string, string> = {};
  generalError: string | null = null;

  private _division: BoatDivision = { name: '', yardStick: 0 };

  set division(d: BoatDivision) { this._division = { ...d }; }
  get division(): BoatDivision { return this._division; }

  clearFieldError(f: string): void { delete this.fieldErrors[f]; }

  async ok(): Promise<void> {
    this.fieldErrors = {};
    this.generalError = null;
    this.saving = true;
    try {
      const response = await this.service.saveDivision(this.seriesId, this.boatClassId, this.division);
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
