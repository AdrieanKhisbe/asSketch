package core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import game.Dictionnaire;
import game.Joueur;
import game.Partie;
import core.ASSketchServer.Options;

public class Server implements Runnable{

	private ServerSocket sockServ;
	private Integer port;
	protected Integer nbMax;
	protected Dictionnaire dico;
	
	protected List<Socket> waitingSocket;
	// EXT Spectateur
	protected List<Joueur> joueurs;
	
	
	protected List<Partie> parties; // TODO ? distinguer partie, et round?
	
	// TODO!!: déclaration des threads 
	
	/**
	 * Simple constructeurs
	 * @param opt les options crées avec JCommander
	 */
	public Server(Options opt){
		
		try {
			port = opt.port;
			sockServ = new ServerSocket(port);
			
			
			dico = new Dictionnaire(opt.dico); 
			joueurs = new LinkedList<Joueur>();
			
			nbMax = opt.nbJoueurs;
			
			// TODO: initialisation des threads
				
		} catch (IOException e) {
			// TODO To improve error handling
			System.err.println("error:"+ e.getMessage());
			// e.printStackTrace();
			System.exit(1);
		}
		
	}
	
	
	public void addJoueur(Joueur j)
		{
		 joueurs.add(j);
		}
	
	public synchronized  void removeJoueur(Joueur j)
	{
		// ?? ferme ton à ce niveau les flux?
		 j.close();
		 joueurs.remove(j);
	
	}
	
	// EXT Spect
	
	
	
	public void run()
	{
		//TODO vrai run à faire!
		
		System.out.println(dico);
		System.out.println("Random word: " +  dico.getWord());
		
		
		// Lance le connexion recepteur: receptionne nouvelles connexions
		// Lance le Connexion Handler: gère les nouvelles connexion: to client/ spectateur
		
			
		
		System.out.println("I don't run, yet...");
	
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
}
