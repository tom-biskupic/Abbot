import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { AuthorizedUserService } from '../../../../../core/services/authorized-user.service';
import { FieldValidationError } from '../../../../../core/models/race.model';

@Component({
  selector: 'app-authorized-user-form',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './authorized-user-form.component.html'
})
export class AuthorizedUserFormComponent {
  readonly modal = inject(NgbActiveModal);
  private readonly service = inject(AuthorizedUserService);

  seriesId!: number;
  emailAddress = '';
  saving = false;
  fieldErrors: Record<string, string> = {};
  generalError: string | null = null;

  async ok(): Promise<void> {
    this.fieldErrors = {};
    this.generalError = null;
    this.saving = true;
    try {
      const response = await this.service.authorize(this.seriesId, this.emailAddress);
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

  clearFieldError(field: string): void { delete this.fieldErrors[field]; }

  private applyErrors(errors: FieldValidationError[]): void {
    for (const e of errors) {
      if (e.field) this.fieldErrors[e.field] = e.defaultMessage;
      else if (!this.generalError) this.generalError = e.defaultMessage;
    }
  }
}
