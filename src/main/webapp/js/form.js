angular.module("abbot").controller("formController",function($scope, $http, $q, object,context,resource)
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

    $scope.postUpdate = function()
    {
    	var deferred = $q.defer();
    	
        $scope.formErrors = [];

        $http.post($scope.context + $scope.resource,$scope.object).then(
            function(response)
            {
                if (response.data.status == "SUCCESS")
                {
                    return deferred.resolve();
                } 
                else
                {
                    for (i = 0; i < response.data.errorMessageList.length; i++)
                    {
                        $scope.formErrors[response.data.errorMessageList[i].field] = response.data.errorMessageList[i].defaultMessage;
                    }
                }
            });
        
        return deferred.promise;
    };
});
