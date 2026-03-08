export interface Fleet {
  id: number;
  fleetName: string;
  competeOnYardstick?: boolean;
  raceSeriesId?: number;
}

export interface Competition {
  id: number;
  name: string;
  fleet: Fleet;
  raceSeriesId?: number;
}

export type RaceStatus = 'NOT_RUN' | 'COMPLETED' | 'ABANDONED';

export interface FieldValidationError {
  field?: string;       // present for field-level errors, absent for object-level
  defaultMessage: string;
}

export interface ValidationResponse {
  status: 'SUCCESS' | 'FAIL';
  errorMessageList?: FieldValidationError[];
  generalErrorText?: string;
}

export interface Race {
  id?: number;
  raceSeriesId?: number;
  raceDate: number;   // Unix timestamp (ms) from backend
  name: string;
  raceNumber?: number;
  fleet: Fleet;
  shortCourseRace: boolean;
  competitions: Competition[];
  raceStatus?: RaceStatus;
}
