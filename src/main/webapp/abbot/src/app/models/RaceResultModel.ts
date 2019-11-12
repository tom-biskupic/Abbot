import { BoatModel } from './BoatModel';

export enum ResultStatus
{
  FINISHED = 'FINISHED',
  DNF = 'DNF', 	        //	Did not finish
  OCS = 'OCS', 	        // 	Did not start; on the course side of the starting line at her
                        //	starting signal and failed to start, or broke rule 30.1
  ZFP = 'ZPF',	        // 	20% penalty under rule 30.2
  UFD = 'UFD',	        //	Disqualification under rule 30.3
  BFD = 'BFD',	        //	Disqualification under rule 30.4
  SCP = 'SCP',	        //	Scoring Penalty applied
  RET = 'RET',	        // 	Retired
  DSQ = 'DSQ',	        // 	Disqualification
  DNE = 'DNE',	        // 	Disqualification that is not excludable
  RDG = 'RDG',	        // 	Redress given
  DPI = 'DPI', 	        //	Discretionary penalty imposed
  DNC = 'DNC', 	        // 	Did not start; did not come to the starting area
  DNS = 'DNS' 	        // 	Did not start (other than DNC and OCS)
}

export interface RaceResultModel
{
  id: number;
  raceId: number;
  boat: BoatModel;
  handicap: number;
  overrideHandicap: boolean;
  startTime: Date;
  finishTime: Date;
  status: ResultStatus;
  sailingTime: number;
  correctedTime: number;
  handicapPlace: number;
  scratchPlace: number;
}
