
abbotModule.controller("raceSeriesController",function($scope,$http,$controller)
{
    angular.extend(this,$controller('listController', {$scope: $scope}));
    
    $scope.newObject = function()
    {
        object = {};
        $scope.editNew(
                $scope.contextPath,
                $scope.itemResource,
                $scope.editDialogId,object,
                "raceSeriesDialogInstanceController").then(
                        function() 
                        { 
                            $scope.loadPage($scope.page.number) 
                        });;
    }

});

angular.module("abbot").controller("raceSeriesDialogInstanceController",function($scope, $http, $controller, $uibModalInstance,object,context,resource )
{
    angular.extend(
            this,
            $controller(    
                    'dialogInstanceController', 
                    {$scope: $scope, $http: $http, $uibModalInstance: $uibModalInstance, object: object, context: context,resource: resource}));

    $scope.seasonTypeValues = 
        [   {
              id: 'SEASON',
              label: 'Season',
            }, 
            {
              id: 'REGATTA',
              label: 'Regatta'
            }
        ];
});