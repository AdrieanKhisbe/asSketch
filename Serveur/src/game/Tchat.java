package game;

import game.joueurs.Joueur;

import java.util.Calendar;
import java.util.Date;

import tools.Protocol;

public class Tchat {

	public String message;
	public Joueur auteur;
	public Date timestamps;
	
	
	
	public String toCommand(){
		return Protocol.newListen(auteur, message);
	}



	public Tchat(String message, Joueur auteur) {
		super();
		this.message = message;
		this.auteur = auteur;
		this.timestamps = Calendar.getInstance().getTime();
	}
	
}
