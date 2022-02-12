package view.components;

import util.Util;
import view.components.MyLabel.LabelSize;


import beans.Chat;
import beans.Forum;

public class ForumEntryBox extends EntryBox {

	private Forum forum;
	private Chat gamerChat;

	// this might get more items in order to update messages in the chat, like task
	// or chat id
	public ForumEntryBox(Chat gamerChat, Forum forum) {
		super(Util.getFriendsPretty(forum));
		if ( forum == null ) return;
		this.forum = forum;
		this.gamerChat = gamerChat;
		if ( forum.getGenre() != null )
			this.add(new MyLabel(forum.getGenre().getGenre(), LabelSize.SMALL), 0, 0);
		if ( forum.getGame() != null ) {
			this.add(new MyLabel(forum.getGame().getName(), LabelSize.SMALL), 0, 0);
		}

	}

}
