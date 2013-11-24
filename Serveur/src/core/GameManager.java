package core;

import game.Dictionnaire;
import game.Joueur;
import game.Ligne;
import game.ListeJoueur;
import game.Role;
import game.Round;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import tools.IO;

public class GameManager extends Thread {
	private static final int TROUND = 1000 * 60;
	private static final int TFOUND = 1000 * 5;
	private static final int TPAUSE = 1000 * 5;
	private static final int NBCHEATWARN = 3;
	// fusionné avec partie

	// BONUX Singleton pattern?

	private Server server;

	private ListeJoueur joueurs;
	private Dictionnaire dico;

	// Timer
	private final ExecutorService timer;
	private final Object endRound; // HERE : switch to atomic (utilisé quand
									// partie anulée)
	// ou objet etat.
	private final AtomicBoolean wordFound;
	private final Runnable timerGame;
	private final Runnable timerFound;

	// Cheat Warning

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

		this.timer = Executors.newFixedThreadPool(2);
		this.wordFound = new AtomicBoolean(false); // used as sync var
		this.endRound = new Object();

		this.timerFound = new Runnable() {
			@Override
			public void run() {
				try {
					synchronized (wordFound) {
						wordFound.wait();
					}
					IO.trace("Début timer motTrouvé de Xs");
					Thread.sleep(TFOUND);

					IO.trace("Temps écoulé");
					synchronized (endRound) {
						endRound.notify();
					}
				} catch (InterruptedException e) {
					IO.traceDebug("Timer Found Interrompu");
				}
			}
		};
		this.timerGame = new Runnable() {
			@Override
			public void run() {
				try {
					IO.trace("Début timer tour de Xs");
					Thread.sleep(TROUND);
					IO.trace("Temps écoulé");
					synchronized (endRound) {
						endRound.notify();
					}
				} catch (InterruptedException e) {
					IO.traceDebug("Timer Round Interrompu");
				}
			}
		};
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

				// Pause entre parties
				try {
					Thread.sleep(TPAUSE);
				} catch (InterruptedException e) {
					IO.traceDebug("Jeu interrompu (ne devrait pas avoir lieu)");
				}

			} else {
				IO.trace("Round annulé, " + dessinateur.getUsername()
						+ "ayant quitté le jeu avant son tour");
			}

		}
		//
		broadcastJoueurs(Protocol.newScoreGame(joueurs.getJoueurs()));
		broadcastJoueurs("GOODBYE/");
		// suppress game object/
		IO.trace("Avant joueur close!!");
		joueurs.close(); // LOCK induces FIXME

		timer.shutdown();
		IO.trace("Fini de Joueur!!");

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

		// Crée nouveau objet tour.
		tourCourrant = new Round(dessinateur, chercheurs, mot);
		rounds.add(tourCourrant);

		// Met en place le timer
		wordFound.set(false);
		Future<?> futureGame = timer.submit(timerGame);
		Future<?> futureFound = timer.submit(timerFound);

		// alt with futures.
		// http://stackoverflow.com/questions/2275443/how-to-timeout-a-thread

		// Avertit début du tour
		dessinateur.send(Protocol.newRoundDesinateur(mot));

		broadcastJoueursExcept(Protocol.newRoundChercheur(dessinateur),
				dessinateur);

		IO.trace("Partie en cours");
		synchronized (endRound) {
			try {
				endRound.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// TODO traitement supplémentaire
			// get raison fin

		}
		IO.trace("Fin du round");
		// arrete les timer si tournent encore
		futureGame.cancel(true);
		futureFound.cancel(true);

		// remet joueur etat indéterminé
		for (Joueur j : joueurs.getJoueurs()) {
			j.setRoleCourrant(Role.indéterminé);
		}

		// ---------------
		// Gère résultats
		ArrayList<Joueur> trouveurs = tourCourrant.getTrouveurs();

		// compute score
		// TODO handle skip, quit,
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
	// Méthodes ou les game Joueur Handler envoient message!
	// TODO: syncrhonized to change. (so delete synchronize block) Voir

	void tryGuess(Joueur j, String mot) {
		// CHECK in game statut

		// Si bonne suggestion
		if (tourCourrant.guess(mot)) {
			IO.trace("Guess réussi de " + j + " : " + mot);

			tourCourrant.setHasFound(j);

			broadcastJoueurs(Protocol.newWordFound(j));

			// vérifie si tous n'ont pas trouvé
			if (!tourCourrant.stillSearching()) {
				synchronized (endRound) {
					endRound.notify();
				}
			} // handle timeout si mot pas déjà trouvé
			else if (!wordFound.get()) {
				synchronized (wordFound) {
					wordFound.set(true);
					wordFound.notify();
				}

				broadcastJoueurs(Protocol.newWordFoundTimeout(TFOUND));
			}

		} else {
			broadcastJoueurs(Protocol.newGuess(mot));
			IO.trace("Guess infructuex de " + j + " : '" + mot + "'");
		}

		IO.trace("Joueur " + j + " suggere '" + mot + "'");

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

	void notifyCheat(Joueur j) {

		if (tourCourrant.addCheatWarn(j)) {
			IO.trace("Joueur " + j + " viens de prévenir d'un cheat");
			broadcastJoueurs(Protocol.newWarned(j));
			if (tourCourrant.getNbWarn() >= NBCHEATWARN) {
				IO.trace("Trop c'est trop, on arrete de jouer");
				synchronized (endRound) {
					endRound.notify();
				}

			} else {
				IO.trace("Joueur " + j + " avait déjà prévenu d'un cheat");
				// MAYBE: balance mot protocole
			}
		}

	}
}
