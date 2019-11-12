import { Component, OnInit, Input } from '@angular/core';
import { BoatService } from 'src/app/services/boat.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ConfirmDeleteService } from 'src/app/services/confirm-delete.service';
import { faPlusSquare, faSync, faTrashAlt } from '@fortawesome/free-solid-svg-icons';
import { BoatModel } from 'src/app/models/BoatModel';
import { EditBoatComponent } from '../edit-boat/edit-boat.component';
import { BoatClassService } from 'src/app/services/boat-class-service';
import { BoatClassModel } from 'src/app/models/BoatClassModel';

@Component({
  selector: 'app-race-series-registered-boats',
  templateUrl: './race-series-registered-boats.component.html',
  styleUrls: ['./race-series-registered-boats.component.css']
})
export class RaceSeriesRegisteredBoatsComponent implements OnInit {

  @Input()
  raceSeriesId: number;

  boats: BoatModel[];
  numPages = 0;
  pageNumber = 0;
  pageSize = 5;
  collectionSize = 0;

  faPlusSquare = faPlusSquare;
  faSync = faSync;
  faTrashAlt = faTrashAlt;

  boatClasses: BoatClassModel[] = [];

  constructor(
    private boatService: BoatService,
    private boatClassService: BoatClassService,
    private modalService: NgbModal,
    private confirmService: ConfirmDeleteService ) { }

  ngOnInit()
  {
    this.loadPage(1);
  }

  private fetchPage(pageNumber: number)
  {
    this.boatService.getBoats$(this.raceSeriesId, pageNumber - 1, this.pageSize).subscribe(
      res =>
      {
        this.boats = res.content;
        this.numPages = res.totalPages;
        this.pageNumber = res.number + 1;
        this.collectionSize = res.totalPages * this.pageSize;
      },
      err => console.error(err)
    );

    this.boatClassService.getAllBoatClasses$(this.raceSeriesId).subscribe(
      res => this.boatClasses = res
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

  addBoat()
  {
    const newBoat: BoatModel =
    {
      id: null,
      raceSeriesID: this.raceSeriesId,
      name: '',
      sailNumber: '',
      boatClass: null,
      division: null,
      visitor: false,
      skipper: '',
      crew: ''
    };

    const modelRef = this.modalService.open(EditBoatComponent);
    modelRef.componentInstance.boat = newBoat;
    modelRef.componentInstance.boatClasses = this.boatClasses;
    modelRef.componentInstance.raceSeriesId = this.raceSeriesId;

    modelRef.result.then(
      result =>
      {
        this.boatService.addBoat$(this.raceSeriesId, result).subscribe(
          result =>
          {
            this.loadPage(this.pageNumber);
          },
          error =>
          {
            console.log('Add boat failed ' + error.toString());
          }
        );
      }
    );
  }

  editBoat(boat: BoatModel)
  {

    const modelRef = this.modalService.open(EditBoatComponent);
    modelRef.componentInstance.boat = boat;
    modelRef.componentInstance.boatClasses = this.boatClasses;
    modelRef.componentInstance.raceSeriesId = this.raceSeriesId;
    
    modelRef.result.then(
      result =>
      {
        this.boatService.addBoat$(this.raceSeriesId, result).subscribe(
          result =>
          {
            this.loadPage(this.pageNumber);
          },
          error =>
          {
            console.log('Update boat failed ' + error.toString());
          }
        );
      }
    );
  }

  deleteBoat(boat: BoatModel)
  {
    this.confirmService.confirmDelete("Are you sure you want to delete this boat?").then(
      result =>
      {
        if (result)
        {
          this.boatService.deleteBoat$(this.raceSeriesId, boat).subscribe(
            response => this.loadPage(this.pageNumber),
            error => console.error("Failed to delete boat ", error) );
        }
      }
    );
  }
}
