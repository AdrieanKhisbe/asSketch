package tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Tools to trace. 
 * RENAME
 * Imprime à
 * 
 */
public class IO {

	static private File logfile = null;
	static private BufferedWriter logwriter = null;

	public static void setLogFile(String file) {
		// EXC

		if (logwriter != null) {
			// ferme le flux

			try {
				logwriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		logfile = new File(file);
		// TODO test existance fichier, pour ne pas écraser?

		try {
			logwriter = new BufferedWriter(new FileWriter(logfile));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.err.println("Nouvelle Trace dans fichier " + file);

	}

	public static void trace(String message) {

		// Mise en forme du message. recupération thread et heure.

		// Test si log file ouvert.

		// Imprime message sur sortie standard et dans le log.

		System.out.println(message);
		if (logwriter != null) {
			try {
				logwriter.append(message);
				logwriter.append("\n");
				logwriter.flush();    // Lag?
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// TODO voir si ne peut pas plutot flusher
	}
	
	
	/**
	 * Ferme le logFile courant
	 */
	private static void endLogTrace() {
		try {
			logwriter.flush();
			logwriter.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logwriter = null;
		logfile = null;

		System.err.println("Fin de la trace ");
	}
	
	

	public static void main(String[] args) {
		IO.trace("a");
		IO.setLogFile("B");
		IO.trace("B in B");
		IO.endLogTrace();
		IO.trace("C");
	}



}
