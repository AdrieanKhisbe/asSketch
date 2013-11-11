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
		
		
		System.out.println("Arret du serveur AssScketch"); 
	}
	

	/**
	 * Options De asSketch
     */
	public static class Options {
		@Parameter(names = { "max", "--max" }, description = "Nombre maximum de joeurs")
		private Integer nbJouers = 4;

		@Parameter(names = { "port", "--port" }, description = "Port à utiliser")
		private Integer port = 2013;

		@Parameter(names = { "dico", "--dico" }, description = "Les mots à utiliser")
		private String dico = "test"; // Dico test

		@Parameter(names = { "timeout", "--timeout" }, description = "Délai après découverte d'un mot")
		private Integer timeout = 2013;

		@Parameter(names = { "d", "debug", "--debug" }, description = "Mode debug", hidden = true)
		private boolean debug = false;

		@Parameter(names = { "-h", "--help" }, help = true, hidden = true)
		private boolean help;

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
			sb.append("\nmax " + nbJouers);
			sb.append("\nport " + port);
			sb.append("\ndico " + dico);
			return sb.toString();
		}
	}

}
