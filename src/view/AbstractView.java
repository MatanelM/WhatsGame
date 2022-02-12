package view;

import java.util.ArrayList;

import beans.Forum;
import beans.FriendRequest;
import beans.Game;
import beans.Gamer;
import beans.Notification;
import interfaces.ViewListener;

public interface AbstractView {

	void registerListener(ViewListener listener);

	void updateNotificationsInView(ArrayList<Notification> notifications);

	void showErrorInView(String message);
	
	void showGamerPane(Gamer gamer);

	void addForumsByGenre(ArrayList<Forum> list);

	void friendRequestAcceptedInView(FriendRequest notification);

	void friendRequestDeclinedInView(FriendRequest notification);

	void loadGamesToView(ArrayList<Game> allGames);

	void addFriendsSearchInView(ArrayList<Gamer> gamers);

	void addForumInView(Forum forum);

}
