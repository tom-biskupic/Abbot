angular.module("abbot").controller("fleetController",function($scope,$http,$controller,$rootScope)
{
    angular.extend(this,$controller('listController', {$scope: $scope}));

	$scope.raceSeriesID = $rootScope.seriesID;
    $scope.init(
    		'/Abbot3',
    		'/raceseries/'+$scope.raceSeriesID+'/fleetlist.json',
    		'/raceseries/'+$scope.raceSeriesID+'/fleet.json',
    		'views/fleetform.html',
    		'Fleet');
    
    $scope.setDialogController('fleetDialogInstanceController');
});

angular.module("abbot").controller("fleetDialogInstanceController",function($scope, $http, $controller, $uibModalInstance,$rootScope,object,context,resource )
{
    angular.extend(
            this,
            $controller(    
                    'dialogInstanceController', 
                    {$scope: $scope, $http: $http, $uibModalInstance: $uibModalInstance, object: object, context: context,resource: resource}));

	$scope.raceSeriesID = $rootScope.seriesID;

    $http.get(context+'/raceseries/'+$scope.raceSeriesID+'/boatclasslist.json/all').then(
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
