package game;

import java.util.ArrayList;

import core.GameManager;
import core.Protocol;

public class Round {

	// HERE we change status?

	Joueur dessinateur;
	ArrayList<Joueur> chercheurs;
	ArrayList<Joueur> trouveurs; // ? good idea?
	Dessin sketch;
	String mot; // copy
	// GameManager gm;
	// final Object wordFound; //DIS

	Couleur currentColor;
	Integer currentSize;

	public Round(Joueur dessinateur, ArrayList<Joueur> chercheurs, String mot) {

		// this.gm=gm;
		this.dessinateur = dessinateur;
		this.chercheurs = chercheurs;
		// this.wordFound = wordFound;

		trouveurs = new ArrayList<>();
		sketch = new Dessin();
	}

	public Joueur getDessinateur() {
		return dessinateur;
	}

	public Integer getCurrentSize() {
		return currentSize;
	}

	public void setCurrentSize(Integer currentSize) {
		this.currentSize = currentSize;
	}

	public Couleur getCurrentColor() {
		return currentColor;
	}

	public void setCurrentColor(int r, int g, int b) {
		this.currentColor = new Couleur(r, g, b);
	}

	public Ligne addLigne(Integer x1, Integer y1, Integer x2, Integer y2) {
		return sketch.addLine(x1, y1, x2, y2, currentSize, currentColor);

		// SEE DOUBT not good place?
	}

	public String getDessinCommands() {
		return sketch.toCommand();
	}

	public ArrayList<Joueur> getTrouveurs() {
		return trouveurs;
	}

	public void setHasFound(Joueur j) {
		// CHECK + sync
		trouveurs.add(j);
		j.setRoleCourrant(Role.trouveur);
		chercheurs.remove(j);

	}
	
	public boolean guess(String essai){
		return mot.equals(essai);
	}
}
