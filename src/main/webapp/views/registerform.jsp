<%@include file="/views/tagincludes.jsp" %>

<!DOCTYPE html>
<html lang="en">
 
 	<%@include file="/views/header.jsp" %>

  	<body>

	<%@include file="/views/navbar.jsp" %>
	
		    <div class="container">
	
	      <h1>Register</h1>
	      
	      <form:form method="POST" commandName="User" id="userForm">
		      <fieldset>
		      	<div class="form-group">
		      		<label for="userFirstName">First name<span class="required">*</span></label>
		      		<form:input cssClass="form-control" path="firstName" name="firstName" />
		      		<form:errors path="firstName" cssClass="help-inline" element="span" />
		      	</div>
		      	<div class="form-group">
		      		<label for="lastName">Second name<span class="required">*</span></label>
		      		<form:input cssClass="form-control" path="lastName" name="lastName" />
		      		<form:errors path="lastName" cssClass="help-inline" element="span" />
		      	</div>
		      	<div class="form-group">
		      		<label for="email">Email address<span class="required">*</span></label>
		      		<form:input cssClass="form-control" path="email" name="email" />
		      		<form:errors path="email" cssClass="help-inline" element="span" />		      		
		      	</div>
		      	<div class="form-group">
		      		<label for="organisation">Organisation/Club</label>
		      		<form:input path="organisation" cssClass="form-control" name="organisation" />
		      		<form:errors path="organisation" cssClass="help-inline" element="span" />
		      	</div>
		      	<div class="form-group">
		      		<label for=password">Password<span class="required">*</span></label>
		      		<form:password path="password" cssClass="form-control" name="password" />
		      		<form:errors path="password" cssClass="help-inline" element="span" />		      		
		      	</div>
		      	<div class="form-group">
		      		<label for="passwordConfirm">Password confirm<span class="required">*</span></label>
		      		<input type="password" class="form-control" name="passwordConfirm" />
		      	</div>
		      	<button type="submit" class="btn btn-primary">Register</button>
			</fieldset>
	      </form:form>
	    </div> <!-- /container -->
		
	<%@include file="/views/includejs.jsp" %>
	
	<script type="text/javascript">
		$(document).ready(function() {  // <-- enclose your code in a DOM ready handler
		
		    $("#userForm").validate({
		        rules: 
		        {
		            passwordConfirm: 
		            {
		                equalTo: "#password",
		            }
		        }
		    });		
		});
	</script>
	
	
	</body>
		
</html>
