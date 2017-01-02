<%@include file="/views/tagincludes.jsp"%>

<core:set var="activePage" scope="session" value="races" />

<!DOCTYPE html>
<html lang="en">
    <%@include file="/views/header.jsp"%>

	<body ng-app="abbot" ng-controller="raceResultsController"
	    ng-init="init('${pageContext.request.contextPath}','/raceseries/'+${raceSeries.id}+'/racelist.json','/raceseries/'+${raceSeries.id}+'/race.json','editRaceModal')">
	
	    <%@include file="/views/navbar.jsp"%>
	    <h1>{{racename}} - {{racedate}}</h1>
	                
	    <div class="container">
	    </div>
	    
	    <%@include file="/views/includejs.jsp"%>
	    <script>
	        <%@include file="/js/abbot.js" %>
	        <%@include file="/js/dialog.js" %>
	        <%@include file="/js/listcontroller.js" %>
	        <%@include file="/js/raceresultcontroller.js" %>
	        <%@include file="/js/utils.js" %>
	    </script>
	</body>
</html>
