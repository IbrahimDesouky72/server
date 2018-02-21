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
import javafx.stage.WindowEvent;

/**
 *
 * @author Mariam
 */
public class MainServerApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader myloader = new FXMLLoader(getClass().getResource("/fxml/ServerUi.fxml"));

        //Create new mainUI controller instance 
        MainUiController myMainUIController = new MainUiController();

        // Attach mainUI contorller to the loader
        myloader.setController(myMainUIController);

        // Load the FXML file and getx   root node       
        Parent root = myloader.load();

        // Create a scene and attach root node to it
        Scene scene = new Scene(root);

        scene.getStylesheets().add("/styles/Styles.css");
        stage.setTitle("Server Gui");
        stage.setScene(scene);
        stage.setResizable(false);

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                myMainUIController.stop(null);
                System.exit(0);
            }
        });
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
