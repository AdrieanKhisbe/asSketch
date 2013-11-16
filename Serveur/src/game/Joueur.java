package game;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Joueur {

	
	private final String username;
	private final BufferedReader in;
	private final DataOutputStream out;
	private final Socket socket;
	
	//TODO: autres variables à créer!
	// -> role courant
	
	
	public Joueur(Socket client, String login) throws IOException{
		socket = client;
		username = login;
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new DataOutputStream(socket.getOutputStream());
		
	}
	
	
	public void send(String command){
		// TODO
	}
	
	
	public String toString(){
		return "Joueur:" + username
				//+" [host:"+this.getHost()+"]"
				;
	}

	public void close() {
		try {
			in.close();
			out.close();
			socket.close();
			
		//TODO   trace(j+" viens d'etre fermé");
			//TODO Trace.  (faire un level d'importance?)
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//TODO: readcommand?
	
}
