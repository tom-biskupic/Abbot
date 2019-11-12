import { BoatDivisionModel, BoatClassModel } from 'src/app/models/BoatClassModel';

export interface FleetSelector
{
  id: number;
  boatClass: BoatClassModel;
  boatDivision: BoatDivisionModel;
}

export interface FleetModel
{
  id: number;
  raceSeriesId: number;
  fleetName: number;
  fleetClasses: FleetSelector[];
  competeOnYardstick: boolean;
}
