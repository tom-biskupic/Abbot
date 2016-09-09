<%@include file="/views/tagincludes.jsp" %>

<core:set var="navPage" scope="session" value="manageUsers"/>

<!DOCTYPE html>
<html lang="en">
	<%@include file="/views/header.jsp" %>

  	<body 	ng-app="abbot" 
  			ng-controller="userListController" 
  			ng-init="init('${pageContext.request.contextPath}','/userlist.json','/user.json','editUserModal')">
	 	<%@include file="/views/navbar.jsp" %>
	
		<div class="container">

			<div class="btn-group pull-right" role="group" aria-label="...">
  				<button type="button" class="btn btn-default" ng-click="newObject()">
  					<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
  				</button>
  				<button type="button" class="btn btn-default" ng-click="loadPage(page.number)">
  					<span class="glyphicon glyphicon-refresh" aria-hidden="true"></span>
  				</button>
 			</div>
			
			<table name="page.content" class="table table-striped" >
				<thead>
					<tr>
						<td>Email</td>
						<td>Administrator</td>
						<td>First Name</td>
						<td>Last Name</td>
						<td>Organisation</td>
					</tr>
				</thead>
				<tbody>
					<tr ng-repeat="user in page.content">
						<td>
							<a ng-click="editObject(user.id)">{{user.email}}</a>
						</td>
						<td>
							<span ng-class="['glyphicon', user.administrator ? 'glyphicon-ok' : 'glyphicon-remove']" aria-hidden="true"></span>
						</td>
						<td>{{user.firstName}}</td>
						<td>{{user.lastName}}</td>
						<td>{{user.organisation}}</td>
					</tr>
				</tbody>  				 
			</table>
			<table-list-nav />	
    	</div> <!-- /container -->				

		<script type="text/ng-template" id="editUserModal">
			<div class="modal-header">
  				<button type="button" class="close" data-dismiss="modal" ng-click="cancel()">
  					<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
				</button>
    			<h4 class="modal-title" id="myModalLabel">User</h4>
  			</div>
	        <div class="modal-body">
				<div class="form-group">
					<label for="userFirstName">First name<span class="required">*</span></label>
					<input class="form-control" ng-model="object.firstName" name="firstName" />
					<span class="help-inline" ng-show="hasError('firstName')">{{getError("firstName")}}</span>
				</div>
				<div class="form-group">
					<label for="lastName">Second name<span class="required">*</span></label>
					<input class="form-control" ng-model="object.lastName" />
					<span class="help-inline" ng-show="hasError('lastName')">{{getError("lastName")}}</span>
				</div>
				<div class="form-group">
					<label for="email">Email address<span class="required">*</span></label>
					<input class="form-control" ng-model="object.email" />
					<span class="help-inline" ng-show="hasError('email')">{{getError("email")}}</span>
				</div>
				<div class="form-group">
					<label for="organisation">Organisation/Club</label>
					<input path="organisation" class="form-control" ng-model="object.organisation" />
				</div>
				<div class="checkbox">
					<label>
						<input type="checkbox" ng-model="object.administrator"> Administrator
					</label>
				</div>
				<div class="form-group">
					<label for=password">Password<span class="required">*</span></label>
					<input type="password" class="form-control" ng-model="object.password" />
					<span class="help-inline" ng-show="hasError('password')">{{getError("password")}}</span>
				</div>
				<div class="form-group">
					<label for="passwordConfirm">Password confirm<span class="required">*</span></label>
					<input type="password" class="form-control" ng-model="passwordConfirm" />
					<span class="help-inline" ng-show="hasError('passwordConfirm')">{{getError("passwordConfirm")}}</span>
				</div>			  
	        </div>
	        <div class="modal-footer">
	            <button class="btn btn-primary" type="button" ng-click="ok()">OK</button>
	            <button class="btn btn-warning" type="button" ng-click="cancel()">Cancel</button>
	        </div>
	    </script>
	    
	 	<%@include file="/views/includejs.jsp" %>
	 	<script>
	 		<%@include file="/js/abbot.js" %>
	 		<%@include file="/js/dialog.js" %>
	 		<%@include file="/js/listcontroller.js" %>
	 		<%@include file="/js/usercontroller.js" %>
 		</script>
	</body>
</html>
