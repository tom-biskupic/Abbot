abbotModule.controller("handicapSettingsController",function($scope,$http,$controller,$rootScope)
{
    angular.extend(this,$controller('listController', {$scope: $scope}));
    
	$scope.raceSeriesID = $rootScope.seriesID;
    $scope.init('/Abbot3','/raceseries/'+$scope.raceSeriesID+'/handicaplimitlist.json','/raceseries/'+$scope.raceSeriesID+'/handicaplimit.json','views/handicaplimitform.html')
    
    $scope.setDialogController('handicapSettingsDialogInstanceController');
});

angular.module("abbot").controller("handicapSettingsDialogInstanceController",function($scope, $http, $controller, $uibModalInstance,$rootScope,object,context,resource )
{
    angular.extend(
            this,
            $controller(    
                    'dialogInstanceController', 
                    {
                    	$scope: $scope, 
                    	$http: $http, 
                    	$uibModalInstance: $uibModalInstance, 
                    	object: object, 
                    	context: context,
                    	resource: resource}));

    $scope.raceSeriesID = $rootScope.seriesID;
    
    $http.get(context+'/raceseries/'+$scope.raceSeriesID+'/fleetlist.json/all').then(
            function(response) 
            { 
                $scope.fleets = response.data;
            });

});
