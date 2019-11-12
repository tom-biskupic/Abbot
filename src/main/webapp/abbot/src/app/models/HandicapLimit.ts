import { FleetModel } from './FleetModel';
export interface HandicapLimit
{
  id: number;
  raceSeriesID: number;
  fleet: FleetModel;
  limit: number;
}

