/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.talktoki.server.controller;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Mariam
 */
public class MainServerApp extends Application{
   
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/ServerUi.fxml"));
        Scene scene = new Scene(root);
        
        
        scene.getStylesheets().add("/styles/Styles.css");
        stage.setTitle("Server Gui");
        stage.setScene(scene);
       
        stage.show();
    }
    
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
