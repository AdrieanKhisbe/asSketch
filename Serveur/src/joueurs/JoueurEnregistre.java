package joueurs;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import core.Connexion;

public class JoueurEnregistre extends Joueur implements Serializable {

	private static final long serialVersionUID = 6884148533126977784L;
	
	
	private String password;
	private ArrayList<Resultat> scores;

	// HERE: attention gérer connexion!!!!!!! how?? set connexion?
	public JoueurEnregistre(Connexion client, String login, String mdp)
			throws IOException {
		super(client, login);
		// TODO Auto-generated constructor stub
		this.password = mdp;
		this.scores = new ArrayList<>();
	}

	public boolean checkPassword(String mdp) {
		return mdp == password;
	}

	// TODO toString

	public int nbVictoires() {
		int tot = 0;
		for (Resultat r : scores) {
			if (r.position == 1)
				tot++;
		}
		return tot;
	}

	public double nbMoyenVictoire() {
		double nbPartie = scores.size();
		return nbPartie == 0 ? 0 : nbVictoires() / nbPartie;
	}

	/**
	 * Score total du joueur
	 * 
	 * @return Le score cumulé sur ensemble des parties
	 */
	public int scoreTotal() {

		int tot = 0;
		for (Resultat r : scores) {
			tot = r.score;
		}
		return tot;
	}

	public double scoreMoyen() {
		double nbPartie = scores.size();
		return nbPartie == 0 ? 0 : scoreTotal() / nbPartie;
	}

	/**
	 * Archive résultats courrant, et réinitialse celui ci.
	 */
	public void saveResult() {
		scores.add(currentResult);
		currentResult = new Resultat();

	}

	public void setConnexion(Connexion c) {
		this.connexion = c;
	}

}
