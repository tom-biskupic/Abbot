import { RaceSeriesModel } from 'src/app/models/RaceSeriesModel';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { RaceSeriesService } from 'src/app/services/race-series.service';

@Component({
  selector: 'app-race-series',
  templateUrl: './race-series.component.html',
  styleUrls: ['./race-series.component.css']
})
export class RaceSeriesComponent implements OnInit {

  raceSeriesId = 0;
  raceSeries: RaceSeriesModel;

  constructor(private route: ActivatedRoute,
              private raceSeriesService: RaceSeriesService) { }

  ngOnInit() {
    this.route.params.subscribe(
      params =>
      {
        this.raceSeriesId = params['raceSeriesId'];
        this.raceSeriesService.getRaceSeries(this.raceSeriesId).subscribe(
          result =>
          {
            this.raceSeries = result;
          }
        )
      }
    )
  }

}
