package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

import beans.Chat;
import beans.Country;
import beans.Forum;
import beans.FriendRequest;
import beans.Game;
import beans.Gamer;
import beans.Genre;
import beans.Notification;
import core.ConnectionPool;
import interfaces.FriendActions;

public class FriendDB implements FriendActions {

	private final String getUserFriends = "select * from ("
			+ "select distinct GamerID, Nickname, Email, Country, ChatID " + "from gamers_friends, gamer "
			+ "Where gamers_friends.SenderID=gamer.GamerID " + "AND ReceiverID=? AND isAccepted=1 " + "UNION "
			+ "select distinct GamerID, Nickname, Email, Country, ChatID " + "from gamers_friends, gamer "
			+ "Where gamers_friends.ReceiverID=gamer.GamerID " + "AND SenderID=? AND isAccepted=1 " + ") as result ;";

	private final String preparedStatementAccept = "update gamers_friends " + "set isAccepted = 1 "
			+ "WHERE ReceiverID=? AND SenderID=?;";

	private final String preparedStatementDecline = "update gamers_friends " + "set isAccepted = -1 "
			+ "WHERE ReceiverID=? AND SenderID=?;";

	@Override
	public ArrayList<Gamer> getAllGamerFriends(String gamerId) {
		ArrayList<Gamer> gamers = new ArrayList<>();
		Connection con = ConnectionPool.getInstance().getConnection();

		try (PreparedStatement pst = con.prepareStatement(getUserFriends)) {
			pst.setString(1, gamerId);
			pst.setString(2, gamerId);

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

	// gets 2 id, one of reciever and one of sender and set isAccepted to 1
	@Override
	public void accepctFriendRequest(String ReceiverID, String SenderID) {
		Connection conn = ConnectionPool.getInstance().getConnection();

		try (PreparedStatement pst = conn.prepareStatement(preparedStatementAccept)) {

			pst.setString(1, ReceiverID);
			pst.setString(2, SenderID);
			
			pst.execute();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.getInstance().returnConnection(conn);
		}
	}

	@Override
	public FriendRequest getFriendRequestByID(String reqID) {

		String statement = "select * from gamers_friends where FriendRequestID=?;";
		FriendRequest request = null;
		Connection conn = ConnectionPool.getInstance().getConnection();

		UserDB userDB = new UserDB();

		try (PreparedStatement pst = conn.prepareStatement(statement)) {
			pst.setString(1, reqID);

			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					request = new FriendRequest();
					request.setFrienRequestID(rs.getString("FriendRequestID"));
					request.setGamerSender(userDB.getGamerByID(rs.getString("SenderID")));
					request.setGamerReceiver(userDB.getGamerByID(rs.getString("ReceiverID")));
					request.setAccepted(rs.getInt("isAccepted") == 0 ? false : true);
					request.setDate((rs.getTimestamp("SentAt")).toLocalDateTime().toLocalDate());

				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.getInstance().returnConnection(conn);
		}
		return request;
	}

	@Override
	public void declineFriendRequest(String reciever, String sender) {
		Connection conn = ConnectionPool.getInstance().getConnection();

		try (PreparedStatement pst = conn.prepareStatement(preparedStatementDecline)) {

			pst.setString(1, reciever);
			pst.setString(2, sender);

			pst.execute();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.getInstance().returnConnection(conn);
		}
	}

	@Override
	public ArrayList<Gamer> getAllGamersByGenre(Genre genre) {
		String statement = "select * from gamer_genre natural join gamer where GenreID=?";
		ArrayList<Gamer> gamers = new ArrayList<>();
		Connection con = ConnectionPool.getInstance().getConnection();

		try (PreparedStatement pst = con.prepareStatement(statement)) {
			pst.setInt(1, genre.getGenreID());

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
	public ArrayList<Gamer> getAllGamersByGame(Game game) {
		String statement = "select * from gamer_game natural join game where GameID=?";
		ArrayList<Gamer> gamers = new ArrayList<>();
		Connection con = ConnectionPool.getInstance().getConnection();

		try (PreparedStatement pst = con.prepareStatement(statement)) {
			pst.setInt(1, game.getId());

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

}
