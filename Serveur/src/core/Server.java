package core;

import game.Dictionnaire;
import game.Joueur;
import game.Partie;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tools.IO;
import core.ASSketchServer.Options;

public class Server implements Runnable {

	private ServerSocket sockServ;

	private Integer port;
	protected Integer nbMax;
	protected Dictionnaire dico;

	// Threads and connexions
	protected LinkedList<Socket> waitingSockets;
	private ConnexionStacker cs;
	private ConnexionHandler ch[];
	private ArrayList<TATJoueurHandler> gamerListers;

	// TODO Thread handler
	// Pool worker
	private ExecutorService workers;

	private GameManager gm;

	// EXT Spectateur
	protected ArrayList<Joueur> joueurs;
	private boolean acceptingNewJoueurs;

	protected ArrayList<Partie> parties; // TODO ? distinguer partie, et round?

	/**
	 * Simple constructeurs
	 * 
	 * @param opt
	 *            les options crées avec JCommander
	 */
	public Server(Options opt) {

		try {

			// OBJECTS
			dico = new Dictionnaire(opt.dico);
			joueurs = new ArrayList<Joueur>();
			acceptingNewJoueurs = true;
			nbMax = opt.nbJoueurs;

			port = opt.port;
			sockServ = new ServerSocket(port);

			waitingSockets = new LinkedList<Socket>();
			// THREADS.
			cs = new ConnexionStacker();
			ch = new ConnexionHandler[2]; // PARAM
			for (int i = 0; i < ch.length; i++) {
				ch[i] = new ConnexionHandler(i);
			}
			gm = new GameManager(joueurs, dico);
			gamerListers = new ArrayList<TATJoueurHandler>();

			workers = Executors.newFixedThreadPool(nbMax);
			// PARAM
			// See: differents excutors

			// TODO: initialisation des threads

		} catch (IOException e) {
			// TODO To improve error handling (ressayer ouvrir socket
			System.err.println("error:" + e.getMessage());
			// e.printStackTrace();
			System.exit(1);
		}

	} // End of Constructeur

	/** Socket Handling */

	public synchronized void addWaitingSocket(Socket s) {
		waitingSockets.add(s);
	}

	boolean waitingConnexion() {
		return (waitingSockets.size() != 0);
	}

	public synchronized Socket takeWaitingSocket() {
		return waitingSockets.pollFirst();
	}

	/** Joueurs Handling */
	// SEE? good place to put them?
	public void addJoueur(Joueur j) {
		synchronized (joueurs) {
			joueurs.add(j);
		}
	}

	public synchronized void removeJoueur(Joueur j) {
		// ?? ferme ton à ce niveau les flux?
		j.close();
		joueurs.remove(j);

	}

	// BONUX EXT Spect

	//
	public void broadcastJoueurs(final String message) {
		// SEE callable pour avoir retour d'erreur?
		Runnable messenger = new Runnable() {
			@Override
			public void run() {
				synchronized (joueurs) {
					// SEE: not performant?
					if(joueurs.isEmpty()){
						return;
					}

					IO.trace(joueurs.toString());
					for (Joueur j : joueurs) {
						j.send(message);
					}
				}
				IO.trace("Message \"" + message + "\" brodcasté ");
			}
		};
		workers.submit(messenger);

	}
	
	
	private void addGamerListener(Joueur j){
		TATJoueurHandler gl = new TATJoueurHandler(j, j.getUsername(), this);
		synchronized (gamerListers) {
			gamerListers.add(gl);
			gl.start();
			
		}
		
		
	}
	
	

	/**
	 * --------------SERVER RUN ----------
	 * 
	 */

	public void run() {
		// TODO

		cs.start();
		for (ConnexionHandler chi : ch)
			chi.start();

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
		private DataOutputStream outchan;

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
					outchan = new DataOutputStream(client.getOutputStream());

					// Met un timeout à la lecture sur la socket
					client.setSoTimeout(3000); // BONUX: temps augmente au fur
												// et à mesure

					// Traitement selon commande reçue
					String command = null;

					// WARNING: is that the good way to do?
					TryReadTimeout: while (true) {
						try {
							command = inchan.readLine();

							// Handling of ActionsScript ask
							if (command.contains("policy-file-request")) {
								IO.traceDebug("Et un actionscript qui se pointe");
								outchan.writeChars("<?xml version=\"1.0\"?><cross-domain-policy><allow-access-from domain=\"*\" to-ports=\"*\" /></cross-domain-policy>\0");
								outchan.flush();
								// read new command
								command = inchan.readLine();
							}

							break TryReadTimeout;

						} catch (SocketTimeoutException ste) {
							//
							if (waitingConnexion()) {
								IO.traceDebug("Renvoie dormir");
								// inchan.close();outchan.close();
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
					String[] tokens = command.split("/");
					if (tokens.length > 0 && tokens[0].equals("CONNECT")) {
						// REFACTOR parser
						if (tokens.length == 2) {

							if (acceptingNewJoueurs) {

								String joueurName = tokens[1];
								Connexion con = new Connexion(client, inchan,
										outchan);
								Joueur jou = new Joueur(con, joueurName);
								 addGamerListener(jou);
								//CHECK name non présent?
								
								synchronized (joueurs) {
									// confirm me
									jou.send("CONNECTED/"+jou.getUsername()+"/");
									// previens joueur courant autres connecté
									for(Joueur j : joueurs){
										jou.send("CONNECTED/"+j.getUsername()+"/");
									}
									

									// averti connexion autres joueurs
									broadcastJoueurs("CONNECTED/" + joueurName +"/");
									//TODO brodcast but Me
									joueurs.add(jou);
											

									// Lance le jeu si tout le monde est là
									if (joueurs.size() == nbMax) {
										acceptingNewJoueurs = false;
										gm.start();
										
										// SEE: synchronzied accept?: can't do
										// on a boolean.
										// TODO Rafinner Liste joueurs!!
									}
								}

							} else {
								IO.trace("Connexion Refusée, jeu plein");
								closeConnexion("GAME_FULL");
							}
						}

						else {
							closeConnexion("NEXT/TIME/GIVE/ME/A/NAME/");
							// ERROR:name to find
						}

						// BONUX: spectateur
					} else {

						closeConnexion("GOODBYE/BOLOS/");
						IO.traceDebug("Kick out Bolos");

					}
				} catch (SocketException se){
					IO.trace("Socket deconnectée avant connexion joueur");
				} catch (IOException e) {
					// TODO ?? rajoute
					e.printStackTrace();

					
				}

			}

		}

		// TODO: del
		// Q? Throws or Try
		public void closeConnexion(String message) throws IOException {
			outchan.writeChars(message + "\n");
			closeConnexion();

		}

		public void closeConnexion() throws IOException {
			inchan.close();
			outchan.close();
			client.close();
		}

	} // end of ConnexionHandler

}
