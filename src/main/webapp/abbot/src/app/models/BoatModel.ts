import { BoatClassModel, BoatDivisionModel } from './BoatClassModel';

export class BoatModel
{
  id: number;
  raceSeriesID: number;
  name: string;
  sailNumber: string;
  boatClass: BoatClassModel;
  division: BoatDivisionModel;
  visitor: boolean;
  skipper: string;
  crew: string;
}
