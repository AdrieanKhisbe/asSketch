package joueurs;

import java.io.IOException;

import tools.IO;
import core.Connexion;

public class Joueur {

	private final String username;
	private Integer score; 
	// TODO : scoreTotal

	// There?? Regroup in connexion object?
	private final Connexion connexion;

	private Role roleCourrant;

	// TODO: autres variables à créer!

	public Joueur(Connexion client, String login) throws IOException {
		connexion = client;
		username = login;
		score = 0; // BONUX: refine when recreating client.

		roleCourrant = Role.indéterminé;

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

	public Role getRoleCourrant() {
		return roleCourrant;
	}

	public void setRoleCourrant(Role roleCourrant) {
		this.roleCourrant = roleCourrant;
	}

	// SEND, READ, and Close
	public void send(String command) {
		try {
			connexion.send(command);
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
			IO.trace("Connexion du joueur " + this.username
					+ " viens d'etre fermé");
			// TODO Trace. (faire un level d'importance?)

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Integer getScore() {
		return score;
	}

	public void addScore(Integer score) {
		this.score += score;
	}
	
	
	
	

}
