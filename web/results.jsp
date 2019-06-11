<%-- 
    Document   : results
    Created on : May 28, 2019, 4:50:41 PM
    Author     : test
--%>
<%@ page language="java" import = "java.io.*,java.util.*,java.sql.*,java.lang.*"%>
<%@ taglib prefix="s" uri = "/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix = "c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Movie Results</title>
    </head>
    <style type="text/css">
        table, td, th {
                border: 1px solid black;
        }

        table {
                border-collapse: collapse;
                width: 100%;
        }

        th {
                height: 50px;
        }
    </style>
    <body>
        <s:actionerror/>      
        <s:property value = "record_counter" /> Record(s) Found
        <s:if test="SearchType=='Search as Movie Name'">
            <div style="margin-top: 40px; margin-right: 150px; margin-left: 150px;">
                <table id="display">
                    <thead>
                        <tr style="background-color: #E0E0E1;">
                            <th>ID</th>
                            <th>TITLE</th>
                            <th>ACTORS</th>
                            <th>DIRECTORS</th>                            
                            <th>RELEASE DATE (MM/DD/YY)</th>
                            <th>RATING</th>
                            <th>AWARDS</th>
                        </tr>
                    </thead>
                    <s:iterator value="MovieDetailList">
                        <tr>
                            <td><s:property value="imdbid" /></td>
                            <td><s:property value="title" /></td>
                            <td><s:property value="actors" /></td>
                            <td><s:property value="directors" /></td>
                            <s:if test="releasedate !=null">
                                <td><s:property value="releasedate" /></td>
                            </s:if>
                            <s:else>
                                <td>N/A</td>
                            </s:else>
                            <s:if test="rating !=null">
                                <td><s:property value="rating" /></td>
                            </s:if>
                            <s:else>
                                <td>N/A</td>
                            </s:else>
                            <td><s:property value="awards" /></td>                            
                        </tr>
                    </s:iterator>
                </table>
            </div>
	</s:if>
        <s:else>
            <div style="margin-top: 40px; margin-right: 150px; margin-left: 150px;">
			
                <table id="display">
                    <thead>
                        <tr style="background-color: #E0E0E1;">
                            <th>ID</th>
                            <th>NAME</th>
                            <th>BIRTHDAY (MM/DD/YY)</th>
                            <th>PLACE OF BIRTH</th>                            
                            <th>DEATH DAY (MM/DD/YY)</th>
                        </tr>
                    </thead>
                    <s:iterator value="MovieDetailList">
                        <tr>
                            <td><s:property value="imdbid" /></td>
                            <td><s:property value="name" /></td>
                             <s:if test="bday !=null">
                                <td><s:property value="bday" /></td>
                            </s:if>
                            <s:else>
                                <td>N/A</td>
                            </s:else>
                            <td><s:property value="birthplace" /></td>
                             <s:if test="dday !=null">
                                <td><s:property value="dday" /></td>
                            </s:if>
                            <s:else>
                                <td>N/A</td>
                            </s:else>
                        </tr>
                    </s:iterator>
                </table>
            </div>
        </s:else>
        <s:if test="PresentInLocalDB">
        <s:form action="" method="post">
            <s:submit name="More" value="More.."/>            
        </s:form>
        </s:if>
    </body>
</html>
