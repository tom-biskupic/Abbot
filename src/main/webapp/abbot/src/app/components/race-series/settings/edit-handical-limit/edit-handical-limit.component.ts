import { HandicapLimit } from 'src/app/models/HandicapLimit';
import { Component, OnInit, Input } from '@angular/core';
import { FleetModel } from 'src/app/models/FleetModel';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder, FormGroup, Validators, FormControl } from '@angular/forms';

@Component({
  selector: 'app-edit-handical-limit',
  templateUrl: './edit-handical-limit.component.html',
  styleUrls: ['./edit-handical-limit.component.css']
})
export class EditHandicalLimitComponent implements OnInit {
  @Input() handicap: HandicapLimit;
  @Input() raceSeriesId: number;
  @Input() fleets: FleetModel[];

  handicapLimitForm: FormGroup;
  submitted = false;

  constructor(
      private activeModal: NgbActiveModal,
      private formBuilder: FormBuilder )
  {
  }

  ngOnInit()
  {
    this.handicapLimitForm = this.formBuilder.group(
      {
        fleet : new FormControl(this.handicap.fleet ),
        limit : new FormControl(this.handicap.limit, [ Validators.min(1.0) ]),
      });
  }

  compareFleetFunction(optionOne: FleetModel, optionTwo: FleetModel): boolean
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

  ok()
  {
    this.submitted = true;

    if (this.handicapLimitForm.invalid)
    {
        return;
    }

    const result = this.handicapLimitForm.value;
    result.id = this.handicap.id;
    result.raceSeriesID = this.raceSeriesId;
    this.activeModal.close(result);
  }

  get fields()
  {
    return this.handicapLimitForm.controls;
  }

  cancel()
  {
    this.submitted = false;
    this.handicapLimitForm.reset();
    this.activeModal.dismiss('Cancel click');
  }

}
