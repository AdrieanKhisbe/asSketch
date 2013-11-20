package game;

import java.util.LinkedList;

public class Dessin {

	LinkedList<Forme> tracés;

	public Dessin() {
		
		this.tracés = new LinkedList<>();
	}
	
	public void addLine(Integer x1, Integer y1,
			Integer x2, Integer y2, Integer taille, Couleur couleur)
	{
		tracés.	add(new Ligne(x1,x2,y1,y2,taille,couleur));
		
	}
	
	
	public String toCommand(){
		StringBuffer sb = new StringBuffer();
		
		for(Forme f : tracés)
			sb.append(f.toCommand()).append("\n");
				
		return sb.toString();
			
	}
	
	
	public String getLastCommand(){
		return tracés.peekLast().toCommand();			
			
	}
	
	
	
}
