package exceptions;

public class NicknameAlreadyExistsException extends RegisterationException {

	public NicknameAlreadyExistsException() {
		super("The given nickname is already exists.");
	}

}
