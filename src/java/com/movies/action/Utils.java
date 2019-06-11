
package com.movies.action;

import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public final class Utils {
    private static String UserName= "";
    private static String inputLine = "";
    private static JSONParser jparser = null;
    private static JSONObject jobject = null;
    private static Connection connection = null;

    public static String getUserName() {
        return UserName;
    }

    public static void setUserName(String UserName) {
        Utils.UserName = UserName;
    }
    public static Connection ConnectDB(){
        try{            
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/moviesdb", "root", "");
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            return connection;
        }
    } 
    
    public static JSONObject MakeApiCall(String QueryString){
        try{
            URL url = new URL(QueryString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            inputLine = "";
            Scanner scan = new Scanner(url.openStream());
            while(scan.hasNext())
            {
                inputLine+=scan.nextLine();
            }
            jparser = new JSONParser();
            jobject = (JSONObject)jparser.parse(inputLine);
            scan.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{            
            return jobject;
        }
    }
}
