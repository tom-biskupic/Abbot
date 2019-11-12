import { CompetitionModel } from 'src/app/models/CompetitionModel';
import { FleetService } from './../../../services/fleet.service';
import { FleetModel } from './../../../models/FleetModel';
import { RaceService } from './../../../services/race.service';
import { Component, OnInit, Input } from '@angular/core';
import { RaceModel } from 'src/app/models/RaceModel';
import { faPlusSquare, faSync, faTrashAlt } from '@fortawesome/free-solid-svg-icons';
import { EditRaceComponent } from '../edit-race/edit-race.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ConfirmDeleteService } from 'src/app/services/confirm-delete.service';
import { CompetitionService } from 'src/app/services/competition.service';

@Component({
  selector: 'app-race-series-races',
  templateUrl: './race-series-races.component.html',
  styleUrls: ['./race-series-races.component.css']
})
export class RaceSeriesRacesComponent implements OnInit {

  @Input()
  raceSeriesId: number;

  races: RaceModel[];
  numPages = 0;
  pageNumber = 0;
  pageSize = 5;
  collectionSize = 0;

  faPlusSquare = faPlusSquare;
  faSync = faSync;
  faTrashAlt = faTrashAlt;

  fleets: FleetModel[];
  competitions: CompetitionModel[];

  constructor(
    private raceService: RaceService,
    private fleetService: FleetService,
    private competitionService: CompetitionService,
    private modalService: NgbModal,
    private confirmService: ConfirmDeleteService ) { }

  ngOnInit()
  {
    this.fleetService.getAllFleets$(this.raceSeriesId).subscribe(
      result => this.fleets = result
    );
    this.competitionService.getAllCompetitions$(this.raceSeriesId).subscribe(
      result => this.competitions = result
    );

    this.loadPage(1);
  }

  private fetchPage(pageNumber: number)
  {
    this.raceService.getRaces$(this.raceSeriesId, pageNumber - 1, this.pageSize).subscribe(
      res =>
      {
        this.races = res.content;
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

  addRace()
  {
    const newRace: RaceModel =
    {
      id: null,
      raceSeriesId: this.raceSeriesId,
      raceDate: null,
      name: '',
      fleet: null,
      shortCourseRace: false,
      competitions: []
    };

    const modelRef = this.modalService.open(EditRaceComponent);
    modelRef.componentInstance.race = newRace;
    modelRef.componentInstance.fleets = this.fleets;
    modelRef.componentInstance.competitions = this.competitions;

    modelRef.result.then(
      result =>
      {
        this.raceService.addRace$(this.raceSeriesId, result).subscribe(
          result =>
          {
            this.loadPage(this.pageNumber);
          },
          error =>
          {
            console.log('Add race failed ' + error.toString());
          }
        );
      }
    );
  }

  editRace(race: RaceModel)
  {
    const modelRef = this.modalService.open(EditRaceComponent);
    modelRef.componentInstance.race = race;
    modelRef.componentInstance.fleets = this.fleets;
    modelRef.componentInstance.competitions = this.competitions;

    modelRef.result.then(
      result =>
      {
        this.raceService.addRace$(this.raceSeriesId, result).subscribe(
          result =>
          {
            this.loadPage(this.pageNumber);
          },
          error =>
          {
            console.log('Update race failed ' + error.toString());
          }
        );
      }
    );
  }

  deleteRace(race: RaceModel)
  {
    this.confirmService.confirmDelete('Are you sure you want to delete this race?').then(
      result =>
      {
        if ( result )
        {
          this.raceService.deleteRace$(this.raceSeriesId, race).subscribe(
            deleteResult =>
            {
              this.loadPage(this.pageNumber);
            },
            err => console.error('Failed to delete race ' + err)
          );
        }
      }
    );
  }
}

