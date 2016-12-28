'use strict';
describe("Race controller tests", function() 
{
    var mockScope;
    var uibModalInstance;
    var testObject = {};
    var testContext = "abbot"
    var testResource = "race.json"
    var controller
    var backEnd;
    var competition = { id : 1234, name  : "The Muppet's trophy", pointsSystem : 0, drops : 1, resultType : 1, fleet : {} };
    
	beforeEach(angular.mock.module("abbot"));
	

    beforeEach(angular.mock.inject(function ($controller, $rootScope, $http, $httpBackend) 
    {
        mockScope = $rootScope.$new();
        uibModalInstance = {};
        
        backEnd = $httpBackend;
        
        backEnd.expectGET(testContext+"/raceseries/"+"${raceSeries.id}/fleetlist.json/all").respond(
                { 'fleetList': [ ]
                } );

        backEnd.expectGET(testContext+"/raceseries/"+"${raceSeries.id}/competitionlist.json/all").respond(
                { 'fleetList': [ ]
                } );

        controller = $controller("raceDialogInstanceController", 
        {
            $scope: mockScope,
            $http: $http,
            $uibModalInstance: uibModalInstance,
            object : testObject,
            context : testContext,
            resource : testResource
        });
        backEnd.flush();
    }));
    
	afterEach(function() {
        backEnd.verifyNoOutstandingExpectation();
        backEnd.verifyNoOutstandingRequest();
      });
	
	beforeEach(function()
	{
		mockScope.object.competitions = [];
	});
	
    it("will allow the user to add a selection that is not already present", function() 
    {
    	mockScope.addCompetition(competition)
    	expect(mockScope.object.competitions[0]).toEqual(competition)
    });

    it("will not allow the user to add an empty selection", function() 
    {
    	mockScope.addCompetition(undefined)
    	expect(mockScope.object.competitions.length).toEqual(0);
    });
});
