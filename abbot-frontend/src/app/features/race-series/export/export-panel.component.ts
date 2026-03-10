import { Component, inject, signal, OnInit, Input } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ExportService } from '../../../core/services/export.service';
import { CompetitionService } from '../../../core/services/competition.service';
import { FleetService } from '../../../core/services/fleet.service';
import { Competition, Fleet } from '../../../core/models/race.model';

@Component({
  selector: 'app-export-panel',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './export-panel.component.html'
})
export class ExportPanelComponent implements OnInit {
  @Input({ required: true }) seriesId!: number;

  private readonly exportService = inject(ExportService);
  private readonly competitionService = inject(CompetitionService);
  private readonly fleetService = inject(FleetService);

  competitions = signal<Competition[]>([]);
  fleets = signal<Fleet[]>([]);
  loading = signal(false);
  error = signal<string | null>(null);

  // Points export
  selectedCompetition: Competition | undefined;
  competitionsToExport: Competition[] = [];

  // Races export
  selectedRacesFleet: Fleet | undefined;

  // Handicap export
  selectedHandicapFleet: Fleet | undefined;

  ngOnInit(): void {
    this.loading.set(true);
    Promise.all([
      this.competitionService.getAll(this.seriesId),
      this.fleetService.getAll(this.seriesId),
    ]).then(([competitions, fleets]) => {
      this.competitions.set(competitions);
      this.fleets.set(fleets);
    }).catch(() => {
      this.error.set('Failed to load export data.');
    }).finally(() => {
      this.loading.set(false);
    });
  }

  addCompetition(): void {
    if (!this.selectedCompetition) return;
    if (!this.competitionsToExport.some(c => c.id === this.selectedCompetition!.id)) {
      this.competitionsToExport = [...this.competitionsToExport, this.selectedCompetition];
    }
  }

  clearCompetitions(): void {
    this.competitionsToExport = [];
  }

  async exportPoints(): Promise<void> {
    if (this.competitionsToExport.length === 0) return;
    this.error.set(null);
    try {
      const blob = await this.exportService.exportPoints(
        this.seriesId,
        this.competitionsToExport.map(c => c.id!)
      );
      this.saveBlob(blob, 'ExportedCompetitions.html');
    } catch {
      this.error.set('Failed to export points.');
    }
  }

  async exportRaces(): Promise<void> {
    if (!this.selectedRacesFleet) return;
    this.error.set(null);
    try {
      const blob = await this.exportService.exportRaces(this.seriesId, this.selectedRacesFleet.id);
      this.saveBlob(blob, `${this.selectedRacesFleet.fleetName}.html`);
    } catch {
      this.error.set('Failed to export races.');
    }
  }

  async exportHandicaps(shortCourse: boolean): Promise<void> {
    if (!this.selectedHandicapFleet) return;
    this.error.set(null);
    try {
      const blob = shortCourse
        ? await this.exportService.exportShortCourseHandicaps(this.seriesId, this.selectedHandicapFleet.id)
        : await this.exportService.exportHandicaps(this.seriesId, this.selectedHandicapFleet.id);
      this.saveBlob(blob, `${this.selectedHandicapFleet.fleetName}.html`);
    } catch {
      this.error.set('Failed to export handicap table.');
    }
  }

  compareById(a: { id?: number } | undefined, b: { id?: number } | undefined): boolean {
    return a?.id === b?.id;
  }

  private saveBlob(blob: Blob, filename: string): void {
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = filename;
    a.click();
    URL.revokeObjectURL(url);
  }
}
