import { RaceService } from './../../../services/race.service';
import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { RaceDayModel } from 'src/app/models/RaceDayModel';
import { RaceModel } from 'src/app/models/RaceModel';

@Component({
  selector: 'app-race-day-list',
  templateUrl: './race-day-list.component.html',
  styleUrls: ['./race-day-list.component.css' ]
})
export class RaceDayListComponent implements OnInit {

  @Input() raceSeriesId: number;
  @Output() raceSelection = new EventEmitter();

  raceDays: RaceDayModel[] = [];

  constructor( private raceService: RaceService )
  { }

  ngOnInit()
  {
    this.raceService.raceDay$(this.raceSeriesId).subscribe(
      raceDays =>
      {
        this.raceDays = raceDays;
      } );
  }

  select(race: RaceModel)
  {
    this.raceSelection.next(race);
  }
}
