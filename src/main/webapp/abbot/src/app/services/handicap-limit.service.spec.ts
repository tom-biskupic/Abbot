import { TestBed } from '@angular/core/testing';

import { HandicapLimitService } from './handicap-limit.service';

describe('HandicapLimitService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: HandicapLimitService = TestBed.get(HandicapLimitService);
    expect(service).toBeTruthy();
  });
});
