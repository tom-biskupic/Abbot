import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RaceDayListComponent } from './race-day-list.component';

describe('RaceDayListComponent', () => {
  let component: RaceDayListComponent;
  let fixture: ComponentFixture<RaceDayListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RaceDayListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RaceDayListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
