<%-- 
    Document   : index
    Created on : Mar 3, 2025, 10:10:37 AM
    Author     : knguy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>index</h1>
        <% response.sendRedirect(request.getContextPath() + "/home"); %>
    </body>
</html>
