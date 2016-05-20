/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientControllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import ora_client.ClientConnectionManager;
import ora_client.MessageUtils;

/**
 * FXML Controller class
 *
 * @author Karolis
 */
public class StatisticsController implements Initializable {
    private int yesVotes, noVotes;

    @FXML
    private PieChart pieChart;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MessageUtils.sendMessage(ClientConnectionManager.sslSocket, "get_statistics");
        String statistics = MessageUtils.receiveMessage(ClientConnectionManager.sslSocket);
        parseStatisticsMessage(statistics);
        
        ObservableList<PieChart.Data> pieChartData
                = FXCollections.observableArrayList(
                        new PieChart.Data("Yes", yesVotes),
                        new PieChart.Data("No", noVotes));

        pieChart.setData(pieChartData);

    }
    
    public void parseStatisticsMessage(String message){
        StringTokenizer sk = new StringTokenizer(message, ":");
        yesVotes = Integer.parseInt(sk.nextToken());
        noVotes = Integer.parseInt(sk.nextToken());
    }

}
