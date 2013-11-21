package game;

import java.util.ArrayList;

public class Partie {

	ArrayList<Round> rounds;
	Round tourCourrant;

	
	// HERE  TODO : Stocker les joueurs
	// Score sous forme hashmap
	
	public void  newRound(Joueur dessinateur, String mot){
	 
		tourCourrant = new Round (dessinateur, mot);
		//TODO HERE
		
		
	}
	
	public Round getTourCourrant(){
		return tourCourrant;
	}
	
	
	
	
	
}
