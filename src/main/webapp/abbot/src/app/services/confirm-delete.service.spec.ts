import { TestBed } from '@angular/core/testing';

import { ConfirmDeleteService } from './confirm-delete.service';

describe('ConfirmDeleteService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ConfirmDeleteService = TestBed.get(ConfirmDeleteService);
    expect(service).toBeTruthy();
  });
});
