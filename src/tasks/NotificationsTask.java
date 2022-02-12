package tasks;

import java.util.ArrayList;
import java.util.Collections;

import beans.Chat;
import beans.Forum;
import beans.Gamer;
import beans.Message;
import beans.Notification;
import db.ChatDB;
import db.MessageDB;
import db.NotificationsDB;
import javafx.application.Platform;
import javafx.concurrent.Task;
import observer.ObservableMessages;
import observer.ObserverMessages;
import util.Util;

public class NotificationsTask extends Task<Notification> implements ObservableMessages {

	private ArrayList<Notification> notifications;
	private NotificationsDB notificationsDB;

	private ArrayList<ObserverMessages> observers = new ArrayList<ObserverMessages>();

	private Gamer gamer;

	public NotificationsTask(Gamer gamer) {
		this.notifications = new ArrayList<Notification>();
		this.gamer = gamer;
		this.notificationsDB = new NotificationsDB();
	}

	@Override
	protected Notification call() throws Exception {
		while (true) {

			checkNotifications();
			Thread.sleep(1500);
			if (isCancelled())
				return notifications.get(0);
		}
	}

	private void checkNotifications() {
		Platform.runLater(() -> {
			ArrayList<Notification> newNotifications = notificationsDB.checkNotifications(gamer);

			ArrayList<Notification> newMessages = new ArrayList<>();
			newNotifications.forEach(m -> {
				if (m.getNotification() instanceof Message) {
					newMessages.add(m);
				}
			});
			observers.forEach(o -> {
				ArrayList<Message> messages = new ArrayList<>();
				newMessages.forEach(m -> {

					if (messageBelongToObserver(m, o)) {
						messages.add((Message) m.getNotification());
						this.notificationsDB.markAsRead(m, this.gamer);
						newNotifications.remove(m);
					}
				});
				if (!messages.isEmpty()) {
					this.updateObserverWithMessages(o, messages);
				}
			});

			ArrayList<Notification> subtraction = (ArrayList<Notification>) Util.subtract(notifications,
					newNotifications);
			if (!subtraction.isEmpty()) {
				this.notifications.addAll(subtraction);

			}
		});
	}

	@Override
	public void updateObserverWithMessages(ObserverMessages o, ArrayList<Message> messages) {
		o.update(messages);
	}

	private boolean messageBelongToObserver(Notification m, ObserverMessages o) {
		if (m.getNotification() instanceof Message) {

			Message message = (Message) m.getNotification();
			Chat to = o.getChatTo();
			Chat from = o.getChatFrom();
			
			boolean fromAtoB = message.getTo().equals(to) && message.getFrom().equals(from);
			boolean fromBtoA = message.getTo().equals(from) && message.getFrom().equals(to);
			if (fromAtoB || fromBtoA) {
				// case 1 - this chat is of two people talking
				return true;
			}
			Forum forum = new ChatDB().getForumByID(message.getTo().getId());
			if (forum != null && o.getChatTo().equals(message.getTo())) {
				// case 2 - this chat is of a forum, the type of the chat is a forum
				// and the target is a forum that is the chat viwe
				return true;
			}
			return false;

		}
		return false;
	}

	@Override
	public void notifyMessagesObservers(ArrayList<Message> messages) {

	}

	@Override
	public void addListener(ObserverMessages observer) {
		this.observers.add(observer);
		// see every update that relates to this chat and mark it as read
		ArrayList<Notification> newNotifications = notificationsDB.checkNotifications(gamer);
		ArrayList<Notification> newMessages = new ArrayList<>();
		newNotifications.forEach(m -> {
			if (m.getNotification() instanceof Message) {
				newMessages.add(m);
			}
		});
		newMessages.forEach(m -> {

			if (messageBelongToObserver(m, observer)) {
				this.notificationsDB.markAsRead(m, this.gamer);
			}
		});

	}

	@Override
	public void removeListener(ObserverMessages observer) {
		this.observers.remove(observer);
	}

}
