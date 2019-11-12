import { Component, OnInit, Input, Pipe, PipeTransform } from '@angular/core';
import { RaceModel } from 'src/app/models/RaceModel';
import { FleetModel } from 'src/app/models/FleetModel';
import { FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';
import { NgbActiveModal, NgbDateAdapter, NgbDateNativeAdapter } from '@ng-bootstrap/ng-bootstrap';
import { CompetitionModel } from 'src/app/models/CompetitionModel';
import { faTrashAlt } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-edit-race',
  templateUrl: './edit-race.component.html',
  styleUrls: ['./edit-race.component.css'],
  providers: [{provide: NgbDateAdapter, useClass: NgbDateNativeAdapter}]
})
export class EditRaceComponent implements OnInit {

  @Input() race: RaceModel;
  @Input() fleets: FleetModel[];
  @Input() competitions: CompetitionModel[];

  raceForm: FormGroup;
  submitted = false;

  selectedCompetitions: CompetitionModel[] = [];

  faTrashAlt = faTrashAlt;

  constructor(
    private activeModal: NgbActiveModal,
    private formBuilder: FormBuilder ) { }

  ngOnInit()
  {
    this.raceForm = this.formBuilder.group(
      {
        name : new FormControl(this.race.name, [ Validators.required ]),
        raceDate: new FormControl(this.race.raceDate, [Validators.required]),
        shortCourseRace : new FormControl(this.race.shortCourseRace),
        fleet: new FormControl(this.race.fleet === null ? this.fleets[0] : this.race.fleet)
      });

    this.selectedCompetitions = JSON.parse(JSON.stringify(this.race.competitions));
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

  onChangeFleet(fleet: FleetModel)
  {
    //
    //  Remove any competitions that aren't for the newly selected fleet
    //
    this.selectedCompetitions = this.selectedCompetitions.filter( selector => selector.fleet.id === fleet.id );
  }

  addCompetition(competitionToAdd)
  {
    const found = this.selectedCompetitions.find( competition => competition.id === competitionToAdd.id);
    if ( found == null )
    {
      this.selectedCompetitions.push(competitionToAdd);
    }
  }

  removeCompetition(competition: CompetitionModel)
  {
    this.selectedCompetitions = this.selectedCompetitions.filter( selector => selector.id === competition.id );
  }

  ok()
  {
    this.submitted = true;

    if (this.raceForm.invalid)
    {
        return;
    }

    const result = this.raceForm.value;
    result.id = this.race.id;
    result.competitions = this.selectedCompetitions;
    result.raceSeriesId = this.race.raceSeriesId;
    this.activeModal.close(result);
  }

  get fields()
  {
    return this.raceForm.controls;
  }

  cancel()
  {
    this.submitted = false;
    this.raceForm.reset();
    this.activeModal.dismiss('Cancel click');
  }
}
