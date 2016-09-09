abbotModule.controller("boatController",function($scope,$http,$controller)
{
    angular.extend(this,$controller('listController', {$scope: $scope})); 
    
    $scope.newObject = function()
    {
        object = {};
        $scope.editNew(
                $scope.contextPath,
                $scope.itemResource,
                $scope.editDialogId,
                object,
                "boatDialogInstanceController").then(
                        function() 
                        { 
                            $scope.loadPage($scope.page.number) 
                        });;
    }

    $scope.editObject = function(id)
    {
        $scope.edit(
                $scope.contextPath,
                $scope.itemResource,
                $scope.editDialogId,id,
                "boatDialogInstanceController").then(
                        function() 
                        { 
                            $scope.loadPage($scope.page.number) 
                        });;
    }
});

angular.module("abbot").controller("boatDialogInstanceController",function($scope, $http, $controller, $uibModalInstance,object,context,resource )
{
    angular.extend(
            this,
            $controller(    
                    'dialogInstanceController', 
                    {$scope: $scope, $http: $http, $uibModalInstance: $uibModalInstance, object: object, context: context,resource: resource}));

    
    $http.get(context+'/raceseries/'+${raceSeries.id}+'/boatclasslist.json/all').then(
            function(response) 
            { 
                $scope.boatClasses = response.data
            });    
});

