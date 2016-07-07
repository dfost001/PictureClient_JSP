<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<link href="http://localhost:7070${pageContext.request.contextPath}/resources/css/default.css" 
         type="text/css" rel="stylesheet" />
<title>Service Connect Error</title>
</head>
<body>
   <div class="wrapper">
   <h1>An error occurred.</h1><br/>
   <h3>The problem may be temporary. Use the link below to retry.</h3><br/><br/>
   <h3><a href="http://localhost:7070${pageContext.request.contextPath}/index.html">
        Home</a></h3>
   <h4>Technical Support</h4>
   <div>
      <label>Method:</label><br/>
      ${httpErrKey.method}<br/><br/>
      <label>Description:</label><br/>
      ${httpErrKey.friendly}<br/><br/>
       <label>Text:</label><br/>
      ${httpErrkey.text}
      <label>Message:</label><br/>
      ${httpErrkey.message}
   </div>
   </div><!-- end wrapper -->
</body>
</html>