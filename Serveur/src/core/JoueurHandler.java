package core;

import game.Joueur;

import java.io.IOException;

import tools.IO;
import core.exceptions.IllegalCommandException;
import core.exceptions.InvalidCommandException;

public class JoueurHandler extends Thread {

	// BONUX: DP State...

	final Joueur gamer;
	final String username;
	final Server server;
	final GameManager gm;

	// HERE tourCourrant, partie courante?

	// What else?

	public JoueurHandler(Server server, Joueur gamer) {
		super();
		this.gamer = gamer;
		this.username = gamer.getUsername();
		this.server = server;
		gm = server.getGameManager();

		// TEMP
		this.setDaemon(true);
	}

	// / Run Run Run
	public void run() {
		try {
			while (true) {
				try {
					// get command
					String textCommand = gamer.readCommand();

					// parse
					String[] parsedCommand = Protocol.parseCommand(textCommand,
							gamer.getRoleCourrant());

					// StringBuffer sb = new StringBuffer();
					// for(String i : parsedCommand)
					// sb.append(i).append(":");
					//
					// IO.traceDebug(sb.toString());

					// Handle

					switch (parsedCommand[0]) {
					case "EXIT":
						// vérifie que bon joueur,
						if (!parsedCommand[1].equals(username))
							throw new IllegalCommandException(
									"Nom Joueur Invalide");

						// HERE: gérer si dessintaeur
						// Notify le game manager? : TODO: fonction dans
						// gamemanager

						gamer.send(Protocol.newExited(gamer));
						// close connexion
						server.removeJoueur(gamer);
						gamer.close();

						// avertit autre joueur
						server.broadcastJoueurs(Protocol.newExited(gamer));

						return;

					case "SKIP":
						// TODO; cf exit du dessinateur
						break;

					case "WARN":
						gm.notifyCheat(gamer);
						break;

					case "GUESS":
						gm.tryGuess(gamer, parsedCommand[1]);
						break;

					case "SET_LINE":
						try {
							gm.addLigne(Integer.parseInt(parsedCommand[1]),
									Integer.parseInt(parsedCommand[2]),
									Integer.parseInt(parsedCommand[3]),
									Integer.parseInt(parsedCommand[4]));
						} catch (NumberFormatException e) {
							throw new InvalidCommandException(
									"Les arguments doivent être des nombres");
						}

						// 404
						break;

					case "SET_SIZE":

						try {
							gm.setSize(Integer.parseInt(parsedCommand[1]));
						} catch (NumberFormatException e) {
							throw new InvalidCommandException(
									"Les arguments doivent être des nombres");
						}
						break;

					case "SET_COLOR":
						try {
							gm.setColor(Integer.parseInt(parsedCommand[1]),
									Integer.parseInt(parsedCommand[2]),
									Integer.parseInt(parsedCommand[3]));
						} catch (NumberFormatException e) {
							throw new InvalidCommandException(
									"Les arguments doivent être des nombres");
						}
						break;

					// TODO talk

					}
				} catch (NullPointerException e) {
					// Note: avec Buffered Reader, les endOfFile exception sont
					// cachées (comme toutes les IOExceptions)
					// il faut tester si readline renvoit pas null
					// on préférera utiliser une exception
					IO.trace("CONNEXION coupéeee");
					manageExit();
					// ? se retirer liste des kistener?
					return;

				} catch (IOException e) {
					IO.traceDebug("IO exception: " + e.getMessage());
					// continue boucle SEE change?
				} catch (InvalidCommandException e) {
					gamer.send(Protocol.newInvalidCommand(e));

				}

			} //end while
		} catch (ExitException e) {
			
			// MAYBE ou mettre code de gestion directement ici
			IO.traceDebug("Arret du thread handler courant");

		}

	}

	private void manageExit() throws ExitException {
		// Si jeux non lancé
		if (!server.isInGame()) {

			// retire liste, et se tue.
			throw new ExitException();
		}

		// Si dessinateur

		// teste mot trouvé

		// sinon
		server.removeJoueur(gamer);
		server.broadcastJoueurs(Protocol.newExited(gamer));
		// TODO: comment distingue avec fin de partie?
		IO.traceDebug("Thread gestionnaire de " + gamer
				+ " s'arrete suite à la déconnexion de ce dernier");

		// Fin de partie??

	}

	class ExitException extends Exception {

	}
}
