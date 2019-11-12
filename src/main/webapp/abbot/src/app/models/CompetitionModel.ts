import { FleetModel } from 'src/app/models/FleetModel';

export enum PointsSystem
{
  LOW_POINTS = 'LOW_POINTS',
  BONUS_POINTS = 'BONUS_POINTS'
}

export enum ResultType
{
  SCRATCH_RESULT = 'SCRATCH_RESULT',
  HANDICAP_RESULT = 'HANDICAP_RESULT'
}

export interface CompetitionModel
{
  id: number;
  name: string;
  pointsSystem: PointsSystem;
  drops: number;
  fleetSize: number;
  resultType: ResultType;
  fleet: FleetModel;
}
