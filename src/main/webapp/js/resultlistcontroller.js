abbotModule.controller("resultListController",function($scope,$http,$controller,$rootScope,$uibModal)
{
    angular.extend(this,$controller('listController', {$scope: $scope}));
    
    $scope.raceSeriesID = $rootScope.seriesID;
    $scope.raceDays = [];
    $scope.selectedRace = 0;
    
    $scope.setDialogController('raceResultDialogInstanceController');
    
    $http.get('/Abbot3'+'/raceseries/'+$scope.raceSeriesID+'/racedays.json').then(
            function(response)
            {
                $scope.raceDays = response.data;
                if ( $scope.raceDays.length > 0 )
                {
                	lastDay = $scope.raceDays[$scope.raceDays.length-1];
                	$scope.selectRace(lastDay.races[0].id);
                	lastDay.isOpen=true;
                }
            });
    
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
                      fleet: function ()
                      {
                    	return $scope.selectedRace.fleet;
                      }
                    }
                });
        
        return modalInstance.result;
    }
});

angular.module("abbot").controller("raceResultDialogInstanceController",function(
		$scope, $http, $controller, $uibModalInstance,$rootScope,object,context,resource,fleet )
{
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

	$scope.fleet = fleet;
	
	$http.get(context+'/raceseries/'+$scope.raceSeriesId+'/fleet/'+$scope.fleet.id+'/boatlist.json/all').then(
		function(response) 
		{ 
			$scope.boats = response.data;
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

