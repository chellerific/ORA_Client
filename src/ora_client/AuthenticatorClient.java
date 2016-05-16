/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ora_client;

import com.sun.javaws.exceptions.InvalidArgumentException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author Panagiotis Bitharis
 */
public class AuthenticatorClient {

    private String username = "";
    private String password = "";
    //stores the salt sent from server
    private String salt = "";
    //stored the hashed password
    private String hashedPass="";
    //this number refers to the r sent from server 
    //it is random generated and is used for iterations of the f() function
    private int random;
    private String challengeParameters = "";
    //this number is the bit length of the hashed password
    private final int HASH_BIT_LENGTH = 512;
    //This number defines ow many times to run hash algorithm
    //when calculating the hashed password
    private final int hashIterationNumber = 1024;

    public AuthenticatorClient(String username, String password) {
        if (!username.isEmpty() && !password.isEmpty()) {
            this.username = username;
            this.password = password;
        }else{
            throw new IllegalArgumentException("Field username or password cannot be empty");
        }
    }
    
    public void acceptChallenge(String params){
        try {
            String[] parameters = tokenizeParametersFromServer(params);
            salt = parameters[0];
            random = Integer.parseInt(parameters[1]);
            hashedPass = calculatePasswordHash(password,salt);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(AuthenticatorClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }
    
    public String sendChallengeAnswer(){
        return challengeFunction();
    }
    
    private String challengeFunction(){
        StringBuilder challengeAnswer= new StringBuilder();
        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec keySpec = new PBEKeySpec(hashedPass.toCharArray(), salt.getBytes(), random, HASH_BIT_LENGTH);
            byte[] hashValue = keyFactory.generateSecret(keySpec).getEncoded();
            
            for(int i=0; i<hashValue.length; i++){
                challengeAnswer.append(Integer.toHexString(0xFF &hashValue[i]));
            }
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AuthenticatorClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(AuthenticatorClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return challengeAnswer.toString();
    }
    
    private String calculatePasswordHash(String pass, String salt) throws InvalidKeySpecException{
        StringBuilder hashedPass= new StringBuilder();
        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), hashIterationNumber, HASH_BIT_LENGTH);
            byte[] hashValue = keyFactory.generateSecret(keySpec).getEncoded();
            
            for(int i=0; i<hashValue.length; i++){
                hashedPass.append(Integer.toHexString(0xFF &hashValue[i]));
            }
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AuthenticatorClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hashedPass.toString();
    }
    
    private String[] tokenizeParametersFromServer(String params){
         String[] parameters;
        if(!params.isEmpty()){
            parameters = params.split(" ");
        }else{
            throw new IllegalArgumentException("Empty challenge parameters");
        }
        return parameters;
    }

   

}
