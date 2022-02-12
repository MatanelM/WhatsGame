package view.components;

import java.util.ArrayList;

import beans.Message;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view.components.MessageBox.MessageOrientation;

public class MyMessagesStage extends Stage {

	public static final int WIDTH = 400;
	public static final int HEIGHT = 600;

	private BorderPane root = new BorderPane();
	private VBox vbMessages;
	private MyButton btnSendMessage;
	private TextArea txtMessage;

	public MyMessagesStage(ArrayList<Message> messages, String gamerChatID) {

		vbMessages = new VBox();
		for (Message m : messages) {
			MessageOrientation orientation = gamerChatID.equals(m.getFrom().getId()) ? MessageOrientation.LEFT
					: MessageOrientation.RIGHT;
			this.vbMessages.getChildren().add(new MessageBox(m, orientation));
		}

		// center has messages
		this.root.setCenter(vbMessages);

		vbMessages.setSpacing(4);
		vbMessages.setPadding(new Insets(10, 4, 10, 4));

		// bottom sends new mesage
		btnSendMessage = new MyButton();
		btnSendMessage.setText("Send");

		txtMessage = new TextArea();
		txtMessage.setPrefWidth(280);
		txtMessage.setPrefHeight(100);

		HBox hbMessage = new HBox();
		hbMessage.getChildren().addAll(txtMessage, btnSendMessage);
		hbMessage.setPadding(new Insets(10));
		hbMessage.setSpacing(14);
		hbMessage.setAlignment(Pos.TOP_CENTER);

		this.root.setBottom(hbMessage);

		this.setScene(new Scene(root, WIDTH, HEIGHT));
	}

	public BorderPane getRoot() {
		return root;
	}

	public void setRoot(BorderPane root) {
		this.root = root;
	}

	public VBox getVbMessages() {
		return vbMessages;
	}

	public void setVbMessages(VBox vbMessages) {
		this.vbMessages = vbMessages;
	}

	public MyButton getBtnSendMessage() {
		return btnSendMessage;
	}

	public void setBtnSendMessage(MyButton btnSendMessage) {
		this.btnSendMessage = btnSendMessage;
	}

}
