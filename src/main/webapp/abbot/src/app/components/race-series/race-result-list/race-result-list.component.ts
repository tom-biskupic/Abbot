import { Component, OnInit, Input } from '@angular/core';
import { Observable } from 'rxjs';
import { RaceModel } from 'src/app/models/RaceModel';
import { ThrowStmt } from '@angular/compiler';
import { RaceResultService } from 'src/app/services/race-result.service';
import { RaceResultModel } from 'src/app/models/RaceResultModel';
import { faPlusSquare, faSync, faTrashAlt } from '@fortawesome/free-solid-svg-icons';
import { ConfirmDeleteService } from 'src/app/services/confirm-delete.service';
import { EditRaceResultComponent } from '../edit-race-result/edit-race-result.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { HandicapModel } from '../../../models/Handicap';

@Component({
  selector: 'app-race-result-list',
  templateUrl: './race-result-list.component.html',
  styleUrls: ['./race-result-list.component.css']
})
export class RaceResultListComponent implements OnInit {

  @Input() raceSelectionEvent: Observable<RaceModel>;
  @Input() raceSeriesId: number;

  race: RaceModel = null;
  raceSelectionSubscription: any;
  handicaps: HandicapModel[];
  results: RaceResultModel[];
  numPages = 0;
  pageNumber = 0;
  pageSize = 20;
  collectionSize = 0;

  faPlusSquare = faPlusSquare;
  faSync = faSync;
  faTrashAlt = faTrashAlt;

  constructor(private raceResultService: RaceResultService,
              private modalService: NgbModal,
              private confirmDeleteService: ConfirmDeleteService) { }

  ngOnInit()
  {
    this.raceSelectionSubscription = this.raceSelectionEvent.subscribe(
      race =>
      {
        this.race = race;
        this.fetchPage(1);
        this.raceResultService.getHandicapsForRace$(this.raceSeriesId,this.race).subscribe(
          result => this.handicaps = result,
          error => console.log(error)
        );
      });

  }

  private fetchPage(pageNumber: number)
  {
    this.raceResultService.getResults$(this.raceSeriesId, this.race, pageNumber - 1, this.pageSize).subscribe(
      res =>
      {
        this.results = res.content;
        this.numPages = res.totalPages;
        this.pageNumber = res.number + 1;
        this.collectionSize = res.totalPages * this.pageSize;
      },
      err => console.error(err)
    );
  }

  getPageNumber(): number
  {
    return this.pageNumber;
  }

  loadPage(pageNumber: number)
  {
    this.fetchPage(pageNumber);
  }

  addResult()
  {
    const newResult: RaceResultModel = 
    {
      id: null,
      raceId: this.race.id,
      boat: null,
      handicap: 0,
      overrideHandicap: false,
      startTime: null,
      finishTime: null,
      status: null,
      sailingTime: 0,
      correctedTime: 0,
      handicapPlace: 0,
      scratchPlace: 0
    };

    const modelRef = this.modalService.open(EditRaceResultComponent);
    modelRef.componentInstance.raceResult = newResult;
    modelRef.componentInstance.race = this.race;
    modelRef.componentInstance.raceSeriesId = this.raceSeriesId;
    modelRef.componentInstance.handicaps = this.handicaps;

    modelRef.result.then(
      result =>
      {
        this.raceResultService.addResult$(this.raceSeriesId,this.race,result).subscribe(
          result => { this.loadPage(this.pageNumber) },
          error => { console.log('Add result failed' + error.toString()) }
        )
      });
  }

  editResult(result: RaceResultModel)
  {

    const modelRef = this.modalService.open(EditRaceResultComponent);
    modelRef.componentInstance.raceResult = result;
    modelRef.componentInstance.race = this.race;
    modelRef.componentInstance.raceSeriesId = this.raceSeriesId;
    modelRef.componentInstance.handicaps = this.handicaps;

    modelRef.result.then(
      result =>
      {
        this.raceResultService.addResult$(this.raceSeriesId,this.race,result).subscribe(
          result => { this.loadPage(this.pageNumber) },
          error => { console.log('Edit result failed' + error.toString()) }
        )
      });
  }

  deleteResult(raceResult: RaceResultModel)
  {
    this.confirmDeleteService.confirmDelete("Are you sure you want to delete this result?").then(
      result =>
      {
        if (result)
        {
          this.raceResultService.deleteResult$(this.raceSeriesId, this.race, raceResult).subscribe(
            response => this.loadPage(this.pageNumber),
            error => console.error("Failed to delete boat ", error) );
        }
      }
    );
  }

}
