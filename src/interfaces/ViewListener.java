package interfaces;

import beans.Chat;
import beans.Forum;
import beans.FriendRequest;
import beans.Game;
import beans.Gamer;
import beans.Genre;
import view.ChatView.ChatType;

public interface ViewListener {


	String getUserID();

	void loginFromView(String nickname, String password);

	void getForumsByGenreFromView(Genre e);

	void declineFriendRequestFromView(FriendRequest notification);

	void acceptFriendRequestFromView(FriendRequest notification);

	void readNotificationsFromView();

	void openChatControllerFromView(Chat from, Chat to, ChatType type);
	
	void closeProgramFromView();

	void joinForumFromView(String forumID);

	void addFavGameFromView(Game g);

	void addFavGenreFromView(Genre g);

	void searchFriendByGenreFromView(Genre g);

	void searchFriendByGameFromView(Game g);

	void addFriendFromView(Gamer g);

	void addForumFromView(Forum g);

	
}
