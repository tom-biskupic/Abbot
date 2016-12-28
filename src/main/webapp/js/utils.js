
angular.module("abbot").filter("notAlreadySelected",function()
{
	return function (objects,selectedObjects)
	{
		if (selectedObjects!= undefined && objects != undefined)
		{
			filteredObjects = [];
			for(i=0;i<objects.length;i++)
			{
				isSelected = false;
				for(j=0;j<selectedObjects.length;j++)
				{
					if ( objects[i].id == selectedObjects[j].id )
					{
						isSelected = true;
						break;
					}
				}
				
				if ( !isSelected )
			    {
					filteredObjects.push(objects[i]);
			    }
			}
			
			return filteredObjects;
		}
		else
		{
			if ( objects != undefined )
			{
				return objects;
			}
			else
			{
				return [];
			}
		}
	}
});
