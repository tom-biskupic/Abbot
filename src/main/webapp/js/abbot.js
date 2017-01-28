//
//  This file is here just to provide a single place where the abbot application
//  module is defined
//
angular.module("abbot", 
		[	'ui.bootstrap',
		 	'ngAnimate',
		 	'ngRoute',
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
				templateUrl: 	'/Abbot3/views/loginform.html',
				controller: 	'loggedOnUserController'
			}).when(
			'/',
			{
				templateUrl: 	'/Abbot3/views/welcome.html',
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
