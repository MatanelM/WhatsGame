package user;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashSet;

import beans.Forum;
import beans.FriendRequest;
import beans.Game;
import beans.Gamer;
import beans.Genre;
import beans.Notification;
import beans.Notification.NotificationType;
import db.ChatDB;
import db.FriendDB;
import db.GameDB;
import db.GenresDB;
import db.MessageDB;
import db.NotificationsDB;
import db.UserDB;
import exceptions.InvalidUsernameOrPassowrdException;
import exceptions.RegisterationException;

public class SingletonUser {

	private static SingletonUser instance = null;

	private Gamer gamer;
	private UserDB userDB;
	private MessageDB messageDB;
	private NotificationsDB notificationsDB;
	private FriendDB friendDB;
	private GenresDB genresDB;
	private GameDB gameDB;
	private ChatDB chatDB;
	private ArrayList<Forum> forums;

	public SingletonUser() {
		super();
		this.userDB = new UserDB();
	}

	public synchronized static SingletonUser getInstnace() {
		if (instance == null)
			instance = new SingletonUser();
		return instance;
	}

	public Gamer getGamer() {
		return gamer;
	}

	public void setGamer(Gamer gamer) {
		this.gamer = gamer;
	}

	public Gamer login(String username, String password) throws InvalidUsernameOrPassowrdException {
		this.gamer = userDB.login(username, password);

		if (gamer == null) {
			return null;
		}
		initDB();
		this.gamer.setFriends(friendDB.getAllGamerFriends(this.gamer.getId()));
		this.gamer.setGames(new HashSet<Game>(gameDB.getLikeGamesByID(this.gamer.getId())));
		this.gamer.setLikedGenres(new HashSet<Genre>(genresDB.getGenresFavorites(this.gamer.getId())));
		this.gamer.setNotifications(notificationsDB.checkNotifications(this.gamer));
		this.gamer.setForums(chatDB.getForumsOfGamer(this.gamer.getId()));
		return this.gamer;

	}

	public Gamer register(Gamer gamer) throws RegisterationException {
		userDB.register(gamer);
		this.gamer = gamer;

		initDB();

		return this.gamer;
	}

	private void initDB() {
		messageDB = new MessageDB();
		notificationsDB = new NotificationsDB();
		friendDB = new FriendDB();
		genresDB = new GenresDB();
		gameDB = new GameDB();
		chatDB = new ChatDB();

	}

	public ArrayList<Forum> getForumsByGenre(Genre genre) {

		forums = this.chatDB.getForumByGenre(genre);
		forums.forEach(f -> {
			if (f.getOwner().equals(this.gamer)) {
				forums.remove(f);
			}
		});
		return forums;
	}

	public void declineFriend(FriendRequest notification) {
		this.friendDB.declineFriendRequest(this.gamer.getId(), notification.getGamerSender().getId());
		this.notificationsDB.markAsRead(new Notification(notification, NotificationType.FRIEND), this.gamer);
	}

	public void acceptFriend(FriendRequest notification) {
		this.friendDB.accepctFriendRequest(this.gamer.getId(), notification.getGamerSender().getId());
		this.notificationsDB.markAsRead(new Notification(notification, NotificationType.FRIEND), this.gamer);
	}

	public void readNotifications() {
//		this.gamer.getNotifications().forEach(n -> {
//			if (!n.isRead()) {
//				notificationsDB.markAsRead(n, this.gamer);
//				n.setRead(true);
//			}
//		});
	}

	public void logout() {
		this.gamer = null;
		messageDB = null;
		notificationsDB = null;
		friendDB = null;
		genresDB = null;
		gameDB = null;
		chatDB = null;
	}

	public void joinForum(String forumID) throws SQLIntegrityConstraintViolationException {
		chatDB.addGamerToForum(this.gamer.getId(), forumID);
	}

	public void sendFriendRequest(String gamerID) throws SQLIntegrityConstraintViolationException{
		FriendRequest friendRequest = new FriendRequest(this.gamer, userDB.getGamerByID(gamerID));
		notificationsDB.addFriendRequest(friendRequest);
	}
	
	public void addGenreToFav(Genre genre) {
		this.genresDB.addGenreToFavorites(genre, this.gamer.getId());
	}
	
	public void addGameToFav(Game game) {
		this.gameDB.addGameToFavorites(game, this.gamer.getId());
	}

	public ArrayList<Gamer> searchGamersByGenre(Genre g) {
		return this.friendDB.getAllGamersByGenre(g);
	}
	public ArrayList<Gamer> searchGamersByGame(Game g) {
		return this.friendDB.getAllGamersByGame(g);
	}

	public Forum addForum(Forum f) {
		this.chatDB.addForumChat(f.getId(), this.gamer.getId(), 0, f.getGenre());
		f = chatDB.getForumByID(f.getId());
		return f;
	}


}
