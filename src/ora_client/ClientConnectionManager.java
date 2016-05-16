/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ora_client;

import java.io.BufferedReader;
import java.io.PrintWriter;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 *
 * @author Chelsi
 */
public class ClientConnectionManager {

    private final String strServerName = "127.0.0.1"; // SSL Server Name
    private final int intSSLport = 4443; // Port where the SSL Server is listening
    private PrintWriter out = null;
    private BufferedReader in = null;


    public void connect(String uname, String pass) {
        AuthenticatorClient authenticator = new AuthenticatorClient(uname, pass);

        System.setProperty("javax.net.ssl.trustStore", "src/resources/client.ks");
        System.setProperty("javax.net.ssl.trustStorePassword", "passwd");

        try {
            SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket sslSocket = (SSLSocket) sslsocketfactory.createSocket(strServerName, intSSLport);

            MessageUtils.sendMessage(sslSocket, "Client");
            String srvAnswer = MessageUtils.receiveMessage(sslSocket);
            if(srvAnswer.equalsIgnoreCase("GET USERNAME")){
                MessageUtils.sendMessage(sslSocket, authenticator.getUsername());
            }
            srvAnswer="";
            srvAnswer = MessageUtils.receiveMessage(sslSocket);
            if(!srvAnswer.isEmpty()){
                authenticator.acceptChallenge(srvAnswer);
            }
            MessageUtils.sendMessage(sslSocket, authenticator.sendChallengeAnswer());
            srvAnswer="";
            srvAnswer = MessageUtils.receiveMessage(sslSocket);
            if(srvAnswer.equalsIgnoreCase("OK")){
                
            }else if(srvAnswer.equalsIgnoreCase("FAIL")){
                
            }else{
                System.out.println("Incorrect password");
            }

            sslSocket.close();
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }
}
