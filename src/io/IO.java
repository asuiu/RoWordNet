package io;

import io.UnicodeBOMInputStream.BOM;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Class defining several Input/Output methods Note: call the static method
 * setVerbose(true) to inhibit output to stdout
 * 
 * @author Stefan Dumitrescu
 * @author Radu Petrut
 */
public class IO {

	/**
	 * Private variable that can be set true or false that enables or disables
	 * output of RoWordNetLib to the console. Default is true, set it with the
	 * static method IO.setVerbose(boolean).
	 */
	private static boolean verbose = true;

	/**
	 * @return the verbose variable, deciding whether RoWordNetLib will output
	 *         text to the console or not
	 */
	public static boolean isVerbose() {
		return verbose;
	}

	/**
	 * @param verbose
	 *            the verbose variable, deciding whether RoWordNetLib will
	 *            output text to the console or not
	 */
	public static void setVerbose(boolean verbose) {
		IO.verbose = verbose;
	}

	/**
	 * Opens a file for reading in UTF8 mode, skipping the Byte Order Mark, if
	 * set.
	 * 
	 * @param filePath
	 * @return BufferedReader object
	 * @throws IOException
	 */
	static public BufferedReader openFile(String filePath) throws IOException {
		if (!((File) new File(filePath)).exists())
			throw new IOException("File " + filePath + " does not exist!");

		FileInputStream f1 = new FileInputStream(filePath);
		UnicodeBOMInputStream ubis = new UnicodeBOMInputStream(f1);
		// skip BOM if present;
		if (ubis.getBOM() != BOM.NONE)
			ubis.skipBOM();
		InputStreamReader f2 = new InputStreamReader(ubis, "UTF-8");
		BufferedReader f3 = new BufferedReader(f2);
		return f3;
	}

	/**
	 * Opens an input stream for reading in UTF8 mode, skipping the Byte Order
	 * Mark, if set.
	 * 
	 * @param stream
	 * @return BufferedReader object
	 * @throws IOException
	 */
	static public BufferedReader openFile(InputStream stream) throws IOException {
		UnicodeBOMInputStream ubis = new UnicodeBOMInputStream(stream);
		// skip BOM if present;
		if (ubis.getBOM() != BOM.NONE)
			ubis.skipBOM();
		InputStreamReader f2 = new InputStreamReader(ubis, "UTF-8");
		BufferedReader f3 = new BufferedReader(f2);
		return f3;
	}

	/**
	 * Opens a file correctly (UTF8 mode, BOM ignored) and reads all contents
	 * into a String array
	 * 
	 * @param filePath
	 * @return a String array containing the file, line by line
	 * @throws IOException
	 */
	static public String[] readFile(String filePath) throws IOException {
		BufferedReader br = openFile(filePath);
		ArrayList<String> data = new ArrayList<>();
		String line;
		while ((line = br.readLine()) != null)
			data.add(line.trim());
		return (String[]) data.toArray(new String[0]);
	}

	/**
	 * Default wrapper for System.out.println(Object out)
	 * 
	 * @param out
	 *            - Object to be written to console
	 */
	public static void outln(Object out) {
		if (!verbose)
			return;
		System.out.print("\n" + out);
	}

	/**
	 * Default wrapper for System.out.println(). Prints a new line.
	 */
	public static void outln() {
		if (!verbose)
			return;
		System.out.print("\n");
	}

	/**
	 * Default wrapper for System.out.print(Object out)
	 * 
	 * @param out
	 *            - Object to be written to console
	 */
	public static void out(Object out) {
		if (!verbose)
			return;
		System.out.print(out);
	}

	/**
	 * Pad a string to the right
	 * 
	 * @param s
	 * @param n
	 * @return
	 */
	public static String padRight(String s, int n) {
		return String.format("%1$-" + n + "s", s);
	}

	/**
	 * Pad a string to the left
	 * 
	 * @param s
	 * @param n
	 * @return
	 */
	public static String padLeft(String s, int n) {
		return String.format("%1$" + n + "s", s);
	}

}
