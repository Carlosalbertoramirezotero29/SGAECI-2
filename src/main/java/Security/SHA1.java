/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Security;

/**
 *
 * @author vivi-
 */

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA1 {

    private MessageDigest md;
    private byte[] buffer, digest;
    private String hash = "";
    AsignacionUser_password user= new AsignacionUser_password(); 
    String passxencrip= user.getPassword();//  contraseña asignada
    String userxencrip= user.getUsuario();// usuario asignado 

    public String getHash(String message) throws NoSuchAlgorithmException {
        buffer = message.getBytes();
        md = MessageDigest.getInstance("SHA1");
        md.update(buffer);
        digest = md.digest();

        for (byte aux : digest) {
            int b = aux & 0xff;
            if (Integer.toHexString(b).length() == 1) {
                hash += "0";
            }
            hash += Integer.toHexString(b);
        }
        return hash;
    }
    
    public String contraseña(String passxencrip) {
        SHA1 s = new SHA1();

        try {
            String passencrip=s.getHash(s.getHash(passxencrip));
            return passencrip; //Retorna la contraseña encriptada 
            
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "0";
    }
    public String usuario(String userxencrip) {
        SHA1 s = new SHA1();

        try {
            String Usuencrip= s.getHash(s.getHash(userxencrip));
            return Usuencrip;// Retorna el usuario encriptado 
            
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "0";
    }
}
