import { Component, inject, signal, OnInit, Input } from '@angular/core';
import { NgbModal, NgbPaginationModule } from '@ng-bootstrap/ng-bootstrap';
import { HandicapLimitService } from '../../../../../core/services/handicap-limit.service';
import { HandicapFormComponent } from '../handicap-form/handicap-form.component';
import { HandicapLimit } from '../../../../../core/models/settings.model';
import { Page } from '../../../../../core/models/race-series.model';

@Component({
  selector: 'app-handicap-list',
  standalone: true,
  imports: [NgbPaginationModule],
  templateUrl: './handicap-list.component.html'
})
export class HandicapListComponent implements OnInit {
  @Input({ required: true }) seriesId!: number;

  private readonly service = inject(HandicapLimitService);
  private readonly modalService = inject(NgbModal);

  page = signal<Page<HandicapLimit> | null>(null);
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
      this.error.set('Failed to load handicap limits.');
    } finally {
      this.loading.set(false);
    }
  }

  async openForm(id?: number): Promise<void> {
    const ref = this.modalService.open(HandicapFormComponent);
    ref.componentInstance.seriesId = this.seriesId;
    if (id !== undefined) {
      try {
        ref.componentInstance.limit = await this.service.getById(this.seriesId, id);
      } catch {
        this.error.set('Failed to load handicap limit.');
        ref.dismiss();
        return;
      }
    }
    try { await ref.result; this.load(); } catch { /* dismissed */ }
  }

  async deleteLimit(id: number): Promise<void> {
    if (!confirm('Delete this handicap limit?')) return;
    try {
      await this.service.delete(this.seriesId, id);
      this.load();
    } catch {
      this.error.set('Failed to delete handicap limit.');
    }
  }

  onPageChange(p: number): void { this.currentPage = p; this.load(); }
}
