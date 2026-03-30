import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { UserService, User } from '../../../core/services/user.service';
import { FieldValidationError } from '../../../core/models/race.model';

@Component({
    selector: 'app-user-form',
    imports: [FormsModule],
    templateUrl: './user-form.component.html'
})
export class UserFormComponent {
  readonly modal = inject(NgbActiveModal);
  private readonly userService = inject(UserService);

  saving = false;
  fieldErrors: Record<string, string> = {};
  generalError: string | null = null;
  passwordConfirm = '';

  private _user: User = { firstName: '', lastName: '', email: '', administrator: false };

  set user(u: User) {
    this._user = { ...u };
    this.passwordConfirm = u.password ?? '';
  }
  get user(): User { return this._user; }

  get isNew(): boolean { return !this._user.id; }

  clearFieldError(f: string): void { delete this.fieldErrors[f]; }

  async ok(): Promise<void> {
    this.fieldErrors = {};
    this.generalError = null;

    if (this._user.password !== this.passwordConfirm) {
      this.fieldErrors['passwordConfirm'] = 'Passwords do not match';
      return;
    }

    this.saving = true;
    try {
      const response = await this.userService.save(this._user);
      if (response.status === 'SUCCESS') {
        this.modal.close();
      } else {
        this.applyErrors(response.errorMessageList ?? []);
        if (response.generalErrorText) this.generalError = response.generalErrorText;
      }
    } catch {
      this.generalError = 'An unexpected error occurred.';
    } finally {
      this.saving = false;
    }
  }

  cancel(): void { this.modal.dismiss(); }

  private applyErrors(errors: FieldValidationError[]): void {
    for (const e of errors) {
      if (e.field) this.fieldErrors[e.field] = e.defaultMessage;
      else if (!this.generalError) this.generalError = e.defaultMessage;
    }
  }
}
