/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.talktoki.server.model;

import com.talktoki.chatinterfaces.commans.User;
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
    PreparedStatement preparedStatement = null;

    public ServerModel() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver"); 
          DriverManager.registerDriver(new OracleDriver());
          // con=DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe","hr","hr");
            con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","hr","hr");
            System.out.println(con.isValid(5));
           
           
      } catch (SQLException e) {
          e.printStackTrace();
      } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServerModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void AllUsers(){
       Statement stmt ;
        ResultSet rs=null ;
        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,  
                    ResultSet.CONCUR_UPDATABLE);
             String queryString=  "select * from chat_user";
            rs= stmt.executeQuery(queryString);
            
            while (rs.next()) {
                System.out.println(rs.getString(1));
            
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServerModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    
    }
    public int insertUser(User user){
            int isInserted=0;
            Statement statement;    
        try {
            statement = con.createStatement();
            ResultSet rs = statement.executeQuery("select * from CHAT_USER");
            while(rs.next()){
                if(rs.getString("email").equals(user.getEmail())){
                    return 2;
                }
            
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServerModel.class.getName()).log(Level.SEVERE, null, ex);
            isInserted=3;
        }
        
            
        
       
        String insertTableSQL = "INSERT INTO chat_user"
				+ "(USER_NAME, EMAIL, PASSWORD, GENDER,COUNTRY,STATUS) VALUES"
				+ "(?,?,?,?,?,?)";
        try {
            preparedStatement = con.prepareStatement(insertTableSQL);
            preparedStatement.setString(1, user.getUserName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getGender());
            preparedStatement.setString(5, user.getCountry());
            preparedStatement.setString(6, user.getStatus());
            preparedStatement.executeUpdate();

            System.out.println("Record is inserted into DBUSER table!");
            isInserted=1;
        } catch (SQLException ex) {
            Logger.getLogger(ServerModel.class.getName()).log(Level.SEVERE, null, ex);
            isInserted=3;
        }
        
    
        return isInserted;
    } 
    public static void main(String[] args) {
        ServerModel serverModel=new ServerModel();
        User u=new User();
        u.setUserName("I_Desouky");
        u.setEmail("hima@yahoo.com");
        u.setCountry("Egypt");
        u.setPassword("1234");
        u.setGender("male");
        u.setStatus("offline");
        System.out.println(serverModel.insertUser(u));
    }
    
}
