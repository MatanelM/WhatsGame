package run;


import beans.Country;
import beans.Forum;
import beans.Gamer;
import controller.ChatController;
import db.ChatDB;
import db.GenresDB;
import db.UserDB;
import exceptions.InvalidUsernameOrPassowrdException;
import exceptions.RegisterationException;
import model.Model;
import view.ChatView;
import view.ChatView.ChatType;

public class Test {

	public static void main(String[] args) throws RegisterationException, InvalidUsernameOrPassowrdException {

		

//		userDB.register(gamer);
//		Gamer loggedIn = userDB.login("abc@def.gh", "123456");
//		System.out.println(loggedIn);
//		String chat ="0925247c-9537-49f4-b73d-40fa71325529";
//		ArrayList<Gamer> friendsWithMomo = new FriendDB().getAllGamerFriends(chat);
//		friendsWithMomo.forEach(System.out::println);

//		GenresDB genreDB = new GenresDB();

		// genreDB.getGenresFavorites(userDB.getGamerByEmail("bad@ilo.co").getId()).forEach(System.out::println);
//		ArrayList<Message> messages = new ArrayList<>();
//		messages = new MessageDB().getAllMessagesFromChat("eeadf52b-f171-44b6-872a-da4bcdd59168",
//				"7f1b0dac-1208-4c85-b2b4-298a396f4ca6");
//		for (Message message : messages) {
//			System.out.println(message.getFrom().getId() + " ");
//			System.out.println(message.getContent());
//		}
		UserDB userDB = new UserDB();
		Gamer gamer = userDB.getGamerByEmail("gyh@dhi.co");
		ChatDB chatDB = new ChatDB();
		Forum forum = chatDB.getForumByID("7d0a8b56-fcd1-4ab8-ba9c-1c549a2c9d33");
		ChatView view = new ChatView(gamer.getGamerChat(), forum, ChatType.FORUM);
		Model model = new Model();
		ChatController controller = new ChatController(view, model);
	}

	public static void createGamers() {
		String password = "0000";
		UserDB userDB = new UserDB();
		for (int i = 0; i < 50; i++) {
			String name = createName();
			String email = createEmail();
			Gamer gamer = new Gamer(name, email, password,
					Country.values()[(int) (Math.random() * Country.values().length)]);
			try {
				userDB.register(gamer);
			} catch (RegisterationException e) {
				e.printStackTrace();
			}
		}

	}

	public static char randomChar() {
		return "abcdefghijklmnopqrstuvwxyz".charAt((int) (Math.random() * 26));
	}

	public static char randomVowel() {
		return "aieou".charAt((int) (Math.random() * 5));
	}

	public static String createName() {
		String name = "";
		name += "" + (char) randomChar() + (char) randomVowel() + (char) randomChar() + (char) randomVowel();
		return name;
	}

	public static String createEmail() {
		String email = "";
		email += "" + (char) randomChar() + (char) randomChar() + (char) randomChar() + "@" + (char) randomChar()
				+ (char) randomChar() + (char) randomChar() + ".co";
		return email;
	}

}
