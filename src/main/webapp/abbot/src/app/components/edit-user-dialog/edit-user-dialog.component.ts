import { UserModel } from '../../models/UserModel';
import { Component, OnInit, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormControl, Validators, FormGroup, FormBuilder } from '@angular/forms';
import { PasswordMatch } from 'src/app/validators/PasswordMatch';

@Component({
  selector: 'app-edit-user-dialog',
  templateUrl: './edit-user-dialog.component.html',
  styleUrls: ['./edit-user-dialog.component.css']
})
export class EditUserDialogComponent implements OnInit {

  @Input() user: UserModel;
  userForm: FormGroup;
  submitted = false;

  constructor(  private activeModal: NgbActiveModal,
                private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.userForm = this.formBuilder.group(
      {
        email: new FormControl(this.user.email, [ Validators.required, Validators.email]),
        firstName : new FormControl(this.user.firstName, [ Validators.required ]),
        lastName : new FormControl(this.user.lastName, [ Validators.required ]),
        organisation : new FormControl(this.user.organisation, [ ]),
        administrator : new FormControl(this.user.administrator, [ ]),
        password : new FormControl(this.user.password, [ Validators.required ]),
        passwordConfirm : new FormControl(this.user.password, [ Validators.required ])
      },
      {
        validator: PasswordMatch('password', 'passwordConfirm')
      }
    );
  }

  ok()
  {
    this.submitted = true;

    // stop here if form is invalid
    if (this.userForm.invalid)
    {
        return;
    }

    const result = this.userForm.value;
    result.id = this.user.id;

    this.activeModal.close(result);
  }

  get fields()
  {
    return this.userForm.controls;
  }

  cancel()
  {
    this.submitted = false;
    this.userForm.reset();
    this.activeModal.dismiss('Cancel click');
  }
}
