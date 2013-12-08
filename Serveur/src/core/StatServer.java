package core;

import game.joueurs.Comptes;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import tools.IO;

public class StatServer extends Thread {

	private Comptes comptesJoueurs;
	private ServerSocket statSock;
	int port;
	private static final String BAD_REQUEST = "<html>\n<head><title>400 Bad Request</title></head>\n<body bgcolor=\"white\">\n<center><h1>400 Bad Request</h1></center>\n</body>\n</html>";

	private Integer nbStatsSucces;
	private Integer nbStatsWrongRequest;
	private Integer nbStatsDisconnect; // exception

	// throw pour
	public StatServer(Comptes c, int port) throws IOException {

		// thread Config
		this.setName("StatsThread");
		this.setDaemon(true);

		this.comptesJoueurs = c;
		this.port = port;
		statSock = new ServerSocket(port);
		// serverSocket: backlog??

		// stats
		nbStatsSucces = 0;
		nbStatsDisconnect = 0;
		nbStatsWrongRequest = 0;

	}

	public void run() {

		while (true) {

			try {
				Socket client = statSock.accept();
			//	client.setSoTimeout(1000); // PARAM

				BufferedReader isr = new BufferedReader(new InputStreamReader(
						client.getInputStream()));
				DataOutputStream dos = new DataOutputStream(
						client.getOutputStream());

				String command = isr.readLine();

				// Check commande et agis en conséquence
				if (command.contains("GET") && command.contains("HTTP/1.1")) {

					dos.writeChars(constructPage());
					nbStatsSucces++;
					IO.trace("StatsServer: Stats envoyées");
				} else {
					dos.writeChars(BAD_REQUEST);
					IO.trace("StatsServer: Mauvaise Requete");
					nbStatsWrongRequest++;
					// TODO spam protect
				}

				dos.close();
				isr.close();

			} catch (IOException e) {
				e.printStackTrace(); // TODO TMP
				IO.traceDebug("StatsServer: problème connexion");
				// TODO stat fausse requete , à améliorer
				nbStatsDisconnect++;

			}

		}

	}

	private String constructPage() {
		// TODO: look how to create template (haml ou autre??)
		return "TO BE BUILT\n";
	}

}
