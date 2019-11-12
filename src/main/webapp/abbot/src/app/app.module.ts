import { EditUserDialogComponent } from './components/edit-user-dialog/edit-user-dialog.component';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule, Injectable } from '@angular/core';
import { TreeviewModule } from 'ngx-treeview';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavBarComponent } from './components/nav-bar/nav-bar.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { LoginComponent } from './components/login/login.component';
import { FormsModule, FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HttpInterceptor, HttpRequest, HttpHandler, HTTP_INTERCEPTORS } from '@angular/common/http';
import { ManageUsersComponent } from './components/manage-users/manage-users.component';
import { BasicAuthHtppInterceptorService } from './services/BasicAuthHTTPInterceptorService';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import { ManageRaceSeriesComponent } from './components/manage-race-series/manage-race-series.component';
import { EditRaceSeriesParamsComponent } from './components/edit-race-series-params/edit-race-series-params.component';
import { RaceSeriesComponent } from './components/race-series/race-series.component';
import { RaceSeriesSettingsComponent } from './components/race-series/race-series-settings/race-series-settings.component';
import { RaceSeriesExportComponent } from './components/race-series/race-series-export/race-series-export.component';
import { RaceSeriesRegisteredBoatsComponent } from './components/race-series/race-series-registered-boats/race-series-registered-boats.component';
import { RaceSeriesRacesComponent } from './components/race-series/race-series-races/race-series-races.component';
import { RaceSeriesResultsComponent } from './components/race-series/race-series-results/race-series-results.component';
import { RaceSeriesPointsComponent } from './components/race-series/race-series-points/race-series-points.component';
import { ManageBoatClassesComponent } from './components/race-series/settings/manage-boat-classes/manage-boat-classes.component';
import { EditBoatClassComponent } from './components/race-series/settings/edit-boat-class/edit-boat-class.component';
import { EditBoatDivisionComponent } from './components/race-series/settings/edit-boat-division/edit-boat-division.component';
import { ConfirmDialogComponent } from './components/confirm-dialog/confirm-dialog.component';
import { ManageFleetsComponent } from './components/race-series/settings/manage-fleets/manage-fleets.component';
import { EditFleetComponent } from './components/race-series/settings/edit-fleet/edit-fleet.component';
import { ManageCompetitionsComponent } from './components/race-series/settings/manage-competitions/manage-competitions.component';
import { EditCompetitionComponent } from './components/race-series/settings/edit-competition/edit-competition.component';
import { ManageHandicapsComponent } from './components/race-series/settings/manage-handicaps/manage-handicaps.component';
import { EditHandicalLimitComponent } from './components/race-series/settings/edit-handical-limit/edit-handical-limit.component';
import { ManageAuthorizedUsersComponent } from './components/race-series/settings/manage-authorized-users/manage-authorized-users.component';
import { EditAuthorizedUserComponent } from './components/race-series/settings/edit-authorized-user/edit-authorized-user.component';
import { EditRaceComponent } from './components/race-series/edit-race/edit-race.component';
import { CompetitionsForFleetPipe } from './components/race-series/edit-race/competitions-for-fleet.pipe';
import { EditBoatComponent } from './components/race-series/edit-boat/edit-boat.component';
import { RaceDayListComponent } from './components/race-series/race-day-list/race-day-list.component';
import { RaceResultListComponent } from './components/race-series/race-result-list/race-result-list.component';
import { EditRaceResultComponent } from './components/race-series/edit-race-result/edit-race-result.component';
import { DurationPipe } from './pipes/duration.pipe';

@Injectable()
export class XhrInterceptor implements HttpInterceptor {

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    const xhr = req.clone({
      headers: req.headers.set('X-Requested-With', 'XMLHttpRequest')
    });
    return next.handle(xhr);
  }
}

@NgModule({
  declarations: [
    AppComponent,
    NavBarComponent,
    LoginComponent,
    ManageUsersComponent,
    EditUserDialogComponent,
    ManageRaceSeriesComponent,
    EditRaceSeriesParamsComponent,
    RaceSeriesComponent,
    RaceSeriesSettingsComponent,
    RaceSeriesExportComponent,
    RaceSeriesRegisteredBoatsComponent,
    RaceSeriesRacesComponent,
    RaceSeriesResultsComponent,
    RaceSeriesPointsComponent,
    ManageBoatClassesComponent,
    EditBoatClassComponent,
    EditBoatDivisionComponent,
    ConfirmDialogComponent,
    ManageFleetsComponent,
    EditFleetComponent,
    ManageCompetitionsComponent,
    EditCompetitionComponent,
    ManageHandicapsComponent,
    EditHandicalLimitComponent,
    ManageAuthorizedUsersComponent,
    EditAuthorizedUserComponent,
    EditRaceComponent,
    CompetitionsForFleetPipe,
    EditBoatComponent,
    RaceDayListComponent,
    RaceResultListComponent,
    EditRaceResultComponent,
    DurationPipe
  ],
  imports: [
    BrowserModule,
    FontAwesomeModule,
    NgbModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    TreeviewModule.forRoot()
  ],
  providers: [  { provide: HTTP_INTERCEPTORS, useClass: XhrInterceptor, multi: true },
                { provide: HTTP_INTERCEPTORS, useClass: BasicAuthHtppInterceptorService, multi: true}  ],
  bootstrap: [AppComponent],
  entryComponents: [
    EditUserDialogComponent,
    EditRaceSeriesParamsComponent,
    EditBoatClassComponent,
    EditBoatDivisionComponent,
    ConfirmDialogComponent,
    EditFleetComponent,
    EditCompetitionComponent,
    EditHandicalLimitComponent,
    EditAuthorizedUserComponent,
    EditRaceComponent,
    EditBoatComponent,
    EditRaceResultComponent ]
})
export class AppModule { }
