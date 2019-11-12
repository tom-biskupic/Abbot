
angular.module("abbot").controller("raceResultsController",function($scope,$http,$controller)
{
    angular.extend(this,$controller('listController', {$scope: $scope})); 
});
