package onlineExamSystem;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.sql.*;
/**
 *
 * @author Abdullah
 */
public class ConnectionProvider {
    public static Connection getCon(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/oopdatabase?zeroDateTimeBehavior=CONVERT_TO_NULL","root","Hammad2003.");
            return con;
        }
        catch(Exception e)
        {
            return null;
        }
        
        
    
    }
}
