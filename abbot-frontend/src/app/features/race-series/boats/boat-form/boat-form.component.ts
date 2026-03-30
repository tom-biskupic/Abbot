import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { BoatService } from '../../../../core/services/boat.service';
import { BoatClassService } from '../../../../core/services/boat-class.service';
import { Boat, BoatClass, BoatDivision, FieldValidationError } from '../../../../core/models/race.model';

@Component({
    selector: 'app-boat-form',
    imports: [FormsModule],
    templateUrl: './boat-form.component.html'
})
export class BoatFormComponent implements OnInit {
  readonly modal = inject(NgbActiveModal);
  private readonly boatService = inject(BoatService);
  private readonly boatClassService = inject(BoatClassService);

  seriesId!: number;
  saving = false;
  fieldErrors: Record<string, string> = {};
  generalError: string | null = null;
  boatClasses: BoatClass[] = [];
  availableDivisions: BoatDivision[] = [];

  private _boat: Boat = { name: '', sailNumber: '' };

  set boat(b: Boat) {
    this._boat = { ...b };
    this.onBoatClassChange();
  }
  get boat(): Boat { return this._boat; }

  ngOnInit(): void {
    this.boatClassService.getAll(this.seriesId).then(classes => {
      this.boatClasses = classes;
      this.onBoatClassChange();
    });
  }

  onBoatClassChange(): void {
    const selected = this.boatClasses.find(c => c.id === this._boat.boatClass?.id);
    this.availableDivisions = selected?.divisions ?? [];
    if (!this.availableDivisions.find(d => d.id === this._boat.division?.id)) {
      this._boat.division = undefined;
    }
  }

  // ng-bootstrap select tracks by reference; this comparator matches by id
  compareById(a: { id?: number } | undefined, b: { id?: number } | undefined): boolean {
    return a?.id === b?.id;
  }

  clearFieldError(f: string): void { delete this.fieldErrors[f]; }

  async ok(): Promise<void> {
    this.fieldErrors = {};
    this.generalError = null;
    this.saving = true;
    try {
      const response = await this.boatService.save(this.seriesId, this._boat);
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
