package observer;

import java.util.ArrayList;

import beans.Message;

public interface ObservableMessages {

	void addListener(ObserverMessages observer);
	void removeListener(ObserverMessages observer);
	
	void updateObserverWithMessages(ObserverMessages o, ArrayList<Message> messages);
	void notifyMessagesObservers(ArrayList<Message> messages);
	
}
