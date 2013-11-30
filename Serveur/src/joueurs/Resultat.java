package joueurs;

public class Resultat implements Comparable<Resultat>{

	public Integer score, nbMotsTrouvés, nbFalseSuggestions;
	public Integer position; // BONUX change?
	
	
	public Resultat(int score, int nbMotsTrouvés, int nbSuggestions, int position) {
		this.score = score;
		this.nbMotsTrouvés = nbMotsTrouvés;
		this.nbFalseSuggestions = nbSuggestions;
		this.position = position;
	}
	
	public Resultat() {
		this(0,0,0,-1);
	}
	
	public void addMotTrouvé(){
		nbMotsTrouvés +=1;
	}
	
	public void addFalseSuggestion(){
		nbFalseSuggestions +=1;
	}
	
	public Double hitRate(){
		return nbFalseSuggestions == 0 ? 0  : nbMotsTrouvés /
				(nbMotsTrouvés+ nbFalseSuggestions.doubleValue());
		
	}
	
	public void addScore(int score) {
		this.score += score;
	}
	
	public void setPosition(int nb){
		this.position = nb;
	}

	@Override
	public int compareTo(Resultat o) {
		return score.compareTo(o.score);
	}
	
}
