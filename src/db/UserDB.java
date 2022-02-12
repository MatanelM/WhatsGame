package db;

import java.util.ArrayList;
import java.util.HashSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Stack;

import beans.Chat;
import beans.Country;
import beans.Forum;
import beans.Game;
import beans.Gamer;
import beans.Genre;
import beans.Notification;
import core.ConnectionPool;
import exceptions.EmailAlreadyExistsException;
import exceptions.InvalidUsernameOrPassowrdException;
import exceptions.NicknameAlreadyExistsException;
import exceptions.RegisterationException;
import interfaces.UserActions;
import util.Util;

public class UserDB implements UserActions {

	private final String selectAllGamersStatement = "SELECT * FROM gamer";
	private final String registerQuery = "INSERT INTO gamer (GamerID, Nickname, password_enc_hash_256, Email, Country, ChatID)values (?,?,?,?,?,?);";
	private final String getEmailQuery = "SELECT * FROM gamer WHERE Email=?;";
	private final String getByIDQuery = "SELECT * FROM gamer WHERE GamerID=?;";

	private final String getNicknameQuery = "SELECT * FROM gamer WHERE Nickname=?;";
	private final String loginStatement = "SELECT * FROM gamer WHERE (Email=? OR Nickname=?)AND password_enc_hash_256=?;";
	private final String getForumOwnerQuery = "SELECT * FROM gamer natural join forum WHERE ForumID=?;";

	@Override
	public Gamer register(Gamer gamer) throws RegisterationException {

		Connection con = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement pst = con.prepareStatement(registerQuery)) {

			if (isEmailExists(gamer.getEmail())) {
				throw new EmailAlreadyExistsException();
			}
			if (isNicenameExists(gamer.getNicename())) {
				throw new NicknameAlreadyExistsException();
			}

			pst.setString(1, gamer.getId());
			pst.setString(2, gamer.getNicename());
			pst.setString(3, Util.encrypt256(gamer.getPassword()));
			pst.setString(4, gamer.getEmail());
			pst.setString(5, gamer.getCountry().toString());
			pst.setString(6, gamer.getGamerChat().getId());

			pst.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
		return gamer;
	}

	@Override
	public Gamer login(String username, String password) throws InvalidUsernameOrPassowrdException {
		Gamer gamer = null;

		Connection con = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement pst = con.prepareStatement(loginStatement)) {
			pst.setString(1, username);
			pst.setString(2, username);
			pst.setString(3, Util.encrypt256(password));

			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				gamer = new Gamer(rs.getString("GamerID"), rs.getString("Nickname"), rs.getString("email"),
						Country.valueOf(rs.getString("country")),
						// todo - replace with actual data
						new ArrayList<Gamer>(), new HashSet<Game>(), new HashSet<Genre>(), new Stack<Notification>(),
						new ArrayList<Forum>(), new Chat(rs.getString("ChatID")));
			} else {
				throw new InvalidUsernameOrPassowrdException();
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
		return gamer;
	}

	public ArrayList<Gamer> getAllGamers() {

		ArrayList<Gamer> gamers = new ArrayList<>();
		Connection con = ConnectionPool.getInstance().getConnection();

		try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(selectAllGamersStatement)) {

			// to do
			while (rs.next()) {
				Gamer gamer = new Gamer(rs.getString("GamerID"), rs.getString("Nickname"), rs.getString("email"),
						Country.valueOf(rs.getString("country")),
						// todo - replace with actual data
						new ArrayList<Gamer>(), new HashSet<Game>(), new HashSet<Genre>(), new Stack<Notification>(),
						new ArrayList<Forum>(), new Chat(rs.getString("ChatID")));
				gamers.add(gamer);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}

		return gamers;
	}

	@Override
	public boolean isEmailExists(String email) {
		boolean exists = false;
		Connection con = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement pst = con.prepareStatement(getEmailQuery)) {

			pst.setString(1, email);

			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				exists = true;
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
		return exists;
	}

	@Override
	public boolean isNicenameExists(String email) {
		boolean exists = false;
		Connection con = ConnectionPool.getInstance().getConnection();
		try (PreparedStatement pst = con.prepareStatement(getNicknameQuery)) {

			pst.setString(1, email);

			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				exists = true;
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
		return exists;
	}

	@Override
	public Gamer getGamerByEmail(String email) {
		Connection con = ConnectionPool.getInstance().getConnection();
		Gamer gamer = null;
		try (PreparedStatement pst = con.prepareStatement(getEmailQuery)) {

			pst.setString(1, email);

			ResultSet rs = pst.executeQuery();
			if (rs.next()) {

				gamer = new Gamer(rs.getString("GamerID"), rs.getString("Nickname"), rs.getString("email"),
						Country.valueOf(rs.getString("country")),
						// todo - replace with actual data
						new ArrayList<Gamer>(), new HashSet<Game>(), new HashSet<Genre>(), new Stack<Notification>(),
						new ArrayList<Forum>(), new Chat(rs.getString("ChatID")));

			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
		return gamer;
	}

	@Override
	public void addFriend(Gamer sender, Gamer receiver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void acceptFriend(Gamer reciever, Gamer sender) {
		// TODO Auto-generated method stub

	}

	@Override
	public Gamer getForumOwner(String forumID) {
		Connection con = ConnectionPool.getInstance().getConnection();
		Gamer gamer = null;
		try (PreparedStatement pst = con.prepareStatement(getForumOwnerQuery)) {

			pst.setString(1, forumID);

			ResultSet rs = pst.executeQuery();
			if (rs.next()) {

				gamer = new Gamer(rs.getString("GamerID"), rs.getString("Nickname"), rs.getString("email"),
						Country.valueOf(rs.getString("country")),
						// todo - replace with actual data
						new ArrayList<Gamer>(), new HashSet<Game>(), new HashSet<Genre>(), new Stack<Notification>(),
						new ArrayList<Forum>(), new Chat(rs.getString("ChatID")));

			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
		return gamer;
	}

	@Override
	public Gamer getGamerByID(String gamerID) {
		Connection con = ConnectionPool.getInstance().getConnection();
		Gamer gamer = null;
		try (PreparedStatement pst = con.prepareStatement(getByIDQuery)) {

			pst.setString(1, gamerID);

			ResultSet rs = pst.executeQuery();
			if (rs.next()) {

				gamer = new Gamer(rs.getString("GamerID"), rs.getString("Nickname"), rs.getString("email"),
						Country.valueOf(rs.getString("country")),
						// todo - replace with actual data
						new ArrayList<Gamer>(), new HashSet<Game>(), new HashSet<Genre>(), new Stack<Notification>(),
						new ArrayList<Forum>(), new Chat(rs.getString("ChatID")));

			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
		return gamer;
	}

}
