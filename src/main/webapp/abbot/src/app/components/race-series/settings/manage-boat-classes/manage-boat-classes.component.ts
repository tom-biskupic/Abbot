import { ConfirmDeleteService } from './../../../../services/confirm-delete.service';
import { EditBoatDivisionComponent } from './../edit-boat-division/edit-boat-division.component';
import { EditBoatClassComponent } from './../edit-boat-class/edit-boat-class.component';
import { Component, OnInit, Input } from '@angular/core';
import { BoatClassService } from 'src/app/services/boat-class-service';
import { BoatClassModel, BoatDivisionModel } from 'src/app/models/BoatClassModel';
import { faPlusSquare, faSync, faTrashAlt } from '@fortawesome/free-solid-svg-icons';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-manage-boat-classes',
  templateUrl: './manage-boat-classes.component.html',
  styleUrls: ['./manage-boat-classes.component.css']
})
export class ManageBoatClassesComponent implements OnInit
{
  @Input()
  raceSeriesId: number;

  boatClasses: BoatClassModel[];
  numPages = 0;
  pageNumber = 0;
  pageSize = 5;
  collectionSize = 0;

  faPlusSquare = faPlusSquare;
  faSync = faSync;
  faTrashAlt = faTrashAlt;

  constructor(private boatClassService: BoatClassService,
              private modalService: NgbModal,
              private confirmDeleteService: ConfirmDeleteService ) { }

  ngOnInit()
  {
    this.fetchPage(1);
  }

  private fetchPage(pageNumber: number)
  {
    this.boatClassService.getBoatClasses$(this.raceSeriesId, pageNumber - 1, this.pageSize).subscribe(
      res =>
      {
        this.boatClasses = res.content;
        this.numPages = res.totalPages;
        this.pageNumber = res.number + 1;
        this.collectionSize = res.totalPages * this.pageSize;
      },
      err =>
      {
        console.error(err);
      });
  }

  getBoatClasses(): BoatClassModel[]
  {
    return this.boatClasses;
  }

  getPageNumber(): number
  {
    return this.pageNumber;
  }

  loadPage(pageNumber: number)
  {
    this.fetchPage(pageNumber);
  }

  editBoatClass(boatClass: BoatClassModel)
  {
    const modelRef = this.modalService.open(EditBoatClassComponent);
    modelRef.componentInstance.boatClass = boatClass;
    modelRef.result.then(
      result =>
      {
        this.boatClassService.addBoatClass$(this.raceSeriesId, result).subscribe(
          result =>
          {
            this.loadPage(this.pageNumber);
          },
          error =>
          {
            console.log('Update boat class failed ' + error.toString());
          }
        )
      }, reason => { console.log('Edit boat class cancelled ' + reason )}
    );
  }

  addBoatClass()
  {
    const newBoatClass: BoatClassModel =
    {
      id: null,
      raceSeriesId: this.raceSeriesId,
      name: '',
      yardStick: 0.0,
      divisions: []
    };

    const modelRef = this.modalService.open(EditBoatClassComponent);
    modelRef.componentInstance.boatClass = newBoatClass;
    modelRef.result.then(
      result =>
      {
        this.boatClassService.addBoatClass$(this.raceSeriesId, result).subscribe(
          result =>
          {
            this.loadPage(this.pageNumber);
          },
          error =>
          {
            console.log('Update boat class failed ' + error.toString());
          }
        )
      }
    );
  }

  newBoatDivision(boatClass: BoatClassModel)
  {
    const newBoatDivision: BoatDivisionModel =
    {
      id: null,
      name: '',
      yardStick: 0.0
    };

    const modelRef = this.modalService.open(EditBoatDivisionComponent);
    modelRef.componentInstance.boatDivision = newBoatDivision;
    modelRef.result.then(
      result =>
      {
        this.boatClassService.addDivision$(this.raceSeriesId, boatClass.id, result).subscribe(
          result =>
          {
            this.loadPage(this.pageNumber);
          },
          error =>
          {
            console.log('Update boat division failed ' + error.toString());
          }
        )
      }, reason => { console.log('Edit boat class cancelled ' + reason )}
    );

  }

  editBoatDivision(boatClass: BoatClassModel, division: BoatDivisionModel)
  {
    const modelRef = this.modalService.open(EditBoatDivisionComponent);
    modelRef.componentInstance.boatDivision = division;
    modelRef.result.then(
      result =>
      {
        this.boatClassService.addDivision$(this.raceSeriesId, boatClass.id, result).subscribe(
          result =>
          {
            this.loadPage(this.pageNumber);
          },
          error =>
          {
            console.log('Update boat division failed ' + error.toString());
          }
        )
      }, reason => { console.log('Edit boat class cancelled ' + reason )}
    );

  }

  deleteBoatDivision(boatClass: BoatClassModel, division: BoatDivisionModel)
  {
    this.confirmDeleteService.confirmDelete('Are you sure you want to delete division ' + division.name + '?').then(
      result => {
        if ( result )
        {
          this.boatClassService.deleteDivision$(this.raceSeriesId,boatClass.id,division.id).subscribe(
            result =>
            {
              this.loadPage(this.pageNumber);
            },
            error =>
            {
              console.log('Failed to delete division ' + error.toString());
            }
          )
        }
      }
    )
  }
}
