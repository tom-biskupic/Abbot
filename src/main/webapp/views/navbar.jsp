<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
  <div class="container">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="#">Abbot Sailing Race Manager</a>
    </div>
    <div id="navbar" class="navbar-collapse collapse">
    	<ul class="nav navbar-nav">
    		<security:authorize access="!isAuthenticated()">
            	<li <core:if test="${navPage.equals('homePage')}">class="active"</core:if>><a href="${pageContext.request.contextPath}">Home</a></li>
            </security:authorize>
    		<security:authorize access="isAuthenticated()">
            	<li <core:if test="${navPage.equals('homePage')}">class="active"</core:if>><a href="${pageContext.request.contextPath}/views/raceserieslist.jsp">Home</a></li>
            </security:authorize>            
            <li <core:if test="${navPage.equals('about')}">class="active"</core:if>><a href="${pageContext.request.contextPath}/about">About</a></li>
            <li <core:if test="${navPage.equals('contact')}">class="active"</core:if>><a href="${pageContext.request.contextPath}/contact">Contact</a></li>
            <security:authorize access="hasRole('ROLE_ADMIN')" >
            	<li <core:if test="${navPage.equals('manageUsers')}">class="active"</core:if>><a href="${pageContext.request.contextPath}/userlist">Manager Users</a></li>
            </security:authorize>
        </ul>
        <security:authorize access="!isAuthenticated()">
       		<a class="btn btn-success navbar-btn navbar-right" href="${pageContext.request.contextPath}/views/loginform.jsp" role="button">Login</a>
			<!-- 
	   		<form class="navbar-form navbar-right" role="form" method="post" action="${pageContext.request.contextPath}/j_spring_security_check" >
	     		<div class="form-group">
	       			<input type="text" placeholder="Email" class="form-control" name="j_username">
	     		</div>
	     		<div class="form-group">
	       			<input type="password" placeholder="Password" name="j_password" class="form-control">
	     		</div>
	     		<button type="submit" class="btn btn-success">Sign in</button>
	   		</form>
	   		 -->
	   	</security:authorize>
	   	<security:authorize access="isAuthenticated()">		       		
			<form method="post" action="${pageContext.request.contextPath}/logout" class="navbar-form navbar-right">
		    	<div class="btn-group">
			    	<button type="submit" class="btn btn-inverse"><i class="icon-user icon-white"></i> Logout</button>
		        	<button class="btn btn-inverse dropdown-toggle" data-toggle="dropdown">
	    				<span class="caret"></span>
	    				<span class="sr-only">Toggle Dropdown</span>
					</button>
			        <ul class="dropdown-menu" role="menu">
	    				<li><a href="#">Account Settings</a></li>
					</ul>
	  			</div>
	  			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			</form>
		</security:authorize>
    </div><!--/.navbar-collapse -->
  </div>
</nav>
