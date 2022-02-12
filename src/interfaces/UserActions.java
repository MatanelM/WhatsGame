package interfaces;

import beans.Gamer;
import exceptions.InvalidUsernameOrPassowrdException;
import exceptions.RegisterationException;

public interface UserActions {

	Gamer register(Gamer gamer) throws RegisterationException;
	
	Gamer login(String username, String password) throws InvalidUsernameOrPassowrdException;
	
	boolean isEmailExists(String email);
	boolean isNicenameExists(String email);
	Gamer getGamerByEmail(String email);
	Gamer getGamerByID(String gamerID);
	
	// assuming the user is logged in
	void addFriend(Gamer sender, Gamer receiver);
	void acceptFriend(Gamer reciever, Gamer sender);
	
	Gamer getForumOwner(String forumID);
}
