import { Injectable, signal, computed, inject } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';

export interface UserInfo {
  name: string;
  authorities: { authority: string }[];
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);

  private readonly userSignal = signal<UserInfo | null>(null);

  readonly user = this.userSignal.asReadonly();
  readonly isAuthenticated = computed(() => this.userSignal() !== null);
  readonly isAdmin = computed(() =>
    this.userSignal()?.authorities?.some(a => a.authority === 'ROLE_ADMIN') ?? false
  );

  async checkAuth(): Promise<void> {
    try {
      const user = await firstValueFrom(this.http.get<UserInfo>('/user'));
      this.userSignal.set(user);
    } catch {
      this.userSignal.set(null);
    }
  }

  async login(username: string, password: string): Promise<void> {
    const body = new HttpParams()
      .set('username', username)
      .set('password', password);

    await firstValueFrom(
      this.http.post('/perform_login', body.toString(), {
        headers: new HttpHeaders({ 'Content-Type': 'application/x-www-form-urlencoded' }),
        responseType: 'text'
      })
    );

    await this.checkAuth();
  }

  async logout(): Promise<void> {
    try {
      await firstValueFrom(this.http.post('/logout', null, { responseType: 'text' }));
    } finally {
      this.userSignal.set(null);
      this.router.navigate(['/']);
    }
  }
}
