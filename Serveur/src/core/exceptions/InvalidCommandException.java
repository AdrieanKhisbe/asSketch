package core.exceptions;

/**
 * Exception père des commandes invalides reçues du client
 *
 */
public class InvalidCommandException extends Exception {

	public InvalidCommandException(String message) {
		super(message); // maybe, remplacer les " " par "_"?
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3835326239959660003L;
	
	
}
