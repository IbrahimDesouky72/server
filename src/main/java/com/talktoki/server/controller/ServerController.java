/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.talktoki.server.controller;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author IbrahimDesouky
 */
public class ServerController {
    
    ServerImplemntation serverImplemntation;
    Registry registry ;

    public ServerController()  {
        try {
            registry = LocateRegistry.createRegistry(2000);
        } catch (RemoteException ex) {
            Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public static void main(String[] args)  {
        new ServerController();
    }
    
    public void start(){
                try {
            serverImplemntation=new ServerImplemntation();
            
            
            registry.rebind("chat", serverImplemntation);

        } catch (RemoteException ex) {
            Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
        }
                
                
    
    }
    public void stop(){
        try {
            registry.unbind("chat");
        } catch (RemoteException ex) {
            Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
