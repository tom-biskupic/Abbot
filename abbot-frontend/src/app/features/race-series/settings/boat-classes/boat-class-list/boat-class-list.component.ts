import { Component, inject, signal, OnInit, Input } from '@angular/core';
import { NgbModal, NgbPaginationModule } from '@ng-bootstrap/ng-bootstrap';
import { BoatClassService } from '../../../../../core/services/boat-class.service';
import { BoatClassFormComponent } from '../boat-class-form/boat-class-form.component';
import { BoatDivisionFormComponent } from '../boat-division-form/boat-division-form.component';
import { BoatClass } from '../../../../../core/models/race.model';
import { Page } from '../../../../../core/models/race-series.model';

@Component({
  selector: 'app-boat-class-list',
  standalone: true,
  imports: [NgbPaginationModule],
  templateUrl: './boat-class-list.component.html'
})
export class BoatClassListComponent implements OnInit {
  @Input({ required: true }) seriesId!: number;

  private readonly service = inject(BoatClassService);
  private readonly modalService = inject(NgbModal);

  page = signal<Page<BoatClass> | null>(null);
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
      this.error.set('Failed to load boat classes.');
    } finally {
      this.loading.set(false);
    }
  }

  async openClassForm(boatClassId?: number): Promise<void> {
    const ref = this.modalService.open(BoatClassFormComponent);
    ref.componentInstance.seriesId = this.seriesId;
    if (boatClassId !== undefined) {
      try {
        ref.componentInstance.boatClass = await this.service.getById(this.seriesId, boatClassId);
      } catch {
        this.error.set('Failed to load boat class.');
        ref.dismiss();
        return;
      }
    }
    try { await ref.result; this.load(); } catch { /* dismissed */ }
  }

  async deleteClass(boatClassId: number): Promise<void> {
    if (!confirm('Delete this boat class?')) return;
    try {
      await this.service.delete(this.seriesId, boatClassId);
      this.load();
    } catch {
      this.error.set('Failed to delete boat class.');
    }
  }

  async openDivisionForm(boatClass: BoatClass, divisionId?: number): Promise<void> {
    const ref = this.modalService.open(BoatDivisionFormComponent);
    ref.componentInstance.seriesId = this.seriesId;
    ref.componentInstance.boatClassId = boatClass.id;
    if (divisionId !== undefined) {
      try {
        ref.componentInstance.division = await this.service.getDivision(this.seriesId, boatClass.id!, divisionId);
      } catch {
        this.error.set('Failed to load division.');
        ref.dismiss();
        return;
      }
    }
    try { await ref.result; this.load(); } catch { /* dismissed */ }
  }

  async deleteDivision(boatClassId: number, divisionId: number): Promise<void> {
    if (!confirm('Delete this division?')) return;
    try {
      await this.service.deleteDivision(this.seriesId, boatClassId, divisionId);
      this.load();
    } catch {
      this.error.set('Failed to delete division.');
    }
  }

  onPageChange(p: number): void { this.currentPage = p; this.load(); }
}
