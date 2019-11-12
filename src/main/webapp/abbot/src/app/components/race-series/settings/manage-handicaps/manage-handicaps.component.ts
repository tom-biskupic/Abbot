import { EditHandicalLimitComponent } from './../edit-handical-limit/edit-handical-limit.component';
import { Component, OnInit, Input } from '@angular/core';
import { FleetModel } from 'src/app/models/FleetModel';
import { faPlusSquare, faSync, faTrashAlt } from '@fortawesome/free-solid-svg-icons';
import { HandicapLimit } from 'src/app/models/HandicapLimit';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FleetService } from 'src/app/services/fleet.service';
import { ConfirmDeleteService } from 'src/app/services/confirm-delete.service';
import { HandicapLimitService } from 'src/app/services/handicap-limit.service';
import { race } from 'rxjs/operators';

@Component({
  selector: 'app-manage-handicaps',
  templateUrl: './manage-handicaps.component.html',
  styleUrls: ['./manage-handicaps.component.css']
})
export class ManageHandicapsComponent implements OnInit {

  @Input() raceSeriesId: number;

  handicapLimits: HandicapLimit[];
  numPages = 0;
  pageNumber = 0;
  pageSize = 5;
  collectionSize = 0;
  fleets: FleetModel[];

  faPlusSquare = faPlusSquare;
  faSync = faSync;
  faTrashAlt = faTrashAlt;

  constructor(private handicapService: HandicapLimitService,
              private modalService: NgbModal,
              private fleetService: FleetService,
              private confirmService: ConfirmDeleteService) { }

  ngOnInit()
  {
    this.fleetService.getAllFleets$(this.raceSeriesId).subscribe(
      result =>
      {
        this.fleets = result;
      });

    this.fetchPage(1);
  }

  private fetchPage(pageNumber: number)
  {
    this.handicapService.getHandicapLimits$(this.raceSeriesId, pageNumber - 1, this.pageSize).subscribe(
      res =>
      {
        this.handicapLimits = res.content;
        this.numPages = res.totalPages;
        this.pageNumber = res.number + 1;
        this.collectionSize = res.totalPages * this.pageSize;
      },
      err =>
      {
        console.error(err);
      });
  }

  getPageNumber(): number
  {
    return this.pageNumber;
  }

  loadPage(pageNumber: number)
  {
    this.fetchPage(pageNumber);
  }

  addHandicapLimit()
  {
    const newHandicapLimit: HandicapLimit =
    {
      id: null,
      fleet: this.fleets[0],
      limit: 0.0,
      raceSeriesID: this.raceSeriesId
    };

    const modelRef = this.modalService.open(EditHandicalLimitComponent);
    modelRef.componentInstance.handicap = newHandicapLimit;
    modelRef.componentInstance.raceSeriesId = this.raceSeriesId;
    modelRef.componentInstance.fleets = this.fleets;
    modelRef.result.then(
      result =>
      {
        this.handicapService.addHandicapLimit$(this.raceSeriesId, result).subscribe(
          result =>
          {
            this.loadPage(this.pageNumber);
          },
          error =>
          {
            console.log('Update handicap limit failed ' + error.toString());
          }
        );
      }
    );

  }

  editHandicapLimit(handicapLimit: HandicapLimit)
  {
    const modelRef = this.modalService.open(EditHandicalLimitComponent);
    modelRef.componentInstance.handicap = handicapLimit;
    modelRef.componentInstance.raceSeriesId = this.raceSeriesId;
    modelRef.componentInstance.fleets = this.fleets;
    modelRef.result.then(
      result =>
      {
        this.handicapService.addHandicapLimit$(this.raceSeriesId, result).subscribe(
          result =>
          {
            this.loadPage(this.pageNumber);
          },
          error =>
          {
            console.log('Update handicap limit failed ' + error.toString());
          }
        );
      }
    );
  }

  deleteHandicapLimit(handicapLimit: HandicapLimit)
  {
    this.confirmService.confirmDelete('Are you sure you want to delete this handicap limit?').then(
      result =>
      {
        if ( result )
        {
          this.handicapService.deleteHandicapLimit$(this.raceSeriesId, handicapLimit).subscribe(
            result =>
            {
              this.loadPage(this.pageNumber);
            },
            error =>
            {
              console.log('Delete handicap limit failed ' + error.toString());
            }
          );
        }
      }
    );
  }
}
