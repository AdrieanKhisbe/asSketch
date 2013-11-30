package joueurs;

import java.util.ArrayList;

public class JoueurEnregistre extends Joueur {

	private String password;
	private ArrayList<Resultat> scores;

	public boolean checkPassword(String mdp) {
		return mdp == password;
	}

	// TODO toString

	public int nbVictoires() {
		int tot = 0;
		for (Resultat r : scores) {
			if(r.position == 1) tot++;
		}
		return tot;
	}

	public int scoreTotal() {

		int tot = 0;
		for (Resultat r : scores) {
			tot = r.score;
		}
		return tot;
	}
}
