<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<link href="resources/css/bootstrap.css" rel="stylesheet"
	type="text/css" />
<link href="resources/css/default.css" rel="stylesheet" type="text/css" />
<script src="resources/javascript/jquery-1.11.3.min.js"></script>
<script src="resources/javascript/bootstrap.min.js"></script>
<script src="resources/javascript/picture_main.js"></script>
<title>Pictures Client</title>
</head>
<body>
	<div class="container">
		<h1>Pictures REST Client</h1>
		<div id="debug"></div>
		<c:if test="${showAlert}">
		   <div class="alert alert-danger" id="clientErrorAlert">
		      <button type="button" class="close fade in" data-dismiss="alert">
		      <span>&times;</span>
		      </button>
		      <p><c:out value="${clientError.message}" /></p>
		      <p><c:out value="${clientError.info}" /></p>
		  </div>
		</c:if>
		
		<c:if test="${showDeleteSuccess}">
		   <div class="alert alert-success" id="deleteSuccessAlert">
		      <button type="button" class="close fade in" data-dismiss="alert">
		      <span>&times;</span>
		      </button>
		      <p><c:out value="The following items have been successfully deleted." /></p>
		      <p><c:out value="${deletedPicNames}" /></p>
		  </div>
		</c:if>
		<div class="alert alert-danger"  id="selectErrorAlert">
		      <button type="button" class="close fade in" data-dismiss="alert">
		      <span>&times;</span>
		      </button>
		      <p><c:out value="Please make your checkbox selection before continuing." /></p>
       </div><!-- hidden initially via CSS visibility animated if no selection -->	
	   <p id="debug"></p>	
		<ul class="nav nav-tabs">
			<li class="active"><a data-toggle="tab" href="#view"
				class="btn btn-info">View</a></li>
			<li><a data-toggle="tab" href="#upload" class="btn btn-info">Upload</a>
			</li>
		</ul>
		<div class="tab-content">
			<div id="view" class="tab-pane fade in active">
				
				<br />
				<hr />
				<input type="hidden" id="showModal" name="showModal"
					value="${showModal}" />
				<c:if test="${clientResponseKey.total eq 0}">
					<div class="customerMessage">
						<p>${customerKey.firstName},there are no pictures in your
							storage.</p>
						<p>Please press the Upload tab.</p>
					</div>
				</c:if>
				
				<form action="nextInterval" method="get">
				<input type="hidden" name="sessionHash" value="${sessionHash}" />
				<c:if test="${clientResponseKey.total gt 0}">
				   <div>
				       <h3>Contents of ${customerKey.firstName}'s Storage</h3>
				       <span class="customerMessage">${count} of ${clientResponseKey.total}</span>
				       &nbsp;&nbsp;
				       <c:if test="${count lt clientResponseKey.total}">
				          <input type="submit" name="btnNext" value="Next" class="btn btn-success btn-sm" />
				      </c:if>
				  </div>
				</c:if>
				</form>	
				<c:if test="${sessionScope.sampleListKey ne null}">
				<div>
				<table class="scrollToTable">
				 <tr>
				    <td>[</td>
							<c:forEach begin="1" end="15" var="num">
								<c:choose>
									<c:when test="${num > sessionScope.count}">
										<td><a href="#" class="scrollToDisabled">
										     ${num}</a></td>
									</c:when>
									<c:otherwise>
										<td><a href="#">${num}</a></td>
									</c:otherwise>
								</c:choose>
								<c:choose>
								   <c:when test="${num lt 15}">
								      <td>|</td>
								   </c:when>
								   <c:otherwise><td>&nbsp;</td></c:otherwise>
								</c:choose>   
							</c:forEach>
							<td>]</td>
				 </tr>
				</table>
				</div>
				</c:if>			
				<c:if test="${sessionScope.sampleListKey ne null}">
				   <form action="commands" method="get">
				   <input type="hidden" name="sessionHash" value="${sessionHash}" />
					<table class="picTable">
						<tr>
							<th>Image</th>
							<th>Information</th>
							<th>Delete</th>
							<th>Download</th>
							<td><input type="submit" name="btnZipAll" value="Zip All"  
							     class="btn btn-warning btn-sm"/>
							</td>
						</tr>
						<c:forEach var="sample" items="${sessionScope.sampleListKey}"
							varStatus="status">
							<tr>
								<td><img src="${imgUrlKey[status.index]}" /><br /> <c:out
										value="${sample.originalPicName}" /></td>
								<td><c:out value="${sample.comment}" /><br /><c:out
								        value="${timeStampList[status.index]}" /> 
								<br /> <c:out
								value="Photo Id: ${sample.photoId}" /><br/><c:out 
								value="${sample.picName}" /></td>
								<td><input type="checkbox" name="chkDeletePic"
									value="${sample.photoId}" /></td>
								<td><input type="checkbox" name="chkDownloadPic"
									value="${sample.picName}" /></td>
							    <td>&nbsp;</td>		
							</tr>
						</c:forEach>
						<tr>
						  <td>&nbsp;</td>
						  <td>&nbsp;</td>
						  <td><input type="submit"  value="Delete" name="btnDelete" 
						      class="btn btn-warning btn-sm" />
						  <td><input type="submit" value="Download" name="btnZipSelect"  
						      class="btn btn-warning btn-sm" />
						  <td>&nbsp;</td>      
						</tr>
					</table>
					</form>
				</c:if>
			</div>
			<!-- end view -->
			<div id="upload" class="tab-pane fade">
				<form action="upload2" method="post" enctype="multipart/form-data">
				    <input type="hidden" name="sessionHash" value="${sessionHash}" />
					<div class="row inputFile">
						<div class="col-md-4"></div>
						<div class="col-md-8">
							<label>Select Image File:</label><br /> <input type="file"
								name="uploadImgKey" size="40" />
						</div>
					</div>
					<!-- end row -->
					<div class="row inputText">
						<div class="col-md-4"></div>
						<div class="col-md-8">
							<label>Please Comment:</label><br />
							<textarea rows="5" cols="50" name="uploadTextKey"></textarea>
						</div>
					</div>
					<!-- end row -->
					<div class="row">
						<div class="col-md-4"></div>
						<div class="col-md-8">
							<input type="submit" value="Upload" class="btn btn-success" />							
						</div>
					</div>
					<!-- end row -->
				</form>
			</div>
			<!-- end upload -->
		</div>
		<!-- end tab-content -->

		<div id="loginModal" class="modal fade">
			<form action="login" method="get">
				<div class="modal-dialog modal-lg">
					<div class="modal-content">
						<div class="modal-header">
							<h4 class="modal-title">Customer Login</h4>
						</div>
						<!-- end modal-header -->
						<div class="modal-body">
							<div class="row">
								<div class="col-md-4"></div>
								<div class="col-md-8">
									<label>Customer Id:</label>
								</div>
							</div>
							<div class="row">
								<div class="col-md-4"></div>
								<div class="col-md-8">
									<input name="customerId" size="20" />									
								</div>								
							</div>
							<div class="row">
								<div class="col-md-4"></div>
								<div class="col-md-8 errMsg">
								    ${idNumberFormat}
									${clientError.message}<br/>	
									${clientError.info}						
								</div>								
							</div>
						</div>
						<!-- end modal-body -->
						<div class="modal-footer">
							<input type="submit" class="btn btn-info" value="Login" />
						</div>
						<!-- end modal-footer -->
					</div>
					<!-- end modal-content -->
				</div>
				<!-- end modal-dialog -->
			</form>
		</div>
		<!-- end loginModal -->


	</div>
	<!-- end container -->
</body>
</html>