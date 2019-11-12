import { RaceService } from './../../../services/race.service';
import { Component, OnInit, Input } from '@angular/core';
import { RaceDayModel } from 'src/app/models/RaceDayModel';
import { Subject } from 'rxjs';
import { RaceModel } from 'src/app/models/RaceModel';

@Component({
  selector: 'app-race-series-results',
  templateUrl: './race-series-results.component.html',
  styleUrls: ['./race-series-results.component.css']
})
export class RaceSeriesResultsComponent implements OnInit {

  @Input()
  raceSeriesId: number;

  raceSelectionEventSubject: Subject<RaceModel> = new Subject<RaceModel>();

  constructor() { }

  ngOnInit()
  {
  }

  raceSelected($event)
  {
    console.log('Got race selection for ' + $event.name);
    this.raceSelectionEventSubject.next($event);
  }
}
