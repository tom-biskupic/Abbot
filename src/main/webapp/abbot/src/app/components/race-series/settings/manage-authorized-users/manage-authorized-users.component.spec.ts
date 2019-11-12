import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageAuthorizedUsersComponent } from './manage-authorized-users.component';

describe('ManageAuthorizedUsersComponent', () => {
  let component: ManageAuthorizedUsersComponent;
  let fixture: ComponentFixture<ManageAuthorizedUsersComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ManageAuthorizedUsersComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ManageAuthorizedUsersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
