package core;

import game.Dictionnaire;
import game.Joueur;
import game.JoueurRole;
import game.Ligne;
import game.ListeJoueur;
import game.Partie;
import game.Round;

import java.util.ArrayList;
import java.util.Collections;

import tools.IO;

public class GameManager extends Thread {

	// BONUX Singleton pattern?

	private Server server;

	private ListeJoueur joueurs;
	private Partie game;
	private Dictionnaire dico;

	// FLAG
	private final Object wordFound; // HERE

	// SEE usefull?

	public GameManager(ListeJoueur joueurs, Dictionnaire dico) {
		this.setName("Game Manager");
		this.joueurs = joueurs;
		game = new Partie();
		this.dico = dico;

		wordFound = new Object(); // used as sync var
	}

	public void run() {
		IO.trace("Démarrage Game Manager");
		Integer i = 1;

		// Détermine role de passage
	joueurs.figer();

		IO.trace("Début de la partie! ");
		IO.trace("Liste des joueurs: "+ joueurs);

		for (Joueur dessinateur : joueurs.getOrdre()) {
			// Check si joueur ne s'est pas déconnecté entretemps
			if (joueurs.checkStillConnected(dessinateur)) {
				IO.trace("Nouveau Round n°" + i +", dessinateur "+dessinateur);
				manageRound(dessinateur);
				i++;

			} else {
				IO.trace("Round annulé, " + dessinateur.getUsername()
						+ "ayant quitté le jeu avant son tour");
			}

			//

			IO.trace("Fini de Joueur!!");

		}

	}

	private void manageRound(Joueur dessinateur) {
		String mot = dico.getWord();
		// set roles
		for(Joueur j : joueurs.getJoueurs()){
			if(j.equals(dessinateur)) j.setRoleCourrant(JoueurRole.dessinateur);
			else
				j.setRoleCourrant(JoueurRole.chercheur);
		}
		
	Round r = game.newRound(dessinateur, mot, wordFound);
	

		// HERE
	}

	// Transmetteur
	public void broadcastJoueurs(final String message) {
		server.broadcastJoueurs(message);
		// leger surcout, mais bon, pas duplication code
	}

	public void broadcastJoueursExcept(final String message, final Joueur deaf) {
		server.broadcastJoueursExcept(message, null);
	}
	
	public Partie getPartie(){
		return game;
	}
	
	
	///////////////////////
	// HERE TAT, méthode ou les game Joueur Handler envoient message!
	
	
	void tryGuess(Joueur j, String m){
		// HERE
		
		
		
	}
	
	
	void addLigne(Integer x1, Integer y1,
			// SEE: surchage communication, traite ptetre pas au bon niveau. (mais à vouloirdséparer donnée de envoi message)
		Integer x2, Integer y2){
		Round r = game.getTourCourrant();
		Joueur d = r.getDessinateur();
		
		Ligne l = r.addLigne(x1, y1, x2, y2);
		broadcastJoueursExcept(Protocol.newLigne(l), d); 
		IO.trace("Ligne ajoutée par "+d+":"+l);
	}
	
	void setSize(Integer taille){
		game.getTourCourrant().setCurrentSize(taille);
		IO.trace("Taille dessin fixée à "+taille);
	}
	void setColor(Integer r, Integer g, Integer b){
		game.getTourCourrant().setCurrentColor(r, g, b);
		IO.trace("Taille dessin fixée à "+r+"/"+g+"/"+b+"/");
	}
	
	void sendSuggestion(Joueur j, String mot){
		
		IO.trace("Joueur "+j+"suggere"+mot);
	}
	
	
	

}
