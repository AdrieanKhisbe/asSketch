package game;

import java.util.List;

public class Round {
	
	Joueur dessinateur;
	List<Joueur> chercheurs;
	List<Joueur> trouveurs; //? good idea?
	Dessin scketch;
	
	Integer currentSize;
	
	
	
	
	
	
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
	Couleur currentColor;
	
	
	
	
	
	
	

	

}
