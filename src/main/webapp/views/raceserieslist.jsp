<%@include file="/views/tagincludes.jsp" %>

<core:set var="navPage" scope="session" value="homePage"/>

<!DOCTYPE html>
<html lang="en">
	<%@include file="/views/header.jsp" %>

  	<body 
  		ng-app="abbot" 
  		ng-controller="raceSeriesController" 
  		ng-init="init('${pageContext.request.contextPath}','/raceserieslist.json','/raceseries.json','editRaceSeries')" >

	 	<%@include file="/views/navbar.jsp" %>
	
		<div class="container">		
			<h1>Race Series</h1>
	
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
						<th>Series Name</th>
						<th>Type</th>
						<th>Comment</th>
						<th>Date Created</th>
						<th>Last Updated</th>
					</tr>
				</thead>
				<tbody>
					<tr ng-repeat="raceSeries in page.content">
						<td>
							<a href="${pageContext.request.contextPath}/raceseries/{{raceSeries.id}}/racelist">{{raceSeries.name}}</a>
						</td>
						<td>{{raceSeries.seriesType }}</td>
						<td>{{raceSeries.comment}}</td>
						<td>{{raceSeries.dateCreated | date:'medium'}}</td>
						<td>{{raceSeries.lastUpdated | date:'medium'}}</td>
					</tr>
				</tbody>  				 
			</table>	
			<table-list-nav />	    	
		</div>				

		<script type="text/ng-template" id="editRaceSeries">
			<div class="modal-header">
	      		<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
	        	<h4 class="modal-title" id="myModalLabel">Race Series</h4>
	      	</div>
			<div class="modal-body" >
				<div class="form-group">
					<label for="name">Name<span class="required">*</span></label>
					<input class="form-control" ng-model="object.name" />
					<span class="help-inline" ng-show="hasError('name')">{{getError("name")}}</span>
				</div>
				<div class="form-group">
					<select name="seriesType" 
							class="form-control" 
							ng-model="object.seriesType" 
							ng-options="item.id as item.label for item in seasonTypeValues"
							required>
					</select>
					<span class="help-inline" ng-show="hasError('seriesType')">{{getError("seriesType")}}</span>
				</div>
				<div class="form-group">
					<label for="name">Comment</label>
					<input class="form-control" ng-model="object.comment" />
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
	 		<%@include file="/js/raceseriescontroller.js" %>
 		</script>

	</body>
</html>
