import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditRaceResultComponent } from './edit-race-result.component';

describe('EditRaceResultComponent', () => {
  let component: EditRaceResultComponent;
  let fixture: ComponentFixture<EditRaceResultComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditRaceResultComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditRaceResultComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
