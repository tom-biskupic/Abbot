import { TestBed } from '@angular/core/testing';

import { RaceSeriesService } from './race-series.service';

describe('RaceSeriesService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: RaceSeriesService = TestBed.get(RaceSeriesService);
    expect(service).toBeTruthy();
  });
});
