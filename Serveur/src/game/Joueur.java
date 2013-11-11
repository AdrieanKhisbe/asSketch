package game;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Joueur {

	
	private String username;
	private BufferedReader in;
	private DataOutputStream out;
	private Socket socket;
	
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
	
	//TODO: readcommand?
	
}