<%-- 
    Document   : index
    Created on : May 28, 2019, 12:52:26 PM
    Author     : test
--%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Movies Mania</title>
    </head>
    <style type="text/css">
        .insertform{
            margin: 150px 400px;
        }
    </style>
    <body>
        <s:actionerror/>
        <center><h2> Welcome to Movies Mania </h2></center>
        <div class="insertform">
            <s:form  method = "post" action = "SearchAction">
                <s:label value="Search Details" />
                <s:textfield label="Movie/Actor Name" name = "SearchKeyword" />
                <s:submit value = "Search as Movie Name" name="SearchType" />
                <s:submit value = "Search as Actor Name" name="SearchType"/>
            </s:form>
        </div>
    </body>
</html>
