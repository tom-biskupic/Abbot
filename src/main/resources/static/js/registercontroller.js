
angular.module('abbot.registerController', ['ngRoute']).config(function($routeProvider) 
{
	$routeProvider.when(
			'/register', 
			{
				templateUrl: 'views/registerform.html',
				controller: 'registerController' 
			});
});

angular.module("abbot").controller("registerController",function($scope,$controller,$http,$rootScope,$location)
{
	$scope.object = { }
	
    angular.extend(this,$controller('formController', {$scope: $scope, object : $scope.object, context: '', resource: '/register' }));
    
	$scope.submit = function()
	{
        if ( $scope.object.password != $scope.passwordConfirm )
        {
            $scope.formErrors["passwordConfirm"] = "Passwords do not match";
        }
        else
        {
        	$scope.postUpdate().then( function() { $location.path('/registersuccessful') } );
        }
	}
});
