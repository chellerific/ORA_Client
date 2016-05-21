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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import ora_client.ClientConnectionManager;
import ora_client.MessageUtils;

/**
 * FXML Controller class
 *
 * @author student
 */
public class VotingScreenController implements Initializable {

    @FXML
    private Button downloadQtn, yesVote, noVote, logOut;
    
    @FXML
    private TextArea questionTextArea;
    
    @FXML
    Label errorLabel, greetings;
    
    @FXML
    public void downloadQstn() {
        if (questionTextArea.getText().isEmpty()) {
            MessageUtils.sendMessage(ClientConnectionManager.sslSocket, "get_question");
            MessageUtils.sendMessage(ClientConnectionManager.sslSocket, questionTextArea.getText());
            String result = MessageUtils.receiveMessage(ClientConnectionManager.sslSocket);
            if (result.equals("ACK")) {
                errorLabel.setText("Question downloaded succesfully.");
            } else if (result.equals("NAK")) {
                errorLabel.setText("Download unsuccessful. Please try again.");
            }
        }
    }
    @FXML 
    public void submitVote() {
        if (yesVote.isPressed()) {
            
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    @FXML
    public void logout(ActionEvent event) {
        //send log out request
        loginStage(event);
    }

    public void loginStage(ActionEvent event) {
        try {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/ClientViews/Login.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
