abbotModule.controller("raceController",function($scope,$http,$controller)
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
                "raceDialogInstanceController").then(
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
                "raceDialogInstanceController").then(
                        function() 
                        { 
                            $scope.loadPage($scope.page.number) 
                        });;
    }
});

angular.module("abbot").controller("raceDialogInstanceController",function($scope, $http, $controller, $uibModalInstance,object,context,resource )
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

    $http.get(context+'/raceseries/'+${raceSeries.id}+'/competitionlist.json/all').then(
            function(response) 
            { 
                $scope.competitions = response.data
            });

    $scope.dateOptions = 
    {
    	    formatYear: 'yy',
    	    maxDate: new Date(2020, 5, 22),
    	    minDate: new Date(),
    	    startingDay: 1
    };
    
    $scope.raceDatePopup = 
    {
    	    opened: false
    };
    
    $scope.raceDatePopupOpen = function() 
    {
        $scope.raceDatePopup.opened = true;
    };

    $scope.addCompetition = function(competition)
    {
        if ( $scope.object.competitions === undefined )
        {
            $scope.object.competitions = new Array();
        }
        
    	$scope.object.competitions.push(competition)
    };
    
    $scope.removeCompetition = function(competitionToRemove)
    {
        for( var i=0;i< $scope.object.competitions.length;i++ )
        {
            if ( $scope.object.competitions[i] == competitionToRemove )
            {
                $scope.object.competitions.splice(i,1);
                return;
            }
        }
    }

});

