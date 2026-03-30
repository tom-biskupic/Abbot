import { Component, inject, signal, OnInit, Input } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ExportService } from '../../../core/services/export.service';
import { FleetService } from '../../../core/services/fleet.service';
import { ExportConfig, Fleet } from '../../../core/models/race.model';

@Component({
  selector: 'app-export-panel',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './export-panel.component.html'
})
export class ExportPanelComponent implements OnInit {
  @Input({ required: true }) seriesId!: number;

  private readonly exportService = inject(ExportService);
  private readonly fleetService = inject(FleetService);

  exportConfigs = signal<ExportConfig[]>([]);
  fleets = signal<Fleet[]>([]);
  loading = signal(false);
  error = signal<string | null>(null);

  // Export all
  selectedExportConfig: ExportConfig | undefined;

  // Handicap export
  selectedHandicapFleet: Fleet | undefined;

  ngOnInit(): void {
    this.loading.set(true);
    Promise.all([
      this.exportService.getConfigAll(this.seriesId),
      this.fleetService.getAll(this.seriesId),
    ]).then(([configs, fleets]) => {
      this.exportConfigs.set(configs);
      this.fleets.set(fleets);
    }).catch(() => {
      this.error.set('Failed to load export data.');
    }).finally(() => {
      this.loading.set(false);
    });
  }

  async exportAll(): Promise<void> {
    if (!this.selectedExportConfig) return;
    this.error.set(null);
    try {
      const blob = await this.exportService.exportAll(this.seriesId, this.selectedExportConfig.id!);
      this.saveBlob(blob, `${this.selectedExportConfig.name}.html`);
    } catch {
      this.error.set('Failed to export.');
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
