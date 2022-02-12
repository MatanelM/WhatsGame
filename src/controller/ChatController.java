package controller;

import java.util.ArrayList;

import beans.Forum;
import beans.FriendRequest;
import beans.Gamer;
import beans.Notification;
import interfaces.ModelListener;
import model.Model;
import view.ChatView;

public class ChatController implements ChatViewListener, ModelListener {

	private ChatView view;
	private Model model;

	public ChatController(ChatView view, Model model) {
		super();
		this.view = view;
		this.model = model;

		view.registerListener(this);
		model.registerListener(this);
	}

	@Override
	public void updateNotificationsFromModel(ArrayList<Notification> notifications) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendErrorFromModel(String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loggedInFromModel(Gamer gamer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addForumsByGenreInView(ArrayList<Forum> list) {
		// TODO Auto-generated method stub

	}

	@Override
	public void friendRequestAcceptedFromModel(FriendRequest notification) {
		// TODO Auto-generated method stub

	}

	@Override
	public void friendRequestDeclinedFromModel(FriendRequest notification) {
		// TODO Auto-generated method stub

	}

	@Override
	public void closeChatViewFromView(ChatView chatView) {
		model.removeChatViewListener(chatView);
	}


	public ChatView getView() {
		return this.view;
	}

	@Override
	public void showErrorInView(String string) {
		
	}

	@Override
	public void addFriendstoSearchFromModel(ArrayList<Gamer> gamers) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addedForumInModel(Forum forum) {
		// TODO Auto-generated method stub
		
	}

}
