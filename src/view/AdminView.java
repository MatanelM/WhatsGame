package view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JOptionPane;

import beans.Chat;
import beans.Country;
import beans.Forum;
import beans.FriendRequest;
import beans.Game;
import beans.Gamer;
import beans.Genre;
import beans.Message;
import beans.Notification;
import db.ChatDB;
import db.FriendDB;
import db.GenresDB;
import db.MessageDB;
import db.NotificationsDB;
import db.UserDB;
import decorator.BorderRadiusDescorator;
import decorator.HideDecorator;
import decorator.ShowDecorator;
import exceptions.RegisterationException;
import interfaces.ViewListener;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view.components.GenreBox;
import view.components.InputBox;
import view.components.MyButton;
import view.components.MyComboBox;
import view.components.MyLabel;
import view.components.InputBox.InputType;
import view.components.MyButton.ButtonSizeType;
import view.components.MyLabel.LabelSize;

public class AdminView implements AbstractView {

	private MessageDB messageDB = new MessageDB();
	private ChatDB chatDB = new ChatDB();
	private UserDB userDB = new UserDB();
	private NotificationsDB notificationDB = new NotificationsDB();
	private FriendDB friendDB = new FriendDB();
	private GenresDB genreDB = new GenresDB();

	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;

	public AdminView(Stage stage) {

		TabPane tabPane = new TabPane();

		VBox vbTab1 = new VBox();

		InputBox ibName = new InputBox("Name:", InputType.TEXT);
		ibName.getField().setPromptText("Name");
		InputBox ibEmail = new InputBox("Email:", InputType.TEXT);
		ibEmail.getField().setPromptText("Email");
		InputBox ibPassword = new InputBox("Password:", InputType.PASSWORD);
		InputBox ibPasswordRepeat = new InputBox("Repeat Password:", InputType.PASSWORD);
		ibPasswordRepeat.getField().setPromptText("Repeat Password");
		MyComboBox cbCountry = new MyComboBox("Country:");

		MyButton btnAddPlayer = new MyButton("Add Player", ButtonSizeType.SMALL);
		btnAddPlayer.setOnAction(e -> {
			String name = ibName.getValue();
			String email = ibEmail.getValue();
			String password = ibPassword.getValue();
			String passRepeat = ibPasswordRepeat.getValue();

			String country = cbCountry.getValue();
			if (passRepeat.equals(password)) {
				Gamer gamer = new Gamer(name, email, password, Country.valueOf(country));
				try {
					userDB.register(gamer);
					JOptionPane.showMessageDialog(null, "Player Added");
				} catch (RegisterationException e1) {
					e1.printStackTrace();
				}
			} else {
				JOptionPane.showMessageDialog(null, "Password not match");
			}
		});
		VBox vbTab2 = new VBox();
		VBox vbTab3 = new VBox();
		VBox vbTab4 = new VBox();
		VBox vbTab5 = new VBox();

		// tab1
		Tab tab1 = new Tab("Add Chat", vbTab1);
		InputBox forumOwner = new InputBox("Forum Owner email: ", InputType.TEXT);
		forumOwner.getField().setText("gyh@dhi.co");
		
		FlowPane fpGenresForum = new FlowPane();
		ArrayList<Genre> genresForum = new ArrayList<Genre>();
		Collections.addAll(genresForum, Genre.values());
		genresForum.forEach( g -> {
			
			GenreBox gb = new GenreBox(g);
			gb.setOnMouseClicked(e -> {
				Gamer gamer1 = userDB.getGamerByEmail(forumOwner.getValue());
				if (gamer1 != null) {
					try {

						chatDB.addForumChat(gamer1.getId(), 0, g);
						JOptionPane.showMessageDialog(null, "Added");
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(null, "Player not match");
				}
			});
			fpGenresForum.getChildren().add(gb);
		});
		
		vbTab1.getChildren().add(forumOwner);
		vbTab1.getChildren().add(fpGenresForum);
		// tab2
		Tab tab2 = new Tab("Add Gamer", vbTab2);
		vbTab2.getChildren().addAll(ibName, ibEmail, ibPassword, ibPasswordRepeat, cbCountry, btnAddPlayer);
		vbTab2.setPadding(new Insets(30));
		vbTab2.setSpacing(10);

		// tab3
		Tab tab3 = new Tab("Add Message", vbTab3);

		InputBox ibFrom = new InputBox("From:", InputType.TEXT);
		ibFrom.getField().setText("abc@def.co");
		ibFrom.getField().setPromptText("From email");
		InputBox ibTo = new InputBox("To:", InputType.TEXT);
		ibTo.getField().setText("bed@ilo.co");
		ibTo.getField().setPromptText("To email");
		InputBox ibContent = new InputBox("Content: ", InputType.TEXT);
		ibContent.getField().setPromptText("Content");
		MyButton btnAddMessage = new MyButton("Send Message", ButtonSizeType.SMALL);
		btnAddMessage.setOnAction(e -> {
			Gamer gamer1 = userDB.getGamerByEmail(ibFrom.getValue());
			Gamer gamer2 = userDB.getGamerByEmail(ibTo.getValue());
			String content = ibContent.getValue();

			if (gamer1 != null && gamer2 != null) {

				Message message = new Message(content, gamer1.getGamerChat(), gamer2.getGamerChat(), LocalDate.now());
				try {
					messageDB.addMessage(message);
					JOptionPane.showMessageDialog(null, "Message Sent");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} else {
				JOptionPane.showMessageDialog(null, "Player not match");
			}
		});
		vbTab3.getChildren().addAll(ibFrom, ibTo, ibContent, btnAddMessage, new Separator());
		vbTab3.setPadding(new Insets(30));
		vbTab3.setSpacing(10);
		
		InputBox ibFromChat = new InputBox("From :", InputType.TEXT);
		ibFromChat.getField().setText("gyh@dhi.co");
		InputBox ibToForum = new InputBox("To Forum:", InputType.TEXT);
		ibToForum.getField().setText("6696c811-fa25-411c-b768-1afe93b8f9e9");
		InputBox ibContentForum = new InputBox("Content: ", InputType.TEXT);
		ibContentForum.getField().setPromptText("Content");
		MyButton btnAddMessageForum = new MyButton("Send Message", ButtonSizeType.SMALL);
		btnAddMessageForum.setOnAction( e-> {
			Gamer gamer1 = userDB.getGamerByEmail(ibFromChat.getValue());
			String content = ibContentForum.getValue();
			
			Chat chat = new Chat(ibToForum.getValue());
			if (gamer1 != null) {
				Message message = new Message(content, gamer1.getGamerChat(), chat, LocalDate.now());
				try {
					messageDB.addMessage(message);
					JOptionPane.showMessageDialog(null, "Message Sent");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} else {
				JOptionPane.showMessageDialog(null, "Player not match");
			}
		});
		vbTab3.getChildren().addAll(ibFromChat, ibToForum, ibContentForum, btnAddMessageForum);
		
		
		// tab4
		Tab tab4 = new Tab("Add Friends", vbTab4);
		InputBox ibSender = new InputBox("From:", InputType.TEXT);
		ibSender.getField().setText("abc@def.co");
		ibSender.getField().setPromptText("From email");
		InputBox ibReceiver = new InputBox("To:", InputType.TEXT);
		ibReceiver.getField().setText("bed@ilo.co");
		ibReceiver.getField().setPromptText("To email");
		MyButton btnSend = new MyButton("Send Request", ButtonSizeType.SMALL);
		btnSend.setOnAction(e -> {
			Gamer gamer1 = userDB.getGamerByEmail(ibSender.getValue());
			Gamer gamer2 = userDB.getGamerByEmail(ibReceiver.getValue());
			FriendRequest request = new FriendRequest(gamer1, gamer2, false);
			if (gamer1 != null && gamer2 != null) {
				try {
					notificationDB.addFriendRequest(request);
					JOptionPane.showMessageDialog(null, "sent");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} else {
				JOptionPane.showMessageDialog(null, "Player not match");
			}
		});
		vbTab4.getChildren().addAll(ibSender, ibReceiver, btnSend, new Separator());
		vbTab4.setPadding(new Insets(30));
		vbTab4.setSpacing(10);

		MyLabel acceptLabel = new MyLabel("Accept friend", LabelSize.MEDIUM);
		InputBox ibReceiverAccept = new InputBox("Receiver:", InputType.TEXT);
		ibReceiverAccept.getField().setText("bed@ilo.co");
		ibReceiverAccept.getField().setPromptText("Receiver email");

		InputBox ibSenderAccept = new InputBox("Sender:", InputType.TEXT);
		ibSenderAccept.getField().setText("abc@def.co");
		ibSenderAccept.getField().setPromptText("Sender email");
		MyButton btnAccept = new MyButton("Send Request", ButtonSizeType.SMALL);
		btnAccept.setOnAction(e -> {
			Gamer gamer1 = userDB.getGamerByEmail(ibReceiverAccept.getValue());
			Gamer gamer2 = userDB.getGamerByEmail(ibSenderAccept.getValue());
			if (gamer1 != null && gamer2 != null) {
				try {
					friendDB.accepctFriendRequest(gamer1.getId(), gamer2.getId());
					JOptionPane.showMessageDialog(null, "accepted");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} else {
				JOptionPane.showMessageDialog(null, "Player not match");
			}
		});
		vbTab4.getChildren().addAll(acceptLabel, ibReceiverAccept, ibSenderAccept, btnAccept);

		// tab5
		ScrollPane spTab5 = new ScrollPane();
		FlowPane fpGenres = new FlowPane();
		spTab5.setContent(vbTab5);
		Tab tab5 = new Tab("See genres", spTab5);

		HBox hbName = new HBox();
		MyLabel genresLabel = new MyLabel("Add genres", LabelSize.MEDIUM);
		InputBox ibGenresFav = new InputBox("To:", InputType.TEXT);
		new BorderRadiusDescorator(ibGenresFav.getField(), 20);
		ibGenresFav.getField().setText("bad@ilo.co");
		ibGenresFav.getField().setPromptText("To email");
		hbName.getChildren().addAll(genresLabel, ibGenresFav);
		vbTab5.getChildren().addAll(hbName, new Separator());

		HBox hbSearchGenre = new HBox();
		MyLabel searchLabel = new MyLabel("Search genre", LabelSize.MEDIUM);
		InputBox ibSearchGenre = new InputBox("name", InputType.TEXT);
		ibSearchGenre.setOnKeyReleased(e -> {
			fpGenres.getChildren().forEach(x -> {
				if (x instanceof GenreBox) {
					GenreBox genreBox = (GenreBox) x;
					Genre genre = genreBox.getGenre();
					if (!genre.getGenre().contains(ibSearchGenre.getValue())) {
						new HideDecorator(x);
						x.setManaged(false);
					} else {
						new ShowDecorator(x);
						x.setManaged(true);
					}
				}
			});
		});
		new BorderRadiusDescorator(ibSearchGenre.getField(), 20);
		ibGenresFav.getField().setText("bad@ilo.co");
		ibGenresFav.getField().setPromptText("To email");
		hbSearchGenre.getChildren().addAll(searchLabel, ibSearchGenre);
		vbTab5.getChildren().addAll(hbSearchGenre);

		vbTab5.getChildren().addAll(genresLabel, ibGenresFav);
		for (Genre genre : Genre.values()) {
			GenreBox genreBox = new GenreBox(genre);
			MyButton btnGenre = new MyButton(String.format("%s", genre.getGenre()), ButtonSizeType.MEDIUM);
			Tooltip tooltip = new Tooltip(String.format("%s", genre.getExplantaion()));
			tooltip.setMaxWidth(200);
			tooltip.setPrefWidth(160);
			btnGenre.setTooltip(tooltip);
			btnGenre.setMaxWidth(200);

			genreBox.setOnMouseClicked(e -> {
				Gamer gamer1 = userDB.getGamerByEmail(ibGenresFav.getValue());
				if (gamer1 != null) {
					try {

						genreDB.addGenreToFavorites(genre, gamer1.getId());
						JOptionPane.showMessageDialog(null, "Added");
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(null, "Player not match");
				}
			});

			fpGenres.getChildren().addAll(genreBox);
			new HideDecorator(genreBox);
			genreBox.setManaged(false);
		}
		fpGenres.setVgap(4);
		fpGenres.setHgap(4);
		fpGenres.setPrefWidth(WIDTH);
		fpGenres.setPadding(new Insets(30));

		vbTab5.getChildren().addAll(fpGenres);
		vbTab5.setPrefWidth(WIDTH);
		
		tabPane.getTabs().add(tab1);
		tabPane.getTabs().add(tab5);
		tabPane.getTabs().add(tab4);
		tabPane.getTabs().add(tab3);
		tabPane.getTabs().add(tab2);

		Scene scene = new Scene(tabPane, WIDTH, HEIGHT);
		stage.setScene(scene);
		stage.show();
	}

	@Override
	public void registerListener(ViewListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNotificationsInView(ArrayList<Notification> notifications) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showErrorInView(String message) {
		JOptionPane.showMessageDialog(null, message);
	}


	@Override
	public void addForumsByGenre(ArrayList<Forum> list) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showGamerPane(Gamer gamer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void friendRequestAcceptedInView(FriendRequest notification) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void friendRequestDeclinedInView(FriendRequest notification) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadGamesToView(ArrayList<Game> allGames) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addFriendsSearchInView(ArrayList<Gamer> gamers) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addForumInView(Forum forum) {
		// TODO Auto-generated method stub
		
	}

}
