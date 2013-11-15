package core;

import java.net.ServerSocket;
import java.net.Socket;

public class ConnexionStacker extends Thread {
	
	Server server;
	ServerSocket ssock;
	Socket client;
	
	public ConnexionStacker(Server s, ServerSocket sso){
		this.server = s;
		this.ssock = sso;
	}

/** 
 * Gère en permance nouvelles connexions
 */
	@Override
	public void run() {
		try {
			while (true) {
				client = ssock.accept();
				System.out.println("Nouvelle connexion incoming mise en attente.");
				synchronized (server.waitingSockets) {
					// CHECK Mal concu?
					server.addWaitingSocket(client);
					server.waitingSockets.notify();
					
					/* Plante
					java.lang.IllegalMonitorStateException
					at java.lang.Object.notify(Native Method)
					at core.ConnexionStacker.run(ConnexionStacker.java:29)
					*/
				}
			}
		} catch (Throwable t) {
			t.printStackTrace(System.err);
		}
	}

	}
	
	
	
