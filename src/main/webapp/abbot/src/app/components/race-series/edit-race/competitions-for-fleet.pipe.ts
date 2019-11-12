import { Pipe, PipeTransform } from '@angular/core';
import { CompetitionModel } from 'src/app/models/CompetitionModel';
import { FleetModel } from 'src/app/models/FleetModel';

@Pipe({
  name: 'competitionsForFleet'
})
export class CompetitionsForFleetPipe implements PipeTransform {

  transform(competitions: CompetitionModel[], fleet: FleetModel ): any
  {
    if ( fleet === undefined || fleet == null )
    {
      return [];
    }

    return competitions.filter(competition => competition.fleet.id === fleet.id);
  }

}

