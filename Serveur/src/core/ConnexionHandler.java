package core;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ConnexionHandler extends Thread {
	

	Server server; // Bonne idée de la dupliquer avec le serveur?
		
	public ConnexionHandler(Server s) {
		this.server = s;
	}

	
	
/** 
 * Gère en permance nouvelles connexions
 */
	@Override
	public void run() {
		
		Socket client;
		BufferedReader inchan;
		DataOutputStream outchan;
		// Q? ou variables instances
		
		while(true){
			synchronized(server.waitingSockets)
			{
				if(server.waitingConnexion())
				{
					client = server.takeWaitingSocket();
					// BONUX handle error.
					
					try {
						
						inchan = new BufferedReader(new InputStreamReader (client.getInputStream()));
						outchan = new DataOutputStream(client.getOutputStream());
				
						outchan.writeChars("TOTO");
						
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						
						
					}
		
				}else{
					try {
						server.waitingSockets.wait();
						// at
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			
			
			
		}
		// TODO
	}
	
	public void handleConnexion(){
		
	}

	}
	
	
	
