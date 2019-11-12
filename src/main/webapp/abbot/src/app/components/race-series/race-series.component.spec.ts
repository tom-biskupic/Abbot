import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RaceSeriesComponent } from './race-series.component';

describe('RaceSeriesComponent', () => {
  let component: RaceSeriesComponent;
  let fixture: ComponentFixture<RaceSeriesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RaceSeriesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RaceSeriesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
