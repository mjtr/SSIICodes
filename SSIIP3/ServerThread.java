package ssii;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerThread extends Thread {
	private static Map<String, String> usuarios = new HashMap<String, String>();
	private List<String> baseDatos = new ArrayList<String>();
	Socket socket;

	ServerThread(Socket socket) {

		this.socket = socket;
	}

	public void run() {

		try {
			cargaUsuarios();
			String userAux = "";
			PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			String clientResponse = input.readLine();

			String[] aux = clientResponse.split(",");

			if (usuarios.containsKey(aux[0])) {

				if (usuarios.get(aux[0]).equals(aux[1])) {
						
					userAux = aux[0];
					System.out.println("user  " + aux[0] + " is now connected to the server");
					System.out.println("Usuario y contraseña metidas correctamente");
					output.println("Correct");
					output.flush();
					while (true) {
						String mensaje = input.readLine();
						System.out.println(mensaje);
						String meter = "user: " + userAux + " send: " + mensaje;
						baseDatos.add(meter);
						if (!mensaje.isEmpty()) {

							if (mensaje.equals("quit")) {
								System.out.println(baseDatos);
								output.println("OUT");
								output.flush();
								break;
							}

							output.println("OK");
							output.flush();
							System.out.println("Mensaje procesado correctamente");
						}
					}
				}

			} else {

				output.println("Error, user or password are incorrect");
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void cargaUsuarios() {

		usuarios.put("manuel", "miminono");
		usuarios.put("alex", "hola");

	}
}
