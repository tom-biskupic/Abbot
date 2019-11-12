import { RaceResultModel, ResultStatus } from './../../../models/RaceResultModel';
import { Component, OnInit, Input } from '@angular/core';
import { RaceModel } from 'src/app/models/RaceModel';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { BoatModel } from 'src/app/models/BoatModel';
import { RaceResultService } from 'src/app/services/race-result.service';
import { HandicapModel } from '../../../models/Handicap';

@Component({
  selector: 'app-edit-race-result',
  templateUrl: './edit-race-result.component.html',
  styleUrls: ['./edit-race-result.component.css']
})
export class EditRaceResultComponent implements OnInit
{
  @Input() raceResult: RaceResultModel;
  @Input() raceSeriesId: number;
  @Input() race: RaceModel;
  @Input() handicaps: HandicapModel[];
  
  raceResultForm: FormGroup;
  submitted = false;
  boatsInRace: BoatModel[];

  statusMap = new Map<ResultStatus, string>();

  constructor(
    private activeModal: NgbActiveModal,
    private formBuilder: FormBuilder,
    private raceResultService: RaceResultService )
    { }

  ngOnInit()
  {
    this.statusMap.set(ResultStatus.FINISHED, 'Finished');
    this.statusMap.set(ResultStatus.DNF, 'DNF - Did not finish');
    this.statusMap.set(ResultStatus.OCS, 'OCS - over on start');
    this.statusMap.set(ResultStatus.ZFP, 'ZPF - 20% penalty under rule 30.2');
    this.statusMap.set(ResultStatus.UFD, 'UFD - Disqualification under rule 30.3');
    this.statusMap.set(ResultStatus.BFD, 'BFD - Disqualification under rule 30.4');
    this.statusMap.set(ResultStatus.SCP, 'SCP - Scoring Penalty applied');
    this.statusMap.set(ResultStatus.RET, 'RET - Retired');
    this.statusMap.set(ResultStatus.DSQ, 'DSQ - Disqualification');
    this.statusMap.set(ResultStatus.DNE, 'DNE - Disqualification that is not excludable');
    this.statusMap.set(ResultStatus.RDG, 'RDG - Redress given');
    this.statusMap.set(ResultStatus.DPI, 'DPI - Discretionary penalty imposed');
    this.statusMap.set(ResultStatus.DNC, 'DNC -	Did not start');
    this.statusMap.set(ResultStatus.DNS, 'DNS - Did not start (other than DNC and OCS)');

    this.raceResultService.getUnAddedBoats$(
      this.raceSeriesId, this.race).subscribe(
        boats =>
        {
          this.boatsInRace = boats;
          if ( this.raceResult.boat !== null && boats.length === 0 )
          {
            boats.push(this.raceResult.boat);
          }
        } );

    this.raceResultForm = this.formBuilder.group(
      {
        boat: new FormControl(this.raceResult.boat, [ Validators.required ]),
        overrideHandicap: new FormControl(this.raceResult.overrideHandicap, [ ]),
        handicap: new FormControl(this.raceResult.handicap, [ Validators.required ]),
        startTime: new FormControl(this.timeToHHMMSS(this.raceResult.startTime), [ ]),
        finishTime: new FormControl(this.timeToHHMMSS(this.raceResult.finishTime), [ ]),
        status: new FormControl(this.raceResult.status, [ ]),
      });

      this.enableField('handicap',this.raceResult.overrideHandicap);
  }

  timeToHHMMSS(time: Date): string
  {
    if ( time === null )
    {
      return "";
    }

    return this.getPadded(time.getHours()) + ':' + this.getPadded(time.getMinutes()) + ':' + this.getPadded(time.getSeconds());
  }

  getPadded(value: number)
  {
    let asString = value.toString();
    if ( asString.length === 1 )
    {
      asString = '0' + asString;
    }
    return asString;
  }

  hhmmssToDate(value: string) : Date
  {
    let split : string[] = value.split(':');
    return new Date(
      1970,1,1,
      parseInt(split[0]),
      parseInt(split[1]), 
      split.length >2 ? parseInt(split[2]) : 0 );
  }

  compareBoatFunction(optionOne: BoatModel, optionTwo: BoatModel): boolean
  {
    const oneDefined = optionOne === null || optionOne === undefined;
    const twoDefined = optionTwo === null || optionTwo === undefined;

    if ( ! oneDefined && !twoDefined )
    {
      return true;
    }

    if ( ! oneDefined || !twoDefined )
    {
      return false;
    }

    return optionOne.id === optionTwo.id;
  }

  onChangeBoat(boat: BoatModel)
  {
    let match = this.findHandicapForBoat(boat);

    if ( match !== undefined )
    {
      this.setHandicap(match.value);
    }
    this.raceResultForm.get('overrideHandicap').setValue(false);
  }

  setHandicap(handicap: number)
  {
    this.raceResultForm.get('handicap').setValue(handicap);
  }
  
  findHandicapForBoat(boat: BoatModel)
  {
    let match = this.handicaps.filter( 
      handicap => handicap.boatID == this.raceResultForm.get('boat').value.id);
    return match[0];
  }

  onChangeStatus(status: ResultStatus)
  {
    this.enableField("startTime",this.raceResultService.isStarted(status));
    this.enableField("finishTime",this.raceResultService.isFinished(status));
  }

  revertHandicap()
  {
    let boat = this.raceResultForm.get('boat').value;
    let overrideField = this.raceResultForm.get('overrideHandicap')
    this.enableField('handicap',! overrideField.value);

    if ( ! overrideField.value )
    {
      let match = this.findHandicapForBoat(boat);

      if ( match !== undefined )
      {
        this.setHandicap(match.value);
      }
    }
  } 

  enableField(fieldName: string, enabled: boolean)
  {
    const field = this.raceResultForm.get(fieldName);
    if (!enabled )
    {
      field.disable();
    }
    else
    {
      field.enable();
    }
  }

  ok()
  {
    this.submitted = true;

    if (this.raceResultForm.invalid)
    {
        return;
    }

    const result = this.raceResultForm.value;
    result.id = this.raceResult.id;
    result.raceSeriesId = this.race.raceSeriesId;
    result.startTime = result.startTime !== undefined ? this.hhmmssToDate(result.startTime) : null;
    result.raceId = this.race.id;
    result.finishTime = result.finishTime !== undefined ? this.hhmmssToDate(result.finishTime) : null;

    this.activeModal.close(result);
  }

  get fields()
  {
    return this.raceResultForm.controls;
  }

  cancel()
  {
    this.submitted = false;
    this.raceResultForm.reset();
    this.activeModal.dismiss('Cancel click');
  }
}
