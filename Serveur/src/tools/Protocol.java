package tools;

// MAYBE : move to tools?
import graphiques.Ligne;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import joueurs.Joueur;
import joueurs.Role;
import core.exceptions.IllegalCommandException;
import core.exceptions.InvalidCommandException;
import core.exceptions.UnknownCommandException;
import core.exceptions.WrongArityCommandException;

/**
 * Classe Utilitaire à propos du protocole, tant en émission (MessageBuilder)
 * qu'en réception (parser et checker)
 * 
 * @author adriean
 * 
 */
public class Protocol {

	// TODO BONUX: check typage.....

	/**
	 * Hashmap contenant les COmmande valide, leur arité, et Etat associé.
	 */
	private static final HashMap<String, CommandParameter> gameCommand = new HashMap<>();

	static { // Génère la liste des commandes
		// CONNECT & EXIT
		gameCommand.put("CONNECT", new CommandParameter(Role.nonconnecté, 1));

		gameCommand.put("EXIT", new CommandParameter(Role.indéterminé, 1));

		// SUGGESTION
		gameCommand.put("GUESS", new CommandParameter(Role.chercheur, 1));

		gameCommand.put("WARN", new CommandParameter(Role.chercheur, 1));
		gameCommand.put("SKIP", new CommandParameter(Role.dessinateur, 1));

		// DESSIN

		gameCommand.put("SET_COLOR", new CommandParameter(Role.dessinateur, 3));
		gameCommand.put("SET_SIZE", new CommandParameter(Role.dessinateur, 1));
		gameCommand.put("SET_LINE", new CommandParameter(Role.dessinateur, 4));

		// TALK
		gameCommand.put("TALK", new CommandParameter(Role.indéterminé, 1));
		// considère que tous peuvent parler. meme si cheat possible
	}

	/**
	 * S'assure confrmité de la commande avant de la retourner splitée
	 * 
	 * @param command
	 *            La commande à tester
	 * @param roleCourant
	 *            Le role du joueur géré par le handler
	 * @return Le commande tokenisée
	 * @throws InvalidCommandException
	 *             Exception si commande non conforme protocole
	 */
	public static String[] parseCommand(String command, Role roleCourant)
			throws InvalidCommandException {

		// suppression chaines actionscript \0
		// TODO MAYBE : check le mode?
		/// revoir si singleton option mis en place
		command = command.replaceAll("\u0000", ""); 
		
		IO.traceDebug("Message reçu: " + command);

		
		// Gestion des échappement
		command = command
				.replaceAll(Pattern.quote("\\\\"), Pattern.quote("\\"));

		/// command.replaceAll(Pattern.quote("\\'"), Pattern.quote("'"));

		// Découpe
		String[] tokens = command.split("(?<!\\\\)/");

		// TODO: échapement des chaines envoyées devra aussi être fait!

		CommandParameter cp = gameCommand.get(tokens[0]);
		if (cp == null)
			throw new UnknownCommandException(tokens[0]);
		// check arité
		Integer nbArgs = tokens.length - 1;
		if (cp.arité != nbArgs)
			throw new WrongArityCommandException("expected arity:" + cp.arité
					+ "; was " + nbArgs);

		// check "légalité" commande
		if (cp.equals(Role.indéterminé)) {
			// Dans ce cas, c'est Okay.
		} else if (!roleCourant.equals(cp.role)) {
			throw new IllegalCommandException("command " + tokens[0]
					+ " not available for " + roleCourant);
		}

		return tokens;

	}

	/**
	 * Command Generators! Créer des Chaine de caractères correspondant aux
	 * commandes émises.
	 */
	/// createur de commandes: un peu bazooka
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

	public static String newGuess(Joueur j, String mot) {
		return "GUESSED/" + j.getUsername() + "/" + mot + "/";
	}

	public static String newWordFound(Joueur j) {
		return "WORD_FOUND/" + j.getUsername() + "/";
	}

	public static String newWordFoundTimeout(Integer sec) {
		return "WORD_FOUND_TIMEOUT/" + sec + "/";
	}

	// si j null, pas de vainqueurs donc Looooser
	// MAYBE: update avec nouveau protocole
	public static String newEndRound(Joueur j, String mot) {
		return "END_ROND/" + ((j != null) ? j.getUsername() : "LOSERS") + "/"
				+ mot + "/";
	}

	public static String newScoreRound(ArrayList<Joueur> joueurs) { // PARAM
		// TODO
		StringBuffer sb = new StringBuffer("SCORE_ROUND/");
		for (Joueur j : joueurs) {
			sb.append(j.getUsername()).append("/");
			sb.append(j.getScore()).append("/");
		}
		return sb.toString();
	}

	public static String newScoreGame(ArrayList<Joueur> joueurs) {
		StringBuffer sb = new StringBuffer("SCORE_GAME/");
		// BONUX: score by score....
		for (Joueur j : joueurs) {
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

	public static String newListen(Joueur emetteur, String message) {
		return "LISTEN/" + emetteur.getUsername() + "/"
				+ message.replace("/", "\\/").replace("\\", "\\\\") + "/";
	}

	/**
	 * Main ponctuel de test
	 * 
	 * @param args
	 * @throws InvalidCommandException
	 */
	public static void main(String[] args) throws InvalidCommandException {
		System.out.println(Protocol.parseCommand("CONNECT/AA\\/ABC",
				Role.nonconnecté)[1]);
	}

}

/**
 * Objet local pour stocker les données relative à une commande, Lerole associé,
 * ainsi que l'arité BONUX: utiliser un tableau pour le typage?
 */
class CommandParameter {

	Role role;
	Integer arité;

	CommandParameter(Role r, Integer arité) {
		this.role = r;
		this.arité = arité;
	}

}
