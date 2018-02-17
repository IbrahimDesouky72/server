/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.talktoki.server.controller;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.Observer;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Mariam
 */
/**
 * *
 * the tabpane id @serverTabPane the first Tab service Management id @serviceTab
 * start Button id @startBtn stop button id @stopBtn
 *
 * the second tab announcement id @announcementTab text area for the
 * announcement id @announcementTxtArea button the send to all users id
 * @announcementBtn
 *
 * the third Tab statistic id @statisticTab the pie chart of the gender id
 * @genderStatistic the pie chart of the online and offline users id
 * @onlineStatistic
 *
 */
public class MainUiController implements Initializable{

    @FXML
    private TabPane serverTabPane;

    @FXML
    private Tab serviceTab;

    @FXML
    private Tab announcementTab;

    @FXML
    private Tab statisticTab;

    @FXML
    private JFXButton startBtn;

    @FXML
    private JFXButton stopBtn;

    @FXML
    private TextArea announcementTxtArea;
    
    @FXML
    private JFXButton announcementBtn;

    @FXML
    private PieChart genderStatistic;

    @FXML
    private PieChart onlineStatistic;

    @FXML
    private Button genderBtn;
    
    @FXML
    private Label serverStatus;
    
    ServerController controller;

    @FXML
    void generateGenderChat(MouseEvent event) {
            ObservableList<PieChart.Data> genderStatistics =  FXCollections.observableArrayList();
            PieChart.Data maleData = new PieChart.Data("Male("+60+")", 60);
            PieChart.Data femaleData = new PieChart.Data("Female("+40+")", 40);
            genderStatistics.addAll( maleData ,femaleData);
            genderStatistic.setData(genderStatistics);
            genderStatistic.setTitle("Males and females");
           
            //onlineStatistic.setTitle("Online Users and Offilne Users");
            
            //genderStatistic.setLabelsVisible(true);
            //genderStatistic.setLegendSide(Side.BOTTOM);
    }
    
    
    @FXML
    void generateOnlineStatistic(MouseEvent event) {
             ObservableList<PieChart.Data> onlineOfflineStatisticList =  FXCollections.observableArrayList();
             PieChart.Data onlineData = new PieChart.Data("Online("+30+"%)",30);
             PieChart.Data offlineData = new PieChart.Data("Offline("+70+"%)",70);
            onlineOfflineStatisticList.addAll( onlineData , offlineData);
            onlineStatistic.setData(onlineOfflineStatisticList);
            onlineStatistic.setTitle("Online and offline Users Statistic");
            
    
    }

    @FXML
    public void start(ActionEvent event){
       controller.start();
        System.out.println("com.talktoki.server.controller.MainUiController.initialize()"); 
        serverStatus.setText("Started");
        serverStatus.setStyle("-fx-text-fill:  #5bcc74");
    }
    
    @FXML
    public void stop(ActionEvent event){
        controller.stop();
        serverStatus.setText("Stopped");
        serverStatus.setStyle("-fx-text-fill: #e20f0f");
    }
//    public void mouseClick(MouseEvent event)
//    {
//        genderStatistic.getData().
//    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controller=new ServerController();    
    }
}
