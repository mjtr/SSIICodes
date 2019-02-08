package ssii;

import java.io.IOException;
import java.net.ServerSocket;

import javax.net.ssl.SSLServerSocketFactory;

public class BYODServer {

	public static void main(String[] args) throws IOException {
		System.setProperty("javax.net.ssl.keyStore", "za.store");
//		System.setProperty("javax.net.ssl.keyStore", "C://za.store");

		System.setProperty("javax.net.ssl.keyStorePassword", "miminono");

		ServerSocket serverSocket = ((SSLServerSocketFactory) SSLServerSocketFactory.getDefault())
				.createServerSocket(7070);

		System.out.println("Server up & ready for connection...");

		while (true)
			new ServerThread(serverSocket.accept()).start();

	}

}
