import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./features/home/home.component').then(m => m.HomeComponent)
  },
  {
    path: 'login',
    loadComponent: () => import('./features/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'race-series',
    canActivate: [authGuard],
    loadComponent: () => import('./features/race-series/race-series-list/race-series-list.component').then(m => m.RaceSeriesListComponent)
  },
  {
    path: 'race-series/:id',
    canActivate: [authGuard],
    loadComponent: () => import('./features/race-series/race-series-detail/race-series-detail.component').then(m => m.RaceSeriesDetailComponent)
  },
  {
    path: '**',
    redirectTo: ''
  }
];
