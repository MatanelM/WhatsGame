package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import beans.Chat;
import beans.Forum;
import beans.Gamer;
import beans.Message;
import core.ConnectionPool;
import interfaces.MessageActions;

public class MessageDB implements MessageActions {

	private final String getForumMessagesStatement = "select MessageID,content,ChatFromID,ChatToID,SentAt from forum join message on ForumID=ChatToID WHERE ForumID=? ORDER BY SentAt;";
	private final String getChatMessagesStatement = "With messages_of_chat as (select MessageID, content, ChatFromID, ChatToID, SentAt from chat join message on chat.ChatID=message.ChatToID where ChatToID=? and ChatFromID=? UNION select MessageID, content, ChatFromID, ChatToID, SentAt from chat join message on chat.ChatID=message.ChatFromID where ChatFromID=? and ChatToID=?) select * from messages_of_chat ORDER BY SentAt;";

	@Override
	public Message getMessageById(String id) {

		Message message = null;

		Connection con = ConnectionPool.getInstance().getConnection();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM message WHERE MessageID='" + id + "';");

			if (rs.next()) {
				message = new Message(rs.getString("MessageID"), rs.getString("content"),
						new Chat(rs.getString("ChatFromID")), new Chat(rs.getString("ChatToID")),
						rs.getDate("SentAt").toLocalDate());
			}

			rs.close();
			stmt.close();
			ConnectionPool.getInstance().returnConnection(con);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
			;
		}
		return message;

	}

	@Override
	public ArrayList<Message> getAllMessagesFromChat(String chatID, String friend) {
		ArrayList<Message> messages = new ArrayList<>();
		Connection con = ConnectionPool.getInstance().getConnection();

		try (PreparedStatement pst = con.prepareStatement(getChatMessagesStatement)) {
			pst.setString(1, chatID);
			pst.setString(2, friend);
			pst.setString(3, chatID);
			pst.setString(4, friend);

			try (ResultSet rs = pst.executeQuery()) {
				while (rs.next()) {
					Message message = new Message(rs.getString("MessageID"), rs.getString("content"),
							new Chat(rs.getString("ChatFromID")), new Chat(rs.getString("ChatToID")),
							rs.getDate("SentAt").toLocalDate());
					messages.add(message);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
		return messages;
	}

	@Override
	public ArrayList<Message> getAllMessagesFromForum(String forumID) {
		ArrayList<Message> messages = new ArrayList<>();
		Connection con = ConnectionPool.getInstance().getConnection();

		try (PreparedStatement pst = con.prepareStatement(getForumMessagesStatement)) {
			pst.setString(1, forumID);

			try (ResultSet rs = pst.executeQuery()) {
				while (rs.next()) {
					Message message = new Message(rs.getString("MessageID"), rs.getString("content"),
							new Chat(rs.getString("ChatFromID")), new Chat(rs.getString("ChatToID")),
							rs.getDate("SentAt").toLocalDate());
					messages.add(message);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
		return messages;
	}

	@Override
	public void addMessage(Message message) {
		Connection conn = ConnectionPool.getInstance().getConnection();

		String addMessageStatement = "Insert into message ( MessageID, content, ChatFromID, ChatToID, SentAt) values (?,?,?,?,?);";
		try (PreparedStatement pst = conn.prepareStatement(addMessageStatement)) {

			pst.setString(1, message.getId());
			pst.setString(2, message.getContent());
			pst.setString(3, message.getFrom().getId());
			pst.setString(4, message.getTo().getId());

			java.util.Date date = new Date();
			Object now = new java.sql.Timestamp(date.getTime());
			pst.setObject(5, now);

			pst.execute();

			ChatDB chatDB = new ChatDB();
			Forum forum = chatDB.getForumByID(message.getTo().getId());
			if (forum != null) {
				ArrayList<Gamer> gamers = chatDB.getForumParticipants(forum.getId());
				gamers.forEach(g -> {

					if (!g.getGamerChat().getId().equals(message.getFrom().getId())) {

						addToMessageForum(message, forum, g);

					}

				});
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.getInstance().returnConnection(conn);
		}

	}

	private void addToMessageForum(Message message, Forum forum, Gamer g) {
		Connection conn = ConnectionPool.getInstance().getConnection();
		String statment = "insert into message_forum (MessageID, ForumID, GamerID) values (?,?,?);";
		try (PreparedStatement pst = conn.prepareStatement(statment)) {

			pst.setString(1, message.getId());
			pst.setString(2, forum.getId());
			pst.setString(3, g.getId());

			pst.execute();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.getInstance().returnConnection(conn);
		}

	}
	
	

	@Override
	public void removeMessage(Message message) {
		// TODO Auto-generated method stub

	}

	public void markForumMessageRead(Message message, Forum forum, Gamer gamer) {
		Connection conn = ConnectionPool.getInstance().getConnection();
		String statment = "update message_forum set isRead=1 Where MessageID=? AND  ForumID=? AND GamerID=?;";
		try (PreparedStatement pst = conn.prepareStatement(statment)) {

			pst.setString(1, message.getId());
			pst.setString(2, forum.getId());
			pst.setString(3, gamer.getId());

			pst.execute();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.getInstance().returnConnection(conn);
		}
		
	}

}
