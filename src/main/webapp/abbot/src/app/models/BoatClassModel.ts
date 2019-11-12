
export interface BoatDivisionModel
{
  id: number;
  name: string;
  yardStick: number;
}

export interface BoatClassModel
{
  id: number;
  raceSeriesId: number;
  name: string;
  yardStick: number;
  divisions: BoatDivisionModel[];
}
