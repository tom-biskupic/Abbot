angular.module("abbot").controller("loggedOnUserController",function($scope,$controller,$http,$rootScope,$location)
{
	$scope.userIsAdmin = function()
	{
		return $rootScope.isAdmin;
	}
	
	var authenticate = function(callback) 
	{
		$http.get('/user').then(function(response) 
		{
			$rootScope.isAdmin=false;
	
			if (response.data.name) 
			{
				$rootScope.authenticated = true;
				for(i=0;i<response.data.authorities.length;i++)
				{
					if ( response.data.authorities[i].authority == "ROLE_ADMIN" )
					{
						$rootScope.isAdmin=true;
					}
				}
			} 
			else 
			{
				$rootScope.authenticated = false;
			}
			callback && callback();
		},function(data) 
		{
			$rootScope.authenticated = false;
			callback && callback();
		});
	}
	
	authenticate();
	
	$scope.credentials = {};
	$scope.login = function() 
	{
		$http.post('/perform_login', $.param($scope.credentials), 
		{
	      headers : 
	      {
	        "content-type" : "application/x-www-form-urlencoded",
	      }
		}).then(function(data) 
        {
		    authenticate(function() 
		    {
			    if ($rootScope.authenticated) 
	            {
			    	$location.path("/");
			    	$scope.error = false;
	            }
			    else 
			    {
			    	$location.path("/loginform");
			    	$scope.error = true;
			    }
		    });
	    }, function(data) 
	    {
	    	$location.path("/loginform");
	    	$scope.error = true;
	    	$rootScope.authenticated = false;
	    });
	}
	
	$scope.logout = function() 
	{
		$http.post('logout', {}).finally(function() 
		{
		    $rootScope.authenticated = false;
			$rootScope.isAdmin=false;
		    $location.path("/");
		});
	}
});
