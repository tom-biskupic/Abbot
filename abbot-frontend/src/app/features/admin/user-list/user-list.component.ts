import { Component, inject, signal, OnInit } from '@angular/core';
import { NgbModal, NgbPaginationModule } from '@ng-bootstrap/ng-bootstrap';
import { UserService, User } from '../../../core/services/user.service';
import { AuthService } from '../../../core/services/auth.service';
import { UserFormComponent } from '../user-form/user-form.component';
import { Page } from '../../../core/models/race-series.model';

@Component({
    selector: 'app-user-list',
    imports: [NgbPaginationModule],
    templateUrl: './user-list.component.html'
})
export class UserListComponent implements OnInit {
  private readonly service = inject(UserService);
  private readonly modalService = inject(NgbModal);
  readonly auth = inject(AuthService);

  page = signal<Page<User> | null>(null);
  loading = signal(false);
  error = signal<string | null>(null);
  currentPage = 1;
  readonly pageSize = 20;

  ngOnInit(): void { this.load(); }

  async load(): Promise<void> {
    this.loading.set(true);
    this.error.set(null);
    try {
      this.page.set(await this.service.getList(this.currentPage - 1, this.pageSize));
    } catch {
      this.error.set('Failed to load users.');
    } finally {
      this.loading.set(false);
    }
  }

  async openForm(userId?: number): Promise<void> {
    const ref = this.modalService.open(UserFormComponent);
    if (userId !== undefined) {
      try {
        ref.componentInstance.user = await this.service.getById(userId);
      } catch {
        this.error.set('Failed to load user.');
        ref.dismiss();
        return;
      }
    }
    try { await ref.result; this.load(); } catch { /* dismissed */ }
  }

  async deleteUser(userId: number): Promise<void> {
    if (!confirm('Are you sure you want to delete this user?')) return;
    try {
      await this.service.delete(userId);
      this.load();
    } catch {
      this.error.set('Failed to delete user.');
    }
  }

  onPageChange(p: number): void { this.currentPage = p; this.load(); }
}
