import { Component, OnInit, Input } from '@angular/core';
import { BoatClassModel } from 'src/app/models/BoatClassModel';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-edit-boat-class',
  templateUrl: './edit-boat-class.component.html',
  styleUrls: ['./edit-boat-class.component.css']
})
export class EditBoatClassComponent implements OnInit
{
  @Input() boatClass: BoatClassModel;
  boatClassForm: FormGroup;
  submitted = false;

  constructor(private activeModal: NgbActiveModal,
              private formBuilder: FormBuilder) { }

  ngOnInit()
  {
    this.boatClassForm = this.formBuilder.group(
    {
      name : new FormControl(this.boatClass.name, [ Validators.required ]),
      yardStick : new FormControl(this.boatClass.yardStick, [ Validators.pattern('^[0-9.]*$') ])
    });
  }

  ok()
  {
    this.submitted = true;

    if (this.boatClassForm.invalid)
    {
        return;
    }

    const result = this.boatClassForm.value;
    result.id = this.boatClass.id;
    result.divisions = this.boatClass.divisions;
    result.raceSeriesId = this.boatClass.raceSeriesId;

    this.activeModal.close(result);
  }

  get fields()
  {
    return this.boatClassForm.controls;
  }

  cancel()
  {
    this.submitted = false;
    this.boatClassForm.reset();
    this.activeModal.dismiss('Cancel click');
  }
}
