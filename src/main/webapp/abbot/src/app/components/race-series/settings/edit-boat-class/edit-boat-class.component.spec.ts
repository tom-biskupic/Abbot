import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditBoatClassComponent } from './edit-boat-class.component';

describe('EditBoatClassComponent', () => {
  let component: EditBoatClassComponent;
  let fixture: ComponentFixture<EditBoatClassComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditBoatClassComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditBoatClassComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
