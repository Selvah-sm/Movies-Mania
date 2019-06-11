/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movies.action;

import com.google.zxing.WriterException;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base32;

/**
 *
 * @author test
 */
public class GoogleAuthenticatorServlet extends HttpServlet {
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            AuthCodeGenerator.SecretKeyGenerator();    
            Thread.sleep(3000);
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Google Authenticator</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<center><h3>Connect Via Code: " + AuthCodeGenerator.Secret_Key + "</h3>");
            out.println("<form action=\"AuthenticationServlet\" method=\"post\">");
            out.println("Enter TOTP: <input type=\"text\" name=\"authcode\"/>");
            out.println("<input type=\"submit\" value=\"Submit\"/>");
            out.println("</form></center>");
            out.println("</body>");
            out.println("</html>");
        } catch (InterruptedException | IOException ex) {
            Logger.getLogger(GoogleAuthenticatorServlet.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }




    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
class AuthCodeGenerator  {
    public static String  Secret_Key,TOTPCode;    
    public static void SecretKeyGenerator() {
        Secret_Key = getRandomSecretKey();
        System.out.println(Secret_Key);        
    }
    public static String getRandomSecretKey() {
    SecureRandom random = new SecureRandom();
    byte[] bytes = new byte[20];
    random.nextBytes(bytes);
    Base32 base32 = new Base32();
    String secretKey = base32.encodeToString(bytes);
    return secretKey.toLowerCase().replaceAll("(.{4})(?=.{4})", "$1 ");
}
    
}