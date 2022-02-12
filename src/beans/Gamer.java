package beans;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.UUID;


public class Gamer {

	private String id;
	private String nickname;
	private String password;
	private String email;
	private Country country;

	private List<Gamer> friends;
	private Set<Game> games;
	private Set<Genre> likedGenres;
	private List<Notification> notifications;
	private ArrayList<Forum> forums;
	private Chat gamerChat;
	
	public Gamer(String nicename, String email, String password, Country country) {
		this(UUID.randomUUID().toString(), 
				nicename, email, country, 
				new ArrayList<Gamer>(), 
				new HashSet<Game>(), 
				new HashSet<Genre>(),
				new Stack<>(),
				new ArrayList<Forum>(),
				new Chat());
		this.password = password;
	}
	
	public Gamer(String id, String nicename, String email, Country country, List<Gamer> friends, Set<Game> games,
			Set<Genre> likedGenres, List<Notification> notifications,ArrayList<Forum> forums, Chat gamerChat){
		this.id = id;
		this.nickname = nicename;
		this.email = email;
		this.country = country;
		this.friends = friends;
		this.games = games;
		this.likedGenres = likedGenres;
		this.setNotifications(notifications);
		this.gamerChat = gamerChat;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	public String getNicename() {
		return nickname;
	}

	public void setNicename(String nicename) {
		this.nickname = nicename;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public List<Gamer> getFriends() {
		return friends;
	}

	public void setFriends(List<Gamer> friends) {
		this.friends = friends;
	}

	public Set<Game> getGames() {
		return games;
	}

	public void setGames(Set<Game> games) {
		this.games = games;
	}

	public Set<Genre> getLikedGenres() {
		return likedGenres;
	}

	public void setLikedGenres(Set<Genre> likedGenres) {
		this.likedGenres = likedGenres;
	}

	public Chat getGamerChat() {
		return gamerChat;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Notification> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}

	@Override
	public String toString() {
		return this.nickname;
	}

	public ArrayList<Forum> getForums() {
		return forums;
	}

	public void setForums(ArrayList<Forum> forums) {
		this.forums = forums;
	}

}
