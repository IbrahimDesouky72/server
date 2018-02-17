/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.talktoki.server.model;

import com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase;
import com.talktoki.chatinterfaces.commans.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.ServiceMode;
import oracle.jdbc.driver.OracleDriver;

/**
 *
 * @author IbrahimDesouky
 */
public class ServerModel {

    Connection con;
    PreparedStatement preparedStatement = null;
    Statement statement = null;
    ResultSet resultSet = null;
    String query = "";

    public ServerModel() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            DriverManager.registerDriver(new OracleDriver());
            con = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "hr", "hr");
            //con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "hr", "hr");
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
    private void closeResultSet(){
        try {
            if(statement!=null){
                statement.close();
            }else if(preparedStatement!=null){
                preparedStatement.close();
            }
            
            
            resultSet.close();
        } catch (SQLException ex) {
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
        }finally{
            closeResultSet();
        }

    }

    public User getUser(String email, String password) {
        User user = null;
         query = "select * from chat_user where email ='" + email + "' and password='" + password + "'";
        try {
            statement = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                String userName = resultSet.getString("user_name");
                String userPassword = resultSet.getString("password");
                String Email = resultSet.getString("email");
                String gender = resultSet.getString("gender");
                String country = resultSet.getString("country");
                //String status = resultSet.getString("status");
                setStatus(Email, "online");
                user = new User();
                user.setUserName(userName);
                user.setEmail(Email);
                user.setPassword(userPassword);
                user.setGender(gender);
                user.setCountry(country);
                user.setStatus("online");

            }

        } catch (SQLException ex) {
            Logger.getLogger(ServerModel.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeResultSet();
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
            ResultSet rs = statement.executeQuery("select * from CHAT_USER where email ='" + user.getEmail() + "'");
            if (rs.next()) {
                isExist = true;

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
        closeResultSet();
        return isInserted;
    }

    public int sendFriendRequest(String senderEmail, String receiverEmail) {
        int inserted = 0;
        boolean isNotExist = false;
        boolean areFriends = false;
        boolean isAlreadyrequested = false;
        boolean isConnectionError = false;

        try {
            query = "select * from chat_user where email ='" + receiverEmail + "'";
            statement = con.createStatement();
            resultSet = statement.executeQuery(query);
            if (!(resultSet.next())) {
                inserted = 1;
                isNotExist = true;
                System.out.println("not exist ");
            }
            query = "select * from friends where (sender_email='" + senderEmail + "' and receiver_email='" + receiverEmail + "') or"
                    + "(sender_email='" + receiverEmail + "' and receiver_email='" + senderEmail + "')";
            //statement=con.createStatement();
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                inserted = 2;
                areFriends = true;
                System.out.println("are friends");

            }
            query = "select * from friendrequests where (sender_email='" + senderEmail + "' and receiver_email='" + receiverEmail + "') or"
                    + "(sender_email='" + receiverEmail + "' and receiver_email='" + senderEmail + "')";

            //statement=con.createStatement();
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                inserted = 3;
                isAlreadyrequested = true;
                System.out.println("requested");

            }
            if ((!isNotExist) && (!isAlreadyrequested) && (!areFriends)) {
                query = "insert into friendrequests(SENDER_EMAIL,RECEIVER_EMAIL) values ('" + senderEmail + "','"
                        + receiverEmail + "')";
                statement.executeUpdate(query);
                inserted = 5;
                System.out.println("inserted");

            }

        } catch (SQLException ex) {
            isConnectionError = true;
            Logger.getLogger(ServerModel.class.getName()).log(Level.SEVERE, null, ex);
            inserted = 4;

        } finally {
            closeResultSet();
            return inserted;
        }

    }

    public ArrayList<String> getFriendRequests(String receiverEmail) {
        ArrayList<String> senderEmails = new ArrayList<>();
        query = "select sender_email from friendrequests where RECEIVER_EMAIL='" + receiverEmail + "'";
        System.out.println(query);
        try {
            statement = con.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {

                System.out.println("heloooo");
                senderEmails.add(resultSet.getString("sender_email"));

            }
        } catch (SQLException ex) {
            Logger.getLogger(ServerModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (int i = 0; i < senderEmails.size(); i++) {
            System.out.println(senderEmails.get(i));
        }
        closeResultSet();
        return senderEmails;

    }

    public boolean acceptFriendRequest(String senderEmail, String ReceiverEmail) {
        boolean isAccepted = false;
        // query="delete from friendrequests where sender_email ='"+senderEmail+"' and receiver_email= '"+ReceiverEmail+"'";
        try {
            query = "delete from friendrequests where SENDER_EMAIL ='" + senderEmail + "' and RECEIVER_EMAIL='" + ReceiverEmail + "'";
            statement = con.createStatement();
            int deleted = statement.executeUpdate(query);
            System.out.println(deleted);
            query = "insert into friends (sender_email,receiver_email)values ('" + senderEmail
                    + "','" + ReceiverEmail + "')";
            isAccepted = statement.execute(query);
            isAccepted = true;
        } catch (SQLException ex) {
            Logger.getLogger(ServerModel.class.getName()).log(Level.SEVERE, null, ex);
            isAccepted = false;
        } finally {
            closeResultSet();
            return isAccepted;
        }

    }

    public boolean deleteFriendRequest(String senderEmail, String receiverEmail) {
        boolean isDeleted = false;
        try {

            query = "delete from friendrequests where SENDER_EMAIL ='" + senderEmail + "' and RECEIVER_EMAIL='" + receiverEmail + "'";
            statement = con.createStatement();
            statement.executeUpdate(query);
            isDeleted = true;
        } catch (SQLException ex) {
            Logger.getLogger(ServerModel.class.getName()).log(Level.SEVERE, null, ex);
            isDeleted = false;
        } finally {
            closeResultSet();
            return isDeleted;
        }

    }

    public ArrayList<User> getContactList(String email) {
        ArrayList<User> contacts = new ArrayList<>();
        ArrayList<String> emails = new ArrayList<String>();

        query = "select * from friends where sender_email='" + email + "' or receiver_email='" + email + "'";
        try {
            statement = con.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                //handle if friend name equals userName
                if (resultSet.getString("sender_email").equals(email)) {
                    emails.add(resultSet.getString("receiver_email"));

                } else {
                    emails.add(resultSet.getString("sender_email"));
                }

            }
            for (int i = 0; i < emails.size(); i++) {
                query = "select * from chat_user where email ='" + emails.get(i) + "'";
                resultSet = statement.executeQuery(query);

                while (resultSet.next()) {

                    String username = resultSet.getString("user_name");
                    String userEmail = resultSet.getString("email");
                    String gender = resultSet.getString("gender");
                    String status = resultSet.getString("status");
                    String country = resultSet.getString("country");
                    String pass = "";
                    User user = new User(username, userEmail, pass, gender, country, status);
                    contacts.add(user);

                }

            }

        } catch (SQLException ex) {
            Logger.getLogger(ServerModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        closeResultSet();
        return contacts;

    }

    /**
     * @param groupId
     * @param date
     * @param users
     * @return returnNum (-1 "group already exist", 1 "group inserted correctly", 3 "problem with DB")
     */
    public int createChatGroup(String groupId, List<User> users) {
        int returnNum = 0;
        boolean isExist = false;

        try {
            String sqlQuery = "select * from group_chat where group_id = ?";
            PreparedStatement pStatement = con.prepareStatement(sqlQuery);
            pStatement.setString(1, groupId);
            //pStatement.setString(2, date);
            ResultSet rs = pStatement.executeQuery();
            if (rs.next()) {
                isExist = true;
                returnNum = -1;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServerModel.class.getName()).log(Level.SEVERE, null, ex);
            returnNum = 3;
        }
        if (!isExist) {
            for (User user : users) {
                query = "INSERT INTO group_chat"
                        + "(group_id , group_user) VALUES"
                        + "(?,?)";
                try {
                    preparedStatement = con.prepareStatement(query);
                    preparedStatement.setString(1, groupId);
                    preparedStatement.setString(2, user.getEmail());
                    //preparedStatement.setString(3, date);
                    preparedStatement.execute();
                    returnNum = 1;
                } catch (SQLException ex) {
                    Logger.getLogger(ServerModel.class.getName()).log(Level.SEVERE, null, ex);
                    returnNum = 3;
                }
            }
        }
        closeResultSet();
        return returnNum;
    }

    /**
     * getUserByEmail
     * @param email
     * @return User Object
     */
    public User getUserByEmail(String email) {
        // TODO
        // getConnection()
        User user = new User();
        query = "select * from chat_user where email =?";
        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String userName = resultSet.getString("user_name");
                String userPassword = "";
                String Email = resultSet.getString("email");
                String gender = resultSet.getString("gender");
                String country = resultSet.getString("country");
                String status = resultSet.getString("status");

                user.setUserName(userName);
                user.setEmail(Email);
                user.setPassword(userPassword);
                user.setGender(gender);
                user.setCountry(country);
                user.setStatus(status);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServerModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        //TODO 
        //Close Connection
        closeResultSet();
        return user;
    }

    /**
     * get group
     * @param groupId
     * @param groupDate
     * @return
     */
    public ArrayList<String> getUserGroupsIds(String userEmail) {
        ArrayList<String> userGroupList = new ArrayList<>();
        try {
            String sqlQuery = "select group_id from group_chat where group_user=?";
            PreparedStatement pStatement = con.prepareStatement(sqlQuery);
            pStatement.setString(1, userEmail);
            ResultSet arrayResultSet = pStatement.executeQuery();
            while (arrayResultSet.next()) {
                // System.out.println("rows Number" + resultSetHashMap.getRow());
                String groupIdStr = arrayResultSet.getString(1);
                // System.out.println("groupName:" + group);
                //String date = arrayResultSet.getString(2);
                //System.out.println("groupName:" + date);
                userGroupList.add(groupIdStr);
            }
            arrayResultSet.close();

        } catch (SQLException ex) {
            Logger.getLogger(ServerModel.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            closeResultSet();
        return userGroupList;
        }
        
    }
    /***
     * Check if the group exist or not
     * @param groupId
     * @return true if the group exist esle return false
     */
    public boolean isGroupExist(String groupId) {
        boolean exist = false;
        try {
            String sqlQuery = "select group_id from group_chat where group_id =?";
            PreparedStatement pStatement = con.prepareStatement(sqlQuery);
            pStatement.setString(1, groupId);
            ResultSet isExistResultSet = pStatement.executeQuery();
            if (isExistResultSet.next()) {
                 exist=true;
            }else{
                exist=false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServerModel.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            closeResultSet();
            return exist;
        }
    }
    /**
     * Get User List of a Specific group
     * @param groupId
     * @return a list of users 
     */
    public ArrayList<User> getGroupUsers(String groupId) {
        ArrayList<User> listOfUsers = null;
        if (isGroupExist(groupId)) {
            listOfUsers = new ArrayList<>();
            try {
                String sqlQuery = "select group_user from group_chat where group_id =?";
                PreparedStatement pStatement = con.prepareStatement(sqlQuery);
                pStatement.setString(1, groupId);
                ResultSet usersResultSet = pStatement.executeQuery();
                while (usersResultSet.next()) {
                    String userEmail = usersResultSet.getString(1);
                    User user = getUserByEmail(userEmail);
                    listOfUsers.add(user);
                }

            } catch (SQLException ex) {
                Logger.getLogger(ServerModel.class.getName()).log(Level.SEVERE, null, ex);
            }
            closeResultSet();
            return listOfUsers;
        }
        else
        {
            closeResultSet();
            return listOfUsers;
        }

    }
    
    public void setStatus(String userEmail,String status){
        query="update chat_user set status='"+status +"' where email='"+userEmail+"'";
        try {
            System.out.println(query);
            statement=con.createStatement();
            int x=statement.executeUpdate(query);
            System.out.println(x);
            
        } catch (SQLException ex) {
            Logger.getLogger(ServerModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    
    }
    
    

    public static void main(String[] args) {
        ServerModel serverModel = new ServerModel();
        User u = new User();
        u.setUserName("I_Desouky");
        u.setEmail("Ibrahim.desouky44@gmail.com");
        u.setCountry("Egypt");
        u.setPassword("1234");
        u.setGender("male");
        u.setStatus("offline");
        
////       User u1 = serverModel.getUserByEmail("bodourhassan@gmail.com");
////       User u2 = serverModel.getUserByEmail("bassemgawesh@gmail.com");
////       User u3 = serverModel.getUserByEmail("mahrous@gmail.com");
        //u=serverModel.getUser("Ibrahim.desouky44@gmail.com", "hima");
//        List<User> userList = new ArrayList<>();
//        userList.add(u1);
//        userList.add(u2);
//        userList.add(u3);
//        int x = serverModel.createChatGroup("ChatGroupTest3",userList);
//        int y = serverModel.createChatGroup("ChatGroupTest3",userList);
//        System.out.println("return number" + y);
        
        //System.out.println("resut db = ");
        serverModel.setStatus("mahrous@gmail.com","online");
    }

}
