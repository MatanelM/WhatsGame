package view.components;

import javafx.geometry.Insets;

import beans.FriendRequest;

public class FriendRequestEntryBox extends EntryBox {

	private IconBox x;
	private IconBox v;
	// this might get more items in order to update messages in the chat, like task
	// or chat id
	public FriendRequestEntryBox(FriendRequest request) {
		super(request.getGamerSender().getNicename()+ " would like to add you");
		x = new IconBox("assets/x.png");
		x.setPadding(new Insets(0));

		v = new IconBox("assets/v.png");
		v.setPadding(new Insets(0));

		this.getChildren().addAll(x, v);
	}
	

	public IconBox getXIcon() {
		return this.x;
	}
	public IconBox getVIcon() {
		return this.v;
	}
	

}
