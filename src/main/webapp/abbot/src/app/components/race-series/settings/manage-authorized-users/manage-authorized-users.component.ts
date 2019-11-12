import { AuthorizedUserService } from './../../../../services/authorized-user.service';
import { AuthorizedUserModel } from './../../../../models/AuthorizedUserModel';
import { Component, OnInit, Input } from '@angular/core';
import { faPlusSquare, faSync, faTrashAlt } from '@fortawesome/free-solid-svg-icons';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ConfirmDeleteService } from 'src/app/services/confirm-delete.service';
import { EditAuthorizedUserComponent } from '../edit-authorized-user/edit-authorized-user.component';

@Component({
  selector: 'app-manage-authorized-users',
  templateUrl: './manage-authorized-users.component.html',
  styleUrls: ['./manage-authorized-users.component.css']
})
export class ManageAuthorizedUsersComponent implements OnInit {

  @Input() raceSeriesId: number;

  authorizedUsers: AuthorizedUserModel[];
  numPages = 0;
  pageNumber = 0;
  pageSize = 5;
  collectionSize = 0;

  faPlusSquare = faPlusSquare;
  faSync = faSync;
  faTrashAlt = faTrashAlt;

  constructor(
    private authorizedUserService: AuthorizedUserService,
    private modalService: NgbModal,
    private confirmService: ConfirmDeleteService) { }


  ngOnInit()
  {
    this.fetchPage(1);
  }

  private fetchPage(pageNumber: number)
  {
    this.authorizedUserService.getAuthorizedUsers$(this.raceSeriesId, pageNumber - 1, this.pageSize).subscribe(
      res =>
      {
        this.authorizedUsers = res.content;
        this.numPages = res.totalPages;
        this.pageNumber = res.number + 1;
        this.collectionSize = res.totalPages * this.pageSize;
      },
      err =>
      {
        console.error(err);
      });
  }

  getPageNumber(): number
  {
    return this.pageNumber;
  }

  loadPage(pageNumber: number)
  {
    this.fetchPage(pageNumber);
  }

  addAuthorizedUser()
  {
    const newAuthorizedUser: AuthorizedUserModel =
    {
      id: null,
      currentUser: false,
      emailAddress: '',
      name: ''
    };

    const modelRef = this.modalService.open(EditAuthorizedUserComponent);
    modelRef.componentInstance.authorizedUser = newAuthorizedUser;
    modelRef.componentInstance.raceSeriesId = this.raceSeriesId;
    modelRef.result.then(
      result =>
      {
        this.authorizedUserService.addAuthorizedUser$(this.raceSeriesId, result).subscribe(
          result =>
          {
            this.loadPage(this.pageNumber);
          },
          error =>
          {
            console.log('Add authorized user failed ' + error.toString());
          }
        );
      }
    );
  }

  deleteAuthorizedUser(user: AuthorizedUserModel)
  {
    this.confirmService.confirmDelete('Are you sure you want to revoke this users access to this race series?').then(
      result =>
      {
        if ( result )
        {
          this.authorizedUserService.deleteAuthorizedUser$(this.raceSeriesId, user).subscribe(
            result =>
            {
              this.loadPage(this.pageNumber);
            },
            error =>
            {
              console.log('Delete authorized user failed ' + error.toString());
            }
          );
        }
      }
    );
  }
}
