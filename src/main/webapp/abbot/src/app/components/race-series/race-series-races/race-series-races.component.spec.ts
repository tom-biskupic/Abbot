import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RaceSeriesRacesComponent } from './race-series-races.component';

describe('RaceSeriesRacesComponent', () => {
  let component: RaceSeriesRacesComponent;
  let fixture: ComponentFixture<RaceSeriesRacesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RaceSeriesRacesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RaceSeriesRacesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
