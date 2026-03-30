import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ExportService } from '../../../../../core/services/export.service';
import { CompetitionService } from '../../../../../core/services/competition.service';
import { FleetService } from '../../../../../core/services/fleet.service';
import {
  ExportConfig, ExportConfigItem, CompetitionExportConfigItem,
  FleetExportConfigItem, Competition, Fleet, FieldValidationError
} from '../../../../../core/models/race.model';

@Component({
    selector: 'app-export-config-form',
    imports: [FormsModule],
    templateUrl: './export-config-form.component.html'
})
export class ExportConfigFormComponent implements OnInit {
  readonly modal = inject(NgbActiveModal);
  private readonly service = inject(ExportService);
  private readonly competitionService = inject(CompetitionService);
  private readonly fleetService = inject(FleetService);

  seriesId!: number;
  saving = false;
  competitions: Competition[] = [];
  fleets: Fleet[] = [];
  fieldErrors: Record<string, string> = {};
  generalError: string | null = null;

  selectedCompetitionId: number | null = null;
  selectedFleetId: number | null = null;

  private _config: ExportConfig = { name: '', tableClass: 'results-table', items: [] };

  set config(c: ExportConfig) { this._config = { ...c, items: [...c.items] }; }
  get config(): ExportConfig { return this._config; }

  ngOnInit(): void {
    Promise.all([
      this.competitionService.getAll(this.seriesId),
      this.fleetService.getAll(this.seriesId)
    ]).then(([comps, fleets]) => {
      this.competitions = comps;
      this.fleets = fleets;
    });
  }

  get availableFleets(): Fleet[] {
    const used = new Set(
      (this.config.items.filter(i => i.itemType === 'FLEET') as FleetExportConfigItem[]).map(i => i.fleetId)
    );
    return this.fleets.filter(f => f.id !== undefined && !used.has(f.id));
  }

  get availableCompetitions(): Competition[] {
    const used = new Set(
      (this.config.items.filter(i => i.itemType === 'COMPETITION') as CompetitionExportConfigItem[]).map(i => i.competitionId)
    );
    return this.competitions.filter(c => c.id !== undefined && !used.has(c.id));
  }

  itemLabel(item: ExportConfigItem): string {
    if (item.itemType === 'COMPETITION') {
      return this.competitions.find(c => c.id === item.competitionId)?.name ?? `Competition #${item.competitionId}`;
    }
    return this.fleets.find(f => f.id === item.fleetId)?.fleetName ?? `Fleet #${item.fleetId}`;
  }

  addCompetition(): void {
    if (this.selectedCompetitionId === null) return;
    this.config.items = [...this.config.items, { itemType: 'COMPETITION', competitionId: this.selectedCompetitionId }];
    this.selectedCompetitionId = null;
  }

  addFleet(): void {
    if (this.selectedFleetId === null) return;
    this.config.items = [...this.config.items, { itemType: 'FLEET', fleetId: this.selectedFleetId }];
    this.selectedFleetId = null;
  }

  removeItem(index: number): void {
    this.config.items = this.config.items.filter((_, i) => i !== index);
  }

  moveUp(index: number): void {
    if (index === 0) return;
    const items = [...this.config.items];
    [items[index - 1], items[index]] = [items[index], items[index - 1]];
    this.config.items = items;
  }

  moveDown(index: number): void {
    if (index === this.config.items.length - 1) return;
    const items = [...this.config.items];
    [items[index], items[index + 1]] = [items[index + 1], items[index]];
    this.config.items = items;
  }

  clearFieldError(f: string): void { delete this.fieldErrors[f]; }

  async ok(): Promise<void> {
    this.fieldErrors = {};
    this.generalError = null;
    this.saving = true;
    try {
      const response = await this.service.saveConfig(this.seriesId, this.config);
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

  private readonly knownFields = new Set(['name', 'tableClass']);

  private applyErrors(errors: FieldValidationError[]): void {
    for (const e of errors) {
      if (e.field && this.knownFields.has(e.field)) {
        this.fieldErrors[e.field] = e.defaultMessage;
      } else if (!this.generalError) {
        this.generalError = e.defaultMessage;
      }
    }
  }
}
