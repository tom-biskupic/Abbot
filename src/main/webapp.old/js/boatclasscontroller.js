
angular.module("abbot").controller("boatClassController",function($scope,$http,$controller,$rootScope)
{
    angular.extend(this,$controller('listController', {$scope: $scope}));
    
	$scope.raceSeriesID = $rootScope.seriesID;
	$scope.init(
			'/Abbot3',
			'/raceseries/'+$scope.raceSeriesID+'/boatclasslist.json',
			'/raceseries/'+$scope.raceSeriesID+'/boatclass.json',
			'views/boatclassform.html',
			'Boat Class');

    $scope.newBoatDivision = function(boatClassId)
    {
        $scope.object = {};
        $scope.editNew(
                $scope.contextPath,
                "/raceseries/"+$scope.raceSeriesID+"/boatclass.json/"+boatClassId+"/division.json",
                "views/boatdivisionform.html",
                $scope.object).then(
                        function() { $scope.loadPage($scope.page.number) });
    }
    
    $scope.editBoatDivision = function(boatClassId,divisionId)
    {
        $scope.edit(
                $scope.contextPath,
                "/raceseries/"+$scope.raceSeriesID+"/boatclass.json/"+boatClassId+"/division.json",
                "views/boatdivisionform.html",
                divisionId).then(
                        function() { $scope.loadPage($scope.page.number) });

    }

    $scope.deleteBoatDivision = function(boatClassId,divisionId)
    {
        $http.delete($scope.contextPath + "/raceseries/"+$scope.raceSeriesID+"/boatclass.json/"+boatClassId+"/division.json/"+divisionId).then(
                function(response)
                {
                    $scope.loadPage($scope.page.number);
                });
    }
});
