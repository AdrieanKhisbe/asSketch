package game;

import java.util.ArrayList;

public class Partie {

	ArrayList<Round> rounds;
	Round tourCourrant;

	// HERE TODO : Stocker les joueurs ? why?
	// + Scores?
	// Score sous forme hashmap?

	
	public Partie() {
		this.rounds = new ArrayList<>();
		this.tourCourrant = null;
	}
	
	public void newRound(Joueur dessinateur, String mot) {

	//	tourCourrant = new Round(dessinateur, mot);
		// TODO HERE dois ajouter les joueurs

	}



	public Round getTourCourrant() {
		return tourCourrant;
	}
	
//	String getScore(){
//		
//	}

}
