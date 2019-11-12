import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditRaceSeriesComponent } from './edit-race-series-params.component';

describe('EditRaceSeriesParamsComponent', () => {
  let component: EditRaceSeriesComponent;
  let fixture: ComponentFixture<EditRaceSeriesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditRaceSeriesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditRaceSeriesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
