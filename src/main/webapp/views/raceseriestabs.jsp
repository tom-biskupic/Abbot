<h1>${raceSeries.name}</h1>
<ul class="nav nav-tabs">
	<li role="presentation" <core:if test="${activePage.equals('races')}">class="active"</core:if> > <a href="${pageContext.request.contextPath}/raceseries/${raceSeries.id}/racelist">Races</a></li>
	<li role="presentation" <core:if test="${activePage.equals('boats')}">class="active"</core:if> ><a href="${pageContext.request.contextPath}/raceseries/${raceSeries.id}/boats">Boats</a></li>
	<li role="presentation" <core:if test="${activePage.equals('results')}">class="active"</core:if> ><a href="${pageContext.request.contextPath}/rresultslist.jsp">Results</a></li>
	<li role="presentation" <core:if test="${activePage.equals('settings')}">class="active"</core:if> ><a href="${pageContext.request.contextPath}/raceseries/${raceSeries.id}/settings">Settings</a></li>
</ul>
			
