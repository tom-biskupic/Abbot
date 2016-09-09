
describe("List controller tests", function() 
{
    // Arrange
    var mockScope;
    var controller;
    var backEnd;

    var context = "/Abbot";
    var listResource = "/userlist.json";
    var objectResource = "/user.json";
    var dialogID = "#editUserModal";
    var objectId = 1;
    
    beforeEach(angular.mock.module("abbot"));
    
    beforeEach(angular.mock.inject(function ($controller, $rootScope, $http, $httpBackend) 
    {
        mockScope = $rootScope.$new();
        controller = $controller("listController", 
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

    function setupMockGet(backEnd,page)
    {
        backEnd.expectGET(context+listResource+'?page='+page+'&size=3').respond(
                { 'page': page, 
                  'totalPages': 3,
                  'content': [ { 'id' : 123, 'email': 'fred@nowhere.com' } ]
                } );
    }
    
    it("will use URLs provided", function() 
    {
        setupMockGet(backEnd,0)
        mockScope.init(context,listResource,objectResource,dialogID);
        backEnd.flush();
        
        expect(mockScope.contextPath).toBe(context);
        expect(mockScope.listResource).toBe(listResource);
        expect(mockScope.itemResource).toBe(objectResource);
        expect(mockScope.editDialogId).toBe(dialogID);
    });
    
    it("will setup the pagearray and content from the back-end data", function()
    {
        setupMockGet(backEnd,0)
        mockScope.init(context,listResource,objectResource,dialogID);
        backEnd.flush();
        
        for (var i = 0; i < mockScope.page.totalPages; i++)
        {
            expect(mockScope.pageArray[i]).toBe(i+1);
        }
        
        expect(mockScope.page.content[0].id).toBe(123)
    });
    
    it("will load the nth page of results",function()
    {
        setupMockGet(backEnd,0)
        mockScope.init(context,listResource,objectResource,dialogID);
        backEnd.flush();

        setupMockGet(backEnd,2)
        mockScope.loadPage(2)
        backEnd.flush();
        
        for (var i = 0; i < mockScope.page.totalPages; i++)
        {
            expect(mockScope.pageArray[i]).toBe(i+1);
        }
        
        expect(mockScope.page.content[0].id).toBe(123)
    });
    
    it("will delete the object on the server",function()
    {
        setupMockGet(backEnd,0)
        mockScope.init(context,listResource,objectResource,dialogID);
        backEnd.flush();

        backEnd.expectDELETE(context+objectResource+'/'+objectId).respond(
                { 'status': 'SUCCESS' } );

        spyOn(mockScope,'loadPage');
        
        mockScope.deleteObject(objectId);
        backEnd.flush();
        
        expect(mockScope.loadPage).toHaveBeenCalled();
    });
    
//    it("will load the object data and invoke the edit dialog when editObject is called",function()
//    {
//        setupMockGet(backEnd,0)
//        mockScope.init(context,listResource,objectResource,dialogID);
//        backEnd.flush();
//
//        var dialog = { 'modal' : function() { } }
//        
//        var jqueryVal = spyOn($.fn,"init").and.returnValue(dialog);
//        var modal = spyOn(dialog,'modal');
//        
//        backEnd.expectGET(context+objectResource+'/123').respond(
//                { 'id' : 123, 'email' : 'fred@nowhere.com' });
//                
//        mockScope.editObject(123);
//        backEnd.flush();
//        
//        expect(dialog.modal).toHaveBeenCalled();
//    });        
});

