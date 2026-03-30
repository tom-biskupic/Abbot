import { Component, inject, signal, OnInit, Input } from '@angular/core';
import { NgbModal, NgbPaginationModule } from '@ng-bootstrap/ng-bootstrap';
import { ExportService } from '../../../../../core/services/export.service';
import { ExportConfigFormComponent } from '../export-config-form/export-config-form.component';
import { ExportConfig } from '../../../../../core/models/race.model';
import { Page } from '../../../../../core/models/race-series.model';

@Component({
    selector: 'app-export-config-list',
    imports: [NgbPaginationModule],
    templateUrl: './export-config-list.component.html'
})
export class ExportConfigListComponent implements OnInit {
  @Input({ required: true }) seriesId!: number;

  private readonly service = inject(ExportService);
  private readonly modalService = inject(NgbModal);

  page = signal<Page<ExportConfig> | null>(null);
  loading = signal(false);
  error = signal<string | null>(null);
  currentPage = 1;
  readonly pageSize = 20;

  ngOnInit(): void { this.load(); }

  async load(): Promise<void> {
    this.loading.set(true);
    this.error.set(null);
    try {
      this.page.set(await this.service.getConfigList(this.seriesId, this.currentPage - 1, this.pageSize));
    } catch {
      this.error.set('Failed to load export configurations.');
    } finally {
      this.loading.set(false);
    }
  }

  async openForm(id?: number): Promise<void> {
    const ref = this.modalService.open(ExportConfigFormComponent, { size: 'lg' });
    ref.componentInstance.seriesId = this.seriesId;
    if (id !== undefined) {
      try {
        ref.componentInstance.config = await this.service.getConfig(this.seriesId, id);
      } catch {
        this.error.set('Failed to load export configuration.');
        ref.dismiss();
        return;
      }
    }
    try { await ref.result; this.load(); } catch { /* dismissed */ }
  }

  async deleteConfig(id: number): Promise<void> {
    if (!confirm('Delete this export configuration?')) return;
    try {
      await this.service.deleteConfig(this.seriesId, id);
      this.load();
    } catch {
      this.error.set('Failed to delete export configuration.');
    }
  }

  onPageChange(p: number): void { this.currentPage = p; this.load(); }
}
