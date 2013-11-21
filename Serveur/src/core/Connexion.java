package core;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

public class Connexion {

	private Socket socket;
	private BufferedReader inchan;
	private DataOutputStream outchan;

	public Connexion(Socket socket, BufferedReader inchan,
			DataOutputStream outchan) throws SocketException {
		super();
		this.socket = socket;
		this.inchan = inchan;
		this.outchan = outchan;
		socket.setSoTimeout(0); // cause throw
	}

	public Connexion(Socket s) throws IOException {
		socket = s;
		inchan = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		outchan = new DataOutputStream(socket.getOutputStream());
		s.setSoTimeout(0); // reset timeout
	}

	public void close(String message) throws IOException {
		outchan.writeChars(message + "\n");
		close();

	}

	public void close() throws IOException {
		//BONUX TODO : relier au traceur en mode debug?
		inchan.close();
		outchan.close();
		socket.close();
	}

	public void send(String message) throws IOException {

		outchan.writeChars(message);
	}

	public String getCommand() throws IOException {
		return inchan.readLine();
	}

}
