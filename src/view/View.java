package view;

import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JOptionPane;

import beans.Chat;
import beans.Forum;
import beans.FriendRequest;
import beans.Game;
import beans.Gamer;
import beans.Genre;
import beans.Notification;
import decorator.BlackColorDecorator;
import decorator.BorderRadiusDescorator;
import decorator.GreenBackgroundDecorator;
import decorator.HideDecorator;
import decorator.ShowDecorator;
import interfaces.ViewListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import strategy.EnglishContext;
import strategy.HebrewContext;
import strategy.LanguageEnum;
import strategy.LanguageStrategy;
import util.Util;
import view.ChatView.ChatType;
import view.components.ChatEntryBox;
import view.components.EntryButtonBox;
import view.components.ForumEntryBox;
import view.components.FriendEntryBox;
import view.components.FriendRequestEntryBox;
import view.components.GameBox;
import view.components.GenreBox;
import view.components.IconBox;
import view.components.InputBox;
import view.components.InputVBox;
import view.components.MyButton;
import view.components.MyButton.ButtonSizeType;
import view.components.MyLabel;
import view.components.MessageNotificationEntryBox;
import view.components.MyAddButton;
import view.components.TopBar;
import view.components.InputBox.InputType;
import view.components.MyLabel.LabelSize;

public class View implements AbstractView {

	public static final int WIDTH = 400;
	public static final int HEIGHT = 900;

	private Game tempGame = null;
	private Genre tempGenre = null;
	private ArrayList<Gamer> tempFriends = new ArrayList<>();

	private ArrayList<ViewListener> listeners = new ArrayList<>();
	private VBox vbPanel = new VBox();

	private ScrollPane spProfile = new ScrollPane();
	private VBox vbProfile = new VBox();
	private VBox vbFavGames = new VBox();
	private VBox vbFavGenres = new VBox();

	private ScrollPane spSearch = new ScrollPane();
	private VBox vbSearch = new VBox();
	private VBox vbSearchResult = new VBox();

	private ScrollPane spMain = new ScrollPane();
	private VBox vbMain = new VBox();

	private VBox vbForums = new VBox();
	private VBox vbChats = new VBox();

	private ScrollPane spFriends = new ScrollPane();
	private VBox vbFriendsList = new VBox();
	private VBox vbFriends = new VBox();

	private VBox vbFriendsResult = new VBox();

	private ScrollPane spNotifications = new ScrollPane();
	private VBox vbNotifications = new VBox();
	private BorderPane bpMessages = new BorderPane();
	private ScrollPane spMessages = new ScrollPane();
	private VBox vbMessages = new VBox();

	private static VBox root = new VBox();
	public static LanguageStrategy language = new EnglishContext(root);

	private LanguageEnum langEnum = LanguageEnum.ENGLISH;
	private Gamer gamer = null;
	private ArrayList<Game> allGames = new ArrayList<Game>();

	public View(Stage stage) {
		stage.setOnCloseRequest(e -> {
			listeners.forEach(l -> l.closeProgramFromView());
			stage.close();
		});
		showLogin(stage);
	}

	public void showLogin(Stage stage) {
		root = new VBox();
		root.setBackground(
				new Background(new BackgroundImage(new Image("file:assets/background.jpg"), BackgroundRepeat.REPEAT,
						BackgroundRepeat.NO_REPEAT, new BackgroundPosition(Side.LEFT, 0, true, Side.BOTTOM, 0, true),
						new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true))));
		root.setSpacing(20);
		HBox hb1 = new HBox();

		ComboBox<Label> myCombo = new ComboBox<Label>();
		myCombo.setPrefWidth(20);
		Image imgIL = new Image("file:assets/il.png");
		ImageView imgViewIL = new ImageView();
		imgViewIL.setImage(imgIL);
		Label lblIL = new Label("il");
		lblIL.setGraphic(imgViewIL);

		Image imgEN = new Image("file:assets/en.png");
		ImageView imgViewEN = new ImageView();
		imgViewEN.setImage(imgEN);
		Label lblEN = new Label("en");
		lblEN.setGraphic(imgViewEN);

		myCombo.getItems().addAll(lblIL, lblEN);
		myCombo.getSelectionModel().select(1);

		myCombo.setOnAction(e -> {
			String value = myCombo.getValue().getText();
			switch (value) {
			case "il":
				if (this.langEnum != LanguageEnum.HEBREW) {
					language = new HebrewContext(root);
					this.langEnum = LanguageEnum.HEBREW;
				}
				break;
			case "en":
				if (this.langEnum != LanguageEnum.ENGLISH) {
					language = new EnglishContext(root);
					this.langEnum = LanguageEnum.ENGLISH;
				}
				break;
			default:
				break;
			}
		});
		HBox hb2 = new HBox();
		hb2.setPadding(new Insets(200, 0, 0, 0));
		InputVBox ibNickName = new InputVBox(language.getMessage("login_username"), InputType.TEXT);
		ibNickName.getField().setPrefWidth(220);
		ibNickName.getField().setText("doai");

		hb2.setAlignment(Pos.CENTER);
		hb2.getChildren().addAll(ibNickName);
		HBox hb3 = new HBox();
		InputVBox ibPassword = new InputVBox(language.getMessage("login_password"), InputType.PASSWORD);
		ibPassword.getField().setPrefWidth(220);
		ibPassword.getField().setText("0000");
		
		root.getChildren().add(myCombo);
		MyButton login = new MyButton(this.language.getMessage("login_button"), ButtonSizeType.SMALL);
		login.setOnAction(e -> {
			if (true) {
				JOptionPane.showMessageDialog(null, "Keep working");
				String nickname = ibNickName.getValue();
				String password = ibPassword.getValue();
				
				for (ViewListener l : listeners) {
					l.loginFromView(nickname, password);
				}
				stage.close();
			}
		});
		hb1.getChildren().addAll(login);
		hb1.setAlignment(Pos.CENTER);

		hb3.setAlignment(Pos.CENTER);
		hb3.getChildren().addAll(ibPassword);
		hb3.setAlignment(Pos.CENTER);
		HBox hb4 = new HBox();

		hb4.setAlignment(Pos.CENTER);

		MyLabel lblWhatsGame = new MyLabel("WhatsGame", LabelSize.EXTRA_LARGE);

		new BlackColorDecorator(lblWhatsGame);
		hb4.setPadding(new Insets(200, 0, 0, 0));
		hb4.getChildren().addAll(lblWhatsGame);

		root.getChildren().addAll(hb2, hb3, hb1, hb4);
		Scene scene = new Scene(root, WIDTH, HEIGHT);
		stage.setScene(scene);
		stage.show();
	}

	@Override
	public void showGamerPane(Gamer gamer) {
		this.gamer = gamer;
		Stage stage = new Stage();
		stage.setOnCloseRequest(e -> {
			listeners.forEach(l -> l.closeProgramFromView());
			stage.close();
		});
		root = new VBox();
		root.setBackground(
				new Background(new BackgroundImage(new Image("file:assets/background.jpg"), BackgroundRepeat.REPEAT,
						BackgroundRepeat.NO_REPEAT, new BackgroundPosition(Side.LEFT, 0, true, Side.BOTTOM, 0, true),
						new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true))));
		root.setSpacing(20);
		TopBar topBar = new TopBar(this.getLanguage());
		topBar.getLblProfile().setOnMouseClicked(e -> switchPanelContent(spProfile));
		buildVBMain();
		buildVBProfile();
		buildVBFriends();
		buildVBNotifications();
		buildVBSearch();
		buildVBMessages();
		topBar.getButtonChat().setOnMousePressed(e -> switchPanelContent(spMain));
		topBar.getButtonFriends().setOnMousePressed(e -> switchPanelContent(spFriends));
		topBar.getButtonUpdates().setOnMousePressed(e -> {
			switchPanelContent(spNotifications);
//			listeners.forEach(l -> l.readNotificationsFromView());
		});

		switchPanelContent(spMain);
		root.getChildren().addAll(topBar, vbPanel);

		Scene scene = new Scene(root, WIDTH, HEIGHT);
		stage.setScene(scene);
		stage.show();

	}

	private void buildVBProfile() {
		this.spProfile.setContent(vbProfile);
		MyAddButton add = new MyAddButton("Add games", ButtonSizeType.MEDIUM);
		add.setOnAction(e -> {
			Stage s = new Stage();
			FlowPane fpGames = new FlowPane();
			ScrollPane spGames = new ScrollPane();
			spGames.setContent(fpGames);
			this.allGames.forEach(g -> {
				GameBox gamebox = new GameBox(g);
				fpGames.getChildren().addAll(gamebox);
				gamebox.setOnMouseClicked(e1 -> {
					tempGame = g;
					this.gamer.getGames().add(tempGame);
					listeners.forEach(l -> l.addFavGameFromView(g));
					this.vbFavGames.getChildren().add(new GameBox(g));
					s.close();
					tempGame = null;
				});
			});

			s.setScene(new Scene(spGames, 400, 400));
			s.show();
		});

		this.gamer.getGames().forEach(g -> {
			GameBox box = new GameBox(g);
			vbFavGames.getChildren().add(box);
		});
		MyAddButton addGenre = new MyAddButton("Add genre", ButtonSizeType.MEDIUM);
		addGenre.setOnAction(e -> {
			Stage s = new Stage();
			FlowPane fpGenres = new FlowPane();
			ScrollPane spGenres = new ScrollPane();
			spGenres.setContent(fpGenres);
			ArrayList<Genre> genres = new ArrayList<Genre>();
			Collections.addAll(genres, Genre.values());
			genres.forEach(g -> {
				GenreBox genrebox = new GenreBox(g);
				fpGenres.getChildren().addAll(genrebox);
				genrebox.setOnMouseClicked(e1 -> {
					tempGenre = g;
					this.gamer.getLikedGenres().add(tempGenre);
					this.vbFavGames.getChildren().add(new GenreBox(g));
					listeners.forEach(l -> l.addFavGenreFromView(g));
					s.close();
					tempGenre = null;
				});
			});

			s.setScene(new Scene(spGenres, 400, 400));
			s.show();
		});
		this.gamer.getLikedGenres().forEach(g -> {
			GenreBox box = new GenreBox(g);
			vbFavGenres.getChildren().add(box);
		});
		vbProfile.getChildren().addAll(new MyLabel("Favorite games: ", LabelSize.MEDIUM), vbFavGames, add,
				new MyLabel("Favorite genres: ", LabelSize.MEDIUM), vbFavGenres, addGenre);
	}

	private void buildVBMessages() {
		this.bpMessages.setCenter(spMessages);
		spMessages.setContent(vbMessages);
//		this.bpMessages.setBottom(hbText);

	}

	private void switchPanelContent(Node content) {
		this.vbPanel.getChildren().clear();
		this.vbPanel.getChildren().add(content);
	}

	@Override
	public void registerListener(ViewListener listener) {
		this.listeners.add(listener);
	}

	public LanguageStrategy getLanguage() {
		return language;
	}

	public void setLanguage(LanguageStrategy language) {
		this.language = language;
	}

	// build stuff
	public void buildVBMain() {

		MyLabel lblFriends = new MyLabel("Friends: ", LabelSize.MEDIUM);
		for (Gamer g : this.gamer.getFriends()) {
			ChatEntryBox ceb = new ChatEntryBox(g);
			ceb.setOnMouseClicked(e -> {
				listeners.forEach(l -> {
					l.openChatControllerFromView(gamer.getGamerChat(), g.getGamerChat(), ChatType.CHAT);
				});
			});
			vbChats.getChildren().add(ceb);

		}

		MyLabel lblForums = new MyLabel("Forums: ", LabelSize.MEDIUM);

		for (Forum f : this.gamer.getForums()) {
			ForumEntryBox ceb = new ForumEntryBox(gamer.getGamerChat(), f);
			ceb.setOnMouseClicked(e -> {
				listeners.forEach(l -> {
					l.openChatControllerFromView(gamer.getGamerChat(), f, ChatType.FORUM);
				});
			});
			vbForums.getChildren().add(ceb);
		}
		IconBox iconGlass = new IconBox("assets/glass.png");
		new GreenBackgroundDecorator(iconGlass);
		iconGlass.setTranslateX(340);
		iconGlass.setOnMouseClicked(e -> {
			switchPanelContent(spSearch);
		});
		vbMain.getChildren().addAll(lblFriends, vbChats, lblForums, vbForums);
		vbMain.getChildren().add(iconGlass);
		vbMain.setPrefWidth(395);

		spMain.setContent(vbMain);

	}

	public void buildVBFriends() {

		ArrayList<FriendEntryBox> listChatEntry = new ArrayList<>();

		for (Gamer g : this.gamer.getFriends()) {
			listChatEntry.add(new FriendEntryBox(g));
		}

		MyAddButton btnGenre = new MyAddButton("Search", ButtonSizeType.LARGE);
		btnGenre.setOnAction(e -> {
			Stage s = new Stage();
			FlowPane fpGenres = new FlowPane();
			ScrollPane spGenres = new ScrollPane();
			spGenres.setContent(fpGenres);
			ArrayList<Genre> genres = new ArrayList<Genre>();
			Collections.addAll(genres, Genre.values());
			genres.forEach(g -> {
				GenreBox genrebox = new GenreBox(g);
				fpGenres.getChildren().addAll(genrebox);
				genrebox.setOnMouseClicked(e1 -> {
					listeners.forEach(l -> l.searchFriendByGenreFromView(g));
					s.close();
				});
			});

			s.setScene(new Scene(spGenres, 400, 400));
			s.show();
		});

		MyAddButton btnGame = new MyAddButton("Search", ButtonSizeType.LARGE);
		btnGame.setOnAction(e -> {
			Stage s = new Stage();
			FlowPane fpGames = new FlowPane();
			ScrollPane spGames = new ScrollPane();
			spGames.setContent(fpGames);
			this.allGames.forEach(g -> {
				GameBox gamebox = new GameBox(g);
				fpGames.getChildren().add(gamebox);
				gamebox.setOnMouseClicked(e1 -> {
					tempGame = g;
					listeners.forEach(l -> l.searchFriendByGameFromView(g));
					s.close();
				});
			});

			s.setScene(new Scene(spGames, 400, 400));
			s.show();
		});
		vbFriendsList.getChildren().addAll(listChatEntry);

		vbFriends.getChildren().addAll(vbFriendsList);
		vbFriends.getChildren().addAll(new MyLabel("Search by genre: ", LabelSize.MEDIUM), btnGenre,
				new MyLabel("Search by game: ", LabelSize.MEDIUM), btnGame, vbFriendsResult);

		vbFriends.setPrefWidth(395);
		spFriends.setContent(vbFriends);

	}

	public void buildVBNotifications() {

		ArrayList<Node> listChatEntry = new ArrayList<>();

		for (Notification n : this.gamer.getNotifications()) {
			if (n.getNotification() instanceof FriendRequest) {
				FriendRequestEntryBox box = new FriendRequestEntryBox((FriendRequest) n.getNotification());
				box.getXIcon().setOnMouseClicked(e -> {
					listeners.forEach(l -> l.declineFriendRequestFromView((FriendRequest) n.getNotification()));
					vbNotifications.getChildren().remove(box);
				});
				listChatEntry.add(box);
				box.getVIcon().setOnMouseClicked(e -> {
					listeners.forEach(l -> l.acceptFriendRequestFromView((FriendRequest) n.getNotification()));
					vbNotifications.getChildren().remove(box);
				});
			} else {
				listChatEntry.add(new MessageNotificationEntryBox(n.getContent()));
			}
		}

		vbNotifications.getChildren().addAll(listChatEntry);
		vbNotifications.setPrefWidth(395);
		spNotifications.setContent(vbNotifications);

	}

	public void buildVBSearch() {

		spSearch = new ScrollPane();
		FlowPane fpGenres = new FlowPane();
		spSearch.setContent(vbSearch);

		HBox hbSearchGenre = new HBox();
		InputBox ibSearchGenre = new InputBox("Search genre", InputType.TEXT);
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
		hbSearchGenre.getChildren().addAll(ibSearchGenre);
		vbSearch.getChildren().addAll(hbSearchGenre);

		for (Genre genre : Genre.values()) {
			GenreBox genreBox = new GenreBox(genre);
			MyButton btnGenre = new MyButton(String.format("%s", genre.getGenre()), ButtonSizeType.MEDIUM);
			Tooltip tooltip = new Tooltip(String.format("%s", genre.getExplantaion()));
			tooltip.setMaxWidth(200);
			tooltip.setPrefWidth(160);
			btnGenre.setTooltip(tooltip);
			btnGenre.setMaxWidth(200);

			genreBox.setOnMouseClicked(e -> {
				listeners.forEach(l -> l.getForumsByGenreFromView(genre));
			});

			fpGenres.getChildren().addAll(genreBox);
			new HideDecorator(genreBox);
			genreBox.setManaged(false);
		}
		fpGenres.setHgap(5);
		fpGenres.setVgap(5);
		fpGenres.setPadding(new Insets(10));
		ScrollPane spGenres = new ScrollPane();
		spGenres.setMaxHeight(400);
		spGenres.setPrefHeight(400);
		spGenres.setContent(fpGenres);
		vbSearch.getChildren().add(spGenres);
		vbSearch.setPrefWidth(395);
		vbSearch.getChildren().add(vbSearchResult);
		MyAddButton btnAdd = new MyAddButton("Add forum", ButtonSizeType.LARGE);
		btnAdd.setOnAction(e -> {
			Stage s = new Stage();
			FlowPane fpGenres1 = new FlowPane();
			ScrollPane spGenres1 = new ScrollPane();
			spGenres1.setContent(fpGenres1);
			ArrayList<Genre> genres = new ArrayList<Genre>();
			Collections.addAll(genres, Genre.values());
			genres.forEach(g -> {
				GenreBox genrebox = new GenreBox(g);
				fpGenres1.getChildren().addAll(genrebox);
				genrebox.setOnMouseClicked(e1 -> {
					Forum forum = new Forum();
					forum.setGenre(g);
					forum.setGamers(new ArrayList<>());
					forum.setOwner(this.gamer);
					listeners.forEach(l -> l.addForumFromView(forum));
					s.close();
				});
			});

			s.setScene(new Scene(spGenres1, 400, 400));
			s.show();
		});
		vbSearch.getChildren().add(btnAdd);

		spSearch.setContent(vbSearch);
	}

	@Override
	public void updateNotificationsInView(ArrayList<Notification> notifications) {
		this.vbMain.getChildren().clear();
		for (Notification n : notifications) {
			if (!n.isRead()) {
				Label lbl = new Label("" + n.getContent());
				this.vbMain.getChildren().add(lbl);
			}
		}
	}

	@Override
	public void showErrorInView(String message) {
		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void addForumsByGenre(ArrayList<Forum> list) {
		vbSearchResult.getChildren().clear();
		list.forEach(i -> {

			EntryButtonBox ceb = new EntryButtonBox(i.getGamers().toString());
			ceb.getButton().setOnAction(e -> {
				listeners.forEach(l -> {
					l.joinForumFromView(i.getId());
					this.gamer.getForums().add(i);
					this.addForumInView(i);
				});
			});

			vbSearchResult.getChildren().add(ceb);
		});
	}

	@Override
	public void addForumInView(Forum forum) {
		ForumEntryBox ceb = new ForumEntryBox(gamer.getGamerChat(), forum);
		ceb.setOnMouseClicked(e -> {
			listeners.forEach(l -> {
				l.openChatControllerFromView(gamer.getGamerChat(), forum, ChatType.FORUM);
			});
		});
		this.vbForums.getChildren().add(ceb);
	}

	@Override
	public void friendRequestAcceptedInView(FriendRequest notification) {
		removeFriendRequestFromNotification(notification);
		addChatEntryToMain(notification.getGamerSender().getGamerChat());
		this.gamer.getFriends().add(notification.getGamerSender());
		addChatEntryToFriends(notification.getGamerSender());
	}

	@Override
	public void friendRequestDeclinedInView(FriendRequest notification) {
		removeFriendRequestFromNotification(notification);

	}

	private void addChatEntryToMain(Chat chat) {
		ChatEntryBox ceb = new ChatEntryBox(Util.filterGamerByChatID(this.gamer.getFriends(), chat.getId()));
		ceb.setOnMouseClicked(e -> {
			listeners.forEach(l -> {
				l.openChatControllerFromView(gamer.getGamerChat(), chat, ChatType.CHAT);
			});
		});
		this.vbMain.getChildren().add(ceb);
	}

	private void addChatEntryToFriends(Gamer gamer) {
		this.vbFriends.getChildren().add(new FriendEntryBox(gamer));
	}

	private void removeFriendRequestFromNotification(FriendRequest notification) {
		for (Notification n : this.gamer.getNotifications()) {
			if (n.getNotification() instanceof FriendRequest) {
				if (((FriendRequest) n.getNotification()).equals(notification)) {
					this.gamer.getNotifications().remove(n);
					break;
				}
			}
		}
	}

	@Override
	public void loadGamesToView(ArrayList<Game> allGames) {
		this.allGames = allGames;
	}

	@Override
	public void addFriendsSearchInView(ArrayList<Gamer> gamers) {
		this.tempFriends = gamers;
		this.vbFriendsResult.getChildren().clear();
		gamers.forEach(g -> {
			EntryButtonBox ebb = new EntryButtonBox(g.getNicename());
			ebb.getButton().setOnAction(e1 -> {
				listeners.forEach(l -> l.addFriendFromView(g));
			});
			this.vbFriendsResult.getChildren().add(ebb);
		});
	}

}
