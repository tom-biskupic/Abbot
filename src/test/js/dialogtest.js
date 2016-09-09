'use strict';
describe("Modal Dialog tests", function() 
{
    var mockScope;
    var controller;
    var backEnd;

    var context = "/Abbot";
    var resource = "/user.json";
    var template = "modal.html";
    var modalController = 'testme';
    var defaultController = 'dialogInstanceController';
    var testID = '123';
    
    var object = {description:"This is an object"};
    var uibModalFake = {};
    var rootScope = {};
    
    beforeEach(module("abbot"));

    var fakeModal = 
    {
        result: 
        {
            then: function(confirmCallback, cancelCallback) 
            {
                //Store the callbacks for later when the user clicks on the OK or Cancel button of the dialog
                this.confirmCallBack = confirmCallback;
                this.cancelCallback = cancelCallback;
            }
        },
        close: function( item ) 
        {
            //The user clicked OK on the modal dialog, call the stored confirm callback with the selected item
            this.result.confirmCallBack( item );
        },
        dismiss: function( type ) 
        {
            //The user clicked cancel on the modal dialog, call the stored cancel callback
            this.result.cancelCallback( type );
        }
    };
    
    afterEach(function() 
    {
        backEnd.verifyNoOutstandingExpectation();
        backEnd.verifyNoOutstandingRequest();
        
        uibModalFake.open.calls.reset();
    });

    beforeEach(inject(function($uibModal) 
    {
        spyOn($uibModal, 'open').and.returnValue(fakeModal);
        uibModalFake = $uibModal;
    }));
    
    beforeEach(angular.mock.inject(function ($controller, $rootScope, $http, $httpBackend,_$uibModal_) 
    {
        mockScope = $rootScope.$new();
        controller = $controller("dialogController", 
        {
            $scope: mockScope,
            $http: $http,
            $uibModal: _$uibModal_
        });
        
        backEnd = $httpBackend;
        rootScope = $rootScope;
    }));

    function checkParams(expectedController)
    {
        expect(uibModalFake.open.calls.mostRecent().args[0].templateUrl).toBe(template);
        expect(uibModalFake.open.calls.mostRecent().args[0].animation).toBe(true);
        expect(uibModalFake.open.calls.mostRecent().args[0].controller).toBe(expectedController);
        
        // 
        //  As the http service returns a copy we use toEqual here
        //
        expect(uibModalFake.open.calls.mostRecent().args[0].resolve.object()).toEqual(object);
        
        expect(uibModalFake.open.calls.mostRecent().args[0].resolve.context()).toBe(context);
        expect(uibModalFake.open.calls.mostRecent().args[0].resolve.resource()).toBe(resource);
    }
    
    it("opendialog to call open and pass the correct params through", function()
    {
        var result = mockScope.openDialog(context,resource,template,object,controller);
        expect(uibModalFake.open).toHaveBeenCalled();
        expect(result).toBe(fakeModal.result);
        checkParams(controller);
    });
    
    it("editNew without controller calls openDialog with correct default controller", function()
    {
        var result = mockScope.editNew(context,resource,template,object);
        expect(uibModalFake.open).toHaveBeenCalled();
        expect(result).toBe(fakeModal.result);
        checkParams(defaultController);
    });

    it("editNew with controller passes it through", function()
    {
        var result = mockScope.editNew(context,resource,template,object,controller);
        expect(uibModalFake.open).toHaveBeenCalled();
        expect(result).toBe(fakeModal.result);
        checkParams(controller);
    });

    it("edit gets the object from the URL and then opens a dialog", function()
    {
        backEnd.expectGET(context+resource+'/'+testID).respond(object);

        var handler = jasmine.createSpy('success');
        var result = mockScope.edit(context,resource,template,testID,controller).then(handler);
        
        backEnd.flush();
        expect(uibModalFake.open).toHaveBeenCalled();
        
        fakeModal.close("success");
        
        // This step flushes the queue so the function is called
        rootScope.$apply();
        
        expect(handler).toHaveBeenCalled();
        checkParams(controller);
    });

});
