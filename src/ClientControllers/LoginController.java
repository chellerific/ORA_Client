/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientControllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ora_client.ClientConnectionManager;
import ora_client.MessageUtils;

/**
 * FXML Controller class
 *
 * @author student
 */
public class LoginController implements Initializable {

    public static ClientConnectionManager connectionManager;

    @FXML
    public TextField usernameField, passwordField;
    @FXML
    private Label errorLabel;

    @FXML
    private void logInAction(ActionEvent event) {
        connectionManager = new ClientConnectionManager();
        boolean authorized = connectionManager.connect(usernameField.getText(), passwordField.getText());
        if (authorized) {
            MessageUtils.sendMessage(ClientConnectionManager.sslSocket, "get_status");
            MessageUtils.sendMessage(ClientConnectionManager.sslSocket, usernameField.getText());
            String result = MessageUtils.receiveMessage(ClientConnectionManager.sslSocket);
            if (result.equals("ACK")) {
                voteStage(event);
            } else if (result.equals("NAK")) {
                resultsStage(event);
            }
        } else {
            errorLabel.setText("Username or password incorrect.");
        }
    }

    public void voteStage(ActionEvent event) {
        System.out.println("Change to vote stage!");
        try {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/ClientViews/VotingScreen.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void resultsStage(ActionEvent event) {
        System.out.println("Change to results stage!");
        try {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/ClientViews/Statistics.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb
    ) {
        usernameField.setText("bitharis@mail.com");
        passwordField.setText("myPass");
    }

}
