<%@include file="/views/tagincludes.jsp" %>

<!DOCTYPE html>
<html lang="en">
	<%@include file="/views/header.jsp" %>

  	<body>
	 	<%@include file="/views/navbar.jsp" %>
	
		<div class="container">
			<h1>Login</h1>
		      <form method="POST" action="${pageContext.request.contextPath}/login">
		      	<div class="form-group">
		      		<label for="email">Email</label>
		      		<input class="form-control" name="username" />
		      	</div>
		      	<div class="form-group">
		      		<label for="password">Password</label>
		      		<input type="password" class="form-control" name="password" />
		      	</div>
		      	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/> 
		  		<button type="submit" class="btn btn-primary">Login</button>
		  		<a class="btn" href="${pageContext.request.contextPath}/register" >Register &raquo;</button>
	      </form>			
	    </div> <!-- /container -->

	 	<%@include file="/views/includejs.jsp" %>	
	</body>
</html>
