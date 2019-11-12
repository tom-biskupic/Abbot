import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RaceSeriesResultsComponent } from './race-series-results.component';

describe('RaceSeriesResultsComponent', () => {
  let component: RaceSeriesResultsComponent;
  let fixture: ComponentFixture<RaceSeriesResultsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RaceSeriesResultsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RaceSeriesResultsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
