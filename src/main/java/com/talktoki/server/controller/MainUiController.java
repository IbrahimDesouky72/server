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
        ObservableList<PieChart.Data> genderObservableList = FXCollections.observableArrayList();
        ObservableList<PieChart.Data> onlineOfflineObservableList = FXCollections.observableArrayList();
        ServerModel serverModelObj = new ServerModel();
        float[] genderStatisticArray = serverModelObj.getGenderSatistics();
        int malePercentage = (int) genderStatisticArray[0];
        int femalePercentage = (int) genderStatisticArray[1];

        float[] onlineStatisticArray = serverModelObj.getOnlineStatistic();
        int onlinePercentage = (int) onlineStatisticArray[0];
        int offlinePercentage = (int) onlineStatisticArray[1];

        PieChart.Data maleData = new PieChart.Data("Male(" + malePercentage + "%)", malePercentage);
        PieChart.Data femaleData = new PieChart.Data("Female(" + femalePercentage + "%)", femalePercentage);

        PieChart.Data onlineData = new PieChart.Data("Online(" + onlinePercentage + "%)", onlinePercentage);
        PieChart.Data offlineData = new PieChart.Data("Offline(" + offlinePercentage + "%)", offlinePercentage);

        genderObservableList.addAll(maleData, femaleData);
        genderStatistic.setData(genderObservableList);
        genderStatistic.setTitle("Males and females");
        onlineOfflineObservableList.addAll(onlineData, offlineData);
        onlineStatistic.setData(onlineOfflineObservableList);
        onlineStatistic.setTitle("Online and offline Users Statistic");
    }

    @FXML
    void generateStatistic(ActionEvent event) {
        generateChart();
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
