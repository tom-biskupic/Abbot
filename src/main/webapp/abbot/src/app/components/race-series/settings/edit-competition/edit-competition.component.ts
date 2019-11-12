import { Component, OnInit, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { CompetitionModel, PointsSystem, ResultType } from 'src/app/models/CompetitionModel';
import { FleetService } from 'src/app/services/fleet.service';
import { FleetModel } from 'src/app/models/FleetModel';

@Component({
  selector: 'app-edit-competition',
  templateUrl: './edit-competition.component.html',
  styleUrls: ['./edit-competition.component.css']
})
export class EditCompetitionComponent implements OnInit {

  @Input() competition: CompetitionModel;
  @Input() raceSeriesId: number;
  @Input() fleets: FleetModel[];

  competitionForm: FormGroup;
  submitted = false;
  pointsSystemMap = new Map<PointsSystem, string>();
  resultTypeMap = new Map<ResultType, string>();

  constructor(
      private activeModal: NgbActiveModal,
      private formBuilder: FormBuilder )
  {
  }

  ngOnInit()
  {
    this.pointsSystemMap.set(PointsSystem.BONUS_POINTS, 'Bonus Points System');
    this.pointsSystemMap.set(PointsSystem.LOW_POINTS, 'Low Points System');

    this.resultTypeMap.set(ResultType.HANDICAP_RESULT, 'Use Handicap placings');
    this.resultTypeMap.set(ResultType.SCRATCH_RESULT, 'Use Scratch placings');

    this.competitionForm = this.formBuilder.group(
      {
        name : new FormControl(this.competition.name, [ Validators.required ]),
        fleet: new FormControl(this.competition.fleet ),
        pointsSystem: new FormControl(this.competition.pointsSystem),
        resultType: new FormControl(this.competition.resultType),
        fleetSize: new FormControl(this.competition.fleetSize, [Validators.min(1)]),
        drops: new FormControl(this.competition.drops)
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

  compareCaseIgnore(val1: string, val2: string): boolean
  {
    if ( val1 === val2 )
    {
      return true;
    }

    if ( val1 === null || val2 === null )
    {
      return false;
    }

    return val1.toLowerCase() === val2.toLowerCase();
  }

  getFleets()
  {
    return this.fleets;
  }

  ok()
  {
    this.submitted = true;

    if (this.competitionForm.invalid)
    {
        return;
    }

    const result = this.competitionForm.value;
    result.id = this.competition.id;
    result.raceSeriesId = this.raceSeriesId;
    this.activeModal.close(result);
  }

  get fields()
  {
    return this.competitionForm.controls;
  }

  cancel()
  {
    this.submitted = false;
    this.competitionForm.reset();
    this.activeModal.dismiss('Cancel click');
  }

}
