/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.talktoki.server.controller;

import com.jfoenix.controls.JFXButton;
import com.talktoki.server.model.ServerModel;
import java.net.URL;
import java.util.Observer;
import java.util.ResourceBundle;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

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
 *
 * @announcementBtn
 *
 * the third Tab statistic id @statisticTab the pie chart of the gender id
 * @genderStatistic the pie chart of the online and offline users id
 * @onlineStatistic
 *
 */
public class MainUiController implements Initializable {

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
    private JFXButton refershBtn;

    @FXML
    private Label serverStatus;

    @FXML
    private Label msgLabel;

    ServerController controller;

    private void generateChart() {
                ServerModel serverModelObj = new ServerModel();

                DoubleProperty maleDoubleProperty = new SimpleDoubleProperty(serverModelObj.getMaleNumber());
                DoubleProperty femaleDoubleProperty = new SimpleDoubleProperty(serverModelObj.getFemaleNumber());
                Double totalGender = maleDoubleProperty.doubleValue()+femaleDoubleProperty.doubleValue();
                DoubleProperty onlineDoubleProperty = new SimpleDoubleProperty(serverModelObj.getOnlineUsers());
                DoubleProperty offlineDoubleProperty = new SimpleDoubleProperty(serverModelObj.getOfflineUsers());
                PieChart.Data maleData = new PieChart.Data("male", 0);
                maleData.pieValueProperty().bind(maleDoubleProperty);
                PieChart.Data femaleData = new PieChart.Data("female", 0);
                femaleData.pieValueProperty().bind(femaleDoubleProperty);
                ObservableList<PieChart.Data> genderObservableList = FXCollections.observableArrayList(maleData, femaleData);

                PieChart.Data onlineData = new PieChart.Data("online", 0);
                onlineData.pieValueProperty().bind(onlineDoubleProperty);
                PieChart.Data offlineData = new PieChart.Data("offline", 0);
                offlineData.pieValueProperty().bind(offlineDoubleProperty);
                ObservableList<PieChart.Data> onlineOfflineObservableList = FXCollections.observableArrayList(onlineData, offlineData);
                genderStatistic.setData(genderObservableList);
                genderStatistic.setTitle("Males and females");

                onlineStatistic.setData(onlineOfflineObservableList);
                onlineStatistic.setTitle("Online and offline Users Statistic");

                new Thread(new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        while (true) {
                            Thread.sleep(10000);               
                            maleDoubleProperty.set(serverModelObj.getMaleNumber());
                            femaleDoubleProperty.set(serverModelObj.getFemaleNumber());
                            ///genderStatistic.getData();
                            onlineDoubleProperty.set(serverModelObj.getOnlineUsers());
                            offlineDoubleProperty.set(serverModelObj.getOfflineUsers());
                        }
                    }
                }).start();
            }

    @FXML
    public void start(ActionEvent event) {
        controller.start();
        System.out.println("com.talktoki.server.controller.MainUiController.initialize()");
        serverStatus.setText("Started");
        serverStatus.setStyle("-fx-text-fill:  #5bcc74");
    }

    @FXML
    public void stop(ActionEvent event) {
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
        controller = new ServerController();
        generateChart();

        announcementBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ServerImplemntation impl = controller.getServerImplemntation();
                if (impl == null) {
                    Alert myalert = new Alert(Alert.AlertType.ERROR);
                    myalert.setTitle("ERROR");
                    myalert.setHeaderText("Error sending announcement");
                    myalert.setContentText("Please start the server before trying to send an announcement!");
                    myalert.showAndWait();
                    msgLabel.setVisible(true);
                    msgLabel.setText("ERROR!");
                    msgLabel.setTextFill(Color.RED);
                } else {
                    String text = announcementTxtArea.getText().trim();
                    if (text.length() > 0) {
                        impl.SendAnnouncementToAll(text);
                        msgLabel.setVisible(true);
                        msgLabel.setText("Sent to online users!");
                        msgLabel.setTextFill(Color.GREEN);
                    }
                    announcementTxtArea.clear();
                }
            }
        });

    }

}
