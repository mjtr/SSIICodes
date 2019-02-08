package ssii;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocketFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

public class BYODClient {

	static char[] password;

	public static void main(String[] args) throws UnknownHostException, IOException {
		System.setProperty("javax.net.ssl.trustStore", "za.store");
		Socket socket = ((SSLSocketFactory) SSLSocketFactory.getDefault()).createSocket("127.0.0.1", 7070);

		try {

			PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

			String userName = JOptionPane.showInputDialog(null, "Enter username:");

			JPanel panel = new JPanel();
			JLabel label = new JLabel("Enter a password:");
			JPasswordField pass = new JPasswordField(8);
			panel.add(label);
			panel.add(pass);
			String[] options = new String[] { "OK", "Cancel" };
			JOptionPane.showOptionDialog(null, panel, "The title", JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
					null, options, options[1]);

			password = pass.getPassword();

			String mensaje = userName + "," + new String(password);

			output.println(mensaje);
			output.flush();

			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			String respuesta = input.readLine();

			JOptionPane.showMessageDialog(null, respuesta);

			if (!respuesta.contains("Error")) {

				while (true) {
					String send = JOptionPane.showInputDialog(null,
							"Introduzca el mensaje para el servidor (introduzca 'quit' para cerrar la sesión):");
					output.println(send);
					output.flush();

					String mensajeLeido = input.readLine();
					if (mensajeLeido.equals("OUT"))
						break;

					JOptionPane.showMessageDialog(null, mensajeLeido);

				}
			}
			output.close();
			input.close();
			socket.close();
		} // end try
		catch (IOException ioException) {
			ioException.printStackTrace();
		}
		finally {
			System.exit(0);
		}

	}

}
