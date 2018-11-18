angular.module("abbot").service("confirmService",function($uibModal)
{
	this.confirm = function(objectName)
	{
	    var modalInstance = $uibModal.open(
	            {
	                animation:      true,
	                templateUrl:    'views/confirm.html',
	                controller:     'confirmInstanceController',
	                resolve: {
	                  objectName: function () 
	                  {
	                    return objectName;
	                  },
	                }
	            });
		
	    return modalInstance.result;
	}
});

angular.module("abbot").controller("confirmInstanceController",function($scope, $uibModalInstance,objectName)
{
	$scope.objectName = objectName;
	
    $scope.ok = function () 
    {
    	$uibModalInstance.close();
    };

    $scope.cancel = function () 
    {
        $uibModalInstance.dismiss('cancel');
    };
});

