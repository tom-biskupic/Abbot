import { AuthorizedUserModel } from './../../../../models/AuthorizedUserModel';
import { Component, OnInit, Input } from '@angular/core';
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-edit-authorized-user',
  templateUrl: './edit-authorized-user.component.html',
  styleUrls: ['./edit-authorized-user.component.css']
})
export class EditAuthorizedUserComponent implements OnInit
{
  @Input() authorizedUser: AuthorizedUserModel;
  @Input() raceSeriesId: number;

  authorizedUserForm: FormGroup;
  submitted = false;

  constructor(
    private activeModal: NgbActiveModal,
    private formBuilder: FormBuilder ) { }

  ngOnInit()
  {
    this.authorizedUserForm = this.formBuilder.group(
      {
        emailAddress : new FormControl(this.authorizedUser.emailAddress, [ Validators.required, Validators.email ])
      });

  }

  ok()
  {
    this.submitted = true;

    if (this.authorizedUserForm.invalid)
    {
        return;
    }

    const result = this.authorizedUserForm.value;
    result.id = this.authorizedUser.id;
    result.name = this.authorizedUser.name;
    result.currentUser = this.authorizedUser.currentUser;
    this.activeModal.close(result);
  }

  get fields()
  {
    return this.authorizedUserForm.controls;
  }

  cancel()
  {
    this.submitted = false;
    this.authorizedUserForm.reset();
    this.activeModal.dismiss('Cancel click');
  }

}
