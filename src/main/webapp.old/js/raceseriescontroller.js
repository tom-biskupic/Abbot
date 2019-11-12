
angular.module('abbot.raceSeriesController', ['ngRoute']).config(function($routeProvider) 
{
	$routeProvider.when(
			'/raceseries/:seriesID', 
			{
				templateUrl: 'views/raceseries.html',
				controller: 'raceSeriesController' 
			});
});

angular.module("abbot").controller("raceSeriesController",function($scope,$http,$controller,$routeParams,$rootScope)
{
	//
	//	This is ugly but its too hard to pass around amongst
	//	all the controllers that need it
	//
	$rootScope.seriesID = $routeParams.seriesID;
	
    $http.get('/Abbot3/raceseries.json/'+$routeParams.seriesID).then(function(response)
    {
    	$scope.raceSeries = response.data;
    });
});
