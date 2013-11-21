package core;

import game.Joueur;

import java.io.IOException;

import tools.IO;
import core.exceptions.IllegalCommandException;
import core.exceptions.InvalidCommandException;

public class TATJoueurHandler extends Thread {

	final Joueur gamer;
	final String username;
	final Server server;
	final GameManager gm;

	// HERE tourCourrant, partie courante?

	// What else?

	public TATJoueurHandler( Server server,Joueur gamer) {
		super();
		this.gamer = gamer;
		this.username = gamer.getUsername();
		this.server = server;
		gm = server.getGameManager();
	}

	// / Run Run Run
	public void run() {

		while (true) {
			try {
				// get command
				String textCommand = gamer.readCommand();

				// parse
				String[] parsedCommand = Protocol.parseCommand(textCommand,
						gamer.getRoleCourrant());

				// Handle

				switch (parsedCommand[0]) {
				case "EXIT":
					// vérifie que bon joueur,
					if (!parsedCommand[1].equals(username))
						throw new IllegalCommandException("Nom Joueur Invalide");

					// TODO: gérer si dessintaeur
					// Notify le game manager?

					gamer.send(Protocol.newExited(gamer));
					// close connexion
					server.removeJoueur(gamer);
					gamer.close();

					// avertit autre joueur
					server.broadcastJoueurs(Protocol.newExited(gamer));

					return;

				case "SET_LINE":
					// non atteignable si pas de partie en cours (CHECK)
					// TODO: passer par game manager?
					gm.getPartie()
							.getTourCourrant()
							.addLigne(Integer.parseInt(parsedCommand[1]),
									Integer.parseInt(parsedCommand[2]),
									Integer.parseInt(parsedCommand[3]),
									Integer.parseInt(parsedCommand[4]));

				}

			} catch (IOException e) {
				IO.traceDebug("IO exception: " + e.getMessage());
				// continue boucle SEE change?
			} catch (InvalidCommandException e) {
				gamer.send(Protocol.newInvalidCommand(e));
			} // TODO

		}

	}
}
