import { TestBed } from '@angular/core/testing';

import { BoatClassServiceService } from './boat-class-service';

describe('BoatClassServiceService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: BoatClassServiceService = TestBed.get(BoatClassServiceService);
    expect(service).toBeTruthy();
  });
});
