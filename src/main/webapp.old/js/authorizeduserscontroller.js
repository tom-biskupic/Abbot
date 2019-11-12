angular.module("abbot").controller("authorizedUsersController",function($scope,$http,$controller,$rootScope)
{
	angular.extend(this,$controller('listController', {$scope: $scope}));	
	
	$scope.raceSeriesID = $rootScope.seriesID;

	$scope.init('/Abbot3','/raceseries/'+$scope.raceSeriesID+'/authorizeduserlist.json','/raceseries/'+$scope.raceSeriesID+'/authorizeduser.json','views/authorizeduserform.html')
	
});
