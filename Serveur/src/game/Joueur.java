package game;

import java.io.IOException;

import tools.IO;
import core.Connexion;

public class Joueur {

	private final String username;
	private Integer score; // SEE not a good idea?

	// There?? Regroup in connexion object?
	private final Connexion connexion;

	// TODO: autres variables à créer!
	// -> role courant? (ou dans partie?)

	public Joueur(Connexion client, String login) throws IOException{
		connexion = client;
		username = login;
		score = 0; // BONUX: refine when recreating client.
		
	}
	
	
	public String toString() {
		return "Joueur:" + username
		// +" [host:"+this.getHost()+"]"
		;
	}
	public String getUsername(){
		return this.username;
	}
	

	public void send(String command) {
		try {
			connexion.send(command);
		} catch (IOException e) {
			// TODO Auto-generated catch block 
			e.printStackTrace();
		}
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

	
	// ASK? is getConnexion a good practice?
	
	public String readCommand() throws IOException{
		return connexion.getCommand();
	}


}
