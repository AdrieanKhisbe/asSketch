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
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.LinkedList;

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
	// TODO Thread handler
	// private GameManager gm;

	// EXT Spectateur
	protected ArrayList<Joueur> joueurs;

	protected ArrayList<Partie> parties; // TODO ? distinguer partie, et round?

	/**
	 * Simple constructeurs
	 * 
	 * @param opt
	 *            les options crées avec JCommander
	 */
	public Server(Options opt) {

		try {
			port = opt.port;
			sockServ = new ServerSocket(port);

			waitingSockets = new LinkedList<Socket>();
			cs = new ConnexionStacker();
			ch = new ConnexionHandler[2]; // PARAM
			for (int i = 0; i < ch.length; i++)
				ch[i] = new ConnexionHandler();

			dico = new Dictionnaire(opt.dico);
			joueurs = new ArrayList<Joueur>();

			nbMax = opt.nbJoueurs;

			// TODO: initialisation des threads

		} catch (IOException e) {
			// TODO To improve error handling (ressayer ouvrir socket
			System.err.println("error:" + e.getMessage());
			// e.printStackTrace();
			System.exit(1);
		}

	}

	public synchronized void addWaitingSocket(Socket s) {
		waitingSockets.add(s);
	}

	boolean waitingConnexion() {
		return (waitingSockets.size() != 0);
	}

	public synchronized Socket takeWaitingSocket() {
		return waitingSockets.pollFirst();
	}

	public void addJoueur(Joueur j) {
		joueurs.add(j);
	}

	public synchronized void removeJoueur(Joueur j) {
		// ?? ferme ton à ce niveau les flux?
		j.close();
		joueurs.remove(j);

	}

	// EXT Spect

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

		public void run() {
			@SuppressWarnings("resource") // close in another thread
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
							if (command.equals("<policy-file-request/>")) // maybe
																			// add:
																			// <policy-file-request/>\0
							{
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

					String[] tokens = command.split("/");
					if (tokens.length > 0 && tokens[0].equals("CONNECT")) {
						// parser
						if (tokens.length == 2) {
							// gère connexion joueur de nom tokens[1]
							outchan.writeChars("CONNECTED/" + tokens[1] + "\n");
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

				} catch (IOException e) {
					// TODO ?? rajoute
					e.printStackTrace();

				}

			}

		}

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
