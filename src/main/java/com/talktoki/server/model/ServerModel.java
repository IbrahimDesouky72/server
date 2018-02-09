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
    Statement statement=null;
    ResultSet resultSet=null;

    public ServerModel() {
         try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            DriverManager.registerDriver(new OracleDriver());
            // con=DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe","hr","hr");
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "hr", "hr");
            System.out.println(con.isValid(5));

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServerModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getConnection() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            DriverManager.registerDriver(new OracleDriver());
            // con=DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe","hr","hr");
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "hr", "hr");
            System.out.println(con.isValid(5));

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServerModel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void AllUsers() {
       // getConnection();
        //getConnection();
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            String queryString = "select * from chat_user";
            rs = stmt.executeQuery(queryString);

            while (rs.next()) {
                System.out.println(rs.getString(1));

            }
        } catch (SQLException ex) {
            Logger.getLogger(ServerModel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public User getUser(String email,String password){
        User user=null;
        String query="select * from chat_user where email ='"+email+"' and password='"+password+"'";
        try {
            statement=con.createStatement();
            resultSet=statement.executeQuery(query);
            if(resultSet.next()){
                String userName=resultSet.getString("user_name");
                String userPassword=resultSet.getString("password");
                String Email=resultSet.getString("email");
                String gender=resultSet.getString("gender");
                String country=resultSet.getString("country");
                String status =resultSet.getString("status");
                user=new User();
                user.setUserName(userName);
                user.setEmail(Email);
                user.setPassword(userPassword);
                user.setGender(gender);
                user.setCountry(country);
                user.setStatus(status);
                
                
            
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ServerModel.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            return user;
        }
        
    }

    public int insertUser(User user) {

        /**
         * returning one means that the email inserted correctly returning two
         * means that the email exists in the data base and you should enter
         * another email returning 3 means that you there is database error
         * connection error hint :column names of database are small case in
         * result set like email not EMAIL
         */
        //getConnection();
        int isInserted = 0;
        boolean isExist = false;
        boolean isCoonectinError = false;
        
        try {
            statement = con.createStatement();
            ResultSet rs = statement.executeQuery("select * from CHAT_USER where email ='"+user.getEmail()+"'");
            if(rs.next()) {
                isExist=true;

            }
        } catch (SQLException ex) {
            Logger.getLogger(ServerModel.class.getName()).log(Level.SEVERE, null, ex);
            isCoonectinError = true;
            
        }

        if (!isExist) {
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
                isInserted = 1;
            } catch (SQLException ex) {
                Logger.getLogger(ServerModel.class.getName()).log(Level.SEVERE, null, ex);
                isInserted = 3;
            }

        } else if (isCoonectinError) {
            isInserted = 3;
        } else if (isExist) {
            isInserted = 2;
        }

        return isInserted;
    }

    public static void main(String[] args) {
        ServerModel serverModel = new ServerModel();
        User u = new User();
        u.setUserName("I_Desouky");
        u.setEmail("mahrous@yahoo.com");
        u.setCountry("Egypt");
        u.setPassword("1234");
        u.setGender("male");
        u.setStatus("offline");
       //u=serverModel.getUser("Ibrahim.desouky44@gmail.com", "hima");
        
        System.out.println(serverModel.insertUser(u));
    }

}
