package core;

import game.JoueurRole;

import java.lang.Thread.State;
import java.util.HashMap;

import core.exceptions.InvalidCommandException;



class CommandParser {
		
		private final HashMap<String, CommandParameter> gameCommand = new HashMap<>();
		
		class CommandParameter{
			
			JoueurRole   role;
			Integer arité;
			CommandParameter(JoueurRole r, Integer arité) {
				this.role = r;
				this.arité = arité;
			}
			
			
		}
		
		static {   // Génère la liste des commandes
			//EXIT
			
			

			// DESSIN
			
			// SUGGESTION
			
			

			
			
			
			// TALK
			
			
		}
		
		
		
		static String[] parseCommand(String command, JoueurRole roleCourant) throws InvalidCommandException{
	
			// TODO HERE
			return command.split("/");
			
		}
		
	
		
	}
