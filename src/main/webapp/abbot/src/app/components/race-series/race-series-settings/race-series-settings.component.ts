import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-race-series-settings',
  templateUrl: './race-series-settings.component.html',
  styleUrls: ['./race-series-settings.component.css']
})
export class RaceSeriesSettingsComponent implements OnInit {

  @Input()
  raceSeriesId: number;

  constructor() { }

  ngOnInit() {
  }

}
