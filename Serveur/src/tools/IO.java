package tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Tools to trace. RENAME Imprime à
 * 
 */
public class IO {

	static private File logfile = null;
	static private BufferedWriter logwriter = null;
	static private boolean printTime = true; //TODO
	static private boolean printThread = true;
	static private boolean debugMode = true;

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

				synchronized (logwriter) {
					logwriter.append(message);
					logwriter.append("\n");
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// TODO voir si ne peut pas plutot flusher
	}

	
	// WONDER: bonne idée? SEARCH annotation debug?
	public static void traceDebug(String message){
	   if (debugMode){
		   trace(message);
	   }
	}
	
	
	
	/**
	 * Ferme le logFile courant
	 */
	public static void endLogTrace() {
		if(logwriter == null ) return;
		
		try {
			synchronized (logwriter) {
				logwriter.flush();
				logwriter.close();
				logwriter = null;
				logfile = null;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.err.println("Fin de la trace ");
	}

	/**
	 * Flush logFile courant
	 */
	public static void flushLogTrace() {
		if(logwriter == null ) return;
		
		try {
			synchronized (logwriter) {
				logwriter.flush();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
