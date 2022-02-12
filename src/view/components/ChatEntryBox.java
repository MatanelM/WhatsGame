package view.components;

import java.util.ArrayList;

import beans.Chat;
import beans.Gamer;
import beans.Message;

public class ChatEntryBox extends EntryBox {

	private ArrayList<Message> messages;
	private Chat chat;

	public ChatEntryBox(Gamer g) {
		super(g.getNicename());
		this.chat = g.getGamerChat();
	}

	public ArrayList<Message> getMessages() {
		return messages;
	}

	public void setMessages(ArrayList<Message> messages) {
		this.messages = messages;
	}

}
