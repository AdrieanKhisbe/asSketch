package game;

import graphiques.Couleur;
import graphiques.Dessin;
import graphiques.Ligne;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import joueurs.Joueur;
import joueurs.Role;

public class Round {

	final Joueur dessinateur;
	final ArrayList<Joueur> chercheurs;
	final ArrayList<Joueur> trouveurs; 
	Set<Joueur> cheatWarningList;

	final Dessin sketch;
	final String mot; // copy

	Couleur currentColor;
	Integer currentSize;

	public Round(Joueur dessinateur, ArrayList<Joueur> chercheurs, String mot) {

		// this.gm=gm;
		this.dessinateur = dessinateur;
		this.chercheurs = chercheurs;
		// this.wordFound = wordFound;
		this.mot = mot;
		trouveurs = new ArrayList<>();
		this.cheatWarningList = new HashSet<>();
		sketch = new Dessin();

		// Valeur défaulrs
		currentColor = new Couleur(); // black
		currentSize = 5;

	}

	public Joueur getDessinateur() {
		return dessinateur;
	}

	public Integer getCurrentSize() {
		return currentSize;
	}

	public synchronized boolean stillSearching() {
		return chercheurs.size() == 0;
	}

	public synchronized void setCurrentSize(Integer currentSize) {
		this.currentSize = currentSize;
	}

	public synchronized Couleur getCurrentColor() {
		return currentColor;
	}

	public synchronized void setCurrentColor(int r, int g, int b) {
		this.currentColor = new Couleur(r, g, b);
	}

	public synchronized Ligne addLigne(Integer x1, Integer y1, Integer x2,
			Integer y2) {
		return sketch.addLine(x1, y1, x2, y2, currentSize, currentColor);
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
		return mot.toLowerCase().equals(essai.toLowerCase());
	}

	public synchronized boolean addCheatWarn(Joueur j) {
		return cheatWarningList.add(j);
	}

	public synchronized Integer getNbWarn() {
		return cheatWarningList.size();
	}

	// TODO: end turn: FIGE, et set la raison victoire

}
