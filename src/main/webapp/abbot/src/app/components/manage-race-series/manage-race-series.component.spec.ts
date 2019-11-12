import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageRaceSeriesComponent } from './manage-race-series.component';

describe('ManageRaceSeriesComponent', () => {
  let component: ManageRaceSeriesComponent;
  let fixture: ComponentFixture<ManageRaceSeriesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ManageRaceSeriesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ManageRaceSeriesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
