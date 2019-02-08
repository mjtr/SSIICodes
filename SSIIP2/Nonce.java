package ssii2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.Random;

public class Nonce {


    public String nextString() {
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        return new String(buf);
    }

    public static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final String lower = upper.toLowerCase();

    public static final String digits = "0123456789";

    public static final String alphanum = upper + lower + digits;
    
    private final Random random;
    
    private final char[] symbols;

    private final char[] buf;

    public Nonce(int length, Random random, String symbols) {
        if (length < 1) throw new IllegalArgumentException();
        if (symbols.length() < 2) throw new IllegalArgumentException();
        this.random = Objects.requireNonNull(random);
        this.symbols = symbols.toCharArray();
        this.buf = new char[length];
    }

    public Nonce(int length, Random random) {
        this(length, random, alphanum);
    }

    public Nonce(int length) {
        this(length, new SecureRandom());
    }
    
    public static String getNonce() throws IOException {
    	Nonce aux = new Nonce(12);
    	String ciffer = aux.nextString();
    	writeNonceDB(ciffer);
    	return ciffer;
    }
     
    
    private static void writeNonceDB(String nonce) throws IOException{
    	
    	BufferedWriter out = null;
   	 	out = new BufferedWriter(new FileWriter("NonceDB.txt", true));   
    	 //out = new BufferedWriter(new FileWriter("C:\\Users\\Usuario\\Desktop\\NonceDB.txt", true));   
		    out.write(nonce);   
		    out.newLine();
		    out.close();
    	
    }
    
    
    public static boolean checkNonceDB(String nonce) throws IOException {
    	boolean res = false;
    	//File file2 = new File ("C:\\\\Users\\\\Usuario\\\\Desktop\\\\NonceDB.txt");

    	File file2 = new File ("NonceDB.txt");
    	 String cadena;
         FileReader f = new FileReader(file2);
         BufferedReader b = new BufferedReader(f);
        
         while((cadena = b.readLine())!=null) {
             if(cadena.compareTo(nonce) == 0) {
            	 //significa que el nonce esta en la base de datos
            	 res = true;
            	 
             }
         }
         b.close();
    	
    	return res;
    }
    
    
    public static void deleteNonceDB(String nonce) throws IOException {
    	
    	BufferedWriter out = null;
    	
    	 out = new BufferedWriter(new FileWriter("NonceDB.txt", true));   
		 
		    
    	File file2 = new File ("NonceDB.txt");
    	 String cadena;
         FileReader f = new FileReader(file2);
         BufferedReader b = new BufferedReader(f);
        
         
         
         while((cadena = b.readLine())!=null) {
             if(cadena.compareTo(nonce) == 0) {
            	 //significa que el nonce esta en la base de datos, hay que borrarlo
            	// file2.delete();
            	 FileWriter f2 = new FileWriter(file2, false);
                 f2.write("");
                 f2.close();
            	 
             }
         }
		 out.close();
		 
         b.close();
    	
    }
    
    
  public static void muestraNonceDB() throws FileNotFoundException, IOException {
        
    	String cadena;
    	FileReader f = new FileReader("NonceDB.txt");
        //FileReader f = new FileReader("C:\\\\Users\\\\Usuario\\\\Desktop\\\\NonceDB.txt");
        BufferedReader b = new BufferedReader(f);
        while((cadena = b.readLine())!=null) {
            System.out.println(cadena);
        }
        b.close();
  }
    
    

}
	
	
	
	
	

