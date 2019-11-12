import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RaceResultListComponent } from './race-result-list.component';

describe('RaceResultListComponent', () => {
  let component: RaceResultListComponent;
  let fixture: ComponentFixture<RaceResultListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RaceResultListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RaceResultListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
