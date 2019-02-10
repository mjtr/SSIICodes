package hids;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.*;
import javax.crypto.spec.*;

public class Alumnos {

	static String alg = "HmacSHA1";
//A partir de un mensaje y de su código hash, tenemos que averiguar su clave
	public static String cifrado = "";
	public static String msg = "231456789 487654 500";
	private static String hash = "ab693e49b900b9c86a3a21d02fbb25efdd0bed87";

	private static ArrayList<Character> allowedChars = new ArrayList<Character>();

	public String password;
	private static String result;
	private static int maxLength = 4;
	private static int currentLength = 0;
	private static boolean found = false;
	private static final long startTime = System.currentTimeMillis();
	private static long endTime;
	private static boolean finish = false;

	public static void main(String[] args) throws NoSuchAlgorithmException {
		prueba();
		rellenaVariables();
		calc();
		show();

	}

	public static void prueba() {

		System.out.println("Mensaje         : " + msg);
		// Esta es la clave que deberá encontrar: ABCP
		byte[] decodedKey = { 65, 66, 67, 80 };

		// String resumen = performMACTest(msg, decodedKey);
		String resumen = performMACTest2(msg, new String(decodedKey));

		System.out.println(
				"Clave Hex       : " + byteArrayToHexString(decodedKey) + "\t\tString : " + new String(decodedKey));

		System.out.println("MAC             : " + resumen);

		// cifrado = resumen;

		cifrado = hash;
		System.out.println("El cifrado es :" + cifrado);
	}

	public static String performMACTest2(String s, String decodedKey) {
		String st = "";
		try {
			Mac mac = Mac.getInstance(alg);
			SecretKey key = new SecretKeySpec(decodedKey.getBytes(), 0, decodedKey.getBytes().length, alg);
			mac.init(key);
			mac.update(s.getBytes());
			byte[] b = mac.doFinal();
			st = byteArrayToHexString(b);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return st;
	}

	public static String performMACTest(String s, byte[] decodedKey) {
		String st = "";
		try {
			Mac mac = Mac.getInstance(alg);
			SecretKey key = new SecretKeySpec(decodedKey, 0, decodedKey.length, alg);
			mac.init(key);
			mac.update(s.getBytes());
			byte[] b = mac.doFinal();
			st = byteArrayToHexString(b);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return st;
	}

	private static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e",
			"f" };

	// Convierte un byte a una cadena hexadecimal
	public static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	// Convierte un array de bytes a una cadena hexadecimal
	public static String byteArrayToHexString(byte[] b) {
		String result = "";
		for (int i = 0; i < b.length; ++i)
			result += byteToHexString(b[i]);
		return result;
	}

	// this.hash = Alumnos.msg;
	public static void rellenaVariables() {
		allowedChars.clear();

		for (int i = 65; i < 91; i++)
			allowedChars.add((char) i);

		for (int i = 97; i < 127; i++)
			allowedChars.add((char) i);

		for (int i = 33; i < 65; i++)
			allowedChars.add((char) i);

		// System.out.println("Nuestro conjunto de caracteres es: " +
		// this.allowedChars);

	}

	public static void setLength(int length) {
		maxLength = length;

	}

	public static void calc() throws NoSuchAlgorithmException {
		for (int i = 0; i <= maxLength; i++) {
			if (force("", 0, i))
				break;

			currentLength++;
		}
	}

	private static boolean force(String str, int position, int length) throws NoSuchAlgorithmException {
		for (char chr : allowedChars) {
			showProgress(str + Character.toString(chr));
			// showProgress2(str + Character.toString(chr));

			if (position < length - 1) {
				force(str + Character.toString(chr), position + 1, length);
			}

			String aux = performMACTest2(msg, str + Character.toString(chr));

			if (cifrado.equals(aux)) {

				found = true;
				finish = true;
				result = str + Character.toString(chr);
				endTime = System.currentTimeMillis();

			}

			if (finish == true) {
				break;
			}

		}

		return found;
	}

	private static void showProgress(String str) {
		String find_msg = String.format("buscando en %s caracteres (%s)", currentLength, str);
		System.out.print("\r" + find_msg);
	}
	/*
	 * private static void showProgress2(String str) { //String find_msg =
	 * String.format("buscando en %s caracteres (%s)", currentLength, str);
	 * System.out.print("\r" + str.getBytes()); }
	 */

	public static void show() {
		System.out.println(new Object() {

			public String toString() {
				if (found)
					return String.format("\npassword encontrado!: '%s'\ntiempo de busqueda: %d s\n", result,
							(endTime - startTime) / 1000);

				return "\rnot found";
			}
		});
	}

}
