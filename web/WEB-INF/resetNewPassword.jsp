<%-- 
    Document   : resetNewPassword
    Created on : 25-Nov-2021, 8:55:24 PM
    Author     : wenchi
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Password Page</title>
    </head>
    <body>
        <h1>Enter a new password</h1>
        <form action="reset" method="post">
            <input type="hidden" name="action" value="new">
            <input type="text" name="password" required>
            <input type="submit" value="Submit">
        </form>
    </body>
</html>
