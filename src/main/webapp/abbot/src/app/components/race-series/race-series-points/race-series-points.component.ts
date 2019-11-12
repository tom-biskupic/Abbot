import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-race-series-points',
  templateUrl: './race-series-points.component.html',
  styleUrls: ['./race-series-points.component.css']
})
export class RaceSeriesPointsComponent implements OnInit {

  @Input()
  raceSeriesId: number;

  constructor() { }

  ngOnInit() {
  }

}
