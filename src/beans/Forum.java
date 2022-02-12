package beans;

import java.util.ArrayList;

public class Forum extends Chat {

	private Gamer owner;
	private ArrayList<Gamer> gamers;
	private ArrayList<Message> messages;
	private Genre genre;
	private Game game;

	public Forum() {
		super();
		this.owner = null;
		this.setGamers(new ArrayList<>());
		this.setMessages(new ArrayList<>());
	}

	public Forum(String id, Gamer owner, ArrayList<Gamer> gamers, ArrayList<Message> messages) {
		super(id);
		this.owner = owner;
		this.gamers = gamers;
		this.messages = messages;
		this.genre = null;
		this.game = null;
	}

	public Forum(Forum forum) {
		super(forum.getId());
		this.owner = forum.getOwner();
		this.gamers = forum.getGamers();
		this.messages = forum.getMessages();
	}

	public ArrayList<Gamer> getGamers() {
		return gamers;
	}

	public void setGamers(ArrayList<Gamer> gamers) {
		this.gamers = gamers;
	}

	public ArrayList<Message> getMessages() {
		return messages;
	}

	public void setMessages(ArrayList<Message> messages) {
		this.messages = messages;
	}

	public Gamer getOwner() {
		return owner;
	}

	public void setOwner(Gamer owner) {
		this.owner = owner;
	}

	public Genre getGenre() {
		return genre;
	}

	public void setGenre(Genre genre) {
		this.genre = genre;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}
	

}
