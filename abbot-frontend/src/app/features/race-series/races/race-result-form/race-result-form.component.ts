import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { RaceResultService } from '../../../../core/services/race-result.service';
import { Race, Boat, Handicap, RaceResult, ResultStatus, FieldValidationError } from '../../../../core/models/race.model';

const STATUS_LABELS: { id: ResultStatus; label: string }[] = [
  { id: 'FINISHED', label: 'Finished' },
  { id: 'DNS',      label: 'DNS' },
  { id: 'DNF',      label: 'DNF' },
  { id: 'DNC',      label: 'DNC' },
  { id: 'OCS',      label: 'OCS' },
  { id: 'ZFP',      label: 'ZFP' },
  { id: 'UFD',      label: 'UFD' },
  { id: 'BFD',      label: 'BFD' },
  { id: 'SCP',      label: 'SCP' },
  { id: 'RET',      label: 'RET' },
  { id: 'DSQ',      label: 'DSQ' },
  { id: 'DNE',      label: 'DNE' },
  { id: 'RDG',      label: 'RDG' },
  { id: 'DPI',      label: 'DPI' },
];

// Module-level memory of last used start time string, mirroring the old AngularJS behaviour
let lastStartTime: string | null = null;

@Component({
  selector: 'app-race-result-form',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './race-result-form.component.html'
})
export class RaceResultFormComponent implements OnInit {
  readonly modal = inject(NgbActiveModal);
  private readonly service = inject(RaceResultService);

  seriesId!: number;
  race!: Race;

  saving = false;
  loading = true;
  fieldErrors: Record<string, string> = {};
  generalError: string | null = null;

  readonly statusValues = STATUS_LABELS;
  boats: Boat[] = [];
  private handicaps: Handicap[] = [];

  // Form-bound values
  selectedBoat: Boat | undefined;
  selectedStatus: ResultStatus = 'FINISHED';
  startH = 0; startM = 0; startS = 0;
  finishH = 0; finishM = 0; finishS = 0;
  overrideHandicap = false;
  handicap = 0;

  private editingResultId: number | undefined;
  /** Composed strings used only internally for timeStrToMs() and lastStartTime */
  private startTimeStr = '';
  private finishTimeStr = '';

  set result(r: RaceResult) {
    this.editingResultId = r.id;
    this.selectedBoat = r.boat;
    this.selectedStatus = r.status ?? 'FINISHED';
    const startStr = r.startTime ? this.msToHms(r.startTime) : (lastStartTime ?? '');
    [this.startH, this.startM, this.startS] = this.parseHms(startStr);
    this.startTimeStr = startStr;
    const finishStr = r.finishTime ? this.msToHms(r.finishTime) : '';
    [this.finishH, this.finishM, this.finishS] = this.parseHms(finishStr);
    this.finishTimeStr = finishStr;
    this.overrideHandicap = r.overrideHandicap ?? false;
    this.handicap = r.handicap ?? 0;
  }

  ngOnInit(): void {
    // Pre-fill start time from last used value when creating a new result
    if (!this.editingResultId && lastStartTime) {
      [this.startH, this.startM, this.startS] = this.parseHms(lastStartTime);
      this.startTimeStr = lastStartTime;
    }

    const fleetId = this.race.fleet.id;
    const raceId = this.race.id!;

    Promise.all([
      this.service.getBoatsNotSelected(this.seriesId, raceId),
      this.service.getHandicapsForFleet(this.seriesId, fleetId, raceId),
    ]).then(([boats, handicaps]) => {
      this.handicaps = handicaps;
      // When editing, include the already-selected boat in the dropdown
      if (this.selectedBoat && !boats.find(b => b.id === this.selectedBoat!.id)) {
        this.boats = [this.selectedBoat, ...boats];
      } else {
        this.boats = boats;
      }
    }).catch(() => {
      this.generalError = 'Failed to load form data.';
    }).finally(() => {
      this.loading = false;
    });
  }

  onBoatChanged(): void {
    if (!this.overrideHandicap) {
      this.handicap = this.findHandicapForBoat(this.selectedBoat?.id);
    }
  }

  onOverrideChanged(): void {
    if (!this.overrideHandicap) {
      this.handicap = this.findHandicapForBoat(this.selectedBoat?.id);
    }
  }

  compareById(a: { id?: number } | undefined, b: { id?: number } | undefined): boolean {
    return a?.id === b?.id;
  }

  clearFieldError(f: string): void { delete this.fieldErrors[f]; }

  onStartTimeChanged(): void { this.startTimeStr = this.buildHms(this.startH, this.startM, this.startS); this.clearFieldError('startTime'); }
  onFinishTimeChanged(): void { this.finishTimeStr = this.buildHms(this.finishH, this.finishM, this.finishS); this.clearFieldError('finishTime'); }

  async ok(): Promise<void> {
    this.fieldErrors = {};
    this.generalError = null;
    this.saving = true;
    const hasStart = this.startH || this.startM || this.startS;
    const hasFinish = this.finishH || this.finishM || this.finishS;
    try {
      const result: RaceResult = {
        id: this.editingResultId,
        raceId: this.race.id,
        boat: this.selectedBoat!,
        status: this.selectedStatus,
        startTime: hasStart ? this.timeStrToMs(this.startTimeStr) : undefined,
        finishTime: hasFinish ? this.timeStrToMs(this.finishTimeStr) : undefined,
        overrideHandicap: this.overrideHandicap,
        handicap: this.handicap,
      };

      const response = await this.service.save(this.seriesId, this.race.id!, result);
      if (response.status === 'SUCCESS') {
        if (hasStart) lastStartTime = this.startTimeStr;
        this.modal.close();
      } else {
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

  private findHandicapForBoat(boatId: number | undefined): number {
    if (boatId === undefined) return 0;
    return this.handicaps.find(h => h.boatID === boatId)?.value ?? 0;
  }

  private pad(n: number): string { return String(n).padStart(2, '0'); }
  private buildHms(h: number, m: number, s: number): string { return `${this.pad(h)}:${this.pad(m)}:${this.pad(s)}`; }
  private parseHms(ts: string): [number, number, number] {
    if (!ts) return [0, 0, 0];
    const [h, m, s] = ts.split(':').map(Number);
    return [h ?? 0, m ?? 0, s ?? 0];
  }
  private msToHms(ms: number): string {
    const d = new Date(ms);
    return this.buildHms(d.getHours(), d.getMinutes(), d.getSeconds());
  }

  private timeStrToMs(timeStr: string): number {
    const raceDate = new Date(this.race.raceDate);
    const [h, m, s] = timeStr.split(':').map(Number);
    raceDate.setHours(h, m, s ?? 0, 0);
    return raceDate.getTime();
  }

  private applyErrors(errors: FieldValidationError[]): void {
    for (const e of errors) {
      if (e.field) this.fieldErrors[e.field] = e.defaultMessage;
      else if (!this.generalError) this.generalError = e.defaultMessage;
    }
  }
}
