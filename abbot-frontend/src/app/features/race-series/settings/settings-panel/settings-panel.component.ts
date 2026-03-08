import { Component, Input } from '@angular/core';
import { NgbAccordionModule } from '@ng-bootstrap/ng-bootstrap';
import { BoatClassListComponent } from '../boat-classes/boat-class-list/boat-class-list.component';
import { FleetListComponent } from '../fleets/fleet-list/fleet-list.component';
import { CompetitionListComponent } from '../competitions/competition-list/competition-list.component';
import { HandicapListComponent } from '../handicap/handicap-list/handicap-list.component';
import { AuthorizedUserListComponent } from '../authorized-users/authorized-user-list/authorized-user-list.component';

@Component({
  selector: 'app-settings-panel',
  standalone: true,
  imports: [
    NgbAccordionModule,
    BoatClassListComponent,
    FleetListComponent,
    CompetitionListComponent,
    HandicapListComponent,
    AuthorizedUserListComponent
  ],
  templateUrl: './settings-panel.component.html'
})
export class SettingsPanelComponent {
  @Input({ required: true }) seriesId!: number;
}
