import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RaceSeriesRegisteredBoatsComponent } from './race-series-registered-boats.component';

describe('RaceSeriesRegisteredBoatsComponent', () => {
  let component: RaceSeriesRegisteredBoatsComponent;
  let fixture: ComponentFixture<RaceSeriesRegisteredBoatsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RaceSeriesRegisteredBoatsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RaceSeriesRegisteredBoatsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
