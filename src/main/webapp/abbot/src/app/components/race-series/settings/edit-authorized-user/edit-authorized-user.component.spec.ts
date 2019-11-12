import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditAuthorizedUserComponent } from './edit-authorized-user.component';

describe('EditAuthorizedUserComponent', () => {
  let component: EditAuthorizedUserComponent;
  let fixture: ComponentFixture<EditAuthorizedUserComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditAuthorizedUserComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditAuthorizedUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
