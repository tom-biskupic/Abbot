abbotModule.controller("fleetController",function($scope,$http,$controller)
{
    angular.extend(this,$controller('listController', {$scope: $scope}));

    $scope.newObject = function()
    {
        object = {};
        $scope.editNew(
                $scope.contextPath,
                $scope.itemResource,
                $scope.editDialogId,
                object,
                "fleetDialogInstanceController").then(
                        function() 
                        { 
                            $scope.loadPage($scope.page.number) 
                        });;
    }

    $scope.editObject = function(id)
    {
        $scope.edit(
                $scope.contextPath,
                $scope.itemResource,
                $scope.editDialogId,id,
                "fleetDialogInstanceController").then(
                        function() 
                        { 
                            $scope.loadPage($scope.page.number) 
                        });;
    }
});

angular.module("abbot").controller("fleetDialogInstanceController",function($scope, $http, $controller, $uibModalInstance,object,context,resource )
{
    angular.extend(
            this,
            $controller(    
                    'dialogInstanceController', 
                    {$scope: $scope, $http: $http, $uibModalInstance: $uibModalInstance, object: object, context: context,resource: resource}));

    
    $http.get(context+'/raceseries/'+${raceSeries.id}+'/boatclasslist.json/all').then(
            function(response) 
            { 
                $scope.boatClasses = response.data
            });
    
    $scope.alreadyAdded = function(boatClassToAdd,divisionToAdd)
    {
    	if ( object.fleetClasses == undefined || object.fleetClasses.length==0 )
    	{
    		return false;
    	}
    	
    	foundMatch=false;
    	for(i=0;i<object.fleetClasses.length;i++)
    	{
    		if ( 	object.fleetClasses[i].boatClass.id == boatClassToAdd.id
    				&&
    				( 	divisionToAdd == undefined && object.fleetClasses[i].boatDivision == undefined
    					||
    					divisionToAdd.id == object.fleetClasses[i].boatDivision.id ) )
    		{
    			foundMatch=true;
    			break;
    		}
    	}
    	
    	return foundMatch;
    }

    $scope.nothingSelected = function(boatClassToAdd,divisionToAdd)
    {
       	return ( boatClassToAdd == undefined
       			||
       			(	divisionToAdd == undefined 
       				&&
       				boatClassToAdd.divisions.length != 0 ) )
    }
    
    $scope.addToFleet = function(boatClassToAdd,divisionToAdd)
    {
 
    	if ( 	$scope.nothingSelected(boatClassToAdd,divisionToAdd)
    			||
    			$scope.alreadyAdded(boatClassToAdd,divisionToAdd) )
    	{
    		return;
    	}
    	
        var newFleetSelector = {};
        newFleetSelector.boatClass = boatClassToAdd;
        newFleetSelector.boatDivision = divisionToAdd;

        if ( $scope.object.fleetClasses === undefined )
        {
            $scope.object.fleetClasses = new Array();
        }
        
        $scope.object.fleetClasses.push(newFleetSelector);
    }
    
    $scope.removeFleetSelector = function(fleetSelectorToRemove)
    {
        for( var i=0;i< $scope.object.fleetClasses.length;i++ )
        {
            if ( $scope.object.fleetClasses[i] == fleetSelectorToRemove )
            {
                $scope.object.fleetClasses.splice(i,1);
                return;
            }
        }
    }
});
