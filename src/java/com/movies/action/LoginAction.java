/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movies.action;

import com.opensymphony.xwork2.ActionSupport;
import com.sun.net.httpserver.Authenticator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Selvah
 */
public class LoginAction extends ActionSupport{
    String username,password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    @Override
    public String execute () throws Exception {
        Connection conn = Utils.ConnectDB();
        PreparedStatement ps = conn.prepareStatement("select * from login where username = ? and password = ?");
        ps.setString(1,getUsername());
        ps.setString(2, getPassword());
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            Utils.setUserName(username);
            return SUCCESS;
        }
        else{
            return "fail";
        }
    }
    
}
