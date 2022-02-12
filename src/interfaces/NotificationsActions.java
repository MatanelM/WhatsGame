package interfaces;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

import beans.FriendRequest;
import beans.Gamer;
import beans.Notification;


public interface NotificationsActions {

	ArrayList<Notification> checkNotifications(Gamer gamer);
	// remove a notification
	
	// mark us red 
	void markAsRead(Notification notification, Gamer gamer);
	
	// push notification to user
	boolean hasNotifications(Gamer gamer);
	
	void addFriendRequest(FriendRequest friendRequest) throws SQLIntegrityConstraintViolationException;
	
	
	
}
