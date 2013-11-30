package core;

import tools.IO;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

/**
 * Laucher Class. (JCommander Options Handling)
 * 
 * @author adriean
 * 
 */
public class ASSketchServer {

	public static final Options options = new Options();

	protected static String[] mainArguments = new String[0];

	public static void main(final String[] arguments) throws Exception {

		mainArguments = arguments;
		options.parse(mainArguments);

		if (options.debug) {
			System.out.println(options);
			IO.turnOnDebugMode();
		}

		System.out.println("Lancement du serveur AssScketch");
		Server asServer = new Server();
		asServer.start();
		asServer.join();
		IO.trace("Arret du serveur AssScketch");
	}

	/**
	 * Options De asSketch TODO Add more!!! HERE: singleton mode!!
	 */
	public static class Options {
		// Nota: visibilité default pour les options initialisant le serveur

		@Parameter(names = { "max", "--max" }, description = "Nombre maximum de joeurs")
		Integer nbJoueurs = 2; // TODO change once test over.

		@Parameter(names = { "port", "--port" }, description = "Port à utiliser")
		Integer port = 2013;

		@Parameter(names = { "dico", "--dico" }, description = "Les mots à utiliser")
		String dicoFile = "dicotest"; // Dico test
		// TODO: location to handle!! (mis à la racine pour l'instant)

		@Parameter(names = "--comptes", description = "Le fichier des comptes")
		String comptesFile = "comptes.ser";
		// TODO: location to handle!! (mis à la racine pour l'instant)

		// Timing options
		@Parameter(names = { "timeout", "--timeout" }, description = "Délai après découverte d'un mot")
		Integer tfound = 180;

		@Parameter(names = { "timeround", "--timeround" }, description = "Durée Max partie")
		Integer tround = 30;

		@Parameter(names = { "timepause", "--timepause" }, description = "Temps de pause entre parties")
		Integer tpause = 5;

		@Parameter(names = { "nbwarn", "--nbwarn" }, description = "Nb warn to exlude a player")
		Integer nbCheatWarn = 3;

		@Parameter(names = { "-d", "--daemon" }, description = "Serveur enchaine les parties (non oneshot)")
		boolean daemon = false;

		@Parameter(names = { "d", "debug", "--debug" }, description = "Mode debug", hidden = true)
		boolean debug = false;

		@Parameter(names = { "-h", "--help" }, help = true, hidden = true)
		private boolean help;

		@Parameter(names = { "-a", "--actionscript" }, help = true, hidden = true)
		boolean actionMode;

		public void parse(String[] arguments) {
			JCommander jcp = new JCommander(this, arguments);
			// TODO: ? pas très propre?
			if (options.help) {
				jcp.setProgramName("assKetch");
				jcp.usage();
				System.exit(0);
			}
			// TODO: better handler or Wrong parameter error
		}

		@Override
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("Options:");
			sb.append("\nmax " + nbJoueurs);
			sb.append("\nport " + port);
			sb.append("\ndico " + dicoFile);
			return sb.toString();
		}
	}

}
