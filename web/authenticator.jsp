<%-- 
    Document   : authenticator
    Created on : Jun 9, 2019, 11:13:03 PM
    Author     : Selvah
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Choose Authenticator</title>
    </head>
    <style type="text/css">
        
        #forms {
          margin-top: 10%;
          margin-left: 35%;
          width: 28%;
          border-radius: 5px;
          background-color: #f2f2f2;
          padding: 20px;
          display: flex;
        }
        form {
            padding: 5%;
        }
        input[type=image]:hover {
            box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
        }
    </style>
    <body>  
        <div id = "header"> <center><h2> Select the type of 2FA</h2></center></div>
        <div id = "forms">
            
            <form action ="DuoFrameServlet" method ="get">
                <input type ="image" src="http://localhost:8080/MyMovies/js/duo.png" alt="DUO 2FA AUTHENTICATION" style="width: 150px;"/>
            </form>
            <form action ="GoogleAuthenticatorServlet" method ="post">
                <input type ="image" src="http://localhost:8080/MyMovies/js/google.jpg" alt ="GOOGLE 2FA AUTHENTICATION" style="width: 150px;"/>
            </form>
        </div>
    </body>
</html>
