package sistemaCifrado;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;


import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;

import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

public class BrowseListener {


Cipher ecipher;
Cipher dcipher;

private long startTime = 0;
private long stopTime = 0;

public void start() {
    this.startTime = System.currentTimeMillis();
}


public void stop() {
    this.stopTime = System.currentTimeMillis();
    System.out.println("Tiempo tardado: " + (stopTime - startTime) + "ms");
}


public BrowseListener(SecretKey key)
{
    // Create an 8-byte initialization vector
    byte[] iv = new byte[]
    {
    0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06,
0x07, 0x08, 0x09,0x0a,            0x0b, 0x0c,
0x0d, 0x0e, 0x0f
    };

    AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
    try
    {
        ecipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        dcipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

        // CBC requires an initialization vector
        ecipher.init(Cipher.ENCRYPT_MODE, key/*, paramSpec*/);
        dcipher.init(Cipher.DECRYPT_MODE, key/*, paramSpec*/);
    }
    catch (Exception e)
    {
        e.printStackTrace();
    }
}

// Buffer used to transport the bytes from one stream to another
    byte[] buf = new byte[1024];

    public void encrypt(InputStream in, OutputStream out)
    {
        try
        {
            // Bytes written to out will be encrypted
            out = new CipherOutputStream(out, ecipher);

            // Read in the cleartext bytes and write to out to encrypt
            int numRead = 0;
            while ((numRead = in.read(buf)) >= 0)
            {
                out.write(buf, 0, numRead);
            }
            out.close();
        }
        catch (java.io.IOException e)
        {
        }
    }

    public void decrypt(InputStream in, OutputStream out)
    {
        try
        {
            // Bytes read from in will be decrypted
            in = new CipherInputStream(in, dcipher);

        // Read in the decrypted bytes and write the cleartext to out
            int numRead = 0;
            while ((numRead = in.read(buf)) >= 0)
            {
                out.write(buf, 0, numRead);
            }
            out.close();
        }
        catch (java.io.IOException e)
        {
        }
    }

public BrowseListener() throws NoSuchAlgorithmException, FileNotFoundException {
    
}




private static void lanzar() throws NoSuchAlgorithmException, FileNotFoundException{                                               
    

    // Process the results.

        KeyGenerator  kgen    =   KeyGenerator.getInstance("AES");
        kgen.init(128);
        SecretKey key  =   kgen.generateKey();

        // Create encrypter/decrypter class
        BrowseListener encrypter = new BrowseListener(key);


       BrowseListener s = new BrowseListener();

        s.start(); // timer starts
        
        encrypter.encrypt(new FileInputStream("C:\\Users\\Usuario\\Desktop\\1000kb2.jpg"), new  FileOutputStream("C:\\Users\\Usuario\\Desktop\\encriptado"));
        s.stop();
        // Decrypt
        s.start();
        encrypter.decrypt(new FileInputStream("C:\\Users\\Usuario\\Desktop\\encriptado"),new FileOutputStream("C:\\Users\\Usuario\\Desktop\\desencriptado"));
        s.stop(); // timer stops

    

}  


public static void main(String[] args) throws 
NoSuchAlgorithmException, FileNotFoundException {

    lanzar();


}
}
