import { UserModel } from '../../models/UserModel';
import { EditUserDialogComponent } from './../edit-user-dialog/edit-user-dialog.component';
import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { faPlusSquare } from '@fortawesome/free-solid-svg-icons';
import { faSync } from '@fortawesome/free-solid-svg-icons';
import { faCheck } from '@fortawesome/free-solid-svg-icons';
import { faTimes } from '@fortawesome/free-solid-svg-icons';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-manage-users',
  templateUrl: './manage-users.component.html',
  styleUrls: ['./manage-users.component.css']
})
export class ManageUsersComponent implements OnInit {

  constructor( private userService: UserService, private modalService: NgbModal ) { }

  pageNumber = 0;
  pageSize = 3;
  numPages = 0;
  users: UserModel[];
  collectionSize = 0;

  faPlusSquare = faPlusSquare;
  faSync = faSync;
  faCheck = faCheck;
  faTimes = faTimes;

  ngOnInit()
  {
    this.fetchUserPage(1);
  }

  private fetchUserPage(pageNumber: number)
  {
    this.userService.getUsers$(pageNumber - 1, this.pageSize).subscribe(
      res =>
      {
        this.users = res.content;
        this.numPages = res.totalPages;
        this.pageNumber = res.number + 1;
        this.collectionSize = res.totalPages * this.pageSize;
      },
      err =>
      {
        console.error(err);
      });
  }

  getUsers(): UserModel[]
  {
    return this.users;
  }

  getPageNumber(): number
  {
    return this.pageNumber;
  }

  loadPage(pageNumber: number)
  {
    console.log('manage-users:loadPage(' + pageNumber.toString() + ') called');
    this.fetchUserPage(pageNumber);
  }

  addUser()
  {
    var newUser = {
      email: '',
      isAdmin: false,
      firstName: '',
      lastName: '',
      organisation: '' };

    const modelRef = this.modalService.open(EditUserDialogComponent);
    modelRef.componentInstance.user = newUser;
    modelRef.result.then(
      result =>
      {
        this.userService.addUser$(result).subscribe(
          result =>
          {
            this.loadPage(this.pageNumber);
          },
          error =>
          {
            console.log('Add user failed '+error.toString());
          }
        )
      }
    );

  }

  editUser(user: UserModel)
  {
    const modelRef = this.modalService.open(EditUserDialogComponent);
    modelRef.componentInstance.user = user;
    modelRef.result.then(
      result =>
      {
        this.userService.addUser$(result).subscribe(
          result =>
          {
            this.loadPage(this.pageNumber);
          },
          error =>
          {
            console.log('Update user failed '+error.toString());
          }
        )
      }, reason => { console.log('User dialog cancelled ' + reason )}
    );

  }

}
