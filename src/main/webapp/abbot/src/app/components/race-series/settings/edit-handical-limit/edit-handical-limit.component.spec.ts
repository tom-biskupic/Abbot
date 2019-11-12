import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditHandicalLimitComponent } from './edit-handical-limit.component';

describe('EditHandicalLimitComponent', () => {
  let component: EditHandicalLimitComponent;
  let fixture: ComponentFixture<EditHandicalLimitComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditHandicalLimitComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditHandicalLimitComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
