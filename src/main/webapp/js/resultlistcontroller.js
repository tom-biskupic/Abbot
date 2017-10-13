abbotModule.controller("resultListController",function($scope,$http,$controller,$rootScope,$uibModal)
{
    angular.extend(this,$controller('listController', {$scope: $scope}));
    
    $scope.raceSeriesID = $rootScope.seriesID;
    $scope.raceDays = [];
    $scope.selectedRace = 0;
    
    $scope.setDialogController('raceResultDialogInstanceController');
    
    $scope.loadRaces = function()
    {
	    $http.get('/Abbot3'+'/raceseries/'+$scope.raceSeriesID+'/racedays.json').then(
	            function(response)
	            {
	                $scope.raceDays = response.data;
	                if ( $scope.raceDays.length > 0 )
	                {
	                	if ( $scope.selectedRace == undefined )
	                	{
		                	lastDay = $scope.raceDays[$scope.raceDays.length-1];
		                	$scope.selectRace(lastDay.races[0].id);
		                	lastDay.isOpen=true;
	                	}
	                }
	            });
    }
    
    $scope.selectRace = function(id)
    {
    	$scope.selectedRace = $scope.findRace(id); 
    	$scope.init(
    			'/Abbot3',
    			'/raceseries/'+$scope.raceSeriesID+'/race/'+$scope.selectedRace.id+'/resultlist.json',
    			'/raceseries/'+$scope.raceSeriesID+'/race/'+$scope.selectedRace.id+'/result.json/',
    			'views/raceresultform.html');
    }
    
    $scope.findRace = function(id)
    {
    	for(dayIndex=0;dayIndex<$scope.raceDays.length;dayIndex++)
    	{
    		day = $scope.raceDays[dayIndex];
    		for( raceIndex=0;raceIndex<day.races.length;raceIndex++ )
    		{
    			race = day.races[raceIndex];
    			if ( race.id == id )
    			{
    				return race; 
    			}
    		}
    	}
    	return null;
    }
    
    //
    //	Overload opendialog so we pass the fleet ID to the dialog
    //
    $scope.openDialog = function(context,resource,template,object,controller)
    {
        var modalInstance = $uibModal.open(
                {
                    animation:      true,
                    templateUrl:    template,
                    controller:     controller,
                    resolve: {
                      object: function () 
                      {
                        return object;
                      },
                      context: function () 
                      {
                        return context;
                      },
                      resource: function () 
                      {
                        return resource;
                      },
                      race: function ()
                      {
                    	return $scope.selectedRace;
                      }
                    }
                });
        
        return modalInstance.result;
    }
    
    $scope.loadRaces();
    
    $scope.$on('raceupdated',function(even,data)
    {
    	$scope.loadRaces();
    });

    $scope.updateRaceStatus = function(race)
    {
        var modalInstance = $uibModal.open(
                {
                    animation:      true,
                    templateUrl:    'views/racestatusform.html',
                    controller:     'raceStatusDialogInstanceController',
                    resolve: {
                      object: function () 
                      {
                        return race;
                      },
                      context: function () 
                      {
                        return $scope.contextPath;
                      },
                    }
                });
        
//        modalInstance.result.then( function()
//        		{
//    					$scope.loadRaces(); 
//        		});
    }

});

angular.module("abbot").controller("raceResultDialogInstanceController",function(
		$scope, $http, $controller, $uibModalInstance,$rootScope,object,context,resource,race )
{
	$scope.toDate = function(timeString)
	{
		return new Date('1970-01-01T'+timeString+"Z");
	}
	
	//
	//	Need to fix the start/end time as they don't parse correctly as dates
	//
	//object.startTime = $scope.toDate(object.startTime);
	//object.finishTime = $scope.toDate(object.finishTime);

	object.startTime = new Date(object.startTime);
	object.finishTime = new Date(object.finishTime);

	angular.extend(
		this,
		$controller(    
				'dialogInstanceController', 
		        {	$scope: $scope, 
					$http: $http, 
					$uibModalInstance: $uibModalInstance, 
					object: object, 
					context: context,
					resource: resource}));

	$scope.raceSeriesId = $rootScope.seriesID;

	// $scope.fleet = fleet;
	
	// /raceseries/{raceseriesid}/race/{raceid}/boatsnotselected.json
	
	//$http.get(context+'/raceseries/'+$scope.raceSeriesId+'/fleet/'+$scope.fleet.id+'/boatlist.json/all').then(
	$http.get(context+'/raceseries/'+race.raceSeriesId+'/race/'+race.id+'/boatsnotselected.json').then(
		function(response) 
		{ 
			$scope.boats = response.data;
			
			//
			//	If we are editing an existing entry then add the boat
			//	in that entry
			//
			if ( object.boat != undefined )
			{
				$scope.boats.push(object.boat);
			}
		});

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
	
    $scope.statusValues = 
    	[   {
              id: 'FINISHED',
              label: 'Finished',
            }, 
            {
                id: 'DNS',
                label: 'DNS'
            },
            {
                id: 'DNF',
                label: 'DNF'
            },
            {
                id: 'DNC',
                label: 'DNC'
            },
            {
                id: 'OCS',
                label: 'OCS'
            },
            {
                id: 'ZFP',
                label: 'ZFP'
            },
            {
                id: 'UFD',
                label: 'UFD'
            },
            {
                id: 'BFD',
                label: 'BFD'
            },
            {
                id: 'SCP',
                label: 'SCP'
            },
            {
                id: 'RET',
                label: 'RET'
            },
            {
                id: 'DSQ',
                label: 'DSQ'
            },
            {
                id: 'DNE',
                label: 'DNE'
            },
            {
                id: 'RDG',
                label: 'RDG'
            },
            {
                id: 'DPI',
                label: 'DPI'
            }
        ];
});

function str_pad_left(string,pad,length) 
{
    return (new Array(length+1).join(pad)+string).slice(-length);
}

angular.module("abbot").filter('durationToHHMMSS',function() 
{
	
	return function(duration)
	{
		var hours = Math.floor(duration/3600);
		duration = duration - (hours * 3600);
		
		var minutes = Math.floor(duration/60);
		duration = duration - (minutes * 60);
		
		var seconds = duration;
		return str_pad_left(hours,'0',2)+':'+str_pad_left(minutes,'0',2)+':'+str_pad_left(seconds,'0',2);
	}
});

angular.module("abbot").controller("raceStatusDialogInstanceController",function(
		$scope, $http, $controller, $uibModalInstance, $rootScope, object, context )
{
	$scope.race = object;
	$scope.raceStatus = object.raceStatus;
	$scope.raceId = object.id;
	$scope.raceSeriesId = object.raceSeriesId;
	
	$scope.context = context;
	
    $scope.raceStatusValues = 
    	[	{
            	id: 'NOT_RUN',
            	label: 'Not run',
          	}, 
    		{
              id: 'COMPLETED',
              label: 'Completed',
            }, 
            {
                id: 'ABANDONED',
                label: 'Abandoned',
            }];

    $scope.ok = function () 
    {
    	var url = $scope.context + '/raceseries/'+$scope.raceSeriesId+'/racestatus.json/'+$scope.raceId;
    	
        $http.post(url,$scope.raceStatus).then(
                function(response)
                {
                	$scope.race.raceStatus = $scope.raceStatus;
                	$uibModalInstance.close();
                });
    };

    $scope.cancel = function () 
    {
        $uibModalInstance.dismiss('cancel');
    };

});
