package interfaces;

import java.util.ArrayList;

import beans.Message;

public interface MessageActions {

	Message getMessageById(String id);
	void addMessage(Message message);
	void removeMessage(Message message);
	ArrayList<Message> getAllMessagesFromForum(String forumID);
	
	ArrayList<Message>  getAllMessagesFromChat(String chatID, String friend);
}
