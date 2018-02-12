/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.talktoki.server.controller;

import java.util.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
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
public class MainUiController {

    @FXML
    private TabPane serverTabPane;

    @FXML
    private Tab serviceTab;

    @FXML
    private Tab announcementTab;

    @FXML
    private Tab statisticTab;

    @FXML
    private Button startBtn;

    @FXML
    private Button stopBtn;

    @FXML
    private TextArea announcementTxtArea;

    @FXML
    private PieChart genderStatistic;

    @FXML
    private PieChart onlineStatistic;

    @FXML
    private Button genderBtn;

    @FXML
    void generateGenderChat(MouseEvent event) {
            ObservableList<PieChart.Data> details =  FXCollections.observableArrayList();
            details.addAll(new PieChart.Data("Male percentage", 60) , new PieChart.Data("Female percentage", 40));
            genderStatistic.setData(details);
            genderStatistic.setLabelsVisible(true);
            genderStatistic.setLegendSide(Side.TOP);
    }

//    public void mouseClick(MouseEvent event)
//    {
//        genderStatistic.getData().
//    }
}
