//
//	Lots of this site ends up coming down to managing a table of items and being able
//	to add items to the list, remove or edit items in the list. This controller can 
//	be extended to handle all of these so long as they conform with a simple pattern
//
//	contextPath is the base URL used to access data via REST
//	listResource is the resource used to get lists of items at a time (i.e. '/userlist.json')
//	itemResource is the resourced used to either get details of a particular item or to post updates
//	editDialogId is the ID of the modal edit dialog for items in the list
//
var abbotModule = angular.module("abbot").controller("listController",function($scope, $controller, $http)
{
    angular.extend(this,$controller('dialogController', {$scope: $scope}));
    
    $scope.init = function(contextPath, listResource,itemResource, editDialogId)
    {
        $scope.contextPath = contextPath;
        $scope.listResource = listResource;
        $scope.itemResource = itemResource;
        $scope.editDialogId = editDialogId;

        if ($scope.page)
        {
            page = $scope.page.page;
            size = $scope.page.size;
        } 
        else
        {
            page = 0;
            size = 3;
        }

        $scope.loadPage(0);
        $scope.object = {};
    }

    $scope.loadPage = function(pageNum)
    {
        if (pageNum >= 0 && ($scope.page == undefined || pageNum < $scope.page.totalPages || pageNum == 0))
        {
            listURL = $scope.contextPath + $scope.listResource + "?page=" + pageNum + "&size=" + size;
            
            if ( $scope.isSorted() )
            {
                listURL += "&sort="+ $scope.sortColumn+","+$scope.sortDir;
            }
            
            $http.get(listURL).then(
                            function(response)
                            {
                                $scope.page = response.data;
                                $scope.pageArray = [];

                                for (var i = 0; i < $scope.page.totalPages; i++)
                                {
                                    $scope.pageArray
                                            .push(i + 1);
                                }
                            });
        }
    }

    $scope.editObject = function(id)
    {
        $scope.edit($scope.contextPath,$scope.itemResource,$scope.editDialogId,id).then(
                function() { $scope.loadPage($scope.page.number) });
    }

    $scope.newObject = function()
    {
        $scope.object = {};
        $scope.editNew($scope.contextPath,$scope.itemResource,$scope.editDialogId,$scope.object).then(
                function() { $scope.loadPage($scope.page.number) });
    }

    $scope.deleteObject = function(id)
    {
        $http.delete($scope.contextPath+$scope.itemResource+"/"+id).then(
                function(response) { $scope.loadPage($scope.page.number) });
    }
    
    $scope.getSortClass = function(columnName)
    {
        if ( $scope.sortColumn == undefined || $scope.sortColumn != columnName )
        {
            return "fa-sort";
        }
        else if ( $scope.sortDir == "asc" )
        {
            return "fa-sort-asc";
        }
        else if ( $scope.sortDir == "desc" )
        {
            return "fa-sort-desc";
        }
    }
    
    $scope.sortBy = function(columnName)
    {
        if ( $scope.sortColumn == undefined || $scope.sortColumn != columnName )
        {
            $scope.sortColumn = columnName; 
            $scope.sortDir = "asc"; 
        }
        else
        {
            if ( $scope.sortDir == "asc" )
            {
                $scope.sortDir = "desc";
            }
            else
            {
                $scope.sortDir = "asc";
            }
        }
        $scope.loadPage($scope.page.number)
    }
    
    $scope.isSorted = function()
    {
        return ( $scope.sortColumn != undefined && $scope.sortColumn != "");
    }
    
}).config(function($httpProvider)
{
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $httpProvider.defaults.headers.common[header] = token;
    
}).directive("tableListNav", function () 
{
    return  {
        restrict: 'E',
        replace: false,
        template: '\
            <ul class="pagination" ng-show="page.totalPages>1">\
                <li ng-class="{disabled: page.number === 0}">\
                    <a aria-label="Previous" ng-click="loadPage(page.number-1)">\
                        <span aria-hidden="true">&laquo;</span>\
                    </a>\
                </li>\
                <li ng-repeat="pageNum in pageArray" ng-class="{active: page.number === (pageNum-1)}">\
                    <a ng-click="loadPage(pageNum-1)">{{pageNum}}</a>\
                </li>\
                <li ng-class="{disabled: page.number >= (page.totalPages-1)}">\
                    <a aria-label="Next" ng-click="loadPage(page.number+1)">\
                        <span aria-hidden="true">&raquo;</span>\
                    </a>\
                </li>\
            </ul>'
    };
}).directive("sortHeader",function()
{
    return  {
        restrict: 'A',
        transclude: true,
        template: function(element,attrs)
        {
            var columnName= attrs.columnName;
            var columnHeading= attrs.columnHeading;
            htmlText = '<a ng-click="sortBy(\''+columnName+'\')" >'+columnHeading+'</a><i class="fa fa-fw ng-class: getSortClass(\''+columnName+'\')" />';
            return htmlText;
        }
    };
});
