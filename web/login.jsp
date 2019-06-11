<%-- 
    Document   : login
    Created on : Jun 5, 2019, 11:55:43 PM
    Author     : Selvah
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login Page</title>
    </head>
    <style>
        input[type=text], input[type=password] {
          width: 100%;
          padding: 12px 20px;
          margin: 8px 0;
          display: inline-block;
          border: 1px solid #ccc;
          border-radius: 4px;
          box-sizing: border-box;
        }

        input[type=submit] {
          width: 100%;
          background-color: #4CAF50;
          color: white;
          padding: 14px 20px;
          margin: 8px 0;
          border: none;
          border-radius: 4px;
          cursor: pointer;
        }

        input[type=submit]:hover {
          background-color: #45a049;
        }

        div {
          margin-top: 10%;
          margin-left: 30%;
          width: 40%;
          border-radius: 5px;
          background-color: #f2f2f2;
          padding: 20px;
        }
    </style>
    <body>
        <div>
            <center><h2>Movies Mania Login</h2></center>
        <form action ="LoginAction" method ="post">
            User name: <input type ="text" name="username" /><br/>
            Password: <input type ="password"  name="password"/>            
            <input type ="submit" value ="Login"/>
        </form>
        </div>
    </body>
</html>
