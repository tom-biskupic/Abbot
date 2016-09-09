<%@include file="/views/tagincludes.jsp" %>

<core:set var="navPage" scope="session" value="homePage"/>

<!DOCTYPE html>
<html lang="en">
	<%@include file="/views/header.jsp" %>

  	<body>
	 	<%@include file="/views/navbar.jsp" %>
	
		<div class="container">
			<!--  <h1>Sailing Results Made Simple</h1>
			<p>Abbot is a sailing race manager suitable for club racing or regattas.</p> -->
			<h1>Test</h1>
			<p>Logon to manage your races or click register to create an account</p>
			<p><a href="${pageContext.request.contextPath}/register" class="btn btn-primary btn-large">Register &raquo;</a></p>
	    </div> <!-- /container -->

	 	<%@include file="/views/includejs.jsp" %>	
	</body>
</html>
