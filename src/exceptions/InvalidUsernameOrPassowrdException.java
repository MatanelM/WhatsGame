package exceptions;

public class InvalidUsernameOrPassowrdException extends LoginException {

	public InvalidUsernameOrPassowrdException() {
		super("The username or password are invalid");
	}

}
