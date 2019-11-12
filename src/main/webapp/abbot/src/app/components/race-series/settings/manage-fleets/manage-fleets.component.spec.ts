import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageFleetsComponent } from './manage-fleets.component';

describe('ManageFleetsComponent', () => {
  let component: ManageFleetsComponent;
  let fixture: ComponentFixture<ManageFleetsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ManageFleetsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ManageFleetsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
