
abbotModule.controller("competitionController",function($scope,$http,$controller)
{
    angular.extend(this,$controller('listController', {$scope: $scope}));
    
    $scope.editObject = function(id)
    {
        $scope.edit(
                $scope.contextPath,
                $scope.itemResource,
                $scope.editDialogId,id,
                "competitionDialogInstanceController").then(
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
                "competitionDialogInstanceController").then(
                        function() 
                        { 
                            $scope.loadPage($scope.page.number) 
                        });;
    }

});

angular.module("abbot").controller("competitionDialogInstanceController",function($scope, $http, $controller, $uibModalInstance,object,context,resource )
{
    angular.extend(
            this,
            $controller(    
                    'dialogInstanceController', 
                    {$scope: $scope, $http: $http, $uibModalInstance: $uibModalInstance, object: object, context: context,resource: resource}));

    $http.get(context+'/raceseries/'+${raceSeries.id}+'/fleetlist.json/all').then(
        function(response) 
        { 
            $scope.fleets = response.data
        });

    $scope.pointsSystemValues = 
    [   {
          id: 'LOW_POINTS',
          label: 'Low Points System',
        }, 
        {
          id: 'BONUS_POINTS',
          label: 'Bonus Points System'
        }
    ];
    
    $scope.resultTypeValues = 
    [   {
          id: 'SCRATCH_RESULT',
          label: 'Use scratch placings',
        }, 
        {
          id: 'HANDICAP_RESULT',
          label: 'Use handicap placings'
        }
    ];

});