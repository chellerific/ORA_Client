/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ora_client;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Chelsi
 */
public class ConnectionManager {
    private final int portNumber = 8000;
//    private final String ipAdd = "192.168.0.11"; //CHelsi
    private final String ipAdd = "127.0.0.1"; //Karolis
    public static Socket connection;
    
    public void connect() {
        try {
            connection = new Socket(ipAdd, portNumber);
        } catch (IOException ex) {
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }
    
}
