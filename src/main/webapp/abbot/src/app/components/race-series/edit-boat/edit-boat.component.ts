import { Component, OnInit, Input } from '@angular/core';
import { BoatModel } from 'src/app/models/BoatModel';
import { BoatClassModel, BoatDivisionModel } from 'src/app/models/BoatClassModel';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder, FormGroup, FormControl, Validators, NumberValueAccessor } from '@angular/forms';

@Component({
  selector: 'app-edit-boat',
  templateUrl: './edit-boat.component.html',
  styleUrls: ['./edit-boat.component.css']
})
export class EditBoatComponent implements OnInit {

  @Input() boat: BoatModel;
  @Input() boatClasses: BoatClassModel[];
  @Input() raceSeriesId: number;

  boatForm: FormGroup;
  submitted = false;

  constructor(
    private activeModal: NgbActiveModal,
    private formBuilder: FormBuilder )
    { }

  ngOnInit()
  {
    this.boatForm = this.formBuilder.group(
      {
        name : new FormControl(this.boat.name, [ Validators.required ]),
        sailNumber: new FormControl(this.boat.sailNumber, [Validators.required]),
        boatClass: new FormControl(this.boat.boatClass === null ? this.boatClasses[0] : this.boat.boatClass),
        division: new FormControl(this.selectInitialDivision()),
        skipper: new FormControl(this.boat.skipper),
        crew: new FormControl(this.boat.crew)
      });
  }

  selectInitialDivision()
  {
    if ( this.boat.division !== null )
    {
      return this.boat.division;
    }
    else
    {
      if ( this.boat.boatClass !== null )
      {
        return this.boat.boatClass.divisions[0];
      }
      else
      {
        return this.boatClasses[0].divisions[0];
      }
    }
  }

  compareBoatCLassFunction(optionOne: BoatModel, optionTwo: BoatModel): boolean
  {
    if ( optionOne === null && optionTwo === null )
    {
      return true;
    }
    if ( optionOne === null || optionTwo === null )
    {
      return false;
    }

    return optionOne.id === optionTwo.id;
  }

  compareDivisionFunction(optionOne: BoatDivisionModel, optionTwo: BoatDivisionModel): boolean
  {
    if ( optionOne === null && optionTwo === null )
    {
      return true;
    }
    if ( optionOne === null || optionTwo === null )
    {
      return false;
    }

    return optionOne.id === optionTwo.id;
  }

  onChangeBoatClass(boat: BoatModel)
  {
  }

  ok()
  {
    this.submitted = true;

    if (this.boatForm.invalid)
    {
        return;
    }

    const result = this.boatForm.value;
    result.id = this.boat.id;
    result.raceSeriesID = this.raceSeriesId;
    this.activeModal.close(result);
  }

  get fields()
  {
    return this.boatForm.controls;
  }

  cancel()
  {
    this.submitted = false;
    this.boatForm.reset();
    this.activeModal.dismiss('Cancel click');
  }
}
