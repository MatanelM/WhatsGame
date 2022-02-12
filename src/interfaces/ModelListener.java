package interfaces;

import java.util.ArrayList;

import beans.Forum;
import beans.FriendRequest;
import beans.Gamer;
import beans.Notification;

public interface ModelListener {

	void updateNotificationsFromModel(ArrayList<Notification> notifications);

	void sendErrorFromModel(String message);

	void loggedInFromModel(Gamer gamer);

	void addForumsByGenreInView(ArrayList<Forum> list);

	void friendRequestAcceptedFromModel(FriendRequest notification);

	void friendRequestDeclinedFromModel(FriendRequest notification);

	void showErrorInView(String string);

	void addFriendstoSearchFromModel(ArrayList<Gamer> gamers);

	void addedForumInModel(Forum forum);
	

}
