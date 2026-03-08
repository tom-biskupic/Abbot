import { Component, inject, signal, OnInit, Input } from '@angular/core';
import { NgbModal, NgbPaginationModule } from '@ng-bootstrap/ng-bootstrap';
import { FleetService } from '../../../../../core/services/fleet.service';
import { FleetFormComponent } from '../fleet-form/fleet-form.component';
import { Fleet } from '../../../../../core/models/race.model';
import { Page } from '../../../../../core/models/race-series.model';

@Component({
  selector: 'app-fleet-list',
  standalone: true,
  imports: [NgbPaginationModule],
  templateUrl: './fleet-list.component.html'
})
export class FleetListComponent implements OnInit {
  @Input({ required: true }) seriesId!: number;

  private readonly service = inject(FleetService);
  private readonly modalService = inject(NgbModal);

  page = signal<Page<Fleet> | null>(null);
  loading = signal(false);
  error = signal<string | null>(null);
  currentPage = 1;
  readonly pageSize = 20;

  ngOnInit(): void { this.load(); }

  async load(): Promise<void> {
    this.loading.set(true);
    this.error.set(null);
    try {
      this.page.set(await this.service.getList(this.seriesId, this.currentPage - 1, this.pageSize));
    } catch {
      this.error.set('Failed to load fleets.');
    } finally {
      this.loading.set(false);
    }
  }

  async openForm(fleetId?: number): Promise<void> {
    const ref = this.modalService.open(FleetFormComponent, { size: 'lg' });
    ref.componentInstance.seriesId = this.seriesId;
    if (fleetId !== undefined) {
      try {
        ref.componentInstance.fleet = await this.service.getById(this.seriesId, fleetId);
      } catch {
        this.error.set('Failed to load fleet.');
        ref.dismiss();
        return;
      }
    }
    try { await ref.result; this.load(); } catch { /* dismissed */ }
  }

  async deleteFleet(fleetId: number): Promise<void> {
    if (!confirm('Delete this fleet?')) return;
    try {
      await this.service.delete(this.seriesId, fleetId);
      this.load();
    } catch {
      this.error.set('Failed to delete fleet.');
    }
  }

  onPageChange(p: number): void { this.currentPage = p; this.load(); }
}
