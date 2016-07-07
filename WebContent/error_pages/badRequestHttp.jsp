<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="http://localhost:7070${pageContext.request.contextPath}/resources/css/default.css" 
         type="text/css" rel="stylesheet" />
<title>Http Bad Request</title>
</head>
<body>
   <h1>Application Error (Http Bad Request)</h1>
   <h3><a href="http://localhost:7070${pageContext.request.contextPath}/index.html">
        Home</a></h3>
   <label>Message:</label> ${clientError.message}<br/><br/>
   <label>Detail:</label>${clientError.detail}<br/><br/>
   <label>Info:</label>${clientError.info}
</body>
</html>