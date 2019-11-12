import { Component, OnInit, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-confirm-dialog',
  templateUrl: './confirm-dialog.component.html',
  styleUrls: ['./confirm-dialog.component.css']
})
export class ConfirmDialogComponent implements OnInit
{
  @Input() message: string;

  constructor(private modal: NgbActiveModal) { }

  ngOnInit()
  {
  }

  public ok()
  {
    this.modal.close(true);
  }

  public cancel()
  {
    this.modal.close(false);
  }

}
