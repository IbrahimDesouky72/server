/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.talktoki.server.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.driver.OracleDriver;

/**
 *
 * @author IbrahimDesouky
 */
public class ServerModel {
    Connection con;

    public ServerModel() {
        try {
          DriverManager.registerDriver(new OracleDriver());
           con=DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe","hr","hr");
           
      } catch (SQLException e) {
          e.printStackTrace();
      }
    }
    public void AllUsers(){
       Statement stmt ;
        ResultSet rs=null ;
        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,  
                    ResultSet.CONCUR_UPDATABLE);
             String queryString= new String("select * from chat_user");
            rs= stmt.executeQuery(queryString) ;
            while (rs.next()) {
                System.out.println(rs.getString(1));
            
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServerModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    
    }
    public static void main(String[] args) {
        ServerModel serverModel=new ServerModel();
    }
    
}
