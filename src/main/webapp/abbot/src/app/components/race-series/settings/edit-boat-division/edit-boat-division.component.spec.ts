import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditBoatDivisionComponent } from './edit-boat-division.component';

describe('EditBoatDivisionComponent', () => {
  let component: EditBoatDivisionComponent;
  let fixture: ComponentFixture<EditBoatDivisionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditBoatDivisionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditBoatDivisionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
