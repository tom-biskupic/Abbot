import { Component, inject, signal, OnInit, Input } from '@angular/core';
import { DatePipe } from '@angular/common';
import { NgbPaginationModule } from '@ng-bootstrap/ng-bootstrap';
import { RaceService } from '../../../../core/services/race.service';
import { RaceResultService } from '../../../../core/services/race-result.service';
import { Race, RaceDay, RaceResult } from '../../../../core/models/race.model';
import { Page } from '../../../../core/models/race-series.model';

@Component({
  selector: 'app-race-days',
  standalone: true,
  imports: [DatePipe, NgbPaginationModule],
  templateUrl: './race-days.component.html',
  styles: [`
    .tree-node {
      cursor: pointer;
      user-select: none;
      border-radius: 4px;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }
    .tree-node:hover {
      background-color: var(--bs-secondary-bg);
    }
    .tree-node.selected {
      background-color: var(--bs-primary);
      color: white;
    }
  `]
})
export class RaceDaysComponent implements OnInit {
  @Input({ required: true }) seriesId!: number;

  private readonly raceService = inject(RaceService);
  private readonly raceResultService = inject(RaceResultService);

  raceDays = signal<RaceDay[]>([]);
  loading = signal(false);
  error = signal<string | null>(null);

  expandedDays = signal(new Set<number>());
  selectedRace = signal<Race | null>(null);

  resultsPage = signal<Page<RaceResult> | null>(null);
  resultsLoading = signal(false);
  resultsError = signal<string | null>(null);

  currentPage = 1;
  readonly pageSize = 20;

  ngOnInit(): void {
    this.loadRaceDays();
  }

  async loadRaceDays(): Promise<void> {
    this.loading.set(true);
    this.error.set(null);
    try {
      const days = await this.raceService.getRaceDays(this.seriesId);
      this.raceDays.set(days);

      // Auto-select: expand last day and select its first race
      if (days.length > 0) {
        const lastDay = days[days.length - 1];
        this.expandedDays.set(new Set([lastDay.day]));
        if (lastDay.races.length > 0) {
          await this.selectRace(lastDay.races[0]);
        }
      }
    } catch {
      this.error.set('Failed to load race days.');
    } finally {
      this.loading.set(false);
    }
  }

  toggleDay(day: number): void {
    const current = new Set(this.expandedDays());
    if (current.has(day)) {
      current.delete(day);
    } else {
      current.add(day);
    }
    this.expandedDays.set(current);
  }

  isExpanded(day: number): boolean {
    return this.expandedDays().has(day);
  }

  async selectRace(race: Race): Promise<void> {
    this.selectedRace.set(race);
    this.currentPage = 1;
    await this.loadResults();
  }

  async loadResults(): Promise<void> {
    const race = this.selectedRace();
    if (!race?.id) return;
    this.resultsLoading.set(true);
    this.resultsError.set(null);
    try {
      const page = await this.raceResultService.getList(
        this.seriesId, race.id, this.currentPage - 1, this.pageSize);
      this.resultsPage.set(page);
    } catch {
      this.resultsError.set('Failed to load results.');
    } finally {
      this.resultsLoading.set(false);
    }
  }

  async deleteResult(resultId: number): Promise<void> {
    const race = this.selectedRace();
    if (!race?.id || !confirm('Delete this result?')) return;
    try {
      await this.raceResultService.delete(this.seriesId, race.id, resultId);
      await this.loadResults();
    } catch {
      this.resultsError.set('Failed to delete result.');
    }
  }

  async updateHandicaps(): Promise<void> {
    const race = this.selectedRace();
    if (!race?.id) return;
    try {
      await this.raceResultService.updateHandicaps(this.seriesId, race.id);
      await this.loadResults();
    } catch {
      this.resultsError.set('Failed to update handicaps.');
    }
  }

  onPageChange(p: number): void {
    this.currentPage = p;
    this.loadResults();
  }

  formatDuration(seconds: number | undefined): string {
    if (!seconds) return '';
    const h = Math.floor(seconds / 3600);
    const m = Math.floor((seconds % 3600) / 60);
    const s = seconds % 60;
    return `${String(h).padStart(2, '0')}:${String(m).padStart(2, '0')}:${String(s).padStart(2, '0')}`;
  }
}
