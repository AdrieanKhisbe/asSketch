package core;

import game.Dictionnaire;
import game.Joueur;
import game.Partie;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

import core.ASSketchServer.Options;

public class Server implements Runnable{

	private ServerSocket sockServ;
	
	private Integer port;
	protected Integer nbMax;
	protected Dictionnaire dico;
	
	protected LinkedList<Socket> waitingSockets;
	// EXT Spectateur
	protected ArrayList<Joueur> joueurs;
	
	
	protected ArrayList<Partie> parties; // TODO ? distinguer partie, et round?
	
	// TODO!!: déclaration des threads 
	
	/**
	 * Simple constructeurs
	 * @param opt les options crées avec JCommander
	 */
	public Server(Options opt){
		
		try {
			port = opt.port;
			sockServ = new ServerSocket(port);
			
			waitingSockets = new LinkedList<Socket>();
			
			dico = new Dictionnaire(opt.dico); 
			joueurs = new ArrayList<Joueur>();
			
			nbMax = opt.nbJoueurs;
			
			// TODO: initialisation des threads
				
		} catch (IOException e) {
			// TODO To improve error handling
			System.err.println("error:"+ e.getMessage());
			// e.printStackTrace();
			System.exit(1);
		}
		
	}

	
	public synchronized void addWaitingSocket(Socket s){
		waitingSockets.add(s);
	}
	
	boolean waitingConnexion()
	{
		return (waitingSockets.size() != 0);
	}
	
	public synchronized Socket takeWaitingSocket(){
		return waitingSockets.pollFirst();
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
		
		
		LaunchConnexionStacking();
		LaunchConnexionHandling();
	
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	/***** 
	*/
	// WONDER retourner le threa?
	public void LaunchConnexionStacking(){
		
		new Thread(){
			public void run()
			{
				Socket client = new Socket();
					try {
						while (true) {
							client = sockServ.accept();
							System.out.println("Nouvelle connexion incoming mise en attente.");
							synchronized (waitingSockets) {
								addWaitingSocket(client);
								waitingSockets.notify();
							}
						}
					} catch (Throwable t) {
						t.printStackTrace(System.err);
					}
					
				
			}
		}.start();
	}
	/******* Handle Connection
	 * 
	 */
	
	public void LaunchConnexionHandling(){
		
		new Thread(){
			public void run()
			{
				Socket client;
				BufferedReader inchan;
				DataOutputStream outchan;
				
				// Q? ou variables instances
				
				while(true){
					synchronized(waitingSockets)
					{
						if(waitingConnexion())
						{
							System.out.println("Traitement connexion en attente.");
							client = takeWaitingSocket();
							// BONUX handle error.
							
							try {
								
								inchan = new BufferedReader(new InputStreamReader (client.getInputStream()));
								outchan = new DataOutputStream(client.getOutputStream());
						
								
								// Traitement selon commande reçue
								
								String command = inchan.readLine();
								
								if(command.equals("CONNECT/")){
									// parser
									outchan.writeChars("CONNECTED/\n");
									
									//TODO : GET HERE
																
								}else{
								
									outchan.writeChars("GOODBYE/BOLOS/\n");
									inchan.close();outchan.close();client.close();
									
									
								}

								
								
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								
								
							}
				
						}else{
							try {
								waitingSockets.wait();
								// at
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
					}
					
					
					
				}
				
			}
		}.start();
	}
	
	
}
