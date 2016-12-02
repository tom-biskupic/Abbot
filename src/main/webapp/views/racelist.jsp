<%@include file="/views/tagincludes.jsp" %>

<core:set var="activePage" scope="session" value="races"/>

<!DOCTYPE html>
<html lang="en">
	<%@include file="/views/header.jsp" %>

  	<body
		ng-app="abbot" 
		ng-controller="raceController" 
		ng-init="init('${pageContext.request.contextPath}','/raceseries/'+${raceSeries.id}+'/racelist.json','/raceseries/'+${raceSeries.id}+'/race.json','editRaceModal')">
  	
	 	<%@include file="/views/navbar.jsp" %>
	
		<div class="container">
			<%@include file="/views/raceseriestabs.jsp" %>
			
        <div class="container">     
            <h1>Races</h1>
    
            <div class="btn-group pull-right" role="group" aria-label="...">
                <button type="button" class="btn btn-default" ng-click="newObject()">
                    <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
                </button>
                <button type="button" class="btn btn-default" ng-click="loadPage(page.number)">
                    <span class="glyphicon glyphicon-refresh" aria-hidden="true"></span>
                </button>
            </div>
            
 			<table class="table table-striped" >
                <thead>
                    <tr>
                        <th sort-header column-name="raceDate" column-heading="Race Date"></th>
                        <th sort-header column-name="name" column-heading="Race Name"></th>
                        <th sort-header column-name="fleet.fleetName" column-heading="Fleet"></th>
                        <th sort-header column-name="raceStatus" column-heading="Status"></th>
                        <th></th>
                    </tr>
                </thead>
                <tr ng-repeat="race in page.content">
                    	<td>
                    		<button type="button" class="btn" ng-click="deleteObject(race.id)" >
                            	<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                            </button>
                            <a ng-click="editObject(race.id)">{{race.raceDate | date:'medium'}}</a>
                        </td>
                        <td>{{race.name}}</td>
                        <td>{{race.fleet.fleetName}}</td>
                        <td>{{race.raceStatus}}</td>
                        <td><button type="button" class="btn btn-default" ng-click="editResults(race.id)">Edit Results</button></td>
                    </tr>
                </tbody>                 
			</table>
			<table-list-nav />			
	    </div> <!-- /container -->

		<script type="text/ng-template" id="editRaceModal">
	      	<div class="modal-header">
  				<button type="button" class="close" data-dismiss="modal" ng-click="cancel()">
  					<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
				</button>
    			<h4 class="modal-title" id="myModalLabel">Race</h4>
  			</div>
			<div class="modal-body" >
				<div class="form-group">
					<label for="raceName">Race Name</label>
					<input class="form-control" ng-model="object.name" />
					<span class="help-inline" ng-show="hasError('name')">{{getError("name")}}</span>
				</div>
				<div class="form-group">
					<label for="raceDate">Race Date<span class="required">*</span></label>
					<div class="input-group" >
						<input 	type="text" 
							class="form-control" 
							uib-datepicker-popup="{{format}}" 
							ng-model="object.raceDate" 
							is-open="raceDatePopup.opened" 
							datepicker-options="dateOptions" 
							ng-required="true" 
							close-text="Close" 
							alt-input-formats="altInputFormats" />
          				<span class="input-group-btn">
            				<button type="button" class="btn btn-default" ng-click="raceDatePopupOpen()">
								<i class="glyphicon glyphicon-calendar"></i>
							</button>
          				</span>
					</div>
					<span class="help-inline" ng-show="hasError('raceDate')">{{getError("raceDate")}}</span>
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
 					<div class="panel panel-default">
                        <div class="panel-heading">Competitions</div>
                        <div class="panel-body">
                            <table class="table table-striped" >
                                <thead>
                                    <tr>
                                        <th>Competition Name</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr ng-repeat="competition in object.competitions">
                                        <td>
                                            <button type="button" class="btn" ng-click="removeCompetition(competition)" >
                                                <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                                            </button>
                                            {{competition.name}}</a>
                                        </td>
                                    </tr>
                                </tbody>                 
                            </table>
                            <p class="help-block" ng-show="(object.competitions === undefined || object.competitions.length == 0)">No competitions selected</p>
                            <div class="container-fluid">
                                <div class="col-sm-4">
                                    <select name="competitionToAdd" 
                                        class="form-control" 
                                        ng-model="competitionToAdd" 
                                        ng-options="competition.name for competition in competitions | filter:{ fleet.id : object.fleet.id } track by competition.id"
                                        required>
                                    </select>
                                </div>
                                <div class="col-sm-2 bottom-column">
                                    <button class="btn" type="button" ng-click="addCompetition(competitionToAdd)">Add</button>
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

		<script type="text/ng-template" id="editRaceResults">
	      	<div class="modal-header">
  				<button type="button" class="close" data-dismiss="modal" ng-click="cancel()">
  					<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
				</button>
    			<h4 class="modal-title" id="myModalLabel">Edit Results</h4>
  			</div>
			<div class="modal-body" >
				<div class="form-group">
					<label for="startTime">Start Time<span class="required">*</span></label>
					<div class="input-group" >
						<input 	type="text" 
							class="form-control" 
							uib-datepicker-popup="{{format}}" 
							ng-model="object.startTime" 
							is-open="raceTimePopup.opened" 
							datepicker-options="raceTimeOptions" 
							ng-required="true" 
							close-text="Close" 
							alt-input-formats="altInputFormats" />
          				<span class="input-group-btn">
            				<button type="button" class="btn btn-default" ng-click="raceTimePopupOpen()">
								<i class="glyphicon glyphicon-calendar"></i>
							</button>
          				</span>
					</div>
					<span class="help-inline" ng-show="hasError('startTime')">{{getError("startTime")}}</span>
				</div>
				<div class="form-group">
 					<div class="panel panel-default">
                        <div class="panel-heading">Results</div>
                        <div class="panel-body">
                            <table class="table table-striped" >
                                <thead>
                                    <tr>
                                        <th>Status</th>
										<th>Boat Name</th>
										<th>Finish Time</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr ng-repeat="result in object.results">
                                        <td>
                                            <button type="button" class="btn" ng-click="removeResult(result)" >
                                                <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                                            </button>
                                            {{result.status}}</a>
                                        </td>
										<td>{{result.boat.name}}</td>
										<td>{{result.finishTime}}</td>
                                    </tr>
                                </tbody>                 
                            </table>
                            <p class="help-block" ng-show="(object.competitions === undefined || object.competitions.length == 0)">No Results</p>
                        </div>
					</div>
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
            <%@include file="/js/racecontroller.js" %>
        </script>
	</body>
</html>
