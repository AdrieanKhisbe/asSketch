package game;

import java.util.LinkedList;

import tools.IO;

public class Dessin {

	LinkedList<Forme> tracés;

	public Dessin() {

		this.tracés = new LinkedList<>();
	}

	public Ligne addLine(Integer x1, Integer y1, Integer x2, Integer y2,
			Integer taille, Couleur couleur) {
		Ligne l = new Ligne(x1, x2, y1, y2, taille, couleur);
		IO.trace("Ligne ajoutée: " + l); // ASK bien de mettre traceur ici?
		tracés.add(l);
		return l;
	}

	public String toCommand() {
		StringBuffer sb = new StringBuffer();

		for (Forme f : tracés)
			sb.append(f.toCommand()).append("\n");

		return sb.toString();

	}

	public String getLastCommand() {
		return tracés.peekLast().toCommand();

	}

}
