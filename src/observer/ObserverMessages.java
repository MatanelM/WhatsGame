package observer;

import java.util.ArrayList;

import beans.Chat;
import beans.Message;

public interface ObserverMessages {
	void update(ArrayList<Message> messages);
	Chat getChatTo();
	Chat getChatFrom();
}
