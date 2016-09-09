<%@include file="/views/tagincludes.jsp" %>

<core:set var="activePage" scope="session" value="settings"/>

<!DOCTYPE html>
<html lang="en">
	<%@include file="/views/header.jsp" %>

  	<body ng-app="abbot" ng-controller="raceSeriesSettingsController" >
	 	<%@include file="/views/navbar.jsp" %>
	
		<div class="container">
		
			<%@include file="/views/raceseriestabs.jsp" %>

			<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
				<div class="panel panel-default">
			    	<div class="panel-heading" role="tab" id="boatClassesAccordian">
			      		<h4 class="panel-title">
			        		<a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseBoatClass" aria-expanded="false" aria-controls="collapseBoatClass">
			          		Boat Classes
			        		</a>
			      		</h4>
			    	</div>			
					<div id="collapseBoatClass" class="panel-collapse collapse" role="tabpanel" aria-labelledby="boatClassesAccordian">
	      				<div 
	      					class="panel-body"
	      					ng-controller="boatClassController"
							ng-init="init('${pageContext.request.contextPath}','/raceseries/'+${raceSeries.id}+'/boatclasslist.json','/raceseries/'+${raceSeries.id}+'/boatclass.json','editBoatClassModal')">
				  			<span class="btn-group pull-right" role="group" aria-label="...">
								<button type="button" class="btn btn-default" ng-click="newObject()">
						  			<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
								</button>
						  		<button type="button" class="btn btn-default" ng-click="loadPage(page.number)">
						  		<span class="glyphicon glyphicon-refresh" aria-hidden="true"></span>
						  		</button>
						 	</span>
			   				<p>Configure the list of boat classes competing in races that are part of this race series.</p>
			  						 		
					 		<table name="page.content" class="table table-striped" >
							<thead>
								<tr>
									<th>Class Name</th>
									<th>Divisions</th>
								</tr>
							</thead>
								<tbody>
									<tr ng-repeat="boatClass in page.content">
										<td>
											<button type="button" class="btn" ng-click="deleteObject(boatClass.id)" >
												<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
											</button>
											<a ng-click="editObject(boatClass.id)">{{boatClass.name}}</a>
										</td>
										<td>
											<button type="button pull-left" class="btn btn-default" ng-click="newBoatDivision(boatClass.id)">
						  						<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
						  					</button>						
											<ul class="list-unstyled" ng-repeat="division in boatClass.divisions">
												<li> 
													<button type="button" class="btn" ng-click="deleteBoatDivision(boatClass.id,division.id)" >
														<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
													</button>
													<a ng-click="editBoatDivision(boatClass.id,division.id)">{{division.name}}</a>
												</li>
											</ul>
										</td>
									</tr>
								</tbody>  				 
							</table>
							<table-list-nav />	
						</div>
					</div>
				</div>
				<div class="panel panel-default">
			    	<div class="panel-heading" role="tab" id="fleetAccordian">
			      		<h4 class="panel-title">
			        		<a role="button" data-toggle="collapse" data-parent="#accordion" href="#fleetCollapse" aria-expanded="false" aria-controls="fleetCollapse">
			          		Fleets
			        		</a>
			      		</h4>
			    	</div>			
					<div id="fleetCollapse" class="panel-collapse collapse" role="tabpanel" aria-labelledby="fleetAccordian">
                        <div 
                            class="panel-body"
                            ng-controller="fleetController"
                            ng-init="init('${pageContext.request.contextPath}','/raceseries/'+${raceSeries.id}+'/fleetlist.json','/raceseries/'+${raceSeries.id}+'/fleet.json','editFleetModal')">
                            <span class="btn-group pull-right" role="group" aria-label="...">
                                <button type="button" class="btn btn-default" ng-click="newObject()">
                                    <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
                                </button>
                                <button type="button" class="btn btn-default" ng-click="loadPage(page.number)">
                                <span class="glyphicon glyphicon-refresh" aria-hidden="true"></span>
                                </button>
                            </span>
                            <p>Collect the boat classes into fleets that sail together</p>
                                            
                            <table name="page.content" class="table table-striped" >
                            <thead>
                                <tr>
                                    <th>Fleet Name</th>
                                    <th>Classes</th>
                                </tr>
                            </thead>
                                <tbody>
                                    <tr ng-repeat="fleet in page.content">
                                        <td>
                                            <button type="button" class="btn" ng-click="deleteObject(fleet.id)" >
                                                <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                                            </button>
                                            <a ng-click="editObject(fleet.id)">{{fleet.fleetName}}</a>
                                        </td>
                                        <td>
                                        </td>
                                    </tr>
                                </tbody>                 
                            </table>
                            <table-list-nav />  
                        </div>
					</div>
				</div>
				<div class="panel panel-default">
                    <div class="panel-heading" role="tab" id="competitionAccordian">
                        <h4 class="panel-title">
                            <a role="button" data-toggle="collapse" data-parent="#accordion" href="#competitionCollapse" aria-expanded="false" aria-controls="competitionCollapse">
                            Competitions
                            </a>
                        </h4>
                    </div>          
                    <div id="competitionCollapse" class="panel-collapse collapse" role="tabpanel" aria-labelledby="competitionAccordian">
                        <div 
                            class="panel-body"
                            ng-controller="competitionController"
                            ng-init="init('${pageContext.request.contextPath}','/raceseries/'+${raceSeries.id}+'/competitionlist.json','/raceseries/'+${raceSeries.id}+'/competition.json','editCompetitionModal')">
                            <span class="btn-group pull-right" role="group" aria-label="...">
                                <button type="button" class="btn btn-default" ng-click="newObject()">
                                    <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
                                </button>
                                <button type="button" class="btn btn-default" ng-click="loadPage(page.number)">
                                <span class="glyphicon glyphicon-refresh" aria-hidden="true"></span>
                                </button>
                            </span>
                            <p>Defines a competition that we can generate points tables for</p>
                                            
                            <table name="page.content" class="table table-striped" >
                            <thead>
                                <tr>
                                    <th>Competition Name</th>
                                    <th>Fleet Name</th>
                                </tr>
                            </thead>
                                <tbody>
                                    <tr ng-repeat="competition in page.content">
                                        <td>
                                            <button type="button" class="btn" ng-click="deleteObject(competition.id)" >
                                                <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                                            </button>
                                            <a ng-click="editObject(competition.id)">{{competition.name}}</a>
                                        </td>
                                        <td>
                                            {{competition.fleet.fleetName}}
                                        </td>
                                    </tr>
                                </tbody>                 
                            </table>
                            <table-list-nav />  
                        </div>
                    </div>
                </div>
                <div class="panel panel-default">
                    <div class="panel-heading" role="tab" id="userAccordian">
                        <h4 class="panel-title">
                            <a role="button" data-toggle="collapse" data-parent="#accordion" href="#userCollapse" aria-expanded="false" aria-controls="userCollapse">
                            Authorized Users
                            </a>
                        </h4>
                    </div>          
                    <div id="userCollapse" class="panel-collapse collapse" role="tabpanel" aria-labelledby="userAccordian">
                        <div 
                            class="panel-body" 
                            ng-controller="authorizedUsersController" 
                            ng-init="init('${pageContext.request.contextPath}','/raceseries/'+${raceSeries.id}+'/authorizeduserlist.json','/raceseries/'+${raceSeries.id}+'/authorizeduser.json','addAuthorizedUserModal')">
                            <span class="btn-group pull-right" role="group" aria-label="...">
                                <button type="button" class="btn btn-default" ng-click="newObject()">
                                    <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
                                </button>
                                <button type="button" class="btn btn-default" ng-click="loadPage(page.number)">
                                <span class="glyphicon glyphicon-refresh" aria-hidden="true"></span>
                                </button>
                            </span>
                            <p>Configure the list of users permitted to manage this race series</p>
                                            
                            <table name="page.content" class="table table-striped" >
                            <thead>
                                <tr>
                                    <th>User Name</th>
                                    <th>Email address</th>
                                </tr>
                            </thead>
                                <tbody>
                                    <tr ng-repeat="user in page.content">
                                        <td>
                                            <button type="button" class="btn" ng-click="deleteObject(user.id)" ng-show="!user.currentUser">
                                                <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                                            </button>
                                            {{user.name}}
                                        </td>
                                        <td>
                                            {{user.emailAddress}}
                                        </td>
                                    </tr>
                                </tbody>                 
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
		
		<script type="text/ng-template" id="editBoatClassModal">
			<div class="modal-header">
  				<button type="button" class="close" data-dismiss="modal" ng-click="cancel()">
  					<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
				</button>
    			<h4 class="modal-title" id="myModalLabel">Boat class</h4>
  			</div>
			<div class="modal-body" >
				<div class="form-group">
					<label for="name">Name<span class="required">*</span></label>
					<input class="form-control" ng-model="object.name" name="name" autofocus/>
					<span class="help-inline" ng-show="hasError('name')">{{getError("name")}}</span>
				</div>
				<div class="form-group">
					<label for="yardstick">Yardstick<span class="required">*</span></label>
					<input class="form-control" ng-model="object.yardStick" name="yardstick" />
					<span class="help-inline" ng-show="hasError('yardStick')">{{getError("yardStick")}}</span>
				</div>
			</div>
			<div class="modal-footer">
	            <button class="btn btn-primary" type="button" ng-click="ok()">OK</button>
	            <button class="btn btn-warning" type="button" ng-click="cancel()">Cancel</button>
	        </div>
		</script>

		<script type="text/ng-template" id="editBoatClassDivisionModal">
	      	<div class="modal-header">
  				<button type="button" class="close" data-dismiss="modal" ng-click="cancel()">
  					<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
				</button>
    			<h4 class="modal-title" id="myModalLabel">Boat Class Division</h4>
  			</div>
			<div class="modal-body" >
				<div class="form-group">
					<label for="name">Name<span class="required">*</span></label>
					<input class="form-control" ng-model="object.name" name="name" autofocus/>
					<span class="help-inline" ng-show="hasError('name')">{{getError("name")}}</span>
				</div>
				<div class="form-group">
					<label for="yardStick">Yardstick<span class="required">*</span></label>
					<input class="form-control" ng-model="object.yardStick" name="yardStick" />
					<span class="help-inline" ng-show="hasError('yardStick')">{{getError("yardStick")}}</span>
				</div>
			</div>
			<div class="modal-footer">
	            <button class="btn btn-primary" type="button" ng-click="ok()">OK</button>
	            <button class="btn btn-warning" type="button" ng-click="cancel()">Cancel</button>
	        </div>
		</script>

        <script type="text/ng-template" id="editFleetModal">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" ng-click="cancel()">
                    <span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">Fleet</h4>
            </div>
            <div class="modal-body" >
                <div class="form-group">
                    <label for="name">Name<span class="required">*</span></label>
                    <input class="form-control" ng-model="object.fleetName" name="name" autofocus/>
                    <span class="help-inline" ng-show="hasError('fleetName')">{{getError("fleetName")}}</span>
                </div>
                <div class="form-group">
                    <div class="panel panel-default">
                        <div class="panel-heading">Classes in this Fleet</div>
                        <div class="panel-body">
                            <table class="table table-striped" >
                                <thead>
                                    <tr>
                                        <th>Boat Class</th>
                                        <th>Division</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr ng-repeat="fleetSelector in object.fleetClasses">
                                        <td>
                                            <button type="button" class="btn" ng-click="removeFleetSelector(fleetSelector)" >
                                                <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                                            </button>
                                            {{fleetSelector.boatClass.name}}</a>
                                        </td>
                                        <td>
                                            {{fleetSelector.boatDivision.name}}</a>
                                        </td>
                                    </tr>
                                </tbody>                 
                            </table>
                            <p class="help-block" ng-show="(object.fleetClasses === undefined || object.fleetClasses.length == 0)">No Boat classes selected</p>
                            <div class="container-fluid">
                                <div class="col-sm-4">
                                    <select name="boatClassToAdd" 
                                        class="form-control" 
                                        ng-model="boatClassToAdd" 
                                        ng-options="boatClass.name for boatClass in boatClasses track by boatClass.id"
                                        required>
                                    </select>
                                </div>
                                <div class="col-sm-4">
                                    <select name="divisionToAdd" 
                                        class="form-control" 
                                        ng-model="divisionToAdd" 
                                        ng-options="division.name for division in boatClassToAdd.divisions"
                                        required>
                                    </select>
                                </div>
                                <div class="col-sm-2 bottom-column">
                                    <button class="btn" type="button" ng-click="addToFleet(boatClassToAdd,divisionToAdd)">Add</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-primary" type="button" ng-click="ok()">OK</button>
                <button class="btn btn-warning" type="button" ng-click="cancel()">Cancel</button>
            </div>
        </script>

        <script type="text/ng-template" id="editCompetitionModal">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" ng-click="cancel()">
                    <span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">Competition</h4>
            </div>
            <div class="modal-body" >
                <div class="form-group">
                    <label for="name">Name<span class="required">*</span></label>
                    <input class="form-control" ng-model="object.name" name="name" autofocus/>
                    <span class="help-inline" ng-show="hasError('name')">{{getError("name")}}</span>
                </div>
                <div class="form-group">
                    <label for="fleet">Fleet<span class="required">*</span></label>
                    <select name="fleet" 
                        class="form-control" 
                        ng-model="object.fleet" 
                        ng-options="fleet.fleetName for fleet in fleets track by fleet.id"
                        required>
                    </select>
                </div>
                <div class="form-group">
                    <label for="pointsSystem">Points system<span class="required">*</span></label>
                    <select name="pointsSystem" 
                            class="form-control" 
                            ng-model="object.pointsSystem" 
                            ng-options="item.id as item.label for item in pointsSystemValues"
                            required>
                    </select>
                    <span class="help-inline" ng-show="hasError('pointsSystem')">{{getError("pointsSystem")}}</span>
                </div>
                <div class="form-group">
                    <label for="resultType">Result type to use<span class="required">*</span></label>
                    <select name="resultType" 
                            class="form-control" 
                            ng-model="object.resultType" 
                            ng-options="item.id as item.label for item in resultTypeValues"
                            required>
                    </select>
                    <span class="help-inline" ng-show="hasError('resultType')">{{getError("resultType")}}</span>
                </div>
                <div class="form-group">
                    <label for="fleetSize">Fleet Size<span class="required">*</span></label>
                    <input class="form-control" ng-model="object.fleetSize" name="fleetSize" autofocus/>
                    <span class="help-inline" ng-show="hasError('fleetSize')">{{getError("fleetSize")}}</span>
                </div>
                <div class="form-group">
                    <label for="drops">Number of drops<span class="required">*</span></label>
                    <input class="form-control" ng-model="object.drops" name="drops" autofocus/>
                    <span class="help-inline" ng-show="hasError('drops')">{{getError("drops")}}</span>
                </div>
                
            </div>
            <div class="modal-footer">
                <button class="btn btn-primary" type="button" ng-click="ok()">OK</button>
                <button class="btn btn-warning" type="button" ng-click="cancel()">Cancel</button>
            </div>
        </script>		 
		
		<script type="text/ng-template" id="addAuthorizedUserModal">
	      	<div class="modal-header">
  				<button type="button" class="close" data-dismiss="modal" ng-click="cancel()">
  					<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
				</button>
    			<h4 class="modal-title" id="myModalLabel">Add Authorized User</h4>
  			</div>
			<div class="modal-body" >
				<div class="form-group">
					<label for="emailAddress">Email Address<span class="required">*</span></label>
					<input class="form-control" ng-model="object.emailAddress" name="emailAddress" autofocus/>
					<span class="help-inline" ng-show="hasError('emailAddress')">{{getError("emailAddress")}}</span>
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
	 		<%@include file="/js/boatclasscontroller.js" %>
	 		<%@include file="/js/raceseriessettingscontroller.js" %>
	 		<%@include file="/js/fleetcontroller.js" %>
	 		<%@include file="/js/competitioncontroller.js" %>
	 		<%@include file="/js/authorizeduserscontroller.js" %>
 		</script>
	 		
	</body>
</html>
