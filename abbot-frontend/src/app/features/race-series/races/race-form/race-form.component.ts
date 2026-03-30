import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Race, Fleet, Competition, FieldValidationError } from '../../../../core/models/race.model';
import { FleetService } from '../../../../core/services/fleet.service';
import { CompetitionService } from '../../../../core/services/competition.service';
import { RaceService } from '../../../../core/services/race.service';

@Component({
    selector: 'app-race-form',
    imports: [FormsModule],
    templateUrl: './race-form.component.html'
})
export class RaceFormComponent implements OnInit {
  readonly modal = inject(NgbActiveModal);
  private readonly fleetService = inject(FleetService);
  private readonly competitionService = inject(CompetitionService);
  private readonly raceService = inject(RaceService);

  seriesId!: number;
  saving = false;

  // Setter keeps raceDateStr in sync when parent sets race after open()
  private _race: Race = {
    name: '',
    raceDate: Date.now(),
    fleet: null!,
    shortCourseRace: false,
    competitions: []
  };

  raceDateStr = this.timestampToDateInput(Date.now());
  fieldErrors: Record<string, string> = {};
  generalError: string | null = null;

  set race(r: Race) {
    this._race = { ...r, competitions: [...(r.competitions ?? [])] };
    this.raceDateStr = this.timestampToDateInput(r.raceDate);
  }

  get race(): Race {
    return this._race;
  }

  fleets: Fleet[] = [];
  allCompetitions: Competition[] = [];
  competitionToAdd: Competition | null = null;

  ngOnInit(): void {
    this.fleetService.getAll(this.seriesId).then(f => this.fleets = f);
    this.competitionService.getAll(this.seriesId).then(c => this.allCompetitions = c);
  }

  get availableCompetitions(): Competition[] {
    if (!this.race.fleet) return [];
    const selectedIds = new Set(this.race.competitions.map(c => c.id));
    return this.allCompetitions.filter(
      c => c.fleet.id === this.race.fleet.id && !selectedIds.has(c.id)
    );
  }

  compareById(a: { id: number }, b: { id: number }): boolean {
    return a?.id === b?.id;
  }

  clearFieldError(field: string): void {
    delete this.fieldErrors[field];
  }

  onFleetChange(): void {
    this.race.competitions = [];
    this.competitionToAdd = null;
    this.clearFieldError('fleet');
  }

  addCompetition(): void {
    if (this.competitionToAdd) {
      this.race.competitions = [...this.race.competitions, this.competitionToAdd];
      this.competitionToAdd = null;
      this.clearFieldError('competitions');
    }
  }

  removeCompetition(comp: Competition): void {
    this.race.competitions = this.race.competitions.filter(c => c.id !== comp.id);
  }

  async ok(): Promise<void> {
    this.race.raceDate = this.dateInputToTimestamp(this.raceDateStr);
    this.fieldErrors = {};
    this.generalError = null;
    this.saving = true;
    try {
      const response = await this.raceService.save(this.seriesId, this.race);
      if (response.status === 'SUCCESS') {
        this.modal.close();
      } else {
        this.applyErrors(response.errorMessageList ?? []);
        if (response.generalErrorText) {
          this.generalError = response.generalErrorText;
        }
      }
    } catch {
      this.generalError = 'An unexpected error occurred. Please try again.';
    } finally {
      this.saving = false;
    }
  }

  cancel(): void {
    this.modal.dismiss();
  }

  private applyErrors(errors: FieldValidationError[]): void {
    for (const error of errors) {
      if (error.field) {
        this.fieldErrors[error.field] = error.defaultMessage;
      } else if (!this.generalError) {
        this.generalError = error.defaultMessage;
      }
    }
  }

  private timestampToDateInput(ts: number): string {
    const d = new Date(ts);
    const year = d.getFullYear();
    const month = String(d.getMonth() + 1).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  private dateInputToTimestamp(s: string): number {
    return new Date(s).getTime();
  }
}
