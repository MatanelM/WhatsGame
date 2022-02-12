package exceptions;

public class EmailAlreadyExistsException extends RegisterationException {

	public EmailAlreadyExistsException() {
		super("The given username is already exists.");
	}
	
}
