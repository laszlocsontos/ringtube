<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="javax.servlet.RequestDispatcher" %>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">

<%
Integer statusCode = (Integer)request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

if (statusCode != null) {
  String requestURI = (String)request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
%>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <title>Guru Meditation Error</title>
</head>
<body>
  <h1>Guru Meditation Error</h1>
  <h2><%= statusCode %></h2>
  <p><%= requestURI %></p>
</body>

<%
}
%>

</html>