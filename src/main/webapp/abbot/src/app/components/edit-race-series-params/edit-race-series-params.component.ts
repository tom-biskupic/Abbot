import { RaceSeriesType } from './../../models/RaceSeriesModel';
import { Component, OnInit, Input } from '@angular/core';
import { RaceSeriesModel } from 'src/app/models/RaceSeriesModel';
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-edit-race-series-params',
  templateUrl: './edit-race-series-params.component.html',
  styleUrls: ['./edit-race-series-params.component.css']
})
export class EditRaceSeriesParamsComponent implements OnInit {

  @Input() raceSeries: RaceSeriesModel;
  raceSeriesForm: FormGroup;
  submitted = false;

  constructor(  private activeModal: NgbActiveModal,
                private formBuilder: FormBuilder) { }

  ngOnInit()
  {
    this.raceSeriesForm = this.formBuilder.group(
      {
        name : new FormControl(this.raceSeries.name, [ Validators.required ]),
        seriesType : new FormControl(this.raceSeries.seriesType, [] ),
        comment : new FormControl(this.raceSeries.comment, [  ])
      }
    );
  }

  raceSeriesTypes()
  {
    return Object.values(RaceSeriesType);
  }

  ok()
  {
    this.submitted = true;

    if (this.raceSeriesForm.invalid)
    {
        return;
    }

    const result = this.raceSeriesForm.value;
    result.id = this.raceSeries.id;
    result.dateCreated = this.raceSeries.dateCreated;
    result.lastUpdated = this.raceSeries.lastUpdated;

    this.activeModal.close(result);
  }

  get fields()
  {
    return this.raceSeriesForm.controls;
  }

  cancel()
  {
    this.submitted = false;
    this.raceSeriesForm.reset();
    this.activeModal.dismiss('Cancel click');
  }
}
