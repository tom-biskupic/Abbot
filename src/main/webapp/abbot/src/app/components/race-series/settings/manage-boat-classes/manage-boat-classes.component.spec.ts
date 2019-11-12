import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageBoatClassesComponent } from './manage-boat-classes.component';

describe('ManageBoatClassesComponent', () => {
  let component: ManageBoatClassesComponent;
  let fixture: ComponentFixture<ManageBoatClassesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ManageBoatClassesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ManageBoatClassesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
