import { FleetSelector } from './../../../../models/FleetModel';
import { BoatDivisionModel } from './../../../../models/BoatClassModel';
import { BoatClassService } from 'src/app/services/boat-class-service';
import { FleetModel } from 'src/app/models/FleetModel';
import { Component, OnInit, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { BoatClassModel } from 'src/app/models/BoatClassModel';
import { faTrashAlt } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-edit-fleet',
  templateUrl: './edit-fleet.component.html',
  styleUrls: ['./edit-fleet.component.css']
})
export class EditFleetComponent implements OnInit
{

  @Input() fleet: FleetModel;
  @Input() raceSeriesId: number;

  fleetForm: FormGroup;
  submitted = false;

  boatClasses: BoatClassModel[];

  boatClassToAdd: BoatClassModel;
  divisionToAdd: BoatDivisionModel;

  fleetClasses: FleetSelector[];

  faTrashAlt = faTrashAlt;

  constructor(
    private activeModal: NgbActiveModal,
    private formBuilder: FormBuilder,
    private boatClassService: BoatClassService) { }

  ngOnInit()
  {
    this.fleetForm = this.formBuilder.group(
      {
        fleetName: new FormControl(this.fleet.fleetName, [ Validators.required ]),
        competeOnYardstick: new FormControl(this.fleet.competeOnYardstick ),
      });

      this.boatClassService.getAllBoatClasses$(this.raceSeriesId).subscribe(
        result =>
        {
          this.boatClasses = result;
          if ( result.length > 0 )
          {
            this.boatClassToAdd = result[0];
          }
        }
      );

      this.fleetClasses = JSON.parse(JSON.stringify(this.fleet.fleetClasses));
  }

  classChanged(value)
  {
  }

  getDivisionsOfSelectedClass()
  {
    if (this.boatClassToAdd != null )
    {
      return this.boatClassToAdd.divisions;
    }
    else
    {
      const  emptyDivsionArray: BoatDivisionModel[] = [];

      return emptyDivsionArray;
    }
  }

  getFleetClasses(): FleetSelector[]
  {
    return this.fleetClasses;
  }

  get fields()
  {
    return this.fleetForm.controls;
  }

  matches(  fleetSelector: FleetSelector,
            boatClass: BoatClassModel,
            division: BoatDivisionModel): boolean
  {
    return ( 	fleetSelector.boatClass.id === boatClass.id
              &&
              ( 	division === undefined && fleetSelector.boatDivision === undefined
                  ||
                  division === null && fleetSelector.boatDivision === null
                  ||
                  division.id === fleetSelector.boatDivision.id ) );
  }

  matchesFleet(  fleetSelector1: FleetSelector, fleetSelector2: FleetSelector): boolean
  {
    return this.matches(fleetSelector1, fleetSelector2.boatClass, fleetSelector2.boatDivision);
  }

  addToFleet(boatClass: BoatClassModel, division: BoatDivisionModel)
  {
    for( const fleetSelector of this.fleetClasses )
    {

      if ( this.matches(fleetSelector, boatClass, division) )
      {
        //
        //  It's already present so just return
        //
        return;
      }
    }

    const newFleetSelector : FleetSelector =
    {
      id: null,
      boatClass: boatClass,
      boatDivision: division
    };

    this.fleetClasses.push(newFleetSelector);
  }

  removeFleetSelector(toDelete: FleetSelector)
  {
    this.fleetClasses = this.fleetClasses.filter( selector => ! this.matchesFleet(selector, toDelete) );
  }

  ok()
  {
    this.submitted = true;

    // stop here if form is invalid
    if (this.fleetForm.invalid || this.fleetClasses.length === 0 )
    {
        return;
    }

    const result = this.fleetForm.value;
    result.id = this.fleet.id;
    result.raceSeriesId = this.fleet.raceSeriesId;
    result.fleetClasses = this.fleetClasses;

    this.activeModal.close(result);
  }

  cancel()
  {
    this.activeModal.dismiss();
  }
}
