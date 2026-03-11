import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { Page } from '../models/race-series.model';
import { ValidationResponse } from '../models/race.model';

export interface User {
  id?: number;
  firstName: string;
  lastName: string;
  email: string;
  password?: string;
  organisation?: string;
  administrator: boolean;
}

@Injectable({ providedIn: 'root' })
export class UserService {
  private readonly http = inject(HttpClient);

  getList(page = 0, size = 20): Promise<Page<User>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return firstValueFrom(this.http.get<Page<User>>('/userlist.json', { params }));
  }

  getById(id: number): Promise<User> {
    return firstValueFrom(this.http.get<User>(`/user.json/${id}`));
  }

  save(user: User): Promise<ValidationResponse> {
    return firstValueFrom(this.http.post<ValidationResponse>('/user.json', user));
  }

  delete(id: number): Promise<void> {
    return firstValueFrom(this.http.delete<void>(`/user.json/${id}`));
  }
}
