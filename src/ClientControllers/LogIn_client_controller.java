/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientControllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ora_client.ClientConnectionManager;

/**
 * FXML Controller class
 *
 * @author student
 */
public class LogIn_client_controller implements Initializable {

    public static ClientConnectionManager connectionManager;

    @FXML
    private TextField usernameField;
    private PasswordField passwordField;
    private Button logInBtn;

    @FXML
    private void logInAction() {
        connectionManager = new ClientConnectionManager();
        connectionManager.connect(usernameField.getText(), passwordField.getText());
        //connectManager.connect("bitharis@mail.com", "myPass");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
