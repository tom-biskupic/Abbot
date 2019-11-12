import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RaceSeriesPointsComponent } from './race-series-points.component';

describe('RaceSeriesPointsComponent', () => {
  let component: RaceSeriesPointsComponent;
  let fixture: ComponentFixture<RaceSeriesPointsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RaceSeriesPointsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RaceSeriesPointsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
