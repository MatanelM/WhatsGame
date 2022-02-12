package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;
import java.util.UUID;

import beans.Chat;
import beans.Country;
import beans.Forum;
import beans.Game;
import beans.Gamer;
import beans.Genre;
import beans.Message;
import beans.Notification;
import core.ConnectionPool;
import interfaces.ChatActions;

public class ChatDB implements ChatActions {
	private final String addForumStatement = "INSERT INTO forum (ForumID, GamerID, GenreID, GameID)values (?, ?, ?, ?);";
	private final String addForumParticipantStatement = "INSERT INTO forum_participants (ForumID, GamerID)values (?, ?);";
	private final String removeForumParticipantStatement = "DELETE FROM forum_participants WHERE GamerID=? AND ForumID=?;";
	private final String getForumParticipantsStatement = "select GamerID, Nickname, Email, Country, ChatID from gamer natural join forum_participants where ForumID=?;";
	private final String getForumById = "Select * from forum where ForumID=?;";

	@Override
	public void addForumChat(String GamerID, int gameID, Genre genre) {

		Connection con = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement pst = con.prepareStatement(addForumStatement)) {

			pst.setString(1, UUID.randomUUID().toString());
			pst.setString(2, GamerID);

			if (genre == null)
				pst.setObject(3, null);
			else
				pst.setInt(3, genre.getGenreID());

			if (gameID == 0)
				pst.setObject(4, null);
			else {
				pst.setInt(4, gameID);
			}

			pst.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}

	}

	@Override
	public void addGamerToForum(String GamerID, String ForumID) throws SQLIntegrityConstraintViolationException {

		Connection con = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement pst = con.prepareStatement(addForumParticipantStatement)) {

			pst.setString(1, ForumID);
			pst.setString(2, GamerID);

			pst.execute();
		} catch (SQLIntegrityConstraintViolationException e) {
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}

	}

	@Override
	public void removeGamerFromForum(String GamerID, String ForumID) {
		Connection con = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement pst = con.prepareStatement(removeForumParticipantStatement)) {

			pst.setString(1, GamerID);
			pst.setString(2, ForumID);

			pst.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}

	}

	@Override
	public ArrayList<Gamer> getForumParticipants(String ForumID) {
		ArrayList<Gamer> gamers = new ArrayList<>();
		Connection con = ConnectionPool.getInstance().getConnection();

		try (PreparedStatement pst = con.prepareStatement(getForumParticipantsStatement)) {
			pst.setString(1, ForumID);

			try (ResultSet rs = pst.executeQuery()) {
				while (rs.next()) {
					Gamer gamer = new Gamer(rs.getString("GamerID"), rs.getString("Nickname"), rs.getString("email"),
							Country.valueOf(rs.getString("country")),
							// todo - replace with actual data
							new ArrayList<Gamer>(), new HashSet<Game>(), new HashSet<Genre>(),
							new Stack<Notification>(), new ArrayList<Forum>(), new Chat(rs.getString("ChatID")));
					gamers.add(gamer);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
		return gamers;
	}

	@Override
	public Forum getForumByID(String forumID) {
		if (!isForumExists(forumID)) {
			return null;
		}
		Forum forum = new Forum();
		forum.setId(forumID);
		Connection con = ConnectionPool.getInstance().getConnection();

		try (PreparedStatement pst = con.prepareStatement(getForumById)) {
			pst.setString(1, forumID);

			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					forum.setId(forumID);
					if (rs.getString("GameID") != null) {
						forum.setGame(new GameDB().getGameByID(rs.getInt("GameID")));
					}
					if (rs.getString("GenreID") != null) {
						forum.setGenre(Genre.values()[rs.getInt("GenreID") - 1]);
					}
					forum.setOwner(new UserDB().getForumOwner(forumID));
					forum.setGamers(getForumParticipants(forumID));
					forum.setMessages(getForumMessages(forumID));
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}

		return forum;
	}

	@Override
	public ArrayList<Forum> getForumsOfOwner(String GamerID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Message> getForumMessages(String ForumID) {
		return new MessageDB().getAllMessagesFromForum(ForumID);

	}

	@Override
	public ArrayList<Forum> getForumByGenre(Genre genre) {
		String statement = "select * from forum where GenreID=?;";

		ArrayList<Forum> forums = new ArrayList<>();

		Connection con = ConnectionPool.getInstance().getConnection();

		try (PreparedStatement pst = con.prepareStatement(statement)) {
			pst.setInt(1, genre.getGenreID());

			try (ResultSet rs = pst.executeQuery()) {
				while (rs.next()) {
					Forum forum = this.getForumByID(rs.getString("ForumID"));
					forums.add(forum);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
		return forums;
	}

	public ArrayList<Forum> getForumsOfGamer(String gamerId) {
		String statement = "select * from forum_participants where GamerID=?;";
		ArrayList<Forum> forums = new ArrayList<>();

		Connection con = ConnectionPool.getInstance().getConnection();

		try (PreparedStatement pst = con.prepareStatement(statement)) {
			pst.setString(1, gamerId);

			try (ResultSet rs = pst.executeQuery()) {
				while (rs.next()) {
					Forum forum = this.getForumByID(rs.getString("ForumID"));
					forums.add(forum);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
		return forums;
	}

	@Override
	public ArrayList<Message> getChatMessages(String gamer, String friend) {
		return new MessageDB().getAllMessagesFromChat(gamer, friend);
	}

	@Override
	public boolean isForumExists(String forumID) {
		Connection con = ConnectionPool.getInstance().getConnection();

		try (PreparedStatement pst = con.prepareStatement(getForumById)) {
			pst.setString(1, forumID);

			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					return true;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
		return false;
	}

	public void addForumChat(String id, String id2, int i, Genre genre) {
		Connection con = ConnectionPool.getInstance().getConnection();
		
		try (PreparedStatement pst = con.prepareStatement(addForumStatement)) {

			pst.setString(1, id);
			pst.setString(2, id2);

			if (genre == null)
				pst.setObject(3, null);
			else
				pst.setInt(3, genre.getGenreID());

			if (i == 0)
				pst.setObject(4, null);
			else {
				pst.setInt(4, i);
			}

			pst.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
	}

}
