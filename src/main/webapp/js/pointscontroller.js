angular.module("abbot").controller("pointsController",function($scope,$http,$controller,$rootScope)
{
    $scope.raceSeriesId = $rootScope.seriesID;
    $scope.context = '';
    
    $http.get($scope.context+'/raceseries/'+$scope.raceSeriesId+'/competitionlist.json/all').then(
            function(response) 
            { 
                $scope.competitions = response.data
            });
    
    $scope.displayPointsTable = function(competition)
    {
    	url = $scope.context+'/raceseries/'+$scope.raceSeriesId+'/pointstable.json/'+competition.id;
    	
    	$http.get(url).then(
    			function(response)
    			{
    				$scope.pointsTable = response.data;
    			});
    }
});
