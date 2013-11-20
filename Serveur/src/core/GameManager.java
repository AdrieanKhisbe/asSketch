package core;

import game.Dictionnaire;
import game.Joueur;
import game.Partie;

import java.util.ArrayList;
import java.util.Collections;

import tools.IO;

public class GameManager extends Thread {

	// BONUX Singleton pattern?

	private ArrayList<Joueur> joueurs;
	private Partie game;
	private Dictionnaire dico;
	private Joueur[] frozenJoueurs;

	// SEE usefull?

	public GameManager(ArrayList<Joueur> joueurs, Dictionnaire dico) {
		this.setName("Game Manager");
		game = new Partie();
		this.dico = dico;
		frozenJoueurs = null; // calculé après lancement de la partie
	}

	public void run() {
		Integer i = 1;

		// Détermine role de passage
		synchronized (joueurs) {
			Collections.shuffle(joueurs);
			frozenJoueurs = joueurs.toArray(new Joueur[joueurs.size()]);
		}

		IO.trace("Début de la partie! ");

		for (Joueur dessinateur : frozenJoueurs) {
			// Check si joueur ne s'est pas déconnecté entretemps
			if (joueurs.contains(dessinateur)) {
				IO.trace("Nouveau Round n°" + i);
				manageRound(dessinateur);
				i++;

			} else {
				IO.trace("Round annulé, " + dessinateur.getUsername()
						+ "ayant quitté le jeu avant son tour");
			}

			//

		}

	}

	private void manageRound(Joueur dessinateur) {
		String mot = dico.getWord();
		game.newRound(dessinateur, mot);
//HERE 
	}
}
