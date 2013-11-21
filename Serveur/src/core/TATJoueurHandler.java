package core;

import java.io.IOException;

import core.exceptions.IllegalCommandException;
import core.exceptions.InvalidCommandException;


import tools.IO;

import game.Joueur;

public class TATJoueurHandler extends Thread {

	final Joueur gamer;
	final String username;
	final Server server;

	// What else?
	
	
	public TATJoueurHandler(Joueur gamer, String username, Server server) {
		super();
		this.gamer = gamer;
		this.username = username;
		this.server = server;
	}

	
	/// Run Run Run
	public void run() {

		while (true) {
			try {
				// get command
				String textCommand = gamer.readCommand();

				// parse
				String[] parsedCommand = CommandParser.parseCommand(
						textCommand, gamer.getRoleCourrant());

				// Handle

				switch (parsedCommand[0]) {
				case "EXIT":
					// vérifie que bon joueur,
					if (!parsedCommand[1].equals(username))
						throw new IllegalCommandException("Nom Joueur Invalide");

					// avertit autre joueur
					server.broadcastJoueurs("EXITED/"+username+"/");
					// FIXME : en faisant executer par autre, prend risque soit fermé quand viens temps écrire
					
					// close connexion
					server.removeJoueur(gamer);
					gamer.close();
					return;

				}

			} catch (IOException e) {
				IO.traceDebug("IO exception: " + e.getMessage());
				// continue boucle
			} catch (InvalidCommandException e) {
				gamer.send("INVALID_COMMAND/"
						+ e.getMessage().replace(" ", "_") + "/");
			} // TODO

		}

	}



	// SEE why inner class?

}
