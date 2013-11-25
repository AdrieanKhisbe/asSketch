package core;

import game.Dictionnaire;
import game.Joueur;
import game.ListeJoueur;
import game.Role;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

import tools.IO;
import core.ASSketchServer.Options;
import core.exceptions.IllegalCommandException;
import core.exceptions.InvalidCommandException;
import core.exceptions.WrongArityCommandException;

public class Server extends Thread {

	// BONUX: see finals?
	private ServerSocket sockServ;

	private Integer port;
	protected Integer nbMax;
	protected Dictionnaire dico;

	// Threads and connexions
	protected LinkedList<Socket> waitingSockets;
	private ConnexionStacker cs;
	private ConnexionHandler ch[];
	private ArrayList<JoueurHandler> gamerListeners;

	private AtomicBoolean gameOn;
	private GameManager gm;

	// EXT Spectateur
	protected ListeJoueur joueurs;

	/**
	 * Simple constructeurs
	 * 
	 * @param opt
	 *            les options crées avec JCommander
	 */
	public Server(Options opt) {

		this.setName("Server");
		try {

			// OBJECTS
			dico = new Dictionnaire(opt.dico);

			nbMax = opt.nbJoueurs;
			joueurs = new ListeJoueur(nbMax);

			port = opt.port;
			sockServ = new ServerSocket(port);

			waitingSockets = new LinkedList<Socket>();

			gameOn = new AtomicBoolean(false);

			// THREADS.
			cs = new ConnexionStacker();
			ch = new ConnexionHandler[2]; // PARAM
			for (int i = 0; i < ch.length; i++) {
				ch[i] = new ConnexionHandler(i);
			}
			gm = new GameManager(this, joueurs, dico);
			gamerListeners = new ArrayList<JoueurHandler>();

		} catch (IOException e) {
			// BONUX To improve error handling (ressayer ouvrir Serversocket)
			// ou dico par default
			System.err.println("error:" + e.getMessage());
			// e.printStackTrace();
			System.exit(1);
		}

	} // End of Constructeur

	/** Getters */

	GameManager getGameManager() {
		return gm;
	}

	/** Socket Handling */

	public synchronized boolean isInGame(){
		return gameOn.get();
	}
	
	public synchronized void addWaitingSocket(Socket s) {
		waitingSockets.add(s);
	}

	synchronized boolean waitingConnexion() {
		return (waitingSockets.size() != 0);
	}

	public synchronized Socket takeWaitingSocket() {
		return waitingSockets.pollFirst();
	}

	/** Joueurs Handling */
	public synchronized void addJoueur(Joueur j) {
		joueurs.addJoueur(j);
	}

	// KEEP?
	public synchronized void removeJoueur(Joueur j) {
		joueurs.removeJoueur(j);
	}

	// BONUX EXT Spect

	//
	public void broadcastJoueurs(final String message) {
		broadcastJoueursExcept(message, null);
		// leger surcout, mais bon, pas duplication code
	}

	public void broadcastJoueursExcept(final String message, final Joueur deaf) {
		// SEE callable pour avoir retour d'erreur?

		// NOTE: Temps, disable le thread run

		// Runnable messenger = new Runnable() {
		// @Override
		// public void run() {
		synchronized (joueurs) {
			// LOCK??

			// SEE: not performant?
			if (joueurs.isEmpty()) {
				return;
			}

			IO.trace("Liste joueurs: " + joueurs.toString());
			for (Joueur j : joueurs.getJoueurs()) {
				if (!j.equals(deaf))
					j.send(message);
			}
		}
		IO.trace("Message \"" + message + "\" broadcasté ");
		// }
		// };
		// workers.submit(messenger);

	}

	private void addGamerListener(Joueur j) {
		JoueurHandler gl = new JoueurHandler(this, j);
		synchronized (gamerListeners) {
			gamerListeners.add(gl);
			gl.start();

		}

	}

	/**
	 * --------------SERVER RUN ----------
	 * 
	 */

	public void run() {

		cs.setDaemon(true);
		cs.start();
		for (ConnexionHandler chi : ch) {

			chi.setDaemon(true);
			chi.start();
		}
		IO.trace("Lancement des threads gérants les connexions entrantes");

		synchronized (gameOn) {
			try {
				gameOn.wait();
				gameOn.set(true);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				IO.trace("Erreur précédent le lancement de la partie");
			}
		}
		IO.trace("Active game Manager");
		gm.start();

		try {
			gm.join();

		} catch (InterruptedException e) {
			IO.trace("Arret inattendu du serveur");
		}

	}

	/**
	 * Stack new connexion
	 */
	class ConnexionStacker extends Thread {

		public ConnexionStacker() {
			this.setName("ConnexionStacker");
		}

		public void run() {
			@SuppressWarnings("resource")
			// close in another thread
			Socket client = new Socket();
			try {
				while (true) {
					client = sockServ.accept();

					// TEMPORARY (possible leak)
					PrintWriter outchan = new PrintWriter(
							client.getOutputStream(), true);

					outchan.print(" <?xml version=\"1.0\" encoding=\"UTF-8\"?>"
							+ "<cross-domain-policy>"
							+ "<allow-access-from domain=\"*\" to-ports=\"*\" secure=\"false\" />"
							+ "<site-control permitted-cross-domain-policies=\"master-only\" />"
							+ "</cross-domain-policy>\0");
					// Flash handling
					IO.traceDebug("Putain de policy envoyé automatiquement....");
					outchan.flush();

					IO.trace("Nouvelle connexion incoming mise en attente.");
					synchronized (waitingSockets) {
						addWaitingSocket(client);
						waitingSockets.notify();
					}
				}
			} catch (Throwable t) {
				t.printStackTrace(System.err);
			}

		}
	} // end of ConnexionStacker

	/*******
	 * Handle Connection
	 * 
	 */

	class ConnexionHandler extends Thread {

		private Socket client;
		private BufferedReader inchan;
		private PrintWriter outchan;

		public ConnexionHandler(int i) {
			this.setName("ConnexionHandler<" + i + ">");
		}

		public ConnexionHandler() {
			this(-1);
		}

		// TODO: changer: blockant sur la socket traitée en cours!!
		// REFACTOR!!!
		public void run() {

			// Q? ou variables instances

			HandleLoop: while (true) {
				synchronized (waitingSockets) {
					while (!waitingConnexion()) {
						try {
							IO.traceDebug("Attente Connexion Socket");
							waitingSockets.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					IO.trace("Traitement connexion en attente.");
					client = takeWaitingSocket();
				}
				// BONUX handle error.

				// // TRAITEMENT CONNEXION
				try {

					inchan = new BufferedReader(new InputStreamReader(
							client.getInputStream()));
					outchan = new PrintWriter(client.getOutputStream(), true);
					// nota :autoflush

					// Met un timeout à la lecture sur la socket
					client.setSoTimeout(4000); // BONUX: temps augmente au fur
												// et à mesure

					// Lecture commande
					String command = null;
					// WARNING: is that the good way to do?
					TryReadTimeout: while (true) {
						try {
							command = inchan.readLine();
							IO.traceDebug(command);
							// TODO: handle raw deconnection

							// Handling of ActionsScript ask
							if (command.contains("policy-file-request")) {
								IO.traceDebug("Et un actionscript qui se pointe");
								outchan.print(" <?xml version=\"1.0\" encoding=\"UTF-8\"?>"
										+ "<cross-domain-policy>"
										+ "<allow-access-from domain=\"*\" to-ports=\"*\" secure=\"false\" />"
										+ "<site-control permitted-cross-domain-policies=\"master-only\" />"
										+ "</cross-domain-policy>\0");

								// Flash handling
								outchan.flush();
								// read new command
								IO.traceDebug("Policy file envoyé");
								// closeConnexion("BYEBYE");
								// IO.traceDebug("fermé connexion Action");

								continue HandleLoop;
								// se déconnecte immédiatement
							}
							// FIXME

							break TryReadTimeout;

						} catch (SocketTimeoutException ste) {
							//
							if (waitingConnexion()) {
								IO.traceDebug("Autre socket en attente, renvoie dormir celle courante.");
								// inchan.close();outchan.close(); // SEE
								// (risque fermer socket)
								// restore?
								synchronized (waitingSockets) {
									waitingSockets.add(client);
									waitingSockets.notify();
								}
								continue HandleLoop;
							} else { // si personne attend
								IO.traceDebug("Et un tour de manège");
								continue TryReadTimeout;
							}
						}
					}
					// NOTA: was intialy a dowhile(false), mais marche pas bien
					// sur avec continue

					// Bonux CONNEXION HANDLING

					// / ----- TRAITEMENT REPONSE
					try {
						String[] tokens = Protocol.parseCommand(command,
								Role.nonconnecté);

						if (tokens[0].equals("CONNECT")) {
							synchronized (joueurs) {
								if (!joueurs.isLocked()) {

									String joueurName = tokens[1];

									// Check Login
									if (!joueurs.isLoginDuplicate(joueurName)) {
										throw new IllegalCommandException(
												"Existing Login");
									}

									// NOTE: à partir de ce stade là, en théorie
									// tout est bon, donc on créer l'objet
									// connexion, puis joueur
									Connexion con = new Connexion(client,
											inchan, outchan);
									Joueur jou = new Joueur(con, joueurName);

									addGamerListener(jou);

									IO.traceDebug("Envoi confirmation connexion");

									// confirm me
									jou.send(Protocol.newConnected(jou));
									// previens joueur courant autres connecté
									for (Joueur j : joueurs.getJoueurs()) {
										jou.send(Protocol.newConnected(j));
									}

									// averti connexion autres joueurs
									broadcastJoueursExcept(
											Protocol.newConnected(jou), jou);

									joueurs.addJoueur(jou);

									// Lance le jeu si tout le monde est là
									if (joueurs.isReady()) {
										synchronized (gameOn) {
											gameOn.notify();
										}
									}

								} else {
									IO.trace("Connexion Refusée, jeu plein");
									closeConnexion("GAME_FULL");
								}
							}
						}

						// BONUX: spectateur
					} catch (WrongArityCommandException e) {
						// Utilise close connexion, puisque ne communique pas
						// via objet connexion
						closeConnexion("NEXT/TIME/GIVE/ME/A/NAME/");
					} catch (InvalidCommandException e) {

						closeConnexion("GOODBYE/BOLOS/");
						IO.traceDebug("Kick out Bolos");
					}

					// Fin traitement Connexion reçue en cours

				} catch (SocketException se) {
					IO.trace("Socket deconnectée avant connexion joueur");
					// TODO close connection??
				} catch (NullPointerException e) {
					// TODO ?? rajoute
					IO.traceDebug("Déconnexion barbabre (null)");
					e.printStackTrace();
				} catch (IOException e) {
					// TODO ?? rajoute
					e.printStackTrace();

				}

			}

		}

		// TODO: del REFACTOR
		// Q? Throws or Try
		public void closeConnexion(String message) throws IOException {
			outchan.println(message);
			closeConnexion();

		}

		public void closeConnexion() throws IOException {
			inchan.close();
			outchan.close();
			client.close();
		}

	} // end of ConnexionHandler

}
