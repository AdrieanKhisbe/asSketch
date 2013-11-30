package joueurs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.TreeMap;

import tools.IO;

public class Comptes implements Serializable {

	private static final long serialVersionUID = -7358347607418941482L;

	TreeMap<String, JoueurEnregistre> comptes;

	// Autres param?
	public Comptes() {
		this.comptes = new TreeMap<>();
	}

	public synchronized void addCompte(JoueurEnregistre j) {
		// CHECK pas déjà dedans

		comptes.put(j.getUsername(), j);
	}

	public synchronized JoueurEnregistre getJoueur(String username) {
		return comptes.get(username);
	}

	public synchronized boolean isFreeUsername(String name) {
		return comptes.containsKey(name);
	}

	public void serialize(String filepath) throws IOException {
		File f = new File(filepath);
		f.delete();

		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
		oos.writeObject(this);
		oos.close();
		IO.trace("Comptes on été sérialisés dans le fichier "+filepath);

	}

	public static Comptes deserialize(String filepath) throws IOException {

		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
				filepath));
		Object tmp;
		try {
			tmp = ois.readObject();
		} catch (ClassNotFoundException e) {
			IO.trace("Classe Compte non trouvée....");
			throw new IOException("Problème désérialisation");
		} finally {
			ois.close();
		}

		if (!(tmp instanceof Comptes))
			throw new IOException("Fichier comptient pas un compte");

		return (Comptes) tmp;
	}

}
