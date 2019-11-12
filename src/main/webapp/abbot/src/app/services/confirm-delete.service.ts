import { ConfirmDialogComponent } from './../components/confirm-dialog/confirm-dialog.component';
import { Injectable } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Injectable({
  providedIn: 'root'
})
export class ConfirmDeleteService
{
  constructor(private modalService: NgbModal) { }

  confirmDelete(message: string): Promise<boolean>
  {
    const modelRef = this.modalService.open(ConfirmDialogComponent);
    modelRef.componentInstance.message = message;

    return modelRef.result;
  }
}
