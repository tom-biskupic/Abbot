import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageHandicapsComponent } from './manage-handicaps.component';

describe('ManageHandicapsComponent', () => {
  let component: ManageHandicapsComponent;
  let fixture: ComponentFixture<ManageHandicapsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ManageHandicapsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ManageHandicapsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
