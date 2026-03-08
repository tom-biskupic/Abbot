import { Component, inject, signal, OnInit, Input } from '@angular/core';
import { NgbModal, NgbPaginationModule } from '@ng-bootstrap/ng-bootstrap';
import { AuthorizedUserService } from '../../../../../core/services/authorized-user.service';
import { AuthorizedUserFormComponent } from '../authorized-user-form/authorized-user-form.component';
import { UserSummary } from '../../../../../core/models/settings.model';
import { Page } from '../../../../../core/models/race-series.model';

@Component({
  selector: 'app-authorized-user-list',
  standalone: true,
  imports: [NgbPaginationModule],
  templateUrl: './authorized-user-list.component.html'
})
export class AuthorizedUserListComponent implements OnInit {
  @Input({ required: true }) seriesId!: number;

  private readonly service = inject(AuthorizedUserService);
  private readonly modalService = inject(NgbModal);

  page = signal<Page<UserSummary> | null>(null);
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
      this.error.set('Failed to load authorized users.');
    } finally {
      this.loading.set(false);
    }
  }

  async openForm(): Promise<void> {
    const ref = this.modalService.open(AuthorizedUserFormComponent);
    ref.componentInstance.seriesId = this.seriesId;
    try {
      await ref.result;
      this.load();
    } catch { /* dismissed */ }
  }

  async deleteUser(userId: number): Promise<void> {
    if (!confirm('Remove this user?')) return;
    try {
      const resp = await this.service.delete(this.seriesId, userId);
      if (resp.status === 'SUCCESS') this.load();
      else this.error.set(resp.generalErrorText ?? 'Failed to remove user.');
    } catch {
      this.error.set('Failed to remove user.');
    }
  }

  onPageChange(p: number): void { this.currentPage = p; this.load(); }
}
