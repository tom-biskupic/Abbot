
describe("Race controller tests", function() 
{
	beforeEach(angular.mock.module("abbot"));
	
    beforeEach(angular.mock.inject(function ($controller, $rootScope, $http, $httpBackend) 
    {
        mockScope = $rootScope.$new();
        controller = $controller("raceDialogInstanceController", 
        {
            $scope: mockScope,
            $http: $http
        });
        backEnd = $httpBackend;
    }));
    
	afterEach(function() {
        backEnd.verifyNoOutstandingExpectation();
        backEnd.verifyNoOutstandingRequest();
      });
	
	
    it("will not allow the user to add an empty selection", function() 
    {
    	mockScope.addCompetition(undefined)
    	expect(mockScope.object.competitions)
    });
}