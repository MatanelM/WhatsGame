package interfaces;

import java.util.ArrayList;

import beans.FriendRequest;
import beans.Game;
import beans.Gamer;
import beans.Genre;

public interface FriendActions {

	ArrayList<Gamer> getAllGamerFriends(String gamerId);
	void accepctFriendRequest(String ReceiverID, String SenderID);
	FriendRequest getFriendRequestByID(String reqID);
	void declineFriendRequest(String reciever, String sender);
	
	ArrayList<Gamer> getAllGamersByGenre(Genre genre);
	ArrayList<Gamer> getAllGamersByGame(Game game);
	
	
}
