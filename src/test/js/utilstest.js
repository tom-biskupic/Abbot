'use strict';

describe("Tests the utility functions", function() 
{
	var emptyObjectArray = [];
	var allObjects = [ { id : 1 }, { id : 2 }, { id : 3 } ];
	var selectedObjects = [ { id : 2 }, { id : 3 } ];
	var $filter;
	
	beforeEach(angular.mock.module("abbot"));

	beforeEach(function () 
	{
		inject(function (_$filter_) 
		{
			$filter = _$filter_;
    	});
	});

    it("Filters out objects that are already selected", function() 
    {
    	var filtered = $filter("notAlreadySelected")(allObjects,selectedObjects);
    	expect(filtered.length).toEqual(1);
    	expect(filtered[0].id).toEqual(1);
    });
    
    it("Returns all objects if the selected list is undefined", function() 
    {
    	var filtered = $filter("notAlreadySelected")(allObjects,undefined);
    	expect(filtered.length).toEqual(3);
    });

    it("Returns all objects if the selected list is empty", function() 
    {
    	var filtered = $filter("notAlreadySelected")(allObjects,emptyObjectArray);
    	expect(filtered.length).toEqual(3);
    });

    it("Returns no objects if the object list is undefined", function() 
    {
    	var filtered = $filter("notAlreadySelected")(undefined,undefined);
    	expect(filtered.length).toEqual(0);
    });

    it("Returns no objects if all have been selected", function() 
    {
    	var filtered = $filter("notAlreadySelected")(allObjects,allObjects);
    	expect(filtered.length).toEqual(0);
    });


});
