package game;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Dictionnaire {

	// TODO: Data structure to find.
	List<Entry> dico;
	Random rg;

	// Utiliser un hash pour les utilisation?

	public Dictionnaire(String file) throws FileNotFoundException {

		rg = new Random();
		dico = new ArrayList<Entry>(); // TODO choose array ou linked

		// BufferedReader br = new BufferedReader(new FileReader(file));

		Scanner in = new Scanner(new FileReader(file));

		while (in.hasNextLine()) {
			dico.add(new Entry(in.nextLine()));
		}
		in.close();

	}

	public String getWord() {
		int index = rg.nextInt(dico.size());
		Entry e = dico.get(index);
		e.incr();
		return e.mot;
		//TODO: maybe go for a suffle, and index?
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Liste des mots du dico: ");
		for (Entry e : dico) {
			sb.append(e.mot).append(" ");
		}
		return sb.toString();

	}

	/**
	 * Classe interne Entrée, TODO voir si pas inutile
	 * 
	 */
	class Entry {
		String mot;
		Integer nbUse;

		public Entry(String mot) {
			this.mot = mot;
			this.nbUse = 0;
		}

		public void incr() {
			this.nbUse++;
		}

	}

}
