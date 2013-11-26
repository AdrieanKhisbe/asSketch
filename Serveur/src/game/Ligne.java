package game;

import tools.IO;

public class Ligne extends Forme {

	final Integer x1;
	final Integer y1;
	final Integer x2;
	final Integer y2;
	
	public Ligne(Integer x1, Integer y1, Integer x2, Integer y2,
			Integer taille, Couleur couleur) {
		super(taille, couleur);
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	//TODO del, uiseless
	public Ligne(String x1, String y1 ,String x2, String y2, String taille, String red , String green, String blue){
		super(Integer.parseInt(taille), 
				new Couleur(Integer.parseInt(red),Integer.parseInt(green),Integer.parseInt(blue)));
		this.x1 = Integer.parseInt(x1);
		this.y1 = Integer.parseInt(y1);
		this.x2 = Integer.parseInt(x2);
		this.y2 = Integer.parseInt(y2);
	}

	
	// début, fin


	@Override
	public String toCommand() {
		// TODO More generic? (and readable...)

		return "LINE/" + x1 + "/" + y1 + "/" + x2 + "/" + y2 + "/" + couleur.r
				+ "/" + couleur.g + "/" + couleur.b + "/";
	}

	@Override
	public String toString() {

		return "Ligne de (" + x1 + "," + y1 + ") à (" + x2 + "," + y2 + ")"
				+ "De couleur (" + couleur.r + "/" + couleur.g + "/"
				+ couleur.b + ")";
		// Format more readable
	}
    
     
     public static void main(String[] a) {
     IO.trace(new Ligne(1,2,3,4,6, new Couleur()).toString());    
     }
}
