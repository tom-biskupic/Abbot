export interface BoatDivision {
  id?: number;
  name: string;
  yardStick: number;
}

export interface BoatClass {
  id?: number;
  raceSeriesId?: number;
  name: string;
  yardStick: number;
  divisions: BoatDivision[];
}

export interface FleetSelector {
  id?: number;
  boatClass: BoatClass;
  boatDivision?: BoatDivision | null;
}

export interface Fleet {
  id: number;
  fleetName: string;
  competeOnYardstick?: boolean;
  raceSeriesId?: number;
  fleetClasses?: FleetSelector[];
}

export interface Competition {
  id?: number;
  name: string;
  fleet: Fleet;
  raceSeriesId?: number;
  pointsSystem?: 'LOW_POINTS' | 'BONUS_POINTS';
  resultType?: 'SCRATCH_RESULT' | 'HANDICAP_RESULT';
  fleetSize?: number;
  drops?: number;
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

export interface RaceDay {
  day: number;   // Unix timestamp (ms) from backend
  races: Race[];
}

export interface Boat {
  id?: number;
  raceSeriesID?: number;
  name: string;
  sailNumber: string;
  boatClass?: BoatClass;
  division?: BoatDivision;
  visitor?: boolean;
  skipper?: string;
  crew?: string;
}

export type ResultStatus =
  'FINISHED' | 'DNS' | 'DNF' | 'DNC' | 'OCS' |
  'ZFP' | 'UFD' | 'BFD' | 'SCP' | 'RET' |
  'DSQ' | 'DNE' | 'RDG' | 'DPI';

export interface RaceResult {
  id?: number;
  raceId?: number;
  boat: Boat;
  handicap?: number;
  overrideHandicap?: boolean;
  startTime?: number;     // Unix timestamp (ms)
  finishTime?: number;    // Unix timestamp (ms)
  sailingTime?: number;   // seconds
  correctedTime?: number; // seconds
  scratchPlace?: number;
  handicapPlace?: number;
  status?: ResultStatus;
}
