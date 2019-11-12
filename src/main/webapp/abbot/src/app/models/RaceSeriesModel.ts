
export enum RaceSeriesType
{
    SEASON = 'SEASON',
    REGATTA = 'REGATTA'
}

export interface RaceSeriesModel
{
  id: number;
  seriesType: RaceSeriesType;
  name: string;
  comment: string;
  dateCreated: Date;
  lastUpdated: Date;
}

