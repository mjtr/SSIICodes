package ssii2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.net.SocketFactory;
import javax.swing.JOptionPane;

public class IntegrityVerifierClient {
	
	static String alg = "HmacSHA256";

	public static void main( String args[])
	{
		
		new IntegrityVerifierClient();
	}
	
	// Constructor que abre una conexión Socket para enviar mensaje/MAC al servidor
	public IntegrityVerifierClient(){     
		try { 
			
			SocketFactory socketFactory =  ( SocketFactory ) SocketFactory.getDefault();
			Socket socket = ( Socket ) socketFactory.createSocket("localhost", 7070 ); 
			
			// Crea un PrintWriter para enviar mensaje/MAC al servidor
		
			PrintWriter output = new PrintWriter(new OutputStreamWriter( socket.getOutputStream() ) );
			String userName = JOptionPane.showInputDialog(null,"Introduzca su mensaje:" );
			
			// Envío del mensaje al servidor
			String mensaje = userName;
			System.out.println("El mensaje del cliente ya ha sido enviado");
			
			/***El cliente ya ha escrito su mensaje, ahora tenemos que pedir el nonce, cifrar el mensaje
			 , concatenarlos y enviárselo al servidor
			  ***/
		
			String nonce = ssii2.Nonce.getNonce();
			
			//byte[] decodedKey = {65, 66, 68, 69};
			
			byte[] decodedKey = {65, 66, 67, 68, 69};

			String macdelMensaje = performMACTest(mensaje, decodedKey);			
			
//			String codificado= macdelMensaje  + nonce  + mensaje;
			
			String codificado= macdelMensaje  + "-" + nonce  + "-" + mensaje;

			
			/*Para los test*/
			
			
			
			//String mensajeAlter = "12345678910,12345678911,900";
			
			//String HashError= macdelMensaje  + nonce  + mensajeAlter;
	
			//String nonceError= macdelMensaje  + "qwasdcfa"  + mensaje;

			output.println(codificado); 
			output.flush();
			
			//System.out.println("el mensaje original es: " + codificado);
			//System.out.println("el mensaje alterado es:" + HashError);
			//System.out.println("La base de datos de Nonces contiene: ");
			//ssii2.Nonce.muestraNonceDB();
			/*****************/
			
			
			
			// Crea un objeto BufferedReader para leer la respuesta del servidor
			BufferedReader input = new BufferedReader(new InputStreamReader( socket.getInputStream ()) );
			
			// Lee la respuesta del servidor
			String respuesta = input.readLine();
			
			// Muestra la respuesta al cliente
			JOptionPane.showMessageDialog( null, respuesta);         
			// Se cierran las conexiones
			output.close();
			input.close();
			socket.close();
		} 
		catch ( IOException ioException ) { 
			ioException.printStackTrace(); 
		} 
		// Salida de la aplicacion 
		finally {
			System.exit( 0 );
		}
	}
	
	public static String performMACTest(String s, byte[] decodedKey) {
		String st = "";
		try {
			Mac mac = Mac.getInstance(alg);
			SecretKey key = new SecretKeySpec(decodedKey, 0, decodedKey.length,
					alg);
			mac.init(key);
			mac.update(s.getBytes());
			byte[] b = mac.doFinal();
			st = byteArrayToHexString(b);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return st;
	}
	
	
	public static String byteArrayToHexString(byte[] b) {
		String result = "";
		for (int i = 0; i < b.length; ++i)
			result += byteToHexString(b[i]);
		return result;
	}
	
	public static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}
	
	private static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6",
			"7", "8", "9", "a", "b", "c", "d", "e", "f" };
}
