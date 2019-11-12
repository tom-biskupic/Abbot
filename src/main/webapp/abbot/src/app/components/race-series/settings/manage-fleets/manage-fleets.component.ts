import { EditFleetComponent } from './../edit-fleet/edit-fleet.component';
import { Component, OnInit, Input } from '@angular/core';
import { faPlusSquare, faSync, faTrashAlt } from '@fortawesome/free-solid-svg-icons';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ConfirmDeleteService } from 'src/app/services/confirm-delete.service';
import { FleetModel } from 'src/app/models/FleetModel';
import { FleetService } from 'src/app/services/fleet.service';

@Component({
  selector: 'app-manage-fleets',
  templateUrl: './manage-fleets.component.html',
  styleUrls: ['./manage-fleets.component.css']
})
export class ManageFleetsComponent implements OnInit {

  @Input()
  raceSeriesId: number;

  fleets: FleetModel[];
  numPages = 0;
  pageNumber = 0;
  pageSize = 5;
  collectionSize = 0;

  faPlusSquare = faPlusSquare;
  faSync = faSync;
  faTrashAlt = faTrashAlt;

  constructor(private fleetService: FleetService,
              private modalService: NgbModal,
              private confirmDeleteService: ConfirmDeleteService) { }

  ngOnInit()
  {
    this.fetchPage(1);
  }

  private fetchPage(pageNumber: number)
  {
    this.fleetService.getFleets$(this.raceSeriesId, pageNumber - 1, this.pageSize).subscribe(
      res =>
      {
        this.fleets = res.content;
        this.numPages = res.totalPages;
        this.pageNumber = res.number + 1;
        this.collectionSize = res.totalPages * this.pageSize;
      },
      err =>
      {
        console.error(err);
      });
  }

  getFleets(): FleetModel[]
  {
    return this.fleets;
  }

  getPageNumber(): number
  {
    return this.pageNumber;
  }

  loadPage(pageNumber: number)
  {
    this.fetchPage(pageNumber);
  }

  editFleet(fleet: FleetModel)
  {
    const modelRef = this.modalService.open(EditFleetComponent);
    modelRef.componentInstance.fleet = fleet;
    modelRef.componentInstance.raceSeriesId = this.raceSeriesId;
    modelRef.result.then(
      result =>
      {
        this.fleetService.addFleet$(this.raceSeriesId, result).subscribe(
          result =>
          {
            this.loadPage(this.pageNumber);
          },
          error =>
          {
            console.log('Update fleet failed ' + error.toString());
          }
        )
      }, reason => { console.log('Edit fleet cancelled ' + reason )}
    );

  }

  addFleet()
  {
    const newFleet: FleetModel =
    {
      id: null,
      fleetName: null,
      raceSeriesId: this.raceSeriesId,
      competeOnYardstick: false,
      fleetClasses: []
    };

    const modelRef = this.modalService.open(EditFleetComponent);
    modelRef.componentInstance.fleet = newFleet;
    modelRef.componentInstance.raceSeriesId = this.raceSeriesId;
    modelRef.result.then(
      result =>
      {
        this.fleetService.addFleet$(this.raceSeriesId, result).subscribe(
          result =>
          {
            this.loadPage(this.pageNumber);
          },
          error =>
          {
            console.log('Add fleet failed ' + error.toString());
          }
        )
      }, reason => { console.log('Add fleet cancelled ' + reason )}
    );
  }

  deleteFleet(fleet: FleetModel)
  {
    this.fleetService.deleteFleet$(this.raceSeriesId,fleet).subscribe(
      result =>
      {
        this.loadPage(this.pageNumber);
      },
      error =>
      {
        console.log('Delete fleet failed ' + error.toString());
      }
    )
  }
}
