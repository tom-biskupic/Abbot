import { Component, OnInit, Input } from '@angular/core';
import { BoatDivisionModel } from 'src/app/models/BoatClassModel';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-edit-boat-division',
  templateUrl: './edit-boat-division.component.html',
  styleUrls: ['./edit-boat-division.component.css']
})
export class EditBoatDivisionComponent implements OnInit
{
  @Input() boatDivision: BoatDivisionModel;
  divisionForm: FormGroup;
  submitted = false;

  constructor(private activeModal: NgbActiveModal,
              private formBuilder: FormBuilder) { }


  ngOnInit()
  {
    this.divisionForm = this.formBuilder.group(
      {
        name : new FormControl(this.boatDivision.name, [ Validators.required ]),
        yardStick : new FormControl(this.boatDivision.yardStick, [ Validators.pattern('^[0-9.]*$') ])
      });
  }

  ok()
  {
    this.submitted = true;

    if (this.divisionForm.invalid)
    {
        return;
    }

    const result = this.divisionForm.value;
    result.id = this.boatDivision.id;

    this.activeModal.close(result);
  }

  get fields()
  {
    return this.divisionForm.controls;
  }

  cancel()
  {
    this.submitted = false;
    this.divisionForm.reset();
    this.activeModal.dismiss('Cancel click');
  }

}
