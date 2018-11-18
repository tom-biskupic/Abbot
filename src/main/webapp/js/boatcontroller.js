angular.module("abbot").controller("boatController",function($scope,$http,$controller,$rootScope)
{
    angular.extend(this,$controller('listController', {$scope: $scope})); 
    
	$scope.raceSeriesID = $rootScope.seriesID;

    $scope.init(
    		'/Abbot3',
    		'/raceseries/'+$scope.raceSeriesID+'/boatlist.json',
    		'/raceseries/'+$scope.raceSeriesID+'/boat.json',
    		'views/boatform.html',
    		'Boat');
    
    $scope.setDialogController('boatDialogInstanceController');
});

angular.module("abbot").controller("boatDialogInstanceController",function($scope, $http, $controller, $uibModalInstance,$rootScope, object,context,resource )
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
});

