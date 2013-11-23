package core;

import game.Dictionnaire;
import game.Joueur;
import game.Ligne;
import game.ListeJoueur;
import game.Role;
import game.Round;

import java.util.ArrayList;

import tools.IO;

public class GameManager extends Thread {
	// fusionné avec partie

	// BONUX Singleton pattern?

	private Server server;

	private ListeJoueur joueurs;
	private Dictionnaire dico;

	// FLAG
	private final Object wordFound; // HERE

	// TODO in game:
	// throw Illegal command exception...????

	// Rounds
	ArrayList<Round> rounds;
	Round tourCourrant;

	// SEE usefull?

	public GameManager(Server server, ListeJoueur joueurs, Dictionnaire dico) {
		this.setName("Game Manager");
		this.server = server;
		this.joueurs = joueurs;
		this.rounds = new ArrayList<>();
		this.tourCourrant = null;
		this.dico = dico;

		wordFound = new Object(); // used as sync var
	}

	public void run() {
			
		IO.trace("Démarrage Game Manager");
		Integer i = 1;

		// Détermine role de passage
		joueurs.figer();

		IO.trace("Début de la partie! ");
		IO.trace("Liste des joueurs: " + joueurs);

		for (Joueur dessinateur : joueurs.getOrdre()) {
			// Check si joueur ne s'est pas déconnecté entretemps
			if (joueurs.checkStillConnected(dessinateur)) {
				IO.trace("Nouveau Round n°" + i + ", dessinateur "
						+ dessinateur);
				manageRound(dessinateur);
				i++;

			} else {
				IO.trace("Round annulé, " + dessinateur.getUsername()
						+ "ayant quitté le jeu avant son tour");
			}

			//
			broadcastJoueurs(Protocol.newScoreGame(joueurs.getJoueurs()));
			broadcastJoueurs("GOODBYE/");
			// suppress game object/
			IO.trace("Avant joueur close!!");
			joueurs.close(); // LOCK induces FIXME
			IO.trace("Fini de Joueur!!");

		}

	}

	private void manageRound(Joueur dessinateur) {
		String mot = dico.getWord();
		// set roles
		ArrayList<Joueur> chercheurs = new ArrayList<>();
		for (Joueur j : joueurs.getJoueurs()) {
			if (j.equals(dessinateur)) {
				j.setRoleCourrant(Role.dessinateur);
				chercheurs.add(j);

			} else
				j.setRoleCourrant(Role.chercheur);
		}

		tourCourrant = new Round(dessinateur, chercheurs, mot);
		rounds.add(tourCourrant);

		// HERE
		dessinateur.send(Protocol.newRoundDesinateur(mot));
		String tmp = Protocol.newRoundChercheur(dessinateur);
		IO.trace(tmp);
		broadcastJoueursExcept(tmp, dessinateur);

		// TIMER LOCK

		// remet joueur etat indéterminé
		for (Joueur j : joueurs.getJoueurs()) {
			j.setRoleCourrant(Role.indéterminé);
		}

		IO.trace("Partie en cours");
		// SEE, order? INTERLOCK
		// FIXME

		// Gère résultats
		ArrayList<Joueur> trouveurs = tourCourrant.getTrouveurs();

		// TODO compute score : modifie dessinateur et trouveur
		if (!trouveurs.isEmpty()) {

			broadcastJoueurs(Protocol.newEndRound(trouveurs.get(0), mot));

			int i = 0;
			for (Joueur j : trouveurs) {
				j.addScore(10);
				i++;
			}
			dessinateur.addScore(10 + i - 1);
			// compilateur s'occupera de simplifier cela

		} else {
			// no winner

			broadcastJoueurs(Protocol.newEndRound(null, mot));

		}
		broadcastJoueurs(Protocol.newScoreRound(joueurs.getJoueurs()));

	}

	// Transmetteur
	// CHECK; still usefull? (ramener ptetre ceux du niveau serveur?
	public void broadcastJoueurs(final String message) {
		server.broadcastJoueurs(message);
		// leger surcout, mais bon, pas duplication code
	}

	public void broadcastJoueursExcept(final String message, final Joueur deaf) {
		server.broadcastJoueursExcept(message, deaf);
	}

	// /////////////////////
	// HERE TAT, méthode ou les game Joueur Handler envoient message!

	void tryGuess(Joueur j, String mot) {
		// CHECK in game statut
		// Si bonne suggestion
		if (tourCourrant.guess(mot)) {
			tourCourrant.setHasFound(j);

			broadcastJoueurs(Protocol.newWordFound(j));

			// HERE HANDLE timeout

		} else {
			broadcastJoueurs(Protocol.newGuess(mot));
			IO.trace("Guess infructuex de " + j + " : " + mot);
		}

		IO.trace("Joueur " + j + "suggere" + mot);

	}

	void addLigne(Integer x1, Integer y1,
	// SEE: surchage communication, traite ptetre pas au bon niveau. (mais à
	// vouloirdséparer donnée de envoi message)
			Integer x2, Integer y2) {

		Joueur d = tourCourrant.getDessinateur();

		Ligne l = tourCourrant.addLigne(x1, y1, x2, y2);
		broadcastJoueursExcept(Protocol.newLigne(l), d);
		IO.trace("Ligne ajoutée par " + d + ":" + l);
	}

	void setSize(Integer taille) {
		tourCourrant.setCurrentSize(taille);
		IO.trace("Taille dessin fixée à " + taille);
	}

	void setColor(Integer r, Integer g, Integer b) {
		tourCourrant.setCurrentColor(r, g, b);
		IO.trace("Taille dessin fixée à " + r + "/" + g + "/" + b + "/");
	}
	
}
