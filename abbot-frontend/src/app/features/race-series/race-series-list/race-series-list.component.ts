import { Component, inject, signal, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DatePipe } from '@angular/common';
import { NgbModal, NgbPaginationModule } from '@ng-bootstrap/ng-bootstrap';
import { RaceSeriesService } from '../../../core/services/race-series.service';
import { RaceSeriesFormComponent } from '../race-series-form/race-series-form.component';
import { RaceSeries, Page } from '../../../core/models/race-series.model';

@Component({
    selector: 'app-race-series-list',
    imports: [DatePipe, NgbPaginationModule],
    templateUrl: './race-series-list.component.html'
})
export class RaceSeriesListComponent implements OnInit {
  private readonly service = inject(RaceSeriesService);
  private readonly modalService = inject(NgbModal);
  private readonly router = inject(Router);

  page = signal<Page<RaceSeries> | null>(null);
  loading = signal(false);
  error = signal<string | null>(null);

  currentPage = 1;
  readonly pageSize = 10;

  ngOnInit(): void {
    this.load();
  }

  async load(): Promise<void> {
    this.loading.set(true);
    this.error.set(null);
    try {
      const result = await this.service.getList(this.currentPage - 1, this.pageSize);
      this.page.set(result);
    } catch {
      this.error.set('Failed to load race series.');
    } finally {
      this.loading.set(false);
    }
  }

  onPageChange(p: number): void {
    this.currentPage = p;
    this.load();
  }

  select(series: RaceSeries): void {
    this.router.navigate(['/race-series', series.id]);
  }

  async openForm(series?: RaceSeries): Promise<void> {
    const ref = this.modalService.open(RaceSeriesFormComponent);
    if (series) {
      ref.componentInstance.series = { ...series };
    }
    try {
      const saved: RaceSeries = await ref.result;
      await this.service.save(saved);
      this.load();
    } catch {
      // dismissed
    }
  }
}
