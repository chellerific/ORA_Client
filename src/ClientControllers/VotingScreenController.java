/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientControllers;

import Encryption.AdHoPuK;
import java.io.IOException;
import java.math.BigInteger;
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
import ora_client.AuthenticatorClient;
import ora_client.ClientConnectionManager;
import ora_client.MessageUtils;

/**
 * FXML Controller class
 *
 * @author student
 */
public class VotingScreenController implements Initializable {

    @FXML
    private Button downloadQtn, yesVoteBtn, noVoteBtn, logOut;

    @FXML
    private TextArea questionTextArea;

    @FXML
    Label errorLabel, hasVoted;

    @FXML
    public void downloadQstn() {
        if (questionTextArea.getText().isEmpty()) {
            MessageUtils.sendMessage(ClientConnectionManager.sslSocket, "get_question");
            String result = MessageUtils.receiveMessage(ClientConnectionManager.sslSocket);
            questionTextArea.setText(result);

        }
    }

    public void submitVote() {
        if (yesVoteBtn.isPressed()) {
            System.out.println("Yes");
            BigInteger yes = new BigInteger("1");
            AdHoPuK puk = new AdHoPuK();
            puk.init(AdHoPuK.Cipher.ENCRYPT_MODE, puk.getPublicKey());
            BigInteger yesVote = puk.doFinal(yes);
            MessageUtils.sendMessage(ClientConnectionManager.sslSocket, "submit_vote");
            MessageUtils.sendMessage(ClientConnectionManager.sslSocket, yesVote.toString());
            updateStatus();
            yesVoteBtn.setDisable(true);
            noVoteBtn.setDisable(true);
        } else if (noVoteBtn.isPressed()) {
            System.out.println("No");
            BigInteger no = new BigInteger("0");
            AdHoPuK puk = new AdHoPuK();
            puk.init(AdHoPuK.Cipher.ENCRYPT_MODE, puk.getPublicKey());
            BigInteger noVote = puk.doFinal(no);
            MessageUtils.sendMessage(ClientConnectionManager.sslSocket, "submit_vote");
            MessageUtils.sendMessage(ClientConnectionManager.sslSocket, noVote.toString());
            updateStatus();
            noVoteBtn.setDisable(true);
            yesVoteBtn.setDisable(true);

        }
    }

    private void updateStatus() {
        MessageUtils.sendMessage(ClientConnectionManager.sslSocket, "update_status");
        MessageUtils.sendMessage(ClientConnectionManager.sslSocket, AuthenticatorClient.username);
        String result = MessageUtils.receiveMessage(ClientConnectionManager.sslSocket);
        if (result.equals("ACK")) {
            hasVoted.setText("Thank you for voting.");
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
