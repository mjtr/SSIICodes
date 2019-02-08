package ssii2;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ServerSocketFactory;


public class IntegrityVerifierServer {
	
	public static void main( String args[] ) throws Exception 
	{
		IntegrityVerifierServer server = new IntegrityVerifierServer();
		server.runServer();
	}

	
	private ServerSocket serverSocket;
	static String alg = "HmacSHA256";

	// Constructor del Servidor
	public IntegrityVerifierServer() throws Exception  {       
		// ServerSocketFactory para construir los ServerSockets 
		ServerSocketFactory socketFactory = ( ServerSocketFactory ) ServerSocketFactory.getDefault(); 
		// Creación de un objeto ServerSocket escuchando peticiones en el puerto 7070
		serverSocket = (ServerSocket ) socketFactory.createServerSocket(7070);   
	} 
	// Ejecución del servidor para escuchar peticiones de los clientes
	private void runServer(){
		while (true) {
			// Espera las peticiones del cliente para comprobar mensaje/MAC 
			try {
				System.err.println(
						"Esperando conexiones de clientes...");
				Socket socket =  ( Socket ) serverSocket.accept();
				
				// Abre un BufferedReader para leer los datos del cliente
				BufferedReader input = new BufferedReader(new 
				InputStreamReader(socket.getInputStream()));
				
				// Abre un PrintWriter para enviar datos al cliente
				PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream() ) );
				
				// Se lee del cliente el mensaje y el macdelMensajeEnviado 
				
				String mensaje = input.readLine(); 
				
				System.out.println("El mensaje recibido del cliente es: " + mensaje);

				/*Comenzamos el chequeo del mensaje*/

				String macdelMensajeEnviado ="" ;
				String auxNonce ="" ;
				String texto = "";

				
				
				String[] aux  = mensaje.split("-");
				
				macdelMensajeEnviado = aux[0];
				auxNonce = aux[1];
				texto = aux[2];
				
				System.out.println("Mac del mensaje: " + macdelMensajeEnviado);
				System.out.println("Nonce: " + auxNonce);
				System.out.println("Mensaje: " + texto);

				//System.out.println("La NONCE DB contiene: " );
				//ssii2.Nonce.muestraNonceDB();	
				
				byte[] decodedKey = {65, 66, 67, 68, 69};
				String macdelMensajeCalculado = performMACTest(texto, decodedKey);
				
				/*comprobamos primero el NONCE que tenemos*/
				
				if(ssii2.Nonce.checkNonceDB(auxNonce) == false) {
					System.out.println("El nonce del mensaje no se encuentra en la BD de Nonces");
					output.println( "El mensaje no ha podido ser validado");
										
				}else {
					
					/*ahora comprobamos el mac, pero antes borramos el nonce para que no pueda volver a ser usado*/
					
					//System.out.println("La NonceDB tiene antes de borrar: ");
					//ssii2.Nonce.muestraNonceDB();
					
					ssii2.Nonce.deleteNonceDB(auxNonce);					
					//System.out.println("La NonceDB tiene ahora: ");
					//ssii2.Nonce.muestraNonceDB();

					if (macdelMensajeEnviado.equals(macdelMensajeCalculado)) {
						System.out.println("Mensaje " + texto + " procesado correctamente");
						output.println( "Mensaje enviado integro y correctamente " );
						} 
					else {
						System.out.println("EL HMAC no coincide, el mensaje ha sido alterado");
						output.println( "El mensaje original ha sido alterado, por favor, vuélvelo a enviar");
						} 
					output.close();  
					input.close();  
					socket.close();
					} 
					
				}	
			catch ( IOException ioException ) {   ioException.printStackTrace(); }
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