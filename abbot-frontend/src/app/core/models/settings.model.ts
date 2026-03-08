import { Fleet } from './race.model';

export interface HandicapLimit {
  id?: number;
  raceSeriesID?: number;
  fleet: Fleet;
  limit: number;
}

export interface UserSummary {
  id?: number;
  name: string;
  emailAddress: string;
  currentUser: boolean;
}
