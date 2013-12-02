package game.graphiques;

import java.util.Date;

import tools.IO;

public abstract class Forme {

	Date dateAjout;
	Integer taille;
	Couleur couleur;

	public Forme(Integer taille, Couleur couleur) {
		this.dateAjout = IO.getCal().getTime();
		this.taille = taille;
		this.couleur = couleur;
	}

	public abstract String toCommand();

     
    @Override
	public abstract String toString();

}
