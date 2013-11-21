package core;

import game.Joueur;
import game.JoueurRole;
import game.Ligne;

import java.awt.Color;
import java.util.HashMap;

import core.exceptions.IllegalCommandException;
import core.exceptions.InvalidCommandException;
import core.exceptions.UnknownCommandException;
import core.exceptions.WrongArityCommandException;

public class Protocol {

	// TODO BONUX: check typage.....

	private static final HashMap<String, CommandParameter> gameCommand = new HashMap<>();

	static { // Génère la liste des commandes
		// CONNECT & EXIT
		gameCommand.put("CONNECT", new CommandParameter(JoueurRole.nonconnecté,
				1));

		// SEE connect?
		gameCommand
				.put("EXIT", new CommandParameter(JoueurRole.indéterminé, 1));

		// DESSIN
		gameCommand
				.put("EXIT", new CommandParameter(JoueurRole.indéterminé, 1));

		// SUGGESTION
		gameCommand.put("SET_COLOR", new CommandParameter(JoueurRole.chercheur,
				3));
		gameCommand.put("SET_SIZE", new CommandParameter(
				JoueurRole.dessinateur, 1));
		gameCommand.put("SET_LINE", new CommandParameter(
				JoueurRole.dessinateur, 4));

		// TALK
		gameCommand
				.put("TALK", new CommandParameter(JoueurRole.indéterminé, 1));
		// considère que tous peuvent parler. meme si cheat possible
	}

	static String[] parseCommand(String command, JoueurRole roleCourant)
			throws InvalidCommandException {

		// TODO Handle échappement ? \/
		String[] tokens = command.split("/");
		CommandParameter cp = gameCommand.get(tokens[0]);
		if (cp == null)
			throw new UnknownCommandException(tokens[0]);
		// check arité
		Integer nbArgs = tokens.length - 1;
		if (cp.arité != nbArgs)
			throw new WrongArityCommandException("expected arity:" + cp.arité
					+ "; was " + nbArgs);

		// check legalité
		if (cp.equals(JoueurRole.indéterminé)) {
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

	public static String newEndRound(Joueur j, String mot) {
		return "END_ROND/" + j.getUsername() + "/" + mot + "/";
	}

	public static String newScoreRound(Joueur j, String mot) { // PARAM
		// TODO
		StringBuffer sb = new StringBuffer("SCORE_ROUND/");
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

	// TODO invalid command
	public static String newInvalidCommand(InvalidCommandException e) {
		return "INVALID_COMMAND/" + e.getMessage().replace(" ", "_") + "/";
	}
}

class CommandParameter {

	JoueurRole role;
	Integer arité;

	CommandParameter(JoueurRole r, Integer arité) {
		this.role = r;
		this.arité = arité;
	}
}
