
abbotModule.controller("userListController",function($scope,$controller,$http)
{
    angular.extend(this,$controller('listController', {$scope: $scope}));
	
    $scope.passwordConfirm = "";
    
    $scope.editObject = function(id)
    {
        $scope.edit(
                $scope.contextPath,
                $scope.itemResource,
                $scope.editDialogId,id,
                "userDialogInstanceController").then(
                        function() 
                        { 
                            $scope.loadPage($scope.page.number) 
                        });;
    }

    $scope.newObject = function()
    {
        object = {};
        $scope.editNew(
                $scope.contextPath,
                $scope.itemResource,
                $scope.editDialogId,object,
                "userDialogInstanceController").then(
                        function() 
                        { 
                            $scope.loadPage($scope.page.number) 
                        });;
    }
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
