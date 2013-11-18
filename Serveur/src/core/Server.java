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
			ch = new ConnexionHandler[2]; //PARAM
			for(int i = 0;i<ch.length;i++) ch[i] = new ConnexionHandler();

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
		for(ConnexionHandler i : ch) i.start();
		//ch.start();

	}

	/**
	 * Stack new connexion
	 */
	class ConnexionStacker extends Thread {

		public void run() {
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

		// TODO: changer: blockant sur la socket traitée en cours!!
		// REFACTOR!!!
		public void run() {
			Socket client;
			BufferedReader inchan;
			DataOutputStream outchan;

			// Q? ou variables instances
			
			HandleLoop: 
			while (true) {
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
						client.setSoTimeout(3000); //BONUX: temps augmente au fur et à mesure

						// Traitement selon commande reçue
						String command = null;

						// WARNING: is that the good way to do?
						TryReadTimeout: while(true){
							try {
								command = inchan.readLine();
								
								// Handling of ActionsScript ask
								// if(command.equals() )
								
								break TryReadTimeout; 							
								
							} catch (SocketTimeoutException ste) {
								//
								if (waitingConnexion()) {
									IO.traceDebug("Renvoie dormir");
								//	inchan.close();outchan.close();
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
						// NOTA: was intialy a dowhile(false), mais marche pas bien sur avec continue
						
						

						if (command.equals("CONNECT/")) {
							// parser
							outchan.writeChars("CONNECTED/\n");

							// TODO : GET HERE

						} else {

							outchan.writeChars("GOODBYE/BOLOS/\n");
							IO.traceDebug("Kick out Bolos");
							inchan.close();
							outchan.close();
							client.close();

						}

					} catch (IOException e) {
						// TODO ?? rajoute
						e.printStackTrace();

					}

				}


		}
	} // end of ConnexionHandler

}
