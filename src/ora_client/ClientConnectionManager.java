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


    public boolean connect(String uname, String pass) {
        AuthenticatorClient authenticator = new AuthenticatorClient(uname, pass);

        System.setProperty("javax.net.ssl.trustStore", "src/resources/client.ks");
        System.setProperty("javax.net.ssl.trustStorePassword", "passwd");

        try {
            SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket sslSocket = (SSLSocket) sslsocketfactory.createSocket(strServerName, intSSLport);

            //idenify that you are a voter client 
            MessageUtils.sendMessage(sslSocket, "Client");
            System.out.println("Client: Client");
            //get challenge parameters from server
            String srvAnswer = MessageUtils.receiveMessage(sslSocket);
            System.out.println("Server: "+srvAnswer);
            if(srvAnswer.equalsIgnoreCase("GET USERNAME")){
                //send username
                String un = authenticator.getUsername();
                System.out.println("Client :"+un);
                MessageUtils.sendMessage(sslSocket, un);
            }
            srvAnswer="";
            srvAnswer = MessageUtils.receiveMessage(sslSocket);
            if(srvAnswer.equals("NotFound")){
                return false;
            }
            System.out.println("Server: "+srvAnswer);
            if(!srvAnswer.isEmpty()){
                authenticator.acceptChallenge(srvAnswer);
            }
            MessageUtils.sendMessage(sslSocket, authenticator.sendChallengeAnswer());
            srvAnswer="";
            srvAnswer = MessageUtils.receiveMessage(sslSocket);
            if(srvAnswer.equalsIgnoreCase("OK")){
                System.out.println("Authentication successful");
                return true;
            }else if(srvAnswer.equalsIgnoreCase("FAIL")){
                System.out.println("Incorrect password");
                return false;
            }else{
                System.out.println("Authentication failed unknown reason");
                return false;
            }

        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return false;
    }
}
