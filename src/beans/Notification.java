package beans;

import java.util.UUID;

import interfaces.Notificationable;

public class Notification {

	public enum NotificationType  {
		MESSAGE, FRIEND
	}

	private Notificationable notification;
	private NotificationType type;
	private boolean read;

	public Notification(Notificationable notification, NotificationType type) {
		super();
		this.notification = notification;
		this.type = type;
		this.read = false;
	}

	public Notification(Notificationable notification, NotificationType type, boolean read) {
		this(notification, type);
		this.setRead(read);
	}
	
	public Notificationable getNotification() {
		return notification;
	}

	public void setNotification(Notificationable notification) {
		this.notification = notification;
	}

	public NotificationType getType() {
		return type;
	}

	public void setType(NotificationType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Notification [notification=" + notification + ", type=" + type.name().toLowerCase() + "]";
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public String getContent() {
		if ( this.notification instanceof Message) {
			return ((Message)notification).getContent();
		}else if (notification instanceof FriendRequest ) {
			return ((FriendRequest)notification).getGamerSender().toString();

		}
		return "notification " + UUID.randomUUID().toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((notification == null) ? 0 : notification.hashCode());
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
		Notification other = (Notification) obj;
		if (notification == null) {
			if (other.notification != null)
				return false;
		} else if (!notification.equals(other.notification))
			return false;
		return true;
	}

	
	
	

}
