abbotModule.controller("fleetController",function($scope,$http,$controller)
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
                "fleetDialogInstanceController").then(
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
                "fleetDialogInstanceController").then(
                        function() 
                        { 
                            $scope.loadPage($scope.page.number) 
                        });;
    }
});

angular.module("abbot").controller("fleetDialogInstanceController",function($scope, $http, $controller, $uibModalInstance,object,context,resource )
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
    
    $scope.addToFleet = function(boatClassToAdd,divisionToAdd)
    {
        var newFleetSelector = {};
        newFleetSelector.boatClass = boatClassToAdd;
        newFleetSelector.boatDivision = divisionToAdd;

        if ( $scope.object.fleetClasses === undefined )
        {
            $scope.object.fleetClasses = new Array();
        }
        
        $scope.object.fleetClasses.push(newFleetSelector);
    }
    
    $scope.removeFleetSelector = function(fleetSelectorToRemove)
    {
        for( var i=0;i< $scope.object.fleetClasses.length;i++ )
        {
            if ( $scope.object.fleetClasses[i] == fleetSelectorToRemove )
            {
                $scope.object.fleetClasses.splice(i,1);
                return;
            }
        }
    }
});
