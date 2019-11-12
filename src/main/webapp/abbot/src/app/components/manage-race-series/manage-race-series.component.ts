import { EditRaceSeriesParamsComponent } from './../edit-race-series-params/edit-race-series-params.component';
import { RaceSeriesType, RaceSeriesModel } from './../../models/RaceSeriesModel';
import { Component, OnInit } from '@angular/core';
import { RaceSeriesService } from 'src/app/services/race-series.service';
import { faPlusSquare, faSync, faExternalLinkAlt } from '@fortawesome/free-solid-svg-icons';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-manage-race-series',
  templateUrl: './manage-race-series.component.html',
  styleUrls: ['./manage-race-series.component.css']
})
export class ManageRaceSeriesComponent implements OnInit {
  pageNumber = 0;
  pageSize = 3;
  numPages = 0;
  raceSeries: RaceSeriesModel[];
  collectionSize = 0;

  faPlusSquare = faPlusSquare;
  faSync = faSync;
  faExternalLink = faExternalLinkAlt;

  constructor(private raceSeriesService: RaceSeriesService, private modalService: NgbModal) { }

  ngOnInit()
  {
    this.fetchPage(1);
  }

  private fetchPage(pageNumber: number)
  {
    this.raceSeriesService.getRaceSeries$(pageNumber - 1, this.pageSize).subscribe(
      res =>
      {
        this.raceSeries = res.content;
        this.numPages = res.totalPages;
        this.pageNumber = res.number + 1;
        this.collectionSize = res.totalPages * this.pageSize;
      },
      err =>
      {
        console.error(err);
      });
  }

  getRaceSeries(): RaceSeriesModel[]
  {
    return this.raceSeries;
  }

  getPageNumber(): number
  {
    return this.pageNumber;
  }

  loadPage(pageNumber: number)
  {
    this.fetchPage(pageNumber);
  }

  editRaceSeries(raceSeries: RaceSeriesModel)
  {
    const modelRef = this.modalService.open(EditRaceSeriesParamsComponent);
    modelRef.componentInstance.raceSeries = raceSeries;
    modelRef.result.then(
      result =>
      {
        this.raceSeriesService.addRaceSeries$(result).subscribe(
          result =>
          {
            this.loadPage(this.pageNumber);
          },
          error =>
          {
            console.log('Update race series failed ' + error.toString());
          }
        )
      }, reason => { console.log('Race series dialog cancelled ' + reason )}
    );
  }

  addRaceSeries()
  {
    const newRaceSeries: RaceSeriesModel =
    {
      id: null,
      seriesType: RaceSeriesType.REGATTA,
      name: '',
      comment: '',
      dateCreated: null,
      lastUpdated: null
    };

    const modelRef = this.modalService.open(EditRaceSeriesParamsComponent);
    modelRef.componentInstance.raceSeries = newRaceSeries;
    modelRef.result.then(
      result =>
      {
        this.raceSeriesService.addRaceSeries$(result).subscribe(
          result =>
          {
            this.loadPage(this.pageNumber);
          },
          error =>
          {
            console.log('Update race series failed ' + error.toString());
          }
        )
      }
    );

  }
}
