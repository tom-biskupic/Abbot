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
        
        $http.get(context + resource + "/"+ id).then(function(response)
        {
            $scope.openDialog(context,resource,template,response.data,controller).then(
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

angular.module("abbot").controller("dialogInstanceController",function($scope, $http, $uibModalInstance,$controller,object,context,resource )
{
    angular.extend(this,$controller('formController', {$scope: $scope,object : object, context: context, resource: resource }));

    $scope.ok = function () 
    {
    	$scope.postUpdate().then(function() { $uibModalInstance.close();});
    };

    $scope.cancel = function () 
    {
        $uibModalInstance.dismiss('cancel');
    };
});
