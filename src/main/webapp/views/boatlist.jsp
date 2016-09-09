<%@include file="/views/tagincludes.jsp" %>

<core:set var="activePage" scope="session" value="boats"/>

<!DOCTYPE html>
<html lang="en">
	<%@include file="/views/header.jsp" %>

  	<body 
  		ng-app="abbot" 
  		ng-controller="boatController" 
  		ng-init="init('${pageContext.request.contextPath}','/raceseries/'+${raceSeries.id}+'/boatlist.json','/raceseries/'+${raceSeries.id}+'/boat.json','editBoatModal')">
  		
	 	<%@include file="/views/navbar.jsp" %>
	
		<div class="container">
			<%@include file="/views/raceseriestabs.jsp" %>
			
        <div class="container">     
            <h1>Registered Boats</h1>
    
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
                        <th sort-header column-name="name" column-heading="Name"></th>
                        <th sort-header column-name="sailNumber" column-heading="Sail Number"></th>
                        <th sort-header column-name="boatClass.name" column-heading="Boat Class"></th>
                        <th sort-header column-name="division.name" column-heading="Division"></th>
                    </tr>
                </thead>
                <tbody>
                    <tr ng-repeat="boat in page.content">
                    	<td>
                    		<button type="button" class="btn" ng-click="deleteObject(boat.id)" >
                            	<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                            </button>
                            <a ng-click="editObject(boat.id)">{{boat.name}}</a>
                        </td>
                        <td>{{boat.sailNumber}}</td>
                        <td>{{boat.boatClass.name}}</td>
                        <td>{{boat.division.name}}</td>
                    </tr>
                </tbody>                 
            </table>    
            <table-list-nav />          
        </div>              

		<script type="text/ng-template" id="editBoatModal">
	      	<div class="modal-header">
  				<button type="button" class="close" data-dismiss="modal" ng-click="cancel()">
  					<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
				</button>
    			<h4 class="modal-title" id="myModalLabel">Boat</h4>
  			</div>
			<div class="modal-body" >
				<div class="form-group">
					<label for="sailNumber">Sail Number<span class="required">*</span></label>
					<input class="form-control" ng-model="object.sailNumber" name="sailNumber" autofocus/>
					<span class="help-inline" ng-show="hasError('sailNumber')">{{getError("sailNumber")}}</span>
				</div>
				<div class="form-group">
					<label for="name">Boat name<span class="required">*</span></label>
					<input class="form-control" ng-model="object.name" name="sailNumber" autofocus/>
					<span class="help-inline" ng-show="hasError('name')">{{getError("name")}}</span>
				</div>
				<div class="form-group">
					<label for="boatclass">Boat Class<span class="required">*</span></label>
					<select name="boatclass" 
                    		class="form-control" 
                            ng-model="object.boatClass" 
                            ng-options="boatClass.name for boatClass in boatClasses track by boatClass.id"
                            required>
                    </select>
				</div>
				<div class="form-group">
					<label for="division">Boat Division</label>
					<select name="division" 
                            class="form-control" 
                            ng-model="object.division" 
                            ng-options="division.name for division in object.boatClass.divisions"
                            required>
                   	</select>
				</div>
				<div class="form-group">
					<label for="skipper">Skipper Name</label>
					<input class="form-control" ng-model="object.skipper" name="skipper" autofocus/>
					<span class="help-inline" ng-show="hasError('skipper')">{{getError("skipper")}}</span>
				</div>
				<div class="form-group">
					<label for="crew">Crew Name</label>
					<input class="form-control" ng-model="object.crew" name="crew" autofocus/>
					<span class="help-inline" ng-show="hasError('crew')">{{getError("crew")}}</span>
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
            <%@include file="/js/boatcontroller.js" %>
        </script>
	</body>
</html>
