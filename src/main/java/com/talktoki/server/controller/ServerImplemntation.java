/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.talktoki.server.controller;

import com.talktoki.chatinterfaces.client.ClientInterface;
import com.talktoki.chatinterfaces.commans.Message;
import com.talktoki.chatinterfaces.commans.User;
import com.talktoki.chatinterfaces.server.ServerInterface;
import com.talktoki.server.model.ServerModel;
import java.io.File;
import java.io.FileInputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author IbrahimDesouky
 */
public class ServerImplemntation extends UnicastRemoteObject implements ServerInterface {

    ServerModel serverModel;
    ArrayList<ClientInterface> clients;

    public ServerImplemntation() throws RemoteException {
        serverModel = new ServerModel();
        clients = new ArrayList<>();
    }

    @Override
    public User signIn(String email, String password) throws RemoteException {
        User u = serverModel.getUser(email, password);
        return u;

    }

    @Override
    public void addClient(ClientInterface client) throws RemoteException {
        clients.add(client);
        ArrayList<String> requests = serverModel.getFriendRequests(client.getUser().getEmail());
        for (int i = 0; i < requests.size(); i++) {

            // WE NEEDED SENDER_NAME AND SENDER_EMAIL 
            // YOU CALL CLIENT WITH => Sender email and receiver email !!!
            // client.receiveFriendshipRequest(requests.get(i), client.getUser().getEmail());
            // I WILL SEND EMAIL AND EMAIL UNTIL YOU FIX IT 
            client.receiveFriendshipRequest(requests.get(i), requests.get(i));
        }

    }

    @Override
    public int signUp(User user) throws RemoteException {
        int num = serverModel.insertUser(user);
        System.out.println("num :"+num);
        return num;

    }

    @Override
    public boolean signOut(ClientInterface myclient) throws RemoteException {
        boolean isSignedOut = false;
        System.out.println("STARTING TO REMOVE "+myclient.getUser().getEmail());
        System.out.println("BEFORE REMOVE: " + clients.size());
        clients.remove(myclient);
        System.out.println("AFTER REMOVE: " + clients.size());
        serverModel.setStatus(myclient.getUser().getEmail(), "offline");
        isSignedOut = true;
        return isSignedOut;

    }

    @Override
    public boolean sendToOne(String sender_email, String receiver_email, Message message) throws RemoteException {
        /*boolean isSent=false;
         clients.forEach((ClientInterface clientInterface)->{
         try {
         if(receiver_email.equals(clientInterface.getUser().getEmail())){
         isSent=true;
                    
         }   } catch (RemoteException ex) {
         Logger.getLogger(ServerImplemntation.class.getName()).log(Level.SEVERE, null, ex);
         }
        
         });
         return isSent;*/
        boolean isSent = false;
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).getUser().getEmail().equals(receiver_email)) {
                isSent = true;
                clients.get(i).receiveFromOne(sender_email, message);
                break;
            }

        }

        return isSent;
    }

    @Override
    public int createGroup(String group_id, ArrayList<User> group_members) throws RemoteException {
        int returnValue = serverModel.createChatGroup(group_id, group_members);
        return returnValue;
    }

    @Override
    public boolean sendToGroup(String sender_email, Message message, String group_id) {
        boolean sent = false;
        int groupListCounter = 0;
        ArrayList<User> userList = serverModel.getGroupUsers(group_id);
        for (ClientInterface client : clients) {
            for (User userFromUserList : userList) {
                try {
                    if (client.getUser().getEmail() == userFromUserList.getEmail()) {

                        client.receiveInGroup(group_id, sender_email, message);
                        groupListCounter++;

                    }
                } catch (RemoteException ex) {
                    Logger.getLogger(ServerImplemntation.class.getName()).log(Level.SEVERE, null, ex);
                    sent = false;
                }
            }
        }
        if (groupListCounter == userList.size()) {
            sent = true;
        }
        return sent;
    }

    @Override
    public ArrayList<String> getUserGroupsIDs(String user_email) throws RemoteException {
        ArrayList<String> groupList = serverModel.getUserGroupsIds(user_email);
        return groupList;
    }

    @Override
    public ArrayList<User> getGroupUsers(String group_id) throws RemoteException {
        ArrayList<User> userList = serverModel.getGroupUsers(group_id);
        return userList;
    }

    @Override
    public void notifyStatus(String email, int status) throws RemoteException {
        // TODO Update my status in database   
        if (status == 0) {
            serverModel.setStatus(email, "offline");
        } else if (status == 1) {
            serverModel.setStatus(email, "online");
        } else if (status == 2) {

            serverModel.setStatus(email, "away");
        } else if (status == 3) {

            serverModel.setStatus(email, "busy");
        }

        // Get my friends
        ArrayList<User> friends = serverModel.getContactList(email);
        User changedUser = serverModel.getUserByEmail(email);
        // Notify online friends
        System.out.println("clients size " + clients.size());
        for (ClientInterface client : clients) {
            for (User friend : friends) {
                if (friend.getEmail().equals(client.getUser().getEmail())) {
                    System.out.println("cLIENT IS: " + client.getUser().getEmail());
                    System.out.println("Friend is" + friend.getEmail());
                    client.notifyFriendStatusChanged(changedUser, status);
                }
            }
        }
    }

    @Override
    public ArrayList<User> getContactList(String email) throws RemoteException {
        ArrayList<User> friends = serverModel.getContactList(email);
        return friends;
    }

    @Override
    public int sendFriendshipRequest(String sender, String receiver) throws RemoteException {
        int accepted = 0;
        accepted = serverModel.sendFriendRequest(sender, receiver);
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).getUser().getEmail().equals(receiver)) {
                clients.get(i).receiveFriendshipRequest(sender, sender);
                break;
            }

        }
        return accepted;
    }

    @Override
    public boolean friendshipRequestResponse(String recevier, String sender, boolean accepted) throws RemoteException {
        boolean isAccepted = false;
        if (accepted == true) {
            isAccepted = serverModel.acceptFriendRequest(sender, recevier);

        } else {
            isAccepted = serverModel.deleteFriendRequest(sender, recevier);

        }
        // Update contacts list of both reciever and sender
        for (ClientInterface client : clients) {
            if(client.getUser().getEmail().equals(recevier) || client.getUser().getEmail().equals(sender))
            {
                client.refreshContacts();
            }
        }
        return isAccepted;
    }
    
    /**********Mahrous*********/
    public void SendAnnouncementToAll(String announcement){
        for (ClientInterface client : clients) {
            try {
                client.receiveServerAnnouncement(announcement);
            } catch (RemoteException ex) {
                Logger.getLogger(ServerImplemntation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }                                
    /**********Mahrous*********/
//-1 user not found ,0 error in remote connection,1 success,2 refuse send
/************Bodour*////////////
    
    @Override
    public int SendFile(String sender_username, String reciever_Email,String FileName,byte[] Data,int Length,boolean firstSend) throws RemoteException {
        boolean checkfound=true;
        ClientInterface recieveClient = null;
        System.out.println("recieverrrrr"+reciever_Email);
        System.out.println("length"+clients.size());
        for (ClientInterface client : clients) {
            try {
                System.out.println("every person :"+client.getUser().getEmail());
                if (client.getUser().getEmail().equals(reciever_Email)) {
                   recieveClient=client; 
                     System.out.println("user found");
                     checkfound=true;
                     break;
                } else {
                    System.out.println("The user Not found");
                    checkfound=false;
                }
            } catch (RemoteException ex) {
                return 0;
            }  
        }
        if(checkfound)
        { 
        boolean recieveAccept=recieveClient.reciveFile(sender_username,FileName,Data,Length,firstSend);  
        if(recieveAccept)
            {
                return 1;
            }
            else
            {
                return 2;
            }
        }
        else
        {
          return -1;
        }
    }
    /************Bodour*////////////

}
