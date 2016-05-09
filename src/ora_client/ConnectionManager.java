/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ora_client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 *
 * @author Chelsi
 */
public class ConnectionManager {

//    private final int portNumber = 8000;
////    private final String ipAdd = "192.168.0.11"; //CHelsi
//    private final String ipAdd = "127.0.0.1"; //Karolis
//    public static Socket connection;
    private final String strServerName = "127.0.0.1"; // SSL Server Name
    private final int intSSLport = 4443; // Port where the SSL Server is listening
    private PrintWriter out = null;
    private BufferedReader in = null;

    public void connect() {

        System.setProperty("javax.net.ssl.trustStore","src/resources/client.ks");
        System.setProperty("javax.net.ssl.trustStorePassword", "passwd");
        
        try {
            // Creating Client Sockets
            SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket sslSocket = (SSLSocket) sslsocketfactory.createSocket(strServerName, intSSLport);

            // Initializing the streams for Communication with the Server
            out = new PrintWriter(sslSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));

            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            String userInput = "Hello Testing";
            out.println(userInput);

            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
                System.out.println("echo: " + in.readLine());
            }

            out.println(userInput);

            // Closing the Streams and the Socket
            out.close();
            in.close();
            stdIn.close();
            sslSocket.close();
        } catch (Exception exp) {
            System.out.println(" Exception occurred .... " + exp);
            exp.printStackTrace();
        }
    }
    
    

}
