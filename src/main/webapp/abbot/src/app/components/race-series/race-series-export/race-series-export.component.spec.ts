import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RaceSeriesExportComponent } from './race-series-export.component';

describe('RaceSeriesExportComponent', () => {
  let component: RaceSeriesExportComponent;
  let fixture: ComponentFixture<RaceSeriesExportComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RaceSeriesExportComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RaceSeriesExportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
