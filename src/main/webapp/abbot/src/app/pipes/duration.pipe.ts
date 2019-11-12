import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'duration'
})
export class DurationPipe implements PipeTransform 
{

  str_pad_left(value: number,length=2) 
  {
    let numberStr = value.toString();
    return (new Array(length+1).join('0')+numberStr).slice(-length);
  }

  transform(duration: number): string 
  {
		if ( duration == undefined || duration == 0 )
		{
			return '';
		}
		
		let hours = Math.floor(duration/3600);
		duration = duration - (hours * 3600);
		
		let minutes = Math.floor(duration/60);
		duration = duration - (minutes * 60);
		
		let seconds = duration;
		return this.str_pad_left(hours)+':'+this.str_pad_left(minutes)+':'+this.str_pad_left(seconds);
  }

}
