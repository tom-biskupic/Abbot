abbotModule.controller("authorizedUsersController",function($scope,$http,$controller)
{
	angular.extend(this,$controller('listController', {$scope: $scope}));	
});
