angular.module("abbot").controller("exportController",function($scope,$http,$controller,$rootScope)
{
    $scope.raceSeriesId = $rootScope.seriesID;
    $scope.context = '/Abbot3';
    
    $http.get($scope.context+'/raceseries/'+$scope.raceSeriesId+'/competitionlist.json/all').then(
            function(response) 
            { 
                $scope.competitions = response.data
            });

    $http.get($scope.context+'/raceseries/'+$scope.raceSeriesId+'/fleetlist.json/all').then(
            function(response) 
            { 
                $scope.fleets = response.data
            });

    $scope.exportPointsTable = function(competition)
    {
    	url = $scope.context+'/raceseries/'+$scope.raceSeriesId+'/exportPoints.json/'+competition.id;
    	
    	$http.get(url).then(
    			function(response)
    			{
			        var filename = competition.name+'.html';       
			        $scope.saveData(response.data,filename);
    			});
    }

    $scope.exportRaces = function(fleet)
    {
    	url = $scope.context+'/raceseries/'+$scope.raceSeriesId+'/exportRaces.json/'+fleet.id;
    	
    	$http.get(url).then(
    			function(response)
    			{
			        var filename = fleet.fleetName+'.html';       
			        $scope.saveData(response.data,filename);
    			});
    }

    $scope.saveData = function(data,filename)
    {
        var blob = new Blob([data], {type: 'text/html'});
        
        if (window.navigator && window.navigator.msSaveOrOpenBlob) 
        {
            window.navigator.msSaveOrOpenBlob(blob, filename);
        } 
        else
        {
            var e = document.createEvent('MouseEvents'),
            a = document.createElement('a');
            a.download = filename;
            a.href = window.URL.createObjectURL(blob);
            a.dataset.downloadurl = ['text/json', a.download, a.href].join(':');
            e.initEvent('click', true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
            a.dispatchEvent(e);
            // window.URL.revokeObjectURL(url); // clean the url.createObjectURL resource
        }
    	
    }
});
