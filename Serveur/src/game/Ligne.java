package game;

public class Ligne extends Forme {

	public Ligne(Integer taille, Couleur couleur) {
		super(taille, couleur);
		// TODO Auto-generated constructor stub
	}

	public Ligne( Integer x1, Integer y1,
			Integer x2, Integer y2, Integer taille, Couleur couleur) {
		super(taille, couleur);
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	// début, fin
	Integer x1;
	Integer y1;
	Integer x2;
	Integer y2;

	@Override
	public String toCommand() {
		// TODO More generic? (and readable...)

		return "LINE/" + x1 + "/" + y1 + "/" + x2 + "/" + y2 + "/" + couleur.r
				+ "/" + couleur.g + "/" + couleur.b + "/";
	}

	@Override
	public String toString() {

		return "Ligne de (" + x1 + "," + y1 + ") à (" + x2 + "," + y2 + ")" 
				+ "De couleur (" + couleur.r +  "/" + couleur.g + "/" + couleur.b+")";
		// Format more readable
	}

}
