package controller;

import java.util.ArrayList;

import beans.Chat;
import beans.Forum;
import beans.FriendRequest;
import beans.Game;
import beans.Gamer;
import beans.Genre;
import beans.Notification;
import db.GameDB;
import interfaces.ModelListener;
import interfaces.ViewListener;
import model.Model;
import view.AbstractView;
import view.ChatView;
import view.ChatView.ChatType;

public class Controller implements ViewListener, ModelListener {

	private Model model;
	private AbstractView view;
	private ArrayList<ChatController> chatControllers = new ArrayList<>();
	public Controller(Model model, AbstractView view) {
		this.model = model;
		this.view = view;
		
		this.view.registerListener(this);
		this.model.registerListener(this);
		
		this.loadGamesToView(new GameDB().getAllGames());
		
//		NotificationsTask task = new NotificationsTask(getUserID());
//		new Thread(task).start();
	}


	private void loadGamesToView(ArrayList<Game> allGames) {
		this.view.loadGamesToView(allGames);
	}


	@Override
	public void updateNotificationsFromModel(ArrayList<Notification> notifications) {
		this.view.updateNotificationsInView(notifications);
	}

	@Override
	public String getUserID() {
		return model.getUserID();
	}

	@Override
	public void sendErrorFromModel(String message) {
		view.showErrorInView(message);
	}

	@Override
	public void loginFromView(String nickname, String password) {
		this.model.login(nickname, password);
		
	}

	@Override
	public void getForumsByGenreFromView(Genre e) {
		this.model.getForumsbByGenreInModel(e);
			
	}

	@Override
	public void addForumsByGenreInView(ArrayList<Forum> list) {
		this.view.addForumsByGenre(list);
	}

	@Override
	public void loggedInFromModel(Gamer gamer) {
		view.showGamerPane(gamer);
		
	}

	@Override
	public void declineFriendRequestFromView(FriendRequest notification) {
		this.model.declineFriendRequestInModel(notification);
		
	}

	@Override
	public void acceptFriendRequestFromView(FriendRequest notification) {
		this.model.acceptFriendRequestInModel(notification);
	}

	@Override
	public void friendRequestAcceptedFromModel(FriendRequest notification) {
		this.view.friendRequestAcceptedInView(notification);
	}

	@Override
	public void friendRequestDeclinedFromModel(FriendRequest notification) {
		this.view.friendRequestDeclinedInView(notification);
	}

	@Override
	public void readNotificationsFromView() {
		this.model.readNotificationsInModel();
	}

	@Override
	public void openChatControllerFromView(Chat from, Chat to, ChatType type) {
		ChatView chatView = new ChatView(from, to, type);
		ChatController controller = new ChatController(chatView, model);
		chatControllers.add(controller);
		this.model.openChatControllerInModel(chatView);
	}

	@Override
	public void closeProgramFromView() {
		this.model.closeProgramInModel();
	}


	@Override
	public void joinForumFromView(String forumID) {
		this.model.joinForumInModel(forumID);
	}


	@Override
	public void addFavGameFromView(Game g) {
		this.model.addFavGameInModel(g);
	}


	@Override
	public void addFavGenreFromView(Genre g) {
		this.model.addFavGenreInModel(g);		
	}


	@Override
	public void showErrorInView(String string) {
		this.view.showErrorInView(string);
	}


	@Override
	public void searchFriendByGenreFromView(Genre g) {
		
		this.model.searchFriendByGenreInModel(g);
	}


	@Override
	public void searchFriendByGameFromView(Game g) {
		this.model.searchFriendByGameInModel(g);
		
	}


	@Override
	public void addFriendstoSearchFromModel(ArrayList<Gamer> gamers) {
		this.view.addFriendsSearchInView(gamers);
	}


	@Override
	public void addFriendFromView(Gamer g) {
		this.model.addFriendInModel(g);
	}


	@Override
	public void addForumFromView(Forum f) {
		this.model.addForumFromModel(f);
	}


	@Override
	public void addedForumInModel(Forum forum) {
		this.view.addForumInView(forum);
	}

	
	
	
}
