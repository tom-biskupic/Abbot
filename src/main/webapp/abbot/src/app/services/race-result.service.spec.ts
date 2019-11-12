import { TestBed } from '@angular/core/testing';

import { RaceResultService } from './race-result.service';

describe('RaceResultService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: RaceResultService = TestBed.get(RaceResultService);
    expect(service).toBeTruthy();
  });
});
