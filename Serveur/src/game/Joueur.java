package game;

import java.io.IOException;

import tools.IO;
import core.Connexion;

public class Joueur {

	private final String username;
	private Integer score; // SEE not a good idea?

	// There?? Regroup in connexion object?
	private final Connexion connexion;

	private JoueurRole roleCourrant;
	
	// TODO: autres variables à créer!
	

	public Joueur(Connexion client, String login) throws IOException {
		connexion = client;
		username = login;
		score = 0; // BONUX: refine when recreating client.
		
		roleCourrant = JoueurRole.indéterminé;

	}

	public String toString() {
		return "Joueur:" + username
		// +" [host:"+this.getHost()+"]"
		;
	}

	// GETTER/SETTER
	public String getUsername() {
		return this.username;
	}

	public JoueurRole getRoleCourrant() {
		return roleCourrant;
	}

	public void setRoleCourrant(JoueurRole roleCourrant) {
		this.roleCourrant = roleCourrant;
	}
	
	
	// SEND, READ, and Close
	public void send(String command) {
		try {
			connexion.send(command + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// ASK? is getConnexion a good practice?

		public String readCommand() throws IOException {
			return connexion.getCommand();
		}

	public void close() {
		try {
			connexion.close();
			IO.trace("Connexion joueur " + this.username
					+ " viens d'etre fermé");

			// TODO Trace. (faire un level d'importance?)

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

	
	
	
}
