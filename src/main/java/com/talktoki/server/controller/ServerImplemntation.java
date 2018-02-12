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
    ArrayList<ClientInterface>clients;

    public ServerImplemntation() throws RemoteException{
        serverModel=new ServerModel();
        clients=new ArrayList<>();
    }
    
    
    
    @Override
    public User signIn(String email, String password) throws RemoteException {
        User u=serverModel.getUser(email, password);
        return u;
    
    }
    
    @Override
    public void addClient(ClientInterface client) throws RemoteException {
        clients.add(client);
        ArrayList<String>requests=serverModel.getFriendRequests(client.getUser().getEmail());
        for(int i=0;i<requests.size();i++){
            client.receiveFriendshipRequest(requests.get(i), client.getUser().getEmail());
        }
        
    }

    @Override
    public int signUp(User user) throws RemoteException {
        int num=serverModel.insertUser(user);
        return num;
    
    }

    @Override
    public boolean signOut(ClientInterface myclient) throws RemoteException {
        boolean isSignedOut=false;
        for(int i=0;i<clients.size();i++){
            if(myclient==clients.get(i)){
                clients.remove(i);
                isSignedOut=true;
            
            }
        
        }
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
        boolean isSent=false;
        for(int i=0;i<clients.size();i++){
            if(clients.get(i).getUser().getEmail().equals(receiver_email)){
                isSent=true;
                clients.get(i).receiveFromOne(sender_email, message);
                break;
            }
        
        }
        
        return isSent;
    }

    @Override
    public int createGroup(String group_id, String[] group_members) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean sendToGroup(String sender_email, Message message, String group_id) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    @Override
    public boolean notifyStatus(String email, int status) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<User> getContactList(String email) throws RemoteException {
        ArrayList<User>friends=serverModel.getContactList(email);
        return friends;
    }

    @Override
    public int sendFriendshipRequest(String sender, String receiver) throws RemoteException {
        int accepted=0;
        accepted=serverModel.sendFriendRequest(sender, receiver);
        for(int i=0;i<clients.size();i++){
            if(clients.get(i).getUser().getEmail().equals(receiver)){
                clients.get(i).receiveFriendshipRequest(sender, sender);
                break;
            }
        
        }
        return  accepted;
    }

    @Override
    public boolean friendshipRequestResponse(String recevier, String sender, boolean accepted) throws RemoteException {
        boolean isAccepted=false;
        if(accepted==true){
            isAccepted=serverModel.acceptFriendRequest(sender, recevier);
        
        }else{
            isAccepted=serverModel.deleteFriendRequest(sender, recevier);
        
        }
        
        return isAccepted;
    }
    
}
