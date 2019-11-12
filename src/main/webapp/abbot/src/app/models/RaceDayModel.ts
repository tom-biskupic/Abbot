import { RaceModel } from './RaceModel';

export interface RaceDayModel
{
  day: Date;
  races: RaceModel[];
}
