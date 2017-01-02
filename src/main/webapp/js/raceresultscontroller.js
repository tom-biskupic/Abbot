
angular.module("abbot").controller("raceResultController",function($scope,$http,$controller)
{
    angular.extend(this,$controller('listController', {$scope: $scope})); 
}
