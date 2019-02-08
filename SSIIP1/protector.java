package hids;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class protector {

	static String initialhash = "";
	static String hashLogsDentro = "";
	static String hashLogsFuera = "";

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

//Fichero (de texto o de lo que sea) que queremos proteger
		File file = new File("C:\\Users\\Usuario\\Desktop\\controlar\\proteger.txt");

//Lugar donde irán los Logs del programa
		File file2 = new File("C:\\Users\\Usuario\\Desktop\\logs.txt");

//Archivo que también comprobará que los Logs no hayan sido modificados por un tercero, deberá estar escondido lo mejor posible.
		File file3 = new File("C:\\Users\\Usuario\\Desktop\\controlar\\logs.txt");

		MessageDigest shaDigest = MessageDigest.getInstance("SHA-256");
		// Cogeremos los hash iniciales de cada fichero. En el caso de que algún fichero
		// sea modificado, el hash también lo hará. Iremos comprobando la integridad de
		// los hash cada "x" tiempos
		initialhash = getFileChecksum(shaDigest, file);
		hashLogsFuera = getFileChecksum(shaDigest, file2);
		hashLogsDentro = getFileChecksum(shaDigest, file3);

		System.out.println("initial hash: " + initialhash);
		System.out.println("log dentro hash: " + hashLogsDentro);
		System.out.println("log fuera  hash: " + hashLogsFuera);

//Función que lanza el check para que revisar los hash
		temp();

	}

	private static String getFileChecksum(MessageDigest digest, File file) throws IOException {
		// Get file input stream for reading the file content
		FileInputStream fis = new FileInputStream(file);

		// Create byte array to read data in chunks
		byte[] byteArray = new byte[1024];
		int bytesCount = 0;

		// Read file data and update in message digest
		while ((bytesCount = fis.read(byteArray)) != -1) {
			digest.update(byteArray, 0, bytesCount);
		}
		;

		// close the stream; We don't need it now.
		fis.close();

		// Get the hash's bytes
		byte[] bytes = digest.digest();

		// This bytes[] has bytes in decimal format;
		// Convert it to hexadecimal format
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
		}

		// return complete hash
		return sb.toString();
	}

	private static void temp() {

		new Thread(new Runnable() {
			public void run() {
				try {
					while (true) {
						comparador();
						// Cada día se lanzará el activador (está en segundos la cantidad)
						Thread.sleep(86400);
					}
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
			}
		}).start();

	}

	private static void comparador() {

		File file = null;
		FileReader fr = null;
		BufferedReader br = null;
		String compruebaLogDentro = "";
		String compruebaLogFuera = "";

		try {
			// Apertura del fichero y creacion de BufferedReader para poder
			// hacer una lectura comoda (disponer del metodo readLine()).
			file = new File("C:\\Users\\Usuario\\Desktop\\controlar\\proteger.txt");
			fr = new FileReader(file);
			br = new BufferedReader(fr);

			File file2 = new File("C:\\Users\\Usuario\\Desktop\\logs.txt");
			File file3 = new File("C:\\Users\\Usuario\\Desktop\\controlar\\logs.txt");

			// Lectura del fichero
			String linea;
			// while((linea=br.readLine())!=null)
			// System.out.println(linea);

			// Use sha algorithm
			MessageDigest shaDigest = MessageDigest.getInstance("SHA-256");

			// Get the checksum
			String checksum = getFileChecksum(shaDigest, file);
			// see checksum
			System.out.println("checksum: " + checksum);

			compruebaLogDentro = getFileChecksum(shaDigest, file3);
			compruebaLogFuera = getFileChecksum(shaDigest, file2);

			if (checksum.equals(initialhash)) {

				Date date = new Date();
				DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

				BufferedWriter out = null;
				BufferedWriter out2 = null;

				// antes de escribir comprobamos los logs

				if (!compruebaLogDentro.equals(hashLogsDentro) && !compruebaLogFuera.equals(hashLogsFuera)) {

					System.out.println("Los dos logs han sido modificados, ERROR GRAVE");

				} else if (!compruebaLogDentro.equals(hashLogsDentro)) {

					System.out.println("El log de dentro ha sido modificado");

					out = new BufferedWriter(new FileWriter("C:\\Users\\Usuario\\Desktop\\logs.txt", true));
					out.write("ERROR archivo modificado en la fecha: " + dateFormat.format(date));
					out.newLine();
					out.close();

					File origen = new File("C:\\Users\\Usuario\\\\Desktop\\logs.txt");
					File destino = new File("C:\\Users\\Usuario\\Desktop\\controlar\\logs.txt");
//Como el log de dentro(el primero) ha sido modificado, lo que hacemos es volcar el contenido del log exterior a éste, para recuperar los datos que hayan podido ser borrados/modificados
					try {
						InputStream in = new FileInputStream(origen);
						OutputStream out4 = new FileOutputStream(destino);

						byte[] buf = new byte[1024];
						int len;

						while ((len = in.read(buf)) > 0) {
							out4.write(buf, 0, len);
						}

						in.close();
						out4.close();
					} catch (IOException ioe) {
						ioe.printStackTrace();
					}

				} else if (!compruebaLogFuera.equals(hashLogsFuera)) {
//Igual que antes, pero ahora al revés
					System.out.println("El log de fuera ha sido modificado");
					out = new BufferedWriter(new FileWriter("C:\\Users\\Usuario\\Desktop\\controlar\\logs.txt", true));
					out.write("ERROR archivo modificado en la fecha: " + dateFormat.format(date));
					out.newLine();
					out.close();
					File origen = new File("C:\\Users\\Usuario\\Desktop\\controlar\\logs.txt");
					File destino = new File("C:\\Users\\Usuario\\Desktop\\logs.txt");
					try {
						InputStream in = new FileInputStream(origen);
						OutputStream out4 = new FileOutputStream(destino);

						byte[] buf = new byte[1024];
						int len;

						while ((len = in.read(buf)) > 0) {
							out4.write(buf, 0, len);
						}

						in.close();
						out4.close();
					} catch (IOException ioe) {
						ioe.printStackTrace();
					}

				}

				out = new BufferedWriter(new FileWriter("C:\\Users\\Usuario\\Desktop\\controlar\\logs.txt", true));
				out.write("OK: El archivo ha sido comprobado en la fecha: " + dateFormat.format(date) + " a las "
						+ hourFormat.format(date) + "  y no ha habido nuevas modificaciones");
				out.newLine();
				out.close();

				out2 = new BufferedWriter(new FileWriter("C:\\Users\\Usuario\\Desktop\\logs.txt", true));
				out2.write("OK: El archivo ha sido comprobado en la fecha: " + dateFormat.format(date) + " a las "
						+ hourFormat.format(date) + "  y no ha habido nuevas modificaciones");
				out2.newLine();
				out2.close();
				hashLogsDentro = getFileChecksum(shaDigest, file3);
				hashLogsFuera = getFileChecksum(shaDigest, file2);

			}
//Si el hash del archivo a proteger no es el inicial, significa que me lo han modificado
			if (!checksum.equals(initialhash)) {

				BufferedWriter out = null;
				BufferedWriter out2 = null;
//Hora de la detección de la modificación
				Date date = new Date();
				DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

				System.out.println("ALERTA: Se ha detectado una modificación en el archivo en el chequeo del día: "
						+ dateFormat.format(date));

				System.out.println("ALERTA, EL ARCHIVO A PROTEGER HA SIDO MODIFICADO");

				if (!compruebaLogDentro.equals(hashLogsDentro) && !compruebaLogFuera.equals(hashLogsFuera)) {

					System.out.println("Los dos logs han sido modificados, ERROR GRAVE");

				} else if (!compruebaLogDentro.equals(hashLogsDentro)) {

					System.out.println("El log de dentro ha sido modificado");

					out = new BufferedWriter(new FileWriter("C:\\Users\\Usuario\\Desktop\\logs.txt", true));
					out.write("ERROR archivo modificado en la fecha: " + dateFormat.format(date));
					out.newLine();
					out.close();

					File origen = new File("C:\\Users\\Usuario\\\\Desktop\\logs.txt");
					File destino = new File("C:\\Users\\Usuario\\Desktop\\controlar\\logs.txt");

					try {
						InputStream in = new FileInputStream(origen);
						OutputStream out4 = new FileOutputStream(destino);

						byte[] buf = new byte[1024];
						int len;

						while ((len = in.read(buf)) > 0) {
							out4.write(buf, 0, len);
						}

						in.close();
						out4.close();
					} catch (IOException ioe) {
						ioe.printStackTrace();
					}

				} else if (!compruebaLogFuera.equals(hashLogsFuera)) {

					System.out.println("El log de fuera ha sido modificado");

					System.out.println("El log de dentro ha sido modificado");

					out = new BufferedWriter(new FileWriter("C:\\Users\\Usuario\\Desktop\\controlar\\logs.txt", true));
					out.write("ERROR archivo modificado en la fecha: " + dateFormat.format(date));
					out.newLine();
					out.close();

					File origen = new File("C:\\Users\\Usuario\\Desktop\\controlar\\logs.txt");
					File destino = new File("C:\\Users\\Usuario\\Desktop\\logs.txt");
					try {
						InputStream in = new FileInputStream(origen);
						OutputStream out4 = new FileOutputStream(destino);

						byte[] buf = new byte[1024];
						int len;

						while ((len = in.read(buf)) > 0) {
							out4.write(buf, 0, len);
						}

						in.close();
						out4.close();
					} catch (IOException ioe) {
						ioe.printStackTrace();
					}

				}

				out2 = new BufferedWriter(new FileWriter("C:\\Users\\Usuario\\Desktop\\logs.txt", true));
				out2.write(
						"ALERTA: Se ha detectado una modificaci�n en el archivo durante el chequeo del d�a realizado el: "
								+ dateFormat.format(date));
				out2.newLine();
				out2.close();

				out = new BufferedWriter(new FileWriter("C:\\Users\\Usuario\\Desktop\\controlar\\logs.txt", true));
				out.write(
						"ALERTA: Se ha detectado una modificaci�n en el archivo durante el chequeo del d�a realizado el: "
								+ dateFormat.format(date));
				out.newLine();
				out.close();
				hashLogsDentro = getFileChecksum(shaDigest, file3);
				hashLogsFuera = getFileChecksum(shaDigest, file2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// En el finally cerramos el fichero, para asegurarnos
			// que se cierra tanto si todo va bien como si salta
			// una excepcion.
			try {
				if (null != fr) {
					fr.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

	}

}
