package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Date;

import beans.Forum;
import beans.FriendRequest;
import beans.Gamer;
import beans.Message;
import beans.Notification;
import beans.Notification.NotificationType;
import core.ConnectionPool;
import interfaces.Notificationable;
import interfaces.NotificationsActions;

public class NotificationsDB implements NotificationsActions {

	private MessageDB msgDB = new MessageDB();
	private FriendDB fDB = new FriendDB();

	private String getNotificationOfUser = "With updates as (select * from notification, message where (notification.NotificationID=message.MessageID) and ChatToID=? UNION select * from notification, gamers_friends where (notification.NotificationID=gamers_friends.FriendRequestID) and ReceiverID=? UNION select * from notification, message where notification.NotificationID=message.MessageID and NotificationID in (select MessageID from message_forum where message_forum.isRead=0 and message_forum.ForumID in(select x.ForumID from forum_participants x where GamerID=?) and GamerID=?)) Select * from updates where isRead=0;";
	private String setUserNotificationRead = "update notification set isRead=1 Where isRead=0 and NotificationID in (select NotificationID from notification, message where (notification.NotificationID=message.MessageID) and ChatToID=? UNION select NotificationID from notification, gamers_friends where (notification.NotificationID=gamers_friends.FriendRequestID)  and ReceiverID=? );";
	private String setOneNotificationRead = "update notification set isRead=1 where NotificationID=?;";

	@Override
	public ArrayList<Notification> checkNotifications(Gamer gamer) {

		ArrayList<Notification> notifications = new ArrayList<>();

		Connection con = ConnectionPool.getInstance().getConnection();

		try (PreparedStatement pst = con.prepareStatement(getNotificationOfUser)) {
			pst.setString(1, gamer.getGamerChat().getId());
			pst.setString(2, gamer.getId());
			pst.setString(3, gamer.getId());
			pst.setString(4, gamer.getId());

			try (ResultSet rs = pst.executeQuery()) {
				while (rs.next()) {
					Notificationable notificationable = null;
					NotificationType type = NotificationType.valueOf(rs.getString("type"));
					if (type == NotificationType.FRIEND) {
						notificationable = fDB.getFriendRequestByID(rs.getString("NotificationID"));
					} else if (type == NotificationType.MESSAGE) {
						notificationable = msgDB.getMessageById(rs.getString("NotificationID"));
					}
					notifications.add(new Notification(notificationable, type));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}

		return notifications;

	}

	@Override
	public void markAsRead(Notification notification, Gamer gamer) {

		boolean isForumMessage = false;
		Message message = null;
		Forum forum = null;
		ChatDB chatDB = new ChatDB();
		if ((notification.getNotification() instanceof Message)) {
			message = (Message) notification.getNotification();
			isForumMessage = chatDB.isForumExists(message.getId());
			forum = isForumMessage ? chatDB.getForumByID(message.getTo().getId()) : null;
		}

		if (isForumMessage) {
			markForumMessageReadNotification(message, forum, gamer);
		} else {
			markNotificationAsRead(notification);
		}

	}

	private void markForumMessageReadNotification(Message message, Forum forum, Gamer gamer) {
		new MessageDB().markForumMessageRead(message, forum, gamer);
	}

	private void markNotificationAsRead(Notification notification) {
		Connection conn = ConnectionPool.getInstance().getConnection();

		try (PreparedStatement pst = conn.prepareStatement(setOneNotificationRead)) {

			if (notification.getNotification() instanceof FriendRequest) {
				pst.setString(1, ((FriendRequest) notification.getNotification()).getFrienRequestID());
			} else {
				pst.setString(1, ((Message) notification.getNotification()).getId());
			}

			pst.execute();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.getInstance().returnConnection(conn);
		}
	}

	@Override
	public boolean hasNotifications(Gamer gamer) {
		return false;
	}

	@Override
	public void addFriendRequest(FriendRequest friendRequest) throws SQLIntegrityConstraintViolationException {

		Connection conn = ConnectionPool.getInstance().getConnection();

		String addMessageStatement = "Insert into gamers_friends ( FriendRequestID, SenderID, ReceiverID, isAccepted, SentAt) values (?,?,?,?,?);";
		try (PreparedStatement pst = conn.prepareStatement(addMessageStatement)) {

			pst.setString(1, friendRequest.getFrienRequestID());
			pst.setString(2, friendRequest.getGamerSender().getId());
			pst.setString(3, friendRequest.getGamerReceiver().getId());
			pst.setInt(4, 0);

			java.util.Date date = new Date();
			Object now = new java.sql.Timestamp(date.getTime());
			pst.setObject(5, now);

			pst.execute();
		} catch (SQLIntegrityConstraintViolationException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.getInstance().returnConnection(conn);
		}
	}

	public void readAllNotifications(String gamerid) {
		Connection conn = ConnectionPool.getInstance().getConnection();

		try (PreparedStatement pst = conn.prepareStatement(setUserNotificationRead)) {

			pst.setString(1, gamerid);
			pst.setString(2, gamerid);

			pst.execute();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.getInstance().returnConnection(conn);
		}
	}

}
