package game;

import java.util.ArrayList;

import core.GameManager;
import core.Protocol;

public class Round {
	
	// HERE we change status?
	
	Joueur dessinateur;
	ArrayList<Joueur> chercheurs;
	ArrayList<Joueur> trouveurs; //? good idea?
	Dessin sketch;
	//GameManager gm;
	final Object wordFound;

	Couleur currentColor;
	Integer currentSize;
	
	public Round(//GameManager gm,
			Joueur dessinateur, ArrayList<Joueur> chercheurs, Object wordFound) {
	
		//this.gm=gm;
		this.dessinateur = dessinateur;
		this.chercheurs = chercheurs;
		this.wordFound = wordFound;
		
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
		this.currentColor = new Couleur(r,g,b);
	}
	
	public Ligne addLigne(Integer x1, Integer y1,
			Integer x2, Integer y2){
		return sketch.addLine(x1, y1, x2, y2, currentSize, currentColor);
		
		// SEE DOUBT not good place?
	}
	
	public String getDessinCommands(){
		return sketch.toCommand();
	}
	
	
	
	
	
	

	

}
