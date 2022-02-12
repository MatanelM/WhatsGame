package interfaces;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

import beans.Forum;
import beans.Gamer;
import beans.Genre;
import beans.Message;

public interface ChatActions {

	void addForumChat(String GamerID, int gameID, Genre genre);
	void addGamerToForum(String GamerID, String ForumID) throws SQLIntegrityConstraintViolationException;
	void removeGamerFromForum(String GamerID, String ForumID);
	ArrayList<Gamer> getForumParticipants(String ForumID);
	Forum getForumByID(String ForumID);
	
	ArrayList<Forum> getForumsOfOwner(String GamerID);
	ArrayList<Message> getForumMessages(String ForumID);
	ArrayList<Message> getChatMessages(String gamer, String friend);
	
	ArrayList<Forum> getForumByGenre(Genre genre);
	
	ArrayList<Forum> getForumsOfGamer(String id);
	
	boolean isForumExists(String forumID);
	
}
