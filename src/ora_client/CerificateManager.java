/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ora_client;

/**
 *
 * @author pibit
 */
public class CerificateManager {
    
    
    public void AddCertificate(){
        System.setProperty("javax.net.ssl.trustStore","voter.ks");
        System.setProperty("javax.net.ssl.trustStorePassword", "123456");
    
    }
    
    private void createKeyStore(){
        
    }
    
}
