import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-race-series-export',
  templateUrl: './race-series-export.component.html',
  styleUrls: ['./race-series-export.component.css']
})
export class RaceSeriesExportComponent implements OnInit {

  @Input()
  raceSeriesId: number;

  constructor() { }

  ngOnInit() {
  }

}
