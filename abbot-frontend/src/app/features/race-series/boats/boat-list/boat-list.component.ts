import { Component, inject, signal, OnInit, Input } from '@angular/core';
import { NgbModal, NgbPaginationModule } from '@ng-bootstrap/ng-bootstrap';
import { BoatService } from '../../../../core/services/boat.service';
import { BoatFormComponent } from '../boat-form/boat-form.component';
import { Boat } from '../../../../core/models/race.model';
import { Page } from '../../../../core/models/race-series.model';

@Component({
  selector: 'app-boat-list',
  standalone: true,
  imports: [NgbPaginationModule],
  templateUrl: './boat-list.component.html'
})
export class BoatListComponent implements OnInit {
  @Input({ required: true }) seriesId!: number;

  private readonly service = inject(BoatService);
  private readonly modalService = inject(NgbModal);

  page = signal<Page<Boat> | null>(null);
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
      this.error.set('Failed to load boats.');
    } finally {
      this.loading.set(false);
    }
  }

  async openForm(boatId?: number): Promise<void> {
    const ref = this.modalService.open(BoatFormComponent);
    ref.componentInstance.seriesId = this.seriesId;
    if (boatId !== undefined) {
      try {
        ref.componentInstance.boat = await this.service.getById(this.seriesId, boatId);
      } catch {
        this.error.set('Failed to load boat.');
        ref.dismiss();
        return;
      }
    }
    try { await ref.result; this.load(); } catch { /* dismissed */ }
  }

  async delete(boatId: number): Promise<void> {
    if (!confirm('Delete this boat?')) return;
    try {
      await this.service.delete(this.seriesId, boatId);
      this.load();
    } catch {
      this.error.set('Failed to delete boat.');
    }
  }

  onPageChange(p: number): void { this.currentPage = p; this.load(); }
}
