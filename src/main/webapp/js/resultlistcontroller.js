angular.module("abbot").controller("resultListController",function($scope,$http,$controller,$rootScope,$uibModal,$filter)
{
    angular.extend(this,$controller('listController', {$scope: $scope}));
    
    $scope.raceSeriesID = $rootScope.seriesID;
    $scope.raceDays = [];
    $scope.selectedRace = null;
    $scope.race_tree_data = [];
    $scope.race_tree = {};
    
    $scope.setDialogController('raceResultDialogInstanceController');
    
    $scope.loadRaces = function()
    {
	    $http.get('/Abbot3'+'/raceseries/'+$scope.raceSeriesID+'/racedays.json').then(
	            function(response)
	            {
	                $scope.raceDays = response.data;
	                $scope.race_tree_data = [];
	                
	                var selectedRaceNode = null;
	                var dayNodeToExpand = null;
	                
	            	for(dayIndex=0;dayIndex<$scope.raceDays.length;dayIndex++)
	            	{
	            		day = $scope.raceDays[dayIndex];

	            		var newDayNode = {};
	            		newDayNode.label = $filter('date')(day.day,'dd/MM/yyyy');
	            		newDayNode.children = [];
	            		
	            		for( raceIndex=0;raceIndex<day.races.length;raceIndex++ )
	            		{
	            			var race = day.races[raceIndex];
	            			var newRaceNode = {};

	            			if ( 	$scope.selectedRace != null 
	            					&& 
	            					$scope.selectedRace != undefined
	            					&&
	            					$scope.selectedRace.id == race.id )
	            			{
	            				selectedRaceNode = newRaceNode;
	            				dayNodeToExpand = newDayNode;
	            			}
	            			
	            			newRaceNode.label = race.fleet.fleetName+' - '+race.name;
	            			newRaceNode.data = race;
	            			newDayNode.children.push(newRaceNode);
	            		}
	            		
	            		$scope.race_tree_data.push(newDayNode);
	            	}
	            	
	                if ( $scope.race_tree_data.length > 0 )
	                {
	                	if ( $scope.selectedRace == undefined || $scope.selectedRace == null)
	                	{
		                	lastDayNode = $scope.race_tree_data[$scope.race_tree_data.length-1];
		                	selectedRaceNode = lastDayNode.children[0];
		                	dayNodeToExpand = lastDayNode; 
	                	}
	                	
	                	$scope.race_tree.expand_branch(dayNodeToExpand);
	                	$scope.race_tree.select_branch(selectedRaceNode);
	                }
	            });
    
    }
    
    $scope.race_tree_handler = function(branch) 
    {
    	var _race = branch.data;
    	if ( _race != null )
    	{
    		$scope.selectedRace = _race;
        	$scope.init(
        			'/Abbot3',
        			'/raceseries/'+$scope.raceSeriesID+'/race/'+_race.id+'/resultlist.json',
        			'/raceseries/'+$scope.raceSeriesID+'/race/'+_race.id+'/result.json/',
        			'views/raceresultform.html',
        			"Race Result");
    	}
    	else
    	{
    		//
    		//	So this is a race day - we want to toggle the expanded-ness when they click
    		//
    		$scope.race_tree.expand_branch(branch);
    	}
    };
    
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
        
        modalInstance.result.then( function()
        		{
        			$scope.loadPage($scope.page.number);
        		});
    }

    $scope.addNonStarters = function(race)
    {
        var modalInstance = $uibModal.open(
                {
                    animation:      true,
                    templateUrl:    'views/addnonstartersform.html',
                    controller:     'addNotStartersDialogInstanceController',
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
        
        modalInstance.result.then( function()
        		{
        			$scope.loadPage($scope.page.number);
        		});
    }
});

lastStartTime = null;

angular.module("abbot").controller("raceResultDialogInstanceController",function(
		$scope, $http, $controller, $uibModalInstance,$rootScope,object,context,resource,race )
{
	$scope.race = race;
	
	if ( object.startTime != undefined )
	{
		object.startTime = new Date(object.startTime);
	}

	if ( object.finishTime != undefined )
	{
		object.finishTime = new Date(object.finishTime);
	}

	if ( object.startTime == null || object.startTime == undefined )
	{
		if ( lastStartTime != null )
		{
			object.startTime = lastStartTime;
		}
	}
	
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

	$scope.handicaps = [];
	
	$http.get(context+'/raceseries/'+race.raceSeriesId+'/fleet/'+race.fleet.id+'/'+race.id+'/handicaplist.json').then(
			function(response) 
			{
				$scope.handicaps=response.data;
			});
	
	$scope.onBoatChanged = function()
	{
		$scope.object.handicap = $scope.findHandicapForBoat($scope.object.boat.id);
	}
	
	$scope.revertHandicap = function()
	{
		if ( ! $scope.object.overrideHandicap )
		{
			$scope.object.handicap = $scope.findHandicapForBoat($scope.object.boat.id);
		}
	}
	
	$scope.findHandicapForBoat = function(boatId)
	{
		for (var i=0;i<$scope.handicaps.length;i++)
		{
			if ( $scope.handicaps[i].boatID == boatId )
			{
				// Urgh... This is horrible. Fix me...
				if ( $scope.race.shortCourseRace )
				{
					return $scope.handicaps[i].value/2.0;
				}
				else
				{
					return $scope.handicaps[i].value;
				}
			}
		}
		return 0;
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
    
    //
    //	Override onOk to save the startTime selected for next time
    //
    $scope.parentok = $scope.ok;
    
    $scope.ok = function()
    {
    	if ( $scope.object.startTime != null && $scope.object.startTime != undefined )
    	{
    		lastStartTime = $scope.object.startTime;
    	}
    	
    	return $scope.parentok();
    }
});

function str_pad_left(string,pad,length) 
{
    return (new Array(length+1).join(pad)+string).slice(-length);
}

angular.module("abbot").filter('durationToHHMMSS',function() 
{
	
	return function(duration)
	{
		if ( duration == undefined || duration == 0 )
		{
			return '';
		}
		
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
	$scope.addDNSBoats = true;
	$scope.raceId = object.id;
	$scope.raceSeriesId = object.raceSeriesId;
	$scope.updateHandicaps = true;
	
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

    $scope.resultStatusForNonStarters = "DNS";
    
    $scope.resultStatusValues = 
    	[	{
            	id: 'DNS',
            	label: 'DNS',
          	}, 
    		{
              id: 'DNC',
              label: 'DNC',
            }];

    $scope.ok = function () 
    {
    	var url = $scope.context + '/raceseries/'+$scope.raceSeriesId+'/racestatus.json/'+$scope.raceId;

    	var statusUpdate = {
    			raceStatus:$scope.raceStatus,
    			addDNSBoats:$scope.addDNSBoats,
    			resultStatusForNonStarters:$scope.resultStatusForNonStarters,
    			updateHandicaps:$scope.updateHandicaps};
    	
        $http.post(url,statusUpdate).then(
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

angular.module("abbot").controller("addNotStartersDialogInstanceController",function(
		$scope, $http, $controller, $uibModalInstance, $rootScope, object, context )
{
	$scope.race = object;
	$scope.raceId = object.id;
	$scope.raceSeriesId = object.raceSeriesId;
	$scope.resultStatus = "DNS";
		
	$scope.context = context;
	
    $scope.resultStatusValues = 
    	[	{
            	id: 'DNS',
            	label: 'DNS',
          	}, 
    		{
              id: 'DNC',
              label: 'DNC',
            }];

    $scope.ok = function () 
    {
    	var url = $scope.context + '/raceseries/'+$scope.raceSeriesId+'/race/'+$scope.race.id+'/addnonstarters.json';
    	
        $http.post(url,$scope.resultStatus).then(
                function(response)
                {
                	$uibModalInstance.close();
                });
    };

    $scope.cancel = function () 
    {
        $uibModalInstance.dismiss('cancel');
    };

});
