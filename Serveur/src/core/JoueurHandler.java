package core;

import java.io.IOException;

import joueurs.Joueur;
import joueurs.Role;
import tools.IO;
import tools.Protocol;
import core.exceptions.IllegalCommandException;
import core.exceptions.InvalidCommandException;
import core.exceptions.UnknownCommandException;

public class JoueurHandler extends Thread {

	// BONUX: DP State...

	final Joueur gamer;
	final String username;
	final Server server;
	final GameManager gm;

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

						manageExit(true);
						break; // non atteint

					case "SKIP":
						// HERE TODO; cf exit du dessinateur
						// vérifier trouvur
						break;

					case "CHEAT":
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

					case "TALK":
						gm.broadcastJoueursExcept(
								Protocol.newListen(gamer, parsedCommand[1]),
								gamer);
						// SEE ? broadcast up to game manager?$
				
						// BONUX : here, sauvegarder les messages. ajouter méthode game manager
						break;

					default:
						// devrait pas arriver la en théorie.
						throw new UnknownCommandException(
								"Command inconnue, check if upcase");

					}

				} catch (NullPointerException e) {
					// Note: avec Buffered Reader, les endOfFile exception sont
					// cachées (comme toutes les IOExceptions)
					// il faut tester si readline renvoit pas null
					// on préférera utiliser une exception
					IO.trace("Connexion coupéeee");
					manageExit(false);

				} catch (IOException e) {
					IO.traceDebug("IO exception: " + e.getMessage());
					manageExit(false);
				} catch (InvalidCommandException e) {

					IO.traceDebug("Commande invalide reçue:"
							+ gamer.getUsername() + ": " + e.getMessage());
					gamer.send(Protocol.newInvalidCommand(e));

				}

			} // end while

		} catch (ExitException e) {

			// MAYBE ou mettre code de gestion directement ici?

			// retire le threads des handler.
			IO.traceDebug("Arret du thread handler courant");

		}

	}

	private synchronized void manageExit(boolean cleanExit)
			throws ExitException {
		// Si jeux non lancé: if (!server.isInGame()) {NOtused?

		// retire liste, et se tue.
		if (cleanExit) {
			server.broadcastJoueurs(Protocol.newExited(gamer));
			gamer.close();
		}
		server.removeJoueur(gamer);

		// Si dessinateur
		if (gamer.getRoleCourrant().equals(Role.dessinateur)) {
			gm.handleDessinateurExit();
		}

		IO.traceDebug("Thread gestionnaire de " + gamer
				+ " s'arrete suite à la déconnexion de ce dernier");
		throw new ExitException();

		// SEE Fin de partie??

	}

	// SEE that usefull?
	class ExitException extends Exception {

		private static final long serialVersionUID = 7237200611853588544L;
	}
}
