package view.components;

import beans.Gamer;

public class FriendEntryBox extends EntryBox {

	private Gamer friend;

	// this might get more items in order to update messages in the chat, like task
	// or chat id
	public FriendEntryBox(Gamer friend) {
		super(friend.getNicename());
		
	}

	public Gamer getFriend() {
		return friend;
	}

	public void setFriend(Gamer friend) {
		this.friend = friend;
	}
}
