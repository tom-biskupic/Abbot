
angular.module('abbot.ManageUsersController', ['ngRoute']).config(function($routeProvider) 
{
	$routeProvider.when(
			'/manageusers', 
			{
				templateUrl: 'views/manageusers.html',
				controller: 'ManageUsersController' 
			});
});

angular.module("abbot").controller("ManageUsersController",function($scope,$controller,$http)
{
    angular.extend(this,$controller('listController', {$scope: $scope}));
	
    $scope.init(
    		'/Abbot3',
    		'/userlist.json',
    		'/user.json',
    		'views/editusermodal.html',
    		'User');
    
    $scope.passwordConfirm = "";
    
    $scope.setDialogController('userDialogInstanceController');
});

angular.module("abbot").controller("userDialogInstanceController",function($scope, $http, $controller, $uibModalInstance,object,context,resource )
{
    angular.extend(this,$controller('dialogInstanceController', {$scope: $scope, $http: $http, $uibModalInstance: $uibModalInstance, object: object, context: context,resource: resource}));
    $scope.passwordConfirm=$scope.object.password

    $scope.originalOk = $scope.ok;
    
    $scope.ok = function () 
    {
        if ( $scope.object.password != $scope.passwordConfirm )
        {
            $scope.formErrors["passwordConfirm"] = "Passwords do not match";
        }
        else
        {
            $scope.originalOk();
        }
    }
});
