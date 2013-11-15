package core;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

public class ConnexionHandler implements Runnable{
	
	List<Socket> waitingSockets;
	ServerSocket serverSocket; // Bonne idée de la dupliquer avec le serveur?
	BufferedReader inchan;
	DataOutputStream outchan;
	
	
	public ConnexionHandler(ServerSocket serverSocket) {
		super();
		this.waitingSockets = new Vector<Socket>();
		this.serverSocket = serverSocket;
	}

	boolean waitingConnexion()
	{
		return (waitingSockets.size() != 0);
	}


/** 
 * Gère en permance nouvelles connexions
 */
	@Override
	public void run() {

		// TODO
	}

	}
	
	
	
