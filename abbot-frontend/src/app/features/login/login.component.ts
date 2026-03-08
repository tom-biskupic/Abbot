import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.component.html'
})
export class LoginComponent {
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);

  username = '';
  password = '';
  readonly error = signal(false);
  readonly loading = signal(false);

  async onSubmit(): Promise<void> {
    this.error.set(false);
    this.loading.set(true);
    try {
      await this.auth.login(this.username, this.password);
      this.router.navigate(['/']);
    } catch {
      this.error.set(true);
    } finally {
      this.loading.set(false);
    }
  }
}
