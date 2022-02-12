package model;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

import beans.Forum;
import beans.FriendRequest;
import beans.Game;
import beans.Gamer;
import beans.Genre;
import core.ConnectionPool;
import exceptions.InvalidUsernameOrPassowrdException;
import exceptions.RegisterationException;
import interfaces.ModelListener;
import javafx.stage.Stage;
import tasks.NotificationsTask;
import user.SingletonUser;
import view.ChatView;

public class Model {

	private Gamer gamer = null;
	private NotificationsTask notificationsTask;
	private Thread notificationThread = null;
	
	private ArrayList<ModelListener> listeners;
	private ArrayList<Stage> chatViews;
	
	
	public Model() {
		this.listeners = new ArrayList<ModelListener>();
		this.chatViews = new ArrayList<Stage>();
	}
	
	public void registerListener(ModelListener listener) {
		this.listeners.add(listener);
	}

	public Gamer getGamer() {
		return SingletonUser.getInstnace().getGamer();
	}

	public void register(Gamer gamer) {
		try {
			this.gamer = SingletonUser.getInstnace().register(gamer);
			this.notificationsTask = new NotificationsTask(this.gamer);
			notificationThread = new Thread(notificationsTask);
			notificationThread.start();
			fireLoggedInFromModel(gamer);
		} catch (RegisterationException e) {
			fireErrorFromModel(e.getMessage());
		}
	}

	private void fireErrorFromModel(String message) {
		for(ModelListener l: listeners) {
			l.sendErrorFromModel(message);
		}
	}

//	private void fireAddNotificationsFromModel(ArrayList<Notification> notifications) {
//		for (ModelListener l : listeners) {
//			l.updateNotificationsFromModel(notifications);
//		}
//	}

	public String getUserID() {
		return this.getGamer().getId();
	}

	public void login(String email, String password) {
		try {
			Gamer gamer = SingletonUser.getInstnace().login(email, password);
			this.gamer = gamer;
			this.notificationsTask = new NotificationsTask(this.gamer);
			notificationThread = new Thread(notificationsTask);
			notificationThread.start();
			fireLoggedInFromModel(gamer);
		} catch (InvalidUsernameOrPassowrdException e) {
			fireErrorFromModel(e.getMessage());
		}
	}

	private void fireLoggedInFromModel(Gamer gamer) {
		listeners.forEach(l -> l.loggedInFromModel(gamer));
	}

	public void getForumsbByGenreInModel(Genre e) {

		ArrayList<Forum> list = SingletonUser.getInstnace().getForumsByGenre(e);
		
		fireGetFourmsByGenreFromModel(list);
	}

	private void fireGetFourmsByGenreFromModel(ArrayList<Forum> list) {
		listeners.forEach(l -> l.addForumsByGenreInView(list));
		
	}

	public void declineFriendRequestInModel(FriendRequest notification) {
		SingletonUser.getInstnace().declineFriend(notification);
		fireFriendRequestDeclinedInModel(notification);
		
	}

	private void fireFriendRequestAcceptedInModel(FriendRequest notification) {
		this.listeners.forEach(l -> l.friendRequestAcceptedFromModel(notification));
		
	}

	public void acceptFriendRequestInModel(FriendRequest notification) {
		SingletonUser.getInstnace().acceptFriend(notification);
		fireFriendRequestAcceptedInModel(notification);
		
	}

	private void fireFriendRequestDeclinedInModel(FriendRequest notification) {
		this.listeners.forEach(l -> l.friendRequestDeclinedFromModel(notification));
		
	}

	public void readNotificationsInModel() {
		SingletonUser.getInstnace().readNotifications();
		
	}

	public void openChatControllerInModel(ChatView chatView) {
		notificationsTask.addListener(chatView);
		this.chatViews.add(chatView.getStage());
	}

	public void removeChatViewListener(ChatView chatView) {
		this.notificationsTask.removeListener(chatView);
		this.chatViews.remove(chatView.getStage());
	}

	public void closeProgramInModel() {
		for (Stage s : chatViews) {
			s.close();
		}
		if ( this.notificationThread != null )
			this.notificationThread.interrupt();
		SingletonUser.getInstnace().logout();
		ConnectionPool.getInstance().closeAllConnections();
		
		
	}

	public void joinForumInModel(String forumID) {
		try {
			SingletonUser.getInstnace().joinForum(forumID);
		} catch (SQLIntegrityConstraintViolationException e) {
			listeners.forEach(l -> l.showErrorInView("Already have this chat"));
		}
	}

	public void addFavGameInModel(Game g) {
		SingletonUser.getInstnace().addGameToFav(g);
	}
	public void addFavGenreInModel(Genre g) {
		SingletonUser.getInstnace().addGenreToFav(g);
	}

	public ArrayList<Gamer> searchFriendByGenreInModel(Genre g) {
		ArrayList<Gamer> gamers = SingletonUser.getInstnace().searchGamersByGenre(g);
		if ( gamers.isEmpty() ) {
			listeners.forEach(l -> l.showErrorInView("Not found"));
		}else {
			fireAddFriendsToSearchFromModel(gamers);
		}
		
		return gamers;
	}
	private void fireAddFriendsToSearchFromModel(ArrayList<Gamer> gamers) {
		listeners.forEach(l -> l.addFriendstoSearchFromModel(gamers)); 
	}

	public ArrayList<Gamer> searchFriendByGameInModel(Game g) {
		ArrayList<Gamer> gamers = SingletonUser.getInstnace().searchGamersByGame(g);
		if ( gamers.isEmpty() ) {
			listeners.forEach(l -> l.showErrorInView("Not found"));
		}else {
			fireAddFriendsToSearchFromModel(gamers);
		}
		
		return gamers;
	}

	public void addFriendInModel(Gamer g) {
		try {
			SingletonUser.getInstnace().sendFriendRequest(g.getId());
		} catch (Exception e) {
			listeners.forEach(l -> l.showErrorInView("Cannot add friend"));
		}
	}

	public void addForumFromModel(Forum f) {
		Forum forum = SingletonUser.getInstnace().addForum(f);
		fireForumAdded(forum);
	}

	private void fireForumAdded(Forum forum) {
		listeners.forEach(l -> l.addedForumInModel(forum));
	}

}
