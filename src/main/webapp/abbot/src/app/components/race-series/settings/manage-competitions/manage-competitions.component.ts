import { FleetService } from 'src/app/services/fleet.service';
import { EditCompetitionComponent } from './../edit-competition/edit-competition.component';
import { CompetitionService } from './../../../../services/competition.service';
import { Component, OnInit, Input } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CompetitionModel, PointsSystem, ResultType } from 'src/app/models/CompetitionModel';
import { faPlusSquare, faSync, faTrashAlt } from '@fortawesome/free-solid-svg-icons';
import { FleetModel } from 'src/app/models/FleetModel';
import { ConfirmDeleteService } from 'src/app/services/confirm-delete.service';

@Component({
  selector: 'app-manage-competitions',
  templateUrl: './manage-competitions.component.html',
  styleUrls: ['./manage-competitions.component.css']
})
export class ManageCompetitionsComponent implements OnInit {

  @Input() raceSeriesId: number;

  competitions: CompetitionModel[];
  numPages = 0;
  pageNumber = 0;
  pageSize = 5;
  collectionSize = 0;
  fleets: FleetModel[];

  faPlusSquare = faPlusSquare;
  faSync = faSync;
  faTrashAlt = faTrashAlt;

  constructor(private competitionService: CompetitionService,
              private modalService: NgbModal,
              private fleetService: FleetService,
              private confirmService: ConfirmDeleteService) { }

  ngOnInit()
  {
    this.fetchPage(1);

    this.fleetService.getAllFleets$(this.raceSeriesId).subscribe(
      result =>
      {
        this.fleets = result;
      });
  }

  private fetchPage(pageNumber: number)
  {
    this.competitionService.getCompetitions$(this.raceSeriesId, pageNumber - 1, this.pageSize).subscribe(
      res =>
      {
        this.competitions = res.content;
        this.numPages = res.totalPages;
        this.pageNumber = res.number + 1;
        this.collectionSize = res.totalPages * this.pageSize;
      },
      err =>
      {
        console.error(err);
      });
  }

  getCompetitions(): CompetitionModel[]
  {
    return this.competitions;
  }

  getPageNumber(): number
  {
    return this.pageNumber;
  }

  loadPage(pageNumber: number)
  {
    this.fetchPage(pageNumber);
  }

  editCompetition(competition: CompetitionModel)
  {
    const modelRef = this.modalService.open(EditCompetitionComponent);
    modelRef.componentInstance.competition = competition;
    modelRef.componentInstance.raceSeriesId = this.raceSeriesId;
    modelRef.componentInstance.fleets = this.fleets;

    modelRef.result.then(
      result =>
      {
        this.competitionService.addCompetition$(this.raceSeriesId, result).subscribe(
          result =>
          {
            this.loadPage(this.pageNumber);
          },
          error =>
          {
            console.log('Edit competition failed ' + error.toString());
          }
        );
      }
    );
  }

  addCompetition()
  {
    const newCompetition: CompetitionModel =
    {
      id: null,
      drops: 0,
      fleetSize: 0,
      fleet: this.fleets[0],
      name: '',
      pointsSystem: PointsSystem.LOW_POINTS,
      resultType: ResultType.HANDICAP_RESULT
    };

    const modelRef = this.modalService.open(EditCompetitionComponent);
    modelRef.componentInstance.competition = newCompetition;
    modelRef.componentInstance.fleets = this.fleets;
    modelRef.componentInstance.raceSeriesId = this.raceSeriesId;
    modelRef.result.then(
      result =>
      {
        this.competitionService.addCompetition$(this.raceSeriesId, result).subscribe(
          result =>
          {
            this.loadPage(this.pageNumber);
          },
          error =>
          {
            console.log('Add competition failed ' + error.toString());
          }
        )
      }
    );
  }

  deleteCompetition(competition: CompetitionModel)
  {
    this.confirmService.confirmDelete('Are you sure you want to delete this competition?').then(
      response =>
      {
        if ( response )
        {
          this.competitionService.deleteCompetition$(this.raceSeriesId, competition).subscribe(
            result =>
            {
              this.loadPage(this.pageNumber);
            },
            error =>
            {
              console.log('Delete competition failed ' + error.toString());
            }
          );
        }
      }
    );
  }
}
