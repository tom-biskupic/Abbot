import { Component, inject, signal, OnInit, Input } from '@angular/core';
import { NgbModal, NgbPaginationModule } from '@ng-bootstrap/ng-bootstrap';
import { CompetitionService } from '../../../../../core/services/competition.service';
import { CompetitionFormComponent } from '../competition-form/competition-form.component';
import { Competition } from '../../../../../core/models/race.model';
import { Page } from '../../../../../core/models/race-series.model';

@Component({
  selector: 'app-competition-list',
  standalone: true,
  imports: [NgbPaginationModule],
  templateUrl: './competition-list.component.html'
})
export class CompetitionListComponent implements OnInit {
  @Input({ required: true }) seriesId!: number;

  private readonly service = inject(CompetitionService);
  private readonly modalService = inject(NgbModal);

  page = signal<Page<Competition> | null>(null);
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
      this.error.set('Failed to load competitions.');
    } finally {
      this.loading.set(false);
    }
  }

  async openForm(id?: number): Promise<void> {
    const ref = this.modalService.open(CompetitionFormComponent);
    ref.componentInstance.seriesId = this.seriesId;
    if (id !== undefined) {
      try {
        ref.componentInstance.competition = await this.service.getById(this.seriesId, id);
      } catch {
        this.error.set('Failed to load competition.');
        ref.dismiss();
        return;
      }
    }
    try { await ref.result; this.load(); } catch { /* dismissed */ }
  }

  async deleteCompetition(id: number): Promise<void> {
    if (!confirm('Delete this competition?')) return;
    try {
      await this.service.delete(this.seriesId, id);
      this.load();
    } catch {
      this.error.set('Failed to delete competition.');
    }
  }

  onPageChange(p: number): void { this.currentPage = p; this.load(); }
}
