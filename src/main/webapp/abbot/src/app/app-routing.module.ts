import { ManageRaceSeriesComponent } from './components/manage-race-series/manage-race-series.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { ManageUsersComponent } from './components/manage-users/manage-users.component';
import { RaceSeriesComponent } from './components/race-series/race-series.component';

const routes: Routes = [
  { path: 'loginform', component: LoginComponent },
  { path: 'manageusers', component: ManageUsersComponent},
  { path: 'myraceseries', component: ManageRaceSeriesComponent},
  { path: 'raceseries/:raceSeriesId', component: RaceSeriesComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
 }
