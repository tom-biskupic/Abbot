//
//  In order to not duplicate the server-side validation (and to not duplicate it
//  on the client side) this controller provides code to manage dialogs for editing
//  objects
//
//  The code provides methods to capture errors from the server side and to display
//  those errors within the html
//

angular.module("abbot").controller("dialogController",function ($scope, $uibModal,$http,$q) 
{
    $scope.editNew = function(context,resource,template,object,controller)
    {
        if ( controller == undefined )
        {
            controller = 'dialogInstanceController';
        }
        return $scope.openDialog(context,resource,template,object,controller);
    }
    
    $scope.edit = function(context,resource,template,id,controller)
    {
        var deferred = $q.defer();

        if ( controller == undefined )
        {
            controller = 'dialogInstanceController';
        }
        
        $http.get(context + resource + "/"+ id).success(function(response)
        {
            $scope.openDialog(context,resource,template,response,controller).then(
                    function() 
                    { 
                        deferred.resolve() 
                    } );
        });
        
        return deferred.promise;
    }
    
    $scope.openDialog = function(context,resource,template,object,controller)
    {
        var modalInstance = $uibModal.open(
                {
                    animation:      true,
                    templateUrl:    template,
                    controller:     controller,
                    resolve: {
                      object: function () 
                      {
                        return object;
                      },
                      context: function () 
                      {
                        return context;
                      },
                      resource: function () 
                      {
                        return resource;
                      }
                    }
                });
        
        return modalInstance.result;
    }
});

angular.module("abbot").controller("dialogInstanceController",function($scope, $http, $uibModalInstance,object,context,resource )
{
    $scope.formErrors = {};
    $scope.context = context;
    $scope.resource = resource;
    $scope.object = object;
    
    $scope.hasError = function(fieldName)
    {
        if (typeof ($scope.formErrors) != 'undefined')
        {
            return fieldName in $scope.formErrors;
        } 
        else
        {
            return false;
        }
    }

    $scope.getError = function(fieldName)
    {
        if (typeof ($scope.formErrors) != "undefined" && fieldName in $scope.formErrors)
        {
            return $scope.formErrors[fieldName];
        } 
        else
        {
            return "";
        }
    }

    $scope.ok = function () 
    {
        $scope.formErrors = [];

        $http.post($scope.context + $scope.resource,$scope.object).success(
            function(response)
            {
                if (response.status == "SUCCESS")
                {
                    $uibModalInstance.close();
                } 
                else
                {
                    for (i = 0; i < response.errorMessageList.length; i++)
                    {
                        $scope.formErrors[response.errorMessageList[i].field] = response.errorMessageList[i].defaultMessage;
                    }
                }
            });
    };

    $scope.cancel = function () 
    {
        $uibModalInstance.dismiss('cancel');
    };
});
