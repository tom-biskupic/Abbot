
abbotModule.controller("boatClassController",function($scope,$http,$controller)
{
    angular.extend(this,$controller('listController', {$scope: $scope}));
    
    $scope.newBoatDivision = function(boatClassId)
    {
        $scope.object = {};
        $scope.editNew(
                $scope.contextPath,
                "/raceseries/${raceSeries.id}/boatclass.json/"+boatClassId+"/division.json",
                "editBoatClassDivisionModal",$scope.object).then(
                        function() { $scope.loadPage($scope.page.number) });
    }
    
    $scope.editBoatDivision = function(boatClassId,divisionId)
    {
        $scope.edit(
                $scope.contextPath,
                "/raceseries/${raceSeries.id}/boatclass.json/"+boatClassId+"/division.json",
                "editBoatClassDivisionModal",
                divisionId).then(
                        function() { $scope.loadPage($scope.page.number) });

    }

    $scope.deleteBoatDivision = function(boatClassId,divisionId)
    {
        $http.delete($scope.contextPath + "/raceseries/${raceSeries.id}/boatclass.json/"+boatClassId+"/division.json/"+divisionId).then(
                function(response)
                {
                    $scope.loadPage($scope.page.number);
                });
    }
});
