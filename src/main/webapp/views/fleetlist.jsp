<%@include file="/views/tagincludes.jsp" %>

<core:set var="activePage" scope="session" value="fleets"/>

<!DOCTYPE html>
<html lang="en">
	<%@include file="/views/header.jsp" %>

  	<body>
	 	<%@include file="/views/navbar.jsp" %>
	
		<div class="container">
			<%@include file="/views/raceseriestabs.jsp" %>
			
			<div class="btn-group pull-right" role="group" aria-label="...">
  				<button type="button" class="btn btn-default">
  					<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
  				</button>
  				<button type="button" class="btn btn-default">
  					<span class="glyphicon glyphicon-refresh" aria-hidden="true"></span>
  				</button>
 			</div>
			
			<table class="table table-striped">
				<thead>
					<tr>
						<th></th> <!-- The remove column -->
						<th>Fleet Name</th>
						<th>Description</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>
							<button type="button" class="btn">
								<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
							</button>
						</td>
						<td>Sabt 1up and 2up</td>
						<td><a href="#">Sabots</a></td>
					</tr>
					<tr>
						<td>
							<button type="button" class="btn">
								<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
							</button>
						</td>
						<td><a href="#">12s</a></td>
						<td>12s and Cherubs</td>
					</tr>
					<tr>
						<td>
							<button type="button" class="btn">
								<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
							</button>
						</td>
						<td><a href="#">Lasers</a></td>
						<td>All rigs</td>
					</tr>
					<tr>
						<td>
							<button type="button" class="btn">
								<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
							</button>
						</td>
						<td><a href="#">Laser Radials</a></td>
						<td>Radials only</td>
					</tr>
					<tr>
						<td>
							<button type="button" class="btn">
								<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
							</button>
						</td>
						<td><a href="#">420s</a></td>
						<td></td>
					</tr>
				</tbody>
			</table>
			<nav>
              <ul class="pagination">
                	<li class="disabled"><a href="#">&laquo;</a></li>
                	<li class="active"><a href="#">1</a></li>
                	<li><a href="#">2</a></li>
                	<li><a href="#">3</a></li>
                	<li><a href="#">4</a></li>
                	<li><a href="#">5</a></li>
                	<li><a href="#">&raquo;</a></li>
             	</ul>
        	</nav>
			
	    </div> <!-- /container -->

	 	<%@include file="/views/includejs.jsp" %>	
	</body>
</html>
