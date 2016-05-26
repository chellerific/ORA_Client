/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ora_client;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author Panagiotis Bitharis
 */
public class AuthenticatorClient {

    public static String username = "";
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
    private final int HASH_BIT_LENGTH = 256;
    //This number defines ow many times to run hash algorithm
    //when calculating the hashed password
    private final int hashIterationNumber = 1024;
    private String challAnswer="";

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
        challAnswer = challengeFunction();
        printAll();
        return challAnswer;
    }
    
    private String challengeFunction(){
        System.out.println("calculating challenge hash...");
        String challengeAnswer="";
        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec keySpec = new PBEKeySpec(hashedPass.toCharArray(), salt.getBytes(), random, HASH_BIT_LENGTH);
            byte[] hashValue = keyFactory.generateSecret(keySpec).getEncoded();

            for(int i=0; i<hashValue.length; i++){
                challengeAnswer = challengeAnswer + (Integer.toHexString(0xFF &hashValue[i]));
            }
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AuthenticatorClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(AuthenticatorClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return challengeAnswer;
    }
    
    private String calculatePasswordHash(String pass, String salt) throws InvalidKeySpecException{
        String hashPass="";
        System.out.println("salt bytes: "+salt.getBytes());
        System.out.println("Password: "+pass);
        System.out.println("pass.toCharArray()"+pass.toCharArray());
        
        System.out.println("Calculating paswword hash...");
        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec keySpec = new PBEKeySpec(pass.toCharArray(), salt.getBytes(), hashIterationNumber, HASH_BIT_LENGTH);
            byte[] hashValue = keyFactory.generateSecret(keySpec).getEncoded();
            for(int i=0; i<hashValue.length; i++){
                hashPass = hashPass + Integer.toHexString(0xff &hashValue[i]);
            }
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AuthenticatorClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hashPass;
    }
    
    private String[] tokenizeParametersFromServer(String params){
        System.out.println("tokenizing message...");
         String[] parameters;
        if(!params.isEmpty()){
            parameters = params.split(" ");
        }else{
            throw new IllegalArgumentException("Empty challenge parameters");
        }
        return parameters;
    }
    
    private void printAll(){
        System.out.println("\n------Client Data------");
        System.out.println("username: "+username);
        System.out.println("password: "+hashedPass);
        System.out.println("random number: "+random);
        System.out.println("salt: "+salt);
        System.out.println("challAnswer: "+challAnswer);
        System.out.println("timesToRunHash: "+hashIterationNumber);
        System.out.println("Hash bit length: "+HASH_BIT_LENGTH);
        System.out.println("------End Client Data--------");
    }

   

}
