angular.module("abbot").controller("raceController",function($scope,$http,$controller,$rootScope)
{
    angular.extend(this,$controller('listController', {$scope: $scope})); 

	$scope.raceSeriesID = $rootScope.seriesID;
	$scope.init('/Abbot3','/raceseries/'+$scope.raceSeriesID+'/racelist.json','/raceseries/'+$scope.raceSeriesID+'/race.json','views/raceform.html')

    $scope.newObject = function()
    {
        object = {};
        $scope.editNew(
                $scope.contextPath,
                $scope.itemResource,
                $scope.editDialogId,
                object,
                "raceDialogInstanceController").then(
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
                $scope.editDialogId,
                id,
                "raceDialogInstanceController").then(
                        function() 
                        { 
                            $scope.loadPage($scope.page.number) 
                        });;
    }

    $scope.editResults = function(id)
    {
        $scope.edit(
                $scope.contextPath,
                $scope.itemResource,
                "editRaceResults",
                id,
                "raceResultDialogInstanceController").then(
                        function() 
                        { 
                            $scope.loadPage($scope.page.number) 
                        });
    }

});

angular.module("abbot").controller("raceDialogInstanceController",function($scope, $http, $controller, $uibModalInstance,$rootScope,object,context,resource )
{
    angular.extend(
            this,
            $controller(    
                    'dialogInstanceController', 
                    {$scope: $scope, $http: $http, $uibModalInstance: $uibModalInstance, object: object, context: context,resource: resource}));

    $scope.raceSeriesId = $rootScope.seriesID;
    
    $http.get(context+'/raceseries/'+$scope.raceSeriesId+'/fleetlist.json/all').then(
            function(response) 
            { 
                $scope.fleets = response.data;
            });

    $http.get(context+'/raceseries/'+$scope.raceSeriesId+'/competitionlist.json/all').then(
            function(response) 
            { 
                $scope.competitions = response.data
            });

    if ( $scope.object.competitions === undefined )
    {
        $scope.object.competitions = new Array();
    }
    
    $scope.dateOptions = 
    {
    	    formatYear: 'yy',
    	    maxDate: new Date(2020, 5, 22),
    	    minDate: new Date(),
    	    startingDay: 1
    };
    
    $scope.raceDatePopup = 
    {
    	    opened: false
    };
    
    $scope.raceDatePopupOpen = function() 
    {
        $scope.raceDatePopup.opened = true;
    };

    $scope.addCompetition = function(competition)
    {
    	if ( competition != undefined )
    	{
	    	$scope.object.competitions.push(competition);
    	}
    };
    
    $scope.removeCompetition = function(competitionToRemove)
    {
        for( var i=0;i< $scope.object.competitions.length;i++ )
        {
            if ( $scope.object.competitions[i] == competitionToRemove )
            {
                $scope.object.competitions.splice(i,1);
                return;
            }
        }
    }

});

angular.module("abbot").filter("competitionForFleet",function()
{
	return function (competitions,fleet)
	{
		if (fleet != undefined && competitions != undefined)
		{
			filteredComps = [];
			for(i=0;i<competitions.length;i++)
			{
				if ( competitions[i].fleet.id == fleet.id )
				{
					filteredComps.push(competitions[i]);
				}
			}
			
			return filteredComps;
		}
		else
		{
			return competitions;
		}
	}
});

angular.module("abbot").controller("raceResultDialogInstanceController",function($scope, $http, $controller, $uibModalInstance, uibDateParser, $rootScope, object,context,resource )
{
    angular.extend(
            this,
            $controller(    
                    'dialogInstanceController', 
                    {$scope: $scope, $http: $http, $uibModalInstance: $uibModalInstance, object: object, context: context,resource: resource}));

    $scope.finishStatusValues =
    [
     {
    	 id: 'Completed',  
    	 label: 'Finished'
     },
     {
    	 id: 'DNC',
    	 label : "Did not Compete (DNC)"
     },
     {
    	 id: 'DNS',
    	 label: "Did not start (DNS)"
     },
     {
    	 id: 'OCS',
    	 label: "On course side - failed to start"
     },
     {
    	 id: 'ZFP',
    	 label: '20% Penalty under rule 30.2 (ZFP)'
     },
     {
    	 id: 'BFD',
    	 label: 'Black flag disqualification (BFD)'
     },
     {
    	 id: 'SCP',
    	 label: 'Scoring penalty under rule 44.3 (SCP)'
     },
     {
    	 id: 'DNF',
         label: "Did not finish (DNF)"
     },
     {
    	 id: 'RAF',
    	 label: 'Retired after finishing (RAF)'
     },
     {
    	 id: 'DSQ',
    	 label: 'Disqualified (DSQ)'
     },
     {
    	 id: 'DNE',
         label: 'Disqualification other than DGM under 89.3(b) (DNE)'
     },
     {
    	 id: 'DGM',
         label: 'Disqualification under rule 69.1(b)(2) (DGM)'
     },
     {
    	 id: 'RDG',
         label: 'Redress given (RDG)'
     }
    ];
    
    $scope.reset = function()
    {
    	$scope.resultStatus = 'Completed';
    	$scope.finishTime = '00:00:00';
    	$scope.boatToAdd = null;
    }
    
    $scope.raceSeriesId = $rootScope.seriesID;
    
    $http.get(context+'/raceseries/'+$scope.raceSeriesId+'/fleet/'+object.fleet.id+'/boatlist.json/all').then(
            function(response) 
            { 
                $scope.boats = response.data;
            });

    $scope.timeFormat = 'HH::mm::ss';
    $scope.startTime = object.raceDate == null ? new Date() : object.raceDate;
    $scope.results = [];
    
    $scope.reset();
    
    $scope.statusIsFinished = function() 
    {
    	switch($scope.resultStatus)
    	{
    		case 'DNF':
    		case 'DNS':
    		case 'DNC':
    			return false;
    		default:
    			return true;
    	}
    }
    
    $scope.addResult = function()
    {
    	newResult = {};
    	newResult.status = $scope.resultStatus;
    	newResult.boat = $scope.boatToAdd
    	newResult.finishTime = $scope.finishTime;
    	$scope.results.push(newResult);
    	$scope.reset();
    }
});


