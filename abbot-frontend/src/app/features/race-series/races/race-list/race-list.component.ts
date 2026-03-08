import { Component, inject, signal, OnInit, Input } from '@angular/core';
import { DatePipe } from '@angular/common';
import { NgbModal, NgbPaginationModule } from '@ng-bootstrap/ng-bootstrap';
import { RaceService } from '../../../../core/services/race.service';
import { RaceFormComponent } from '../race-form/race-form.component';
import { Race } from '../../../../core/models/race.model'; // needed for Page<Race> and template binding
import { Page } from '../../../../core/models/race-series.model';

@Component({
  selector: 'app-race-list',
  standalone: true,
  imports: [DatePipe, NgbPaginationModule],
  templateUrl: './race-list.component.html'
})
export class RaceListComponent implements OnInit {
  @Input({ required: true }) seriesId!: number;

  private readonly raceService = inject(RaceService);
  private readonly modalService = inject(NgbModal);

  page = signal<Page<Race> | null>(null);
  loading = signal(false);
  error = signal<string | null>(null);

  currentPage = 1;
  readonly pageSize = 20;

  ngOnInit(): void {
    this.load();
  }

  async load(): Promise<void> {
    this.loading.set(true);
    this.error.set(null);
    try {
      const result = await this.raceService.getList(this.seriesId, this.currentPage - 1, this.pageSize);
      this.page.set(result);
    } catch {
      this.error.set('Failed to load races.');
    } finally {
      this.loading.set(false);
    }
  }

  async openForm(raceId?: number): Promise<void> {
    const ref = this.modalService.open(RaceFormComponent, { size: 'lg' });
    ref.componentInstance.seriesId = this.seriesId;

    if (raceId !== undefined) {
      try {
        const race = await this.raceService.getById(this.seriesId, raceId);
        ref.componentInstance.race = race;
      } catch {
        this.error.set('Failed to load race details.');
        ref.dismiss();
        return;
      }
    }

    try {
      await ref.result;  // resolves when form saves successfully
      this.load();
    } catch {
      // dismissed
    }
  }

  async deleteRace(raceId: number): Promise<void> {
    if (!confirm('Delete this race?')) return;
    try {
      await this.raceService.delete(this.seriesId, raceId);
      this.load();
    } catch {
      this.error.set('Failed to delete race.');
    }
  }

  onPageChange(p: number): void {
    this.currentPage = p;
    this.load();
  }
}
