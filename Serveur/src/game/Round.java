package game;

import java.util.ArrayList;

public class Round {

	// HERE we change status?

	final Joueur dessinateur;
	final ArrayList<Joueur> chercheurs;
	final ArrayList<Joueur> trouveurs; // ? good idea?
	final Dessin sketch;
	final String mot; // copy
	// GameManager gm;
	// final Object wordFound; //DIS

	Couleur currentColor;
	Integer currentSize;

	public Round(Joueur dessinateur, ArrayList<Joueur> chercheurs, String mot) {

		// this.gm=gm;
		this.dessinateur = dessinateur;
		this.chercheurs = chercheurs;
		// this.wordFound = wordFound;
		this.mot = mot;
		trouveurs = new ArrayList<>();
		sketch = new Dessin();
	}

	public Joueur getDessinateur() {
		return dessinateur;
	}

	public Integer getCurrentSize() {
		return currentSize;
	}

	public synchronized boolean stillSearching(){
		return chercheurs.size() == 0;
	}
	
	public synchronized void setCurrentSize(Integer currentSize) {
		this.currentSize = currentSize;
	}

	public synchronized  Couleur getCurrentColor() {
		return currentColor;
	}

	public synchronized  void setCurrentColor(int r, int g, int b) {
		this.currentColor = new Couleur(r, g, b);
	}

	public synchronized  Ligne addLigne(Integer x1, Integer y1, Integer x2, Integer y2) {
		return sketch.addLine(x1, y1, x2, y2, currentSize, currentColor);

		// SEE DOUBT not good place?
	}

	public synchronized String getDessinCommands() {
		return sketch.toCommand();
	}

	public synchronized ArrayList<Joueur> getTrouveurs() {
		return trouveurs;
	}

	public synchronized void setHasFound(Joueur j) {
		// CHECK + sync
		trouveurs.add(j);
		j.setRoleCourrant(Role.trouveur);
		chercheurs.remove(j);

	}

	public synchronized boolean guess(String essai) {
		return mot.equals(essai);
	}
}
