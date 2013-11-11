package core;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;


/**
 * Laucher Class. (JCommander Options Handling)
 * @author adriean
 *
 */
public class Main {

    protected static final Options options = new Options();

    protected static String[] mainArguments = new String[0];
       
        public static void main(final String[] arguments) throws Exception {
  	
    	System.out.println("Lancement du serveur AssScketch"); // change later
    	
    	mainArguments = arguments;
        options.parse(mainArguments);
    	
        System.out.println(options);
        System.out.println(options.nbJouers);
    }

    
    /**
     */
    public static class Options {
        @Parameter(names = { "max", "-max" }, description = "Nombre maximum de joeurs")
        private Integer nbJouers = 4;

        @Parameter(names = { "port", "-port" }, description = "Port à utiliser")
        private Integer port = 2013;

        @Parameter(names = {"dico", "-dico"}, description = "Les mots à utiliser" )
        private String dico = "toto"; // Dicto test
        
        @Parameter(names = { "timeout", "-timeout" }, description = "Délai après découverte d'un mot")
        private Integer timeout = 2013;

        public void parse (String[] arguments) {
            new JCommander(this, arguments);
        }

        @Override
        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append("Options:");
            sb.append("\nmax " + nbJouers );
            sb.append("\nport " + port);
            sb.append("\ndico "+ dico );
            return sb.toString();
        }
    }

	
	
	
	
}
