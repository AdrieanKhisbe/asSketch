package core;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

/**
 * Laucher Class. (JCommander Options Handling)
 * 
 * @author adriean
 * 
 */
public class ASSketchServer {

	protected static final Options options = new Options();

	protected static String[] mainArguments = new String[0];

	public static void main(final String[] arguments) throws Exception {

		mainArguments = arguments;
		options.parse(mainArguments);

		if (options.debug)
			System.out.println(options);

		System.out.println("Lancement du serveur AssScketch"); 
		Server asServer = new Server(options);
		asServer.run();
		
		System.out.println("Arret du serveur AssScketch"); 
	}
	

	/**
	 * Options De asSketch
     */
	public static class Options {
		// Nota: visibilité default pour les options initialisant le serveur
		
		@Parameter(names = { "max", "--max" }, description = "Nombre maximum de joeurs")
		Integer nbJoueurs = 2;


		@Parameter(names = { "port", "--port" }, description = "Port à utiliser")
		Integer port = 2013;

		@Parameter(names = { "dico", "--dico" }, description = "Les mots à utiliser")
		String dico = "dicotest"; // Dico test
		// TODO: location to handle!! (mis à la racine pour l'instant)

		@Parameter(names = { "timeout", "--timeout" }, description = "Délai après découverte d'un mot")
		private Integer timeout = 2013;

		@Parameter(names = { "d", "debug", "--debug" }, description = "Mode debug", hidden = true)
		boolean debug = false;

		@Parameter(names = { "-h", "--help" }, help = true, hidden = true)
		private boolean help;
		
		// TODO ? mode no action scripty policy send?

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
			sb.append("\ndico " + dico);
			return sb.toString();
		}
	}

}
