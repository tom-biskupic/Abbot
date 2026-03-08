export type RaceSeriesType = 'SEASON' | 'REGATTA';

export interface RaceSeries {
  id?: number;
  name: string;
  seriesType: RaceSeriesType;
  comment: string;
  dateCreated?: string;
  lastUpdated?: string;
}

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}
