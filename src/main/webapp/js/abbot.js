//
//  This file is here just to provide a single place where the abbot application
//  module is defined
//
angular.module("abbot", 
		[	'ui.bootstrap',
		 	'ngAnimate',
		 	'ngRoute',
		 	'angularBootstrapNavTree',
		 	'abbot.ManageUsersController',
		 	'abbot.raceSeriesListController',
		 	'abbot.raceSeriesController',
		 	'abbot.registerController',
		 	'abbot.registerSuccessfulController']).config(function($httpProvider,$routeProvider)
{
    //var token = $("meta[name='_csrf']").attr("content");
    //var header = $("meta[name='_csrf_header']").attr("content");

    //$httpProvider.defaults.headers.common[header] = token;
	
	$routeProvider.when(
			'/loginform',
			{
				templateUrl: 	'views/loginform.html',
				controller: 	'loggedOnUserController'
			}).when(
			'/',
			{
				templateUrl: 	'views/welcome.html',
				controller: 	'loggedOnUserController'
			}).when(
			'/contact',
			{
				templateUrl: 	'views/contact.html',
				controller: 	'loggedOnUserController'
			});
});

angular.module('abbot').run(function ($rootScope, $location) 
{
	// enumerate routes that don't need authentication
	var routesThatDontRequireAuth = ['/loginform','/','/contact','/About','/register','','/registersuccessful'];

	// check if current location matches route  
	var routeClean = function (route) 
	{
		for(var i in routesThatDontRequireAuth)
		{
			if ( route == routesThatDontRequireAuth[i] )
			{
				return true;
			}
		}
		return false;
	};

	$rootScope.$on('$routeChangeStart', function (event, next, current) 
	{
		// if route requires auth and user is not logged in
		if (!routeClean($location.url()) && !$rootScope.authenticated) 
		{
			// redirect back to login
			$location.path('/loginform');
		}
	});
});

var BUSY_DELAY = 500;
angular.module("abbot").config(function ($httpProvider) {
	  $httpProvider.interceptors.push('busyHttpInterceptor');
	})
	  .factory('busyHttpInterceptor', ['$q', '$timeout', function ($q, $timeout) {
	    var counter = 0;
	    return {
	      request: function (config) {
	        counter += 1;
	        $timeout(
	          function () {
	            if (counter !== 0) {
	              angular.element('#busy-overlay').show();
	            }
	          },
	          BUSY_DELAY);
	        return config;
	      },
	      response: function (response) {
	        counter -= 1;
	        if (counter === 0) {
	          angular.element('#busy-overlay').hide();
	        }
	        return response;
	      },
	      requestError: function (rejection) {
	        counter -= 1;
	        if (counter === 0) {
	          angular.element('#busy-overlay').hide();
	        }
	        return rejection;
	      },
	      responseError: function (rejection) {
	        counter -= 1;
	        if (counter === 0) {
	          angular.element('#busy-overlay').hide();
	        }
	        return rejection;
	      }
	    }
	  }]);

