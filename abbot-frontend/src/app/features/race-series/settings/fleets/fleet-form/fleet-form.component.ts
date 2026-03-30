import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FleetService } from '../../../../../core/services/fleet.service';
import { BoatClassService } from '../../../../../core/services/boat-class.service';
import { Fleet, BoatClass, BoatDivision, FleetSelector, FieldValidationError } from '../../../../../core/models/race.model';

@Component({
    selector: 'app-fleet-form',
    imports: [FormsModule],
    templateUrl: './fleet-form.component.html'
})
export class FleetFormComponent implements OnInit {
  readonly modal = inject(NgbActiveModal);
  private readonly fleetService = inject(FleetService);
  private readonly boatClassService = inject(BoatClassService);

  seriesId!: number;
  saving = false;
  boatClasses: BoatClass[] = [];
  classToAdd: BoatClass | null = null;
  divisionToAdd: BoatDivision | null = null;
  fieldErrors: Record<string, string> = {};
  generalError: string | null = null;

  private _fleet: Fleet = { id: 0, fleetName: '', competeOnYardstick: false, fleetClasses: [] };

  set fleet(f: Fleet) {
    this._fleet = { ...f, fleetClasses: [...(f.fleetClasses ?? [])] };
  }
  get fleet(): Fleet { return this._fleet; }

  ngOnInit(): void {
    this.boatClassService.getAll(this.seriesId).then(bc => this.boatClasses = bc);
  }

  get availableDivisions(): BoatDivision[] {
    return this.classToAdd?.divisions ?? [];
  }

  onClassChange(): void {
    this.divisionToAdd = null;
  }

  get canAdd(): boolean {
    if (!this.classToAdd) return false;
    if (this.classToAdd.divisions.length > 0 && !this.divisionToAdd) return false;
    return !this.alreadyAdded(this.classToAdd, this.divisionToAdd);
  }

  private alreadyAdded(bc: BoatClass, div: BoatDivision | null): boolean {
    return (this.fleet.fleetClasses ?? []).some(fs =>
      fs.boatClass.id === bc.id &&
      ((!div && !fs.boatDivision) || (div != null && fs.boatDivision?.id === div.id))
    );
  }

  addToFleet(): void {
    if (!this.canAdd) return;
    const selector: FleetSelector = { boatClass: this.classToAdd!, boatDivision: this.divisionToAdd ?? null };
    this.fleet.fleetClasses = [...(this.fleet.fleetClasses ?? []), selector];
    this.classToAdd = null;
    this.divisionToAdd = null;
  }

  removeFleetSelector(fs: FleetSelector): void {
    this.fleet.fleetClasses = (this.fleet.fleetClasses ?? []).filter(f => f !== fs);
  }

  compareById(a: { id: number }, b: { id: number }): boolean { return a?.id === b?.id; }
  clearFieldError(f: string): void { delete this.fieldErrors[f]; }

  async ok(): Promise<void> {
    this.fieldErrors = {};
    this.generalError = null;
    this.saving = true;
    try {
      const response = await this.fleetService.save(this.seriesId, this.fleet);
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
