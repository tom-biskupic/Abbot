import { FleetModel } from 'src/app/models/FleetModel';
import { CompetitionModel } from './CompetitionModel';

export interface RaceModel
{
  id: number;
  raceSeriesId: number;
  raceDate: Date;
  name: string;
  fleet: FleetModel;
  shortCourseRace: boolean;
  competitions: CompetitionModel[];
}
