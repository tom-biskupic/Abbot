
//
//  Models a page of results returned by a Spring
//  pageable repo.
//
export class ResultPage<T>
{
  content: T[];
  number: number;
  totalPages: number;
}
