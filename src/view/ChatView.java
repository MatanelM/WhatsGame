package view;

import java.time.LocalDate;
import java.util.ArrayList;

import beans.Chat;
import beans.Message;
import controller.ChatViewListener;
import db.MessageDB;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import observer.ObserverMessages;
import view.components.MessageBox;
import view.components.MyButton;
import view.components.MessageBox.MessageOrientation;

public class ChatView implements ObserverMessages {

	private Stage stage;
	private HBox hbMain;
	private Label lblText;

	private ArrayList<ChatViewListener> listeners = new ArrayList<>();

	public static final int WIDTH = 400;
	public static final int HEIGHT = 600;

	private BorderPane root = new BorderPane();
	private VBox vbMessages;
	private String gamerChatID;
	private MyButton btnSendMessage;
	private TextArea txtMessage;
	
	private Chat chatFrom;
	private Chat chatTo;
	
	public enum ChatType{
		CHAT, FORUM
	}
	
	public ChatView(Chat from, Chat to, ChatType type) {
		this.chatFrom = from;
		this.chatTo = to;
		
		stage = new Stage();
		
		stage.setOnCloseRequest(e -> {
			this.closeChatWindow();
		});
		
		this.gamerChatID = from.getId();
		ArrayList<Message> messages = new ArrayList<>();

		MessageDB mdb = new MessageDB();
		switch (type)
		{
		case CHAT:
			messages = mdb.getAllMessagesFromChat(this.gamerChatID, this.chatTo.getId());
			break;
		case FORUM:
			messages = mdb.getAllMessagesFromForum(to.getId());
			break;
		}

		vbMessages = new VBox();
		for (Message m : messages) {
			MessageOrientation orientation = gamerChatID.equals(m.getFrom().getId()) ? MessageOrientation.LEFT
					: MessageOrientation.RIGHT;
			this.vbMessages.getChildren().add(new MessageBox(m, orientation));
		}
		ScrollPane spMessages = new ScrollPane();
		spMessages.setContent(vbMessages);
		spMessages.setPrefWidth(390);
		vbMessages.setMinWidth(385);
		
		// center has messages
		this.root.setCenter(spMessages);

		vbMessages.setSpacing(4);
		vbMessages.setPadding(new Insets(10, 4, 10, 4));

		// bottom sends new mesage
		btnSendMessage = new MyButton();
		btnSendMessage.textProperty().bind(View.language.getMessage("chat_button_send"));
		btnSendMessage.setOnAction(e -> {
			Message m = new Message(this.txtMessage.getText(), from, to, LocalDate.now());
			this.sendMessageFromChat(m);
		});
		
		
		txtMessage = new TextArea();
		txtMessage.setPrefWidth(280);
		txtMessage.setPrefHeight(100);

		HBox hbMessage = new HBox();
		hbMessage.getChildren().addAll(txtMessage, btnSendMessage);
		hbMessage.setPadding(new Insets(10));
		hbMessage.setSpacing(14);
		hbMessage.setAlignment(Pos.TOP_CENTER);

		this.root.setBottom(hbMessage);

		stage.setScene(new Scene(root, WIDTH, HEIGHT));
		
		
		stage.show();

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chatFrom == null) ? 0 : chatFrom.hashCode());
		result = prime * result + ((chatTo == null) ? 0 : chatTo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChatView other = (ChatView) obj;
		if (chatFrom == null) {
			if (other.chatFrom != null)
				return false;
		} else if (!chatFrom.equals(other.chatFrom))
			return false;
		if (chatTo == null) {
			if (other.chatTo != null)
				return false;
		} else if (!chatTo.equals(other.chatTo))
			return false;
		return true;
	}

	public void addMessageInView(Message m) {
		MessageOrientation orientation = gamerChatID.equals(m.getFrom().getId()) ? MessageOrientation.LEFT
				: MessageOrientation.RIGHT;
		this.vbMessages.getChildren().add(new MessageBox(m, orientation));
		
	}
	
	public void sendMessageFromChat(Message m ) {
		addMessageInView(m);
		new MessageDB().addMessage(m);
	}
	
	public void registerListener(ChatViewListener l) {
		this.listeners.add(l);
	}

	@Override
	public void update(ArrayList<Message> messages) {
		messages.forEach(this::addMessageInView);
	}

	@Override
	public Chat getChatTo() {
		return this.chatTo;
	}

	@Override
	public Chat getChatFrom() {
		return this.chatFrom;
	}

	public void closeChatWindow() {
		listeners.forEach(l -> l.closeChatViewFromView(this));
		stage.close();
	}

	public Stage getStage() {
		return this.stage;
	}

}