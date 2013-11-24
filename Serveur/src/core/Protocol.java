package core;

import game.Joueur;
import game.Ligne;
import game.Role;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import tools.IO;
import core.exceptions.IllegalCommandException;
import core.exceptions.InvalidCommandException;
import core.exceptions.UnknownCommandException;
import core.exceptions.WrongArityCommandException;

public class Protocol {

	// TODO BONUX: check typage.....

	private static final HashMap<String, CommandParameter> gameCommand = new HashMap<>();

	static { // Génère la liste des commandes
		// CONNECT & EXIT
		gameCommand.put("CONNECT", new CommandParameter(Role.nonconnecté,
				1));

		gameCommand
				.put("EXIT", new CommandParameter(Role.indéterminé, 1));

		// SUGGESTION
		gameCommand.put("GUESS", new CommandParameter(Role.chercheur, 1));
		
		gameCommand.put("WARN", new CommandParameter(Role.chercheur, 1));
		gameCommand.put("SKIP", new CommandParameter(Role.dessinateur, 1));

		// DESSIN

		gameCommand.put("SET_COLOR", new CommandParameter(Role.chercheur,
				3));
		gameCommand.put("SET_SIZE", new CommandParameter(
				Role.dessinateur, 1));
		gameCommand.put("SET_LINE", new CommandParameter(
				Role.dessinateur, 4));

		// TALK
		gameCommand
				.put("TALK", new CommandParameter(Role.indéterminé, 1));
		// considère que tous peuvent parler. meme si cheat possible
	}

	static String[] parseCommand(String command, Role roleCourant)
			throws InvalidCommandException {

		IO.traceDebug("Message reçu: " + command );
		
		
		// Handle échappement ? \/
		String[] tokens = command.split("(?<!\\\\)/");
		// Déséchappement autres caractères TODO
		//TODO: échapement des chaines envoyées devra aussi être fait!
		
		CommandParameter cp = gameCommand.get(tokens[0]);
		if (cp == null)
			throw new UnknownCommandException(tokens[0]);
		// check arité
		Integer nbArgs = tokens.length - 1;
		if (cp.arité != nbArgs)
			throw new WrongArityCommandException("expected arity:" + cp.arité
					+ "; was " + nbArgs);

		// check legalité
		if (cp.equals(Role.indéterminé)) {
			// what?
		} else if (!roleCourant.equals(cp.role)) {
			throw new IllegalCommandException("command " + tokens[0]
					+ " not available for " + roleCourant);
		}

		// TODO HERE
		return tokens;

	}

	// Command Generators!
	// createur de commandes: un peu bazooka
	// BONUX: see si on aurait pu macro générer ceci?

	public static String newConnected(Joueur j) {
		return "CONNECTED/" + j.getUsername() + "/";
	}

	public static String newRoundDesinateur(String mot) {
		return "NEW_ROUND/dessinateur/" + mot + "/";
	}

	public static String newRoundChercheur(Joueur dessinateur) {
		return "NEW_ROUND/chercheur/" + dessinateur.getUsername() + "/";
	}

	public static String newGuess(String mot) {
		return "GUESSED/" + mot + "/";
	}

	public static String newWordFound(Joueur j) {
		return "WORD_FOUND/" + j.getUsername() + "/";
	}

	public static String newWordFoundTimeout(Integer sec) {
		return "WORD_FOUND_TIMEOUT/" + sec + "/";
	}

	// si j null, pas de vainqueurs donc Looooser
	public static String newEndRound(Joueur j, String mot) {
		return "END_ROND/" + ((j != null) ? j.getUsername() : "LOSERS")
				+ "/" + mot + "/";
	}

	public static String newScoreRound(ArrayList<Joueur> joueurs) { // PARAM
		// TODO
		StringBuffer sb = new StringBuffer("SCORE_ROUND/");
		for(Joueur j : joueurs){
			sb.append(j.getUsername()).append("/");
			sb.append(j.getScore()).append("/");
		}
		return sb.toString();
	}
	
	public static String newScoreGame(ArrayList<Joueur> joueurs) {
		StringBuffer sb = new StringBuffer("SCORE_GAME/");
		// BONUX: score by score....
		for(Joueur j : joueurs){
			sb.append(j.getUsername()).append("/");
			sb.append(j.getScore()).append("/");
		}
		return sb.toString();
	}

	public static String newLigne(Ligne l) {
		// REFACTOR?
		return l.toCommand();
	}

	public static String newLigne(Integer x1, Integer y1, Integer x2,
			Integer y2, Integer currentSize, Color currentColor) {
		// REFACTOR?
		return "LINE/" + x1 + "/" + y1 + "/" + x2 + "/" + y2 + "/"
				+ currentSize + "/" + currentColor;
	}

	public static String newExited(Joueur j) {
		return "EXITED/" + j.getUsername() + "/";
	}
	public static String newSkip(Joueur j) {
		return "SKIPED/" + j.getUsername() + "/"; // SEE: redondant
	}
	public static String newWarned(Joueur j) {
		return "WARNED/" + j.getUsername() + "/";
	}

	// TODO invalid command
	public static String newInvalidCommand(InvalidCommandException e) {
		return "INVALID_COMMAND/" + e.getMessage().replace(" ", "_") + "/";
	}

public static void main(String[] args) throws InvalidCommandException {
	System.out.println(Protocol.parseCommand("CONNECT/AA\\/ABC",Role.nonconnecté)[1]);
}

}

class CommandParameter {

	Role role;
	Integer arité;

	CommandParameter(Role r, Integer arité) {
		this.role = r;
		this.arité = arité;
	}
	
	
}

